const actionCodeSettings = {
    // Link to your app's sign-in screen
    url: "https://hospitalapp.page.link/doctors", // Update with your app's URL
    android: {
      packageName: "com.example.hospital", // Update with your app's package name
      installApp: true,
      minimumVersion: "1"
    },
    handleCodeInApp: true
  };
  
  // Send verification email
  await admin.auth().generateEmailVerificationLink(email, actionCodeSettings)
    .then((link) => {
      console.log(`📧 Email verification link sent to ${email}`);
      // You can also send this email through your custom email service (e.g., SendGrid)
    })
    .catch((error) => {
      console.error("❌ Error sending verification email: ", error.message);
    });

    const sendVerificationEmail = (email, link) => {
        // Using a service like SendGrid to send the email with the link
        sendGridClient.send({
          to: email,
          from: "your-email@example.com", // Sender email
          subject: "Verify your email address",
          text: `Click the following link to verify your email: ${link}`
        });
      }
      