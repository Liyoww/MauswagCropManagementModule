package com.cropmanager.cropsync;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CropRepository extends JpaRepository<Crop, Long> {
    // Dagdagan mo nito para mahanap ang crops ng specific user
    List<Crop> findByUserUsernameIgnoreCase(String username);
}