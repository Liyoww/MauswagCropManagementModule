package com.cropmanager.cropsync;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/crops")
@CrossOrigin 
public class CropController {

    @Autowired
    private CropRepository repository;
    
    @Autowired
    private TaskRepository taskRepository;
    
    @Autowired
    private CropTemplateRepository templateRepository;

    @Autowired
    private FarmInputRepository inputRepository;

    @Autowired
    private SupplyRepository supplyRepository;
    
    @Autowired
    private UserRepository userRepository;

    // 1. GET ALL CROPS
    
  @GetMapping
    public List<Crop> getCrops(@RequestParam String username) {
        // Ito ay mas stable kaysa sa manual filtering
        return repository.findByUserUsernameIgnoreCase(username);
    }
    // 2. CREATE NEW PLANNER (Automatically creates tasks from templates)
    @PostMapping
public ResponseEntity<?> addCrop(@RequestBody Crop crop, @RequestParam String username) {
    // Hanapin muna ang user sa DB
    User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new RuntimeException("User not found: " + username));
    
    crop.setUser(user); // I-link ang user sa crop

    if (crop.getDatePlanted() == null) {
        crop.setDatePlanted(LocalDate.now());
    }
    
    Crop savedCrop = repository.save(crop);
    
    // Auto-create tasks logic (Retained)
    List<CropTemplate> templates = templateRepository.findByCropType(savedCrop.getType());
    for (CropTemplate blueprint : templates) {
        Task task = new Task();
        task.setDayOffset(blueprint.getDayNumber()); 
        task.setDescription(blueprint.getTaskName()); 
        task.setCrop(savedCrop);    
        task.setCompleted(false);
        taskRepository.save(task);
    }
    
    return ResponseEntity.ok(savedCrop);
}

    // 3. SAVE NEW CROP TYPE / TEMPLATE (ITO YUNG NAWALA KANINA)
    @PostMapping("/templates")
    public ResponseEntity<?> saveTemplate(@RequestBody CropTemplate template) {
        try {
            CropTemplate saved = templateRepository.save(template);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

    // 4. GET UNIQUE CROP TYPES FOR DROPDOWN
    @GetMapping("/templates/types")
    public List<String> getUniqueCropTypes() {
        return templateRepository.findAll()
                .stream()
                .map(CropTemplate::getCropType)
                .distinct()
                .toList();
    }

    // 5. INPUTS MANAGEMENT
    @PostMapping("/{cropId}/inputs")
@Transactional 
public ResponseEntity<?> addInput(@PathVariable Long cropId, @RequestBody FarmInput inputRequest) {
    try {
        inputRequest.setCropId(cropId);
        inputRequest.setDateApplied(LocalDate.now());

        if (inputRequest.getSupply() != null && inputRequest.getSupply().getId() != null) {
            Supply inventory = supplyRepository.findById(inputRequest.getSupply().getId())
                    .orElseThrow(() -> new RuntimeException("Supply not found"));

            // Stock validation (Double/Float gamitin, wag intValue)
            if (inventory.getStockQuantity() < inputRequest.getQuantityUsed()) {
                return ResponseEntity.badRequest().body("Insufficient stock! Available: " + inventory.getStockQuantity());
            }

            // Bawasan ang stock (Double arithmetic)
            inventory.setStockQuantity(inventory.getStockQuantity() - inputRequest.getQuantityUsed()); 
            
            // I-save muna ang supply para updated ang brandName at quantity
            Supply updatedSupply = supplyRepository.save(inventory);
            
            // I-attach ang updated supply object sa inputRequest
            inputRequest.setSupply(updatedSupply);
        } else {
            inputRequest.setSupply(null);
        }

        FarmInput savedInput = inputRepository.save(inputRequest);
        return ResponseEntity.ok(savedInput);
    } catch (Exception e) {
        return ResponseEntity.status(500).body("Error: " + e.getMessage());
    }
}

    @GetMapping("/{cropId}/inputs")
    public List<FarmInput> getInputsByCrop(@PathVariable Long cropId) {
        return inputRepository.findByCropId(cropId);
    }

    // 6. TASK MANAGEMENT
    @PutMapping("/tasks/{taskId}/toggle")
    public Task toggleTask(@PathVariable Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found"));
        task.setCompleted(!task.isCompleted());
        return taskRepository.save(task);
    }

   @GetMapping("/{id}/tasks")
public List<Task> getTasks(@PathVariable Long id) {
    return repository.findById(id).map(crop -> {
        // 1. Kalkulahin ang age ng crop (Day 1, Day 2, etc.)
        long ageInDays = java.time.temporal.ChronoUnit.DAYS.between(
            crop.getDatePlanted(), 
            LocalDate.now()
        ) + 1; // +1 para ang mismong araw ng pagtanim ay "Day 1"

        return taskRepository.findByCrop(crop).stream()
                // Wag ipakita ang completed kung gusto mong mawala sila agad
                //.filter(task -> !task.isCompleted()) 
                .map(task -> {
                    // 2. I-check kung future task pa ito
                    // Kung ang Day Offset ay mas malaki sa Age, locked siya
                    if (task.getDayOffset() > ageInDays) {
                        task.setDescription("[LOCKED] " + task.getDescription());
                        // Note: Mas maganda kung may 'isLocked' field ang Task Entity mo
                    }
                    return task;
                })
                .sorted(java.util.Comparator.comparingInt(Task::getDayOffset))
                .toList();
    }).orElse(List.of());
}

    // 7. DELETE CROP
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<?> deleteCrop(@PathVariable Long id) {
        // Option: Pwedeng i-delete muna tasks manually kung walang cascade sa DB
        // taskRepository.deleteByCropId(id); 
        repository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}