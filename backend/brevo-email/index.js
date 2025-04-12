require("dotenv").config();
const axios = require("axios");
console.log("Using API Key:", process.env.BREVO_API_KEY);
const sendEmail = async () => {
  const data = {
    sender: { name: "Swasthya App", email: "nisarg.workmail@gmail.com" },
    to: [{ email: "nisargg705@gmail.com", name: "Good Afternoon, Admin" }],
    subject: "Doctor Verification - Swasthya",
    htmlContent: `<p>Hello Sir,</p>
                  <p>Please click the link below to verify your profile:</p>
                  <a href="https://hospitalapp.page.link/doctors">Verify My Profile</a>`
  };
  

  try {
    const res = await axios.post("https://api.brevo.com/v3/smtp/email", data, {
      headers: {
        "Content-Type": "application/json",
        "api-key": process.env.BREVO_API_KEY
      }
    });

    console.log("✅ Email sent successfully!", res.status);
  } catch (err) {
    console.error("❌ Error sending email:", err.response?.data || err.message);
  }
};

sendEmail();
