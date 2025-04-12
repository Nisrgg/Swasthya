const fs = require("fs");

// Load your messy JSON
const raw = require("./doc3.json");

// Flatten the structure
const doctors = raw.flat().filter(d => d.name);

// Generate 10-minute slots from start to end
function generateSlots(start, end) {
  const slots = [];
  let [h, m] = start.split(":").map(Number);
  const [endH, endM] = end.split(":").map(Number);

  while (h < endH || (h === endH && m < endM)) {
    slots.push(
      `${h.toString().padStart(2, "0")}:${m.toString().padStart(2, "0")}`
    );
    m += 10;
    if (m >= 60) {
      h++;
      m -= 60;
    }
  }

  return slots;
}

// Generate full day schedule: 9–12 & 2–5
const fullDaySlots = [
  ...generateSlots("09:00", "12:00"),
  ...generateSlots("14:00", "17:00")
];

// Randomly skip 2–5 slots
function randomlySkipSlots(slots) {
  const clone = [...slots];
  const toSkip = Math.floor(Math.random() * 4) + 2;
  for (let i = 0; i < toSkip; i++) {
    const index = Math.floor(Math.random() * clone.length);
    clone.splice(index, 1);
  }
  return clone;
}

// Days of the week
const weekDays = ["Monday", "Tuesday", "Wednesday", "Thursday", "Friday"];

// Assign slots to each doctor
doctors.forEach((doc) => {
  doc.available_slots = {};
  weekDays.forEach((day) => {
    doc.available_slots[day] = randomlySkipSlots(fullDaySlots).map((t) =>
      formatTo12Hour(t)
    );
  });
});

// Convert "14:30" => "02:30 PM"
function formatTo12Hour(time24) {
  const [h, m] = time24.split(":").map(Number);
  const suffix = h >= 12 ? "PM" : "AM";
  const hour12 = h % 12 === 0 ? 12 : h % 12;
  return `${hour12.toString().padStart(2, "0")}:${m
    .toString()
    .padStart(2, "0")} ${suffix}`;
}

// Save to file
fs.writeFileSync(
  "updated_doctors.json",
  JSON.stringify(doctors, null, 2),
  "utf-8"
);

console.log("✅ Doctors updated with realistic slots.");
