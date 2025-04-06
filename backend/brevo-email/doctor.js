// Load dotenv & other modules
require("dotenv").config();
const axios = require("axios");
const admin = require("firebase-admin");
const serviceAccount = require("./serviceAccountKey.json");

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
      emailVerified: false,
      disabled: false
    });

    // ✅ Assign custom claim to mark user as doctor
    await admin.auth().setCustomUserClaims(user.uid, { role: "doctor" });
    console.log(`✅ Doctor account created for ${email} with UID: ${user.uid}`);

    // ✅ Add doctor details to Firestore
    const doctorInfo = {
      ...doctorData,
      role: "doctor",
      createdAt: admin.firestore.FieldValue.serverTimestamp()
    };
    await db.collection("doctors").doc(user.uid).set(doctorInfo);
    console.log(`✅ Doctor details stored in Firestore!`);

    // ✅ Generate verification link
    const actionCodeSettings = {
      url: "https://hospitalapp.page.link/doctors", // replace with your actual dynamic link
      android: {
        packageName: "com.example.hospital",
        installApp: true,
        minimumVersion: "1"
      },
      handleCodeInApp: true
    };

    const link = await admin.auth().generateEmailVerificationLink(email, actionCodeSettings);
    console.log(`📧 Firebase verification link generated`);

    // ✅ Send Brevo email
    await sendVerificationEmail(email, doctorData.name, link);

  } catch (error) {
    console.error("❌ Error creating doctor: ", error.message);
  }
}

// ✉️ Brevo Email Sender Function
async function sendVerificationEmail(email, doctorName, verificationLink) {
  const data = {
    sender: { name: "Swasthya App", email: "nisarg.workmail@gmail.com" },
    to: [{ email: email, name: doctorName }],
    subject: "Swasthya - Verify Your Doctor Account",
    htmlContent: `
      <p>Dear ${doctorName},</p>
      <p>Welcome to Swasthya! Please verify your doctor account by clicking the link below:</p>
      <a href="${verificationLink}">Verify My Account</a>
      <p>This link will open the app and complete your setup.</p>
      <br><p>Regards,<br/>Swasthya Team</p>
    `
  };

  try {
    const res = await axios.post("https://api.brevo.com/v3/smtp/email", data, {
      headers: {
        "Content-Type": "application/json",
        "api-key": process.env.BREVO_API_KEY
      }
    });

    console.log(`✅ Verification email sent to ${email}`);
  } catch (error) {
    console.error("❌ Error sending Brevo email:", error.response?.data || error.message);
  }
}

// ✅ Example Data
const doctorData = {
  name: "Dr. Manav Mehta",
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

// 🔥 Create a new doctor with email verification
createDoctor("patelmanav2406@gmail.com", "hello1234", doctorData);