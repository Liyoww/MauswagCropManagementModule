package com.cropmanager.cropsync;

import jakarta.persistence.*;

@Entity
public class Supply {
    
    @Id // DAPAT NANDITO ITO SA TAPAT NG ID
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 
    
    @ManyToOne // Hiwalay dapat ito
    @JoinColumn(name = "user_id")
    private User user;

    private String category;
    private String brandName;
    private Double weight;
    private String unit;
    private Double stockQuantity;   

    // 1. Empty Constructor
    public Supply() {}

    // 2. Full Constructor
    public Supply(String category, String brandName, Double weight, String unit, Double stockQuantity, User user) {
        this.category = category;
        this.brandName = brandName;
        this.weight = weight;
        this.unit = unit;
        this.stockQuantity = stockQuantity;
        this.user = user;
    }

    // --- GETTERS AND SETTERS ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; } // Importante: May setter dapat ang ID

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getBrandName() { return brandName; }
    public void setBrandName(String brandName) { this.brandName = brandName; }

    public Double getWeight() { return weight; }
    public void setWeight(Double weight) { this.weight = weight; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public Double getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(Double stockQuantity) { this.stockQuantity = stockQuantity; }
}