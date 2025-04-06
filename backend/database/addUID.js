const fs = require("fs");
const crypto = require("crypto");

// Read the existing JSON file
const data = JSON.parse(fs.readFileSync("doctors_by_specialization.json", "utf-8"));

for (const specialization in data) {
  data[specialization] = data[specialization].map(doctor => ({
    ...doctor,
    uid: generateUID()
  }));
}

// Function to generate a simple UID
function generateUID() {
  return crypto.randomUUID(); // You can replace this with your real UID source
}

// Write back to a new file or overwrite
fs.writeFileSync("doctors_with_uids.json", JSON.stringify(data, null, 2));

console.log("✅ doctors_with_uids.json created with UIDs.");
