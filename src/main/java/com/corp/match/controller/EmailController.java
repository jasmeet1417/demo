package com.corp.match.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.corp.match.service.EmailService;
import com.corp.match.uttility.EnrichmentApi;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@RestController
@RequestMapping("/email")
public class EmailController {

    private Map<String, String> otpToDomain = new HashMap<>();
    private Map<String, Boolean> otpStatus = new HashMap<>();

    @Autowired
    private EmailService emailService;

    @GetMapping("/send")
    public String sendEmail(@RequestParam String to) {
        String otp = String.format("%05d", new Random().nextInt(100000)); // 5-digit OTP
        otpStatus.put(otp, false);

        String domain = emailService.sendVerificationOtp(to, otp);
        otpToDomain.put(otp, domain);

        return "Verification OTP sent to " + to;
    }

    @GetMapping("/verify")
    public String verifyOtp(@RequestParam String otp) {
        if (!otpStatus.containsKey(otp)) {
            return "Invalid or expired OTP!";
        }

        otpStatus.put(otp, true);
        String domain = otpToDomain.get(otp);
        String companyName = EnrichmentApi.getCompanyNameFromDomain(domain);

        return "Email verified successfully! Person works at " +
                (companyName != null ? companyName : "an unknown company") + ".";
    }
}
