package com.cropmanager.cropsync;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder; // Dagdag ito
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin 
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // I-inject ang encoder

    // SIGN UP: Dito natin i-ha-hash ang password
    @PostMapping("/signup")
    public ResponseEntity<?> register(@RequestBody User user) {
        if(userRepository.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username already exists!");
        }
        
        // HASHING STEP: Gawing random string ang plain text password
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        
        userRepository.save(user);
        return ResponseEntity.ok("User registered successfully!");
    }

    // LOGIN: Gamitin ang matches() imbes na equals()
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User user) {
        Optional<User> foundUser = userRepository.findByUsername(user.getUsername());
        
        // DEBUGGING LOGS (Para makita mo sa terminal)
        System.out.println("Login Attempt: " + user.getUsername());

        if(foundUser.isPresent()) {
            // IMPORTANT: matches(input_plain_text, database_hashed_text)
            boolean isMatch = passwordEncoder.matches(user.getPassword(), foundUser.get().getPassword());
            System.out.println("Match Result: " + isMatch);

            if(isMatch) {
                // Huwag ibalik ang password sa response
                User responseUser = foundUser.get();
                responseUser.setPassword(null); 
                return ResponseEntity.ok(responseUser);
            }
        }
        
        return ResponseEntity.status(401).body("Invalid username or password!");
    }
}