// const admin = require("firebase-admin");
// const fs = require("fs");

// const serviceAccount = require("./serviceAccountKey.json");

// admin.initializeApp({
//   credential: admin.credential.cert(serviceAccount),
// });

// const db = admin.firestore();

// const emailsAndPasswords = JSON.parse(fs.readFileSync("email_pass.json"));
// const doctorData = JSON.parse(fs.readFileSync("doc3.json"));

// async function createDoctorsAndUploadData() {
//   for (const entry of emailsAndPasswords) {
//     const { email, password } = entry;

//     try {
//       // 1. Create Auth User
//       const user = await admin.auth().createUser({ email, password });
//       console.log(`✅ Created user: ${email} | UID: ${user.uid}`);

//       // 2. Set custom claim: role = doctor
//       await admin.auth().setCustomUserClaims(user.uid, { role: "doctor" });
//       console.log(`🔐 Set custom claim for ${email}: role = doctor`);

//       // 3. Find doctor data from JSON
//       const doctor = doctorData.find(doc => doc.email === email);

//       if (!doctor) {
//         console.warn(`⚠️ No matching doctor data found for: ${email}`);
//         continue;
//       }

//       // 4. Upload to Firestore with UID as doc ID
//       await db.collection("doctors").doc(user.uid).set({
//         ...doctor,
//         uid: user.uid
//       });

//       console.log(`📦 Firestore doc created for ${email}`);

//     } catch (err) {
//       console.error(`❌ Error with ${email}: ${err.message}`);
//     }
//   }
// }

// createDoctorsAndUploadData();

const admin = require("firebase-admin");
const fs = require("fs");

const serviceAccount = require("./serviceAccountKey.json");

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
});

const db = admin.firestore();

const emailsAndPasswords = JSON.parse(fs.readFileSync("email_pass.json"));
const doctorData = JSON.parse(fs.readFileSync("doc3.json"));

async function createDoctorsAndUploadData() {
  for (const entry of emailsAndPasswords) {
    const { email, password } = entry;

    try {
      // 1. Get or create user
      let user;
      try {
        user = await admin.auth().getUserByEmail(email);
        console.log(`👤 Existing user found: ${email}`);
      } catch {
        user = await admin.auth().createUser({ email, password });
        console.log(`✅ Created new user: ${email}`);
      }

      // 2. Set custom claim if not already set
      const userRecord = await admin.auth().getUser(user.uid);
      const existingClaims = userRecord.customClaims || {};

      if (existingClaims.role !== "doctor") {
        await admin.auth().setCustomUserClaims(user.uid, { role: "doctor" });
        console.log(`🔐 Custom claim set: ${email} → role=doctor`);
      } else {
        console.log(`🔍 Custom claim already exists for ${email}`);
      }

      // 3. Check if Firestore doc exists
      const docRef = db.collection("doctors").doc(user.uid);
      const docSnap = await docRef.get();

      if (!docSnap.exists) {
        const doctor = doctorData.find(doc => doc.email === email);

        if (!doctor) {
          console.warn(`⚠️ No matching doctor data found for: ${email}`);
          continue;
        }

        await docRef.set({
          ...doctor,
          uid: user.uid
        });

        console.log(`📦 Firestore doc created for ${email}`);
      } else {
        console.log(`🗂️ Firestore doc already exists for ${email}`);
      }

    } catch (err) {
      console.error(`❌ Error with ${email}: ${err.message}`);
    }
  }
}

createDoctorsAndUploadData();
