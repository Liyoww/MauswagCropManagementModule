package com.cropmanager.cropsync;

import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    
    // Ipapasa natin ang Crop object at ang petsa (Today)
    List<Task> findByCropAndDueDate(Crop crop, LocalDate dueDate);
    
    // Panatilihin natin ito para sa "History/Cycle View" sa hinaharap
    List<Task> findByCrop(Crop crop);
}