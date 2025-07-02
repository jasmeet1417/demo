package com.corp.match.controller;



import com.corp.match.entity.UserProfile;
import com.corp.match.service.EmailService;
import com.corp.match.service.UserProfileService;
import com.corp.match.uttility.EnrichmentApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/register")
public class UserController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserProfileService userProfileService;

    private Map<String, UserProfile> pendingUsers = new HashMap<>();  // email -> profile
    private Map<String, String> otpMap = new HashMap<>();             // email -> otp

    @PostMapping("/init")
    public String initiateRegistration(@RequestBody UserProfile profile) {
        String email = profile.getWorkEmail();
        String otp = String.format("%05d", new Random().nextInt(100000));

        // Save user temporarily
        pendingUsers.put(email, profile);
        otpMap.put(email, otp);

        emailService.sendVerificationOtp(email, otp);

        return "OTP sent to " + email;
    }

    @PostMapping("/verify")
    public String verifyOtp(@RequestParam String email, @RequestParam String otp) {
        if (!otpMap.containsKey(email)) {
            return "Invalid or expired OTP. Try registering again.";
        }

        if (!otpMap.get(email).equals(otp)) {
            return "Incorrect OTP.";
        }

        // OTP is correct
        UserProfile profile = pendingUsers.get(email);
        String domain = email.substring(email.indexOf('@') + 1);
        String companyName = EnrichmentApi.getCompanyNameFromDomain(domain);

        profile.setCompanyName(companyName);
        profile.setStatus("Offline");

        userProfileService.saveUser(profile);  // save to DB

        // clean up
        otpMap.remove(email);
        pendingUsers.remove(email);

        return "User " + profile.getFullName() + " working at " +
               (companyName != null ? companyName : "unknown company") +
               " registered successfully.";
    }
}
