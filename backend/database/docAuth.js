const admin = require("firebase-admin");
const fs = require("fs");

// Initialize Firebase Admin SDK
const serviceAccount = require("./serviceAccountKey.json");

admin.initializeApp({
  credential: admin.credential.cert(serviceAccount),
});

const db = admin.firestore();

const doctors = [
  { email: "anjali@example.com", password: "password123" },
  { email: "arjun@example.com", password: "password123" },
  { email: "meera@example.com", password: "password123" },
  { email: "ravi.iyer@example.com", password: "password123" },
  { email: "nivedita.reddy@example.com", password: "password123" },
  { email: "simran.kaur@example.com", password: "password123" },
  { email: "harshad.k@example.com", password: "password123" },
  { email: "tenzing.n@example.com", password: "password123" },
  { email: "pooja.desai@example.com", password: "password123" },
  { email: "manpreet.singh@example.com", password: "password123" },
  { email: "aparna.das@example.com", password: "password123" },
  { email: "irfan.qureshi@example.com", password: "password123" },
  { email: "ayesha.khan@example.com", password: "password123" }
];

async function migrateDoctors() {
  const snapshot = await db.collection("doctors").get();
  const oldDocs = snapshot.docs.map(doc => ({ id: doc.id, data: doc.data() }));

  for (const doc of doctors) {
    try {
      // Create auth user
      const user = await admin.auth().createUser({
        email: doc.email,
        password: doc.password
      });

      console.log(`✅ Created user: ${doc.email} | UID: ${user.uid}`);

      // Find existing doc by matching email
      const oldDoc = oldDocs.find(d => d.data.email === doc.email);

      if (oldDoc) {
        // Create new doc with UID
        await db.collection("doctors").doc(user.uid).set(oldDoc.data);

        // Delete old doc
        await db.collection("doctors").doc(oldDoc.id).delete();

        console.log(`🔁 Migrated doctor doc from ${oldDoc.id} ➝ ${user.uid}`);
      } else {
        console.warn(`⚠️ No Firestore doc found for: ${doc.email}`);
      }

    } catch (err) {
      console.error(`❌ Error with ${doc.email}: ${err.message}`);
    }
  }
}

migrateDoctors();