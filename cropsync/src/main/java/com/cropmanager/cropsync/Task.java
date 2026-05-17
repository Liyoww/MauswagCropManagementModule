package com.cropmanager.cropsync;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int dayOffset; 
    private String description;
    private LocalDate dueDate;
    private boolean completed = false; // Starts as unchecked
    
    @ManyToOne
    private Crop crop;

    public Task() {}

    public Task(String description, LocalDate dueDate,int dayOffset, Crop crop) {
        this.description = description;
        this.dueDate = dueDate;
        this.dayOffset = dayOffset; // Store the day number
        this.crop = crop;
    }

    // --- CRITICAL METHODS FOR THE CONTROLLER ---
    public Long getId() { return id; }
    
    public boolean isCompleted() { return completed; }
    
    public void setCompleted(boolean completed) { this.completed = completed; }

    public String getDescription() { return description; }
    public LocalDate getDueDate() { return dueDate; }

    public int getDayOffset() { return dayOffset; }
    public void setDayOffset(int dayOffset) { this.dayOffset = dayOffset; }
    public void setDescription(String description) { this.description = description; }
public void setDueDate(LocalDate dueDate) { this.dueDate = dueDate; }
public void setCrop(Crop crop) { this.crop = crop; }
}