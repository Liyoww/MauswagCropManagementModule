package com.cropmanager.cropsync;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/supplies")
@CrossOrigin // Mahalaga ito para sa frontend connection
public class SupplyController {

    private final SupplyRepository repository;
    private final UserRepository userRepository;

    public SupplyController(SupplyRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    // Ginamit ang @RequestParam para parehas sa CropController
    @GetMapping
    public List<Supply> getAllSupplies(@RequestParam String username) {
        return repository.findByUser_Username(username);
    }

    @PostMapping
    public Supply addSupply(@RequestBody Supply supply, @RequestParam String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        
        supply.setUser(user); 
        return repository.save(supply);
    }

@DeleteMapping("/{id}")
public ResponseEntity<?> deleteSupply(@PathVariable Long id) {
    try {
        if (!repository.existsById(id)) {
            return ResponseEntity.status(404).body("Item not found");
        }
        repository.deleteById(id);
        return ResponseEntity.ok().build();
    } catch (Exception e) {
        // Ito ang magpapakita kung bakit ayaw (ex: ConstraintViolation)
        return ResponseEntity.status(500).body("Error: " + e.getMessage());
    }
}

    @PutMapping("/{id}")
public Supply updateSupply(@PathVariable Long id, @RequestBody Supply updatedSupply) {
    return repository.findById(id)
        .map(supply -> {
            if (updatedSupply.getStockQuantity() != null) {
                supply.setStockQuantity(updatedSupply.getStockQuantity());
            }
            return repository.save(supply);
        })
        .orElseThrow(() -> new RuntimeException("Supply not found with id " + id));
}
}