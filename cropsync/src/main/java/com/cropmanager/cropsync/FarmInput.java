package com.cropmanager.cropsync;

import jakarta.persistence.*;
import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonProperty;

@Entity
public class FarmInput {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "supply_id")
    private Supply supply; 

    private Double quantityUsed; 
    private Long cropId;

    // Bagong fields para sa Daily Tracking
    private String weather; 
    private String remarks;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private LocalDate dateApplied; 

    public FarmInput() {}

    // Updated Constructor para sa manual creation kung kailangan
    public FarmInput(Supply supply, Double quantityUsed, Long cropId, String weather, String remarks) {
        this.supply = supply;
        this.quantityUsed = quantityUsed;
        this.cropId = cropId;
        this.weather = weather;
        this.remarks = remarks;
        this.dateApplied = LocalDate.now();
    }

    // --- GETTERS AND SETTERS ---
    public Long getId() { return id; }

    public Supply getSupply() { return supply; }
    public void setSupply(Supply supply) { this.supply = supply; }

    public Double getQuantityUsed() { return quantityUsed; }
    public void setQuantityUsed(Double quantityUsed) { this.quantityUsed = quantityUsed; }

    public Long getCropId() { return cropId; }
    public void setCropId(Long cropId) { this.cropId = cropId; }

    public String getWeather() { return weather; }
    public void setWeather(String weather) { this.weather = weather; }

    public String getRemarks() { return remarks; }
    public void setRemarks(String remarks) { this.remarks = remarks; }

    public LocalDate getDateApplied() { return dateApplied; }
    public void setDateApplied(LocalDate dateApplied) { this.dateApplied = dateApplied; }
}