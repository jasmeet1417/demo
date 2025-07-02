package com.corp.match.controller;

import com.corp.match.entity.UserAccount;
import com.corp.match.entity.UserProfile;
import com.corp.match.repository.UserAccountRepository;
import com.corp.match.repository.UserProfileRepository;
import com.corp.match.service.EmailService;
import com.corp.match.uttility.EnrichmentApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/verify")
public class WorkEmailVerificationController {

    @Autowired
    private EmailService emailService;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    private final Map<String, String> otpMap = new HashMap<>();

    // Step 1: Send OTP to work email
    @PostMapping("/send-otp")
    public String sendOtp(@RequestParam String personalEmail, @RequestParam String workEmail) {
        Optional<UserAccount> optional = userAccountRepository.findByPersonalEmail(personalEmail);
        if (optional.isEmpty()) return "User not found.";

        String otp = String.format("%05d", new Random().nextInt(100000));
        otpMap.put(workEmail, otp);

        emailService.sendVerificationOtp(workEmail, otp);
        return "OTP sent to work email: " + workEmail;
    }

    // Step 2: Verify OTP and create profile
    @PostMapping("/verify-otp")
    public String verifyOtpAndCreateProfile(
            @RequestParam String personalEmail,
            @RequestParam String otp,
            @RequestParam String fullName,
            @RequestParam int age,
            @RequestParam String gender
    ) {
        Optional<UserAccount> optional = userAccountRepository.findByPersonalEmail(personalEmail);
        if (optional.isEmpty()) return "User not found.";

        UserAccount account = optional.get();
        String workEmail = null;

        // Find work email by OTP map reverse lookup
        for (Map.Entry<String, String> entry : otpMap.entrySet()) {
            if (entry.getValue().equals(otp)) {
                workEmail = entry.getKey();
                break;
            }
        }

        if (workEmail == null) return "Invalid or expired OTP";

        account.setWorkEmailVerified(true);
        userAccountRepository.save(account);

        // Call enrichment API
        String domain = workEmail.substring(workEmail.indexOf('@') + 1);
        String companyName = EnrichmentApi.getCompanyNameFromDomain(domain);

        // Save profile
        UserProfile profile = new UserProfile();
        profile.setAccount(account);
        profile.setWorkEmail(workEmail);
        profile.setFullName(fullName);
        profile.setAge(age);
        profile.setGender(gender);
        profile.setCompanyName(companyName);
        profile.setStatus("Online");
        profile.setLastSeenAt(new java.sql.Timestamp(System.currentTimeMillis()));

        userProfileRepository.save(profile);

        // Remove OTP
        otpMap.remove(workEmail);

        return "Work email verified. Profile created. You can now use the platform.";
    }
}
