package com.cropmanager.cropsync;

import jakarta.persistence.*;

@Entity
public class CropTemplate {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String cropType; // e.g., "Eggplant"
    private String taskName; // e.g., "Fertilizing"
    private int dayNumber;   // e.g., 1, 15, 90

    // 1. Empty Constructor (Required by JPA)
    public CropTemplate() {}

    // 2. Full Constructor (For easy data entry)
    public CropTemplate(String cropType, String taskName, int dayNumber) {
        this.cropType = cropType;
        this.taskName = taskName;
        this.dayNumber = dayNumber;
    }

    // 3. Getters and Setters
    public Long getId() { return id; }
    public String getCropType() { return cropType; }
    public void setCropType(String cropType) { this.cropType = cropType; }
    public String getTaskName() { return taskName; }
    public void setTaskName(String taskName) { this.taskName = taskName; }
    public int getDayNumber() { return dayNumber; }
    public void setDayNumber(int dayNumber) { this.dayNumber = dayNumber; }
}