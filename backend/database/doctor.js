var admin = require("firebase-admin");

var serviceAccount = require("./serviceAccountKey.json");

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount)
});

const db = admin.firestore();

// 🚀 Create Doctor with Email/Password + Verification Link
async function createDoctor(email, password, doctorData) {
  try {
    // ✅ Check if the user already exists
    const existingUser = await admin.auth().getUserByEmail(email).catch(() => null);

    if (existingUser) {
      console.log(`❗️ User with email ${email} already exists!`);
      return;
    }

    // ✅ Create doctor in Firebase Auth
    const user = await admin.auth().createUser({
      email: email,
      password: password,
      emailVerified: false, // Verification needed
      disabled: false
    });

    // ✅ Assign custom claim to mark user as doctor
    await admin.auth().setCustomUserClaims(user.uid, { role: "doctor" });
    console.log(`✅ Doctor account created for ${email} with UID: ${user.uid}`);

    // ✅ Add doctor details to Firestore
    const doctorInfo = {
      ...doctorData,
      role: "doctor", // Include role
      createdAt: admin.firestore.FieldValue.serverTimestamp()
    };

    await db.collection("doctors").doc(user.uid).set(doctorInfo);
    console.log(`✅ Doctor details stored in Firestore!`);

    // ✅ Send Email Verification Link
    const actionCodeSettings = {
      url: "https://hospitalapp.page.link/doctors", // 👈 Put your valid web link here
      android: {
        packageName: "com.example.hospital",
        installApp: true,
        minimumVersion: "1"
      },
      handleCodeInApp: true
    };

    const link = await admin.auth().generateEmailVerificationLink(email, actionCodeSettings);
    console.log(`📧 Email verification link generated: ${link}`);
    sendVerificationEmail(email, link);

  } catch (error) {
    console.error("❌ Error creating doctor: ", error.message);
  }
}

// ✉️ Dummy Email Function (Use Nodemailer for Real Emails)
async function sendVerificationEmail(email, link) {
  console.log(`📨 Email Sent to: ${email}`);
  console.log(`🔗 Verification Link: ${link}`);
}

// ✅ Doctor Data Example
const doctorData = {
  name: "Dr. Willson",
  age: 45,
  gender: "Male",
  specialization: "Cardiologist",
  experience: 15,
  phone: "+919876543210",
  available_slots: {
    Monday: ["10:00", "11:00"],
    Tuesday: ["14:00", "15:00"],
    Wednesday: ["10:00", "11:00"],
    Thursday: ["14:00", "15:00"],
    Friday: ["10:00", "11:00"],
    Saturday: ["14:00", "15:00"]
  }
};

// ✅ Create Doctor Account with Custom Data
createDoctor("nisarg.workmail@gmail.com", "vitb11142", doctorData);