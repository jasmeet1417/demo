package com.corp.match.controller;

import com.corp.match.entity.UserAccount;
import com.corp.match.repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Controller
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Signup endpoint. Accepts form data and registers a new account.
     */
    @PostMapping("/signup")
    public ResponseEntity<String> signup(@RequestParam String personalEmail,
                                         @RequestParam String password) {
        Optional<UserAccount> existing = userAccountRepository.findByPersonalEmail(personalEmail);
        if (existing.isPresent()) {
            return ResponseEntity.badRequest().body("Account with this email already exists.");
        }

        UserAccount account = new UserAccount();
        account.setPersonalEmail(personalEmail);
        account.setPassword(passwordEncoder.encode(password));
        userAccountRepository.save(account);

        return ResponseEntity.ok("Signup successful. Now login at /login");
    }


    // Optional: this page will be accessible after login
    @GetMapping("/home")
    @ResponseBody
    public String homePage() {
        return "Welcome to the secure home page!";
    }
}
