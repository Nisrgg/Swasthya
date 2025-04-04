const admin = require("firebase-admin");

// Initialize Firebase Admin SDK
const serviceAccount = require("./serviceAccountKey.json");

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount)
});

// Replace with the correct UID of the doctor
const doctorUid = "LuChtCeP8VcbdXFwlKSPoYEZhVc2";

admin.auth().getUser(doctorUid)
  .then((userRecord) => {
    console.log("Custom Claims:", userRecord.customClaims);
  })
  .catch((error) => {
    console.log("Error fetching user data:", error);
  });