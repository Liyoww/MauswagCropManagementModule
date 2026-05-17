package com.cropmanager.cropsync;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CropTemplateRepository extends JpaRepository<CropTemplate, Long> {
    // Finds all rules for a specific crop (e.g., all tasks for "Eggplant")
    List<CropTemplate> findByCropType(String cropType);
}