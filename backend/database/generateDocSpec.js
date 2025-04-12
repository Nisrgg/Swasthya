const fs = require("fs");

// Read the input file
const rawData = fs.readFileSync("doc3.json", "utf-8");
const doctors = JSON.parse(rawData);

const result = {};

// Group by specialization
doctors.forEach(doc => {
  const { name, email, specialization } = doc;

  if (name && email && specialization) {
    if (!result[specialization]) {
      result[specialization] = [];
    }

    result[specialization].push({ name, email });
  }
});

// Write output to a new JSON file
fs.writeFileSync("doctors_by_specialization.json", JSON.stringify(result, null, 2));

console.log("✅ doctors_by_specialization.json created successfully.");
