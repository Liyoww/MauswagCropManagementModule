package com.cropmanager.cropsync;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

// Siguraduhin na ang interface name ay tama at ang Entity (FarmInput) ay accessible
public interface FarmInputRepository extends JpaRepository<FarmInput, Long> {
    
    // Ang 'cropId' dito ay dapat tugma sa variable name sa FarmInput.java
    List<FarmInput> findByCropId(Long cropId);
}