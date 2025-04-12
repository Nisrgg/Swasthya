const admin = require("firebase-admin");

// Initialize Firebase Admin
const serviceAccount = require("./serviceAccountKey.json");

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
});

// Function to update custom claim
async function updateDoctorApprovalStatus(email, approved) {
  try {
    const user = await admin.auth().getUserByEmail(email);

    // Merge with existing claims (preserve other claims like role)
    const existingClaims = user.customClaims || {};

    await admin.auth().setCustomUserClaims(user.uid, {
      ...existingClaims,
      approved: approved
    });

    console.log(`✅ Custom claim updated for ${email}: approved=${approved}`);
  } catch (error) {
    console.error(`❌ Failed to update claim for ${email}: ${error.message}`);
  }
}

// Example usage:
updateDoctorApprovalStatus("ayesha.khan@example.com", true);
// To unapprove:
// updateDoctorApprovalStatus("doctor1@example.com", false);
