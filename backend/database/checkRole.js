// const admin = require("firebase-admin");

// // Initialize Firebase Admin SDK
// const serviceAccount = require("../../serviceAccountKey.json");

// admin.initializeApp({
//   credential: admin.credential.cert(serviceAccount)
// });

// // Replace with the correct UID of the doctor
// const doctorUid = "vAVXtZjBviOVdsVlP7TodOnORmT2";

// admin.auth().getUser(doctorUid)
//   .then((userRecord) => {
//     console.log("Custom Claims:", userRecord.customClaims);
//   })
//   .catch((error) => {
//     console.log("Error fetching user data:", error);
//   });


  const admin = require("firebase-admin");
admin.initializeApp({
  credential: admin.credential.cert(require("./serviceAccountKey.json"))
});

async function checkClaims(email) {
  const user = await admin.auth().getUserByEmail(email);
  console.log(user.customClaims); // should show { role: "doctor", approved: true/false }
}

checkClaims("ayesha.khan@example.com");
