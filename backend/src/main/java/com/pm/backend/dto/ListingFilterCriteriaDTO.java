package com.pm.backend.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class ListingFilterCriteriaDTO {
    
    // Price range filtering
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    
    // Area range filtering
    private Integer minArea;
    private Integer maxArea;
    
    // Location filtering
    private List<String> cities;
    private List<String> zipCodes;
    
    // Room filtering
    private Integer beds;
    private Integer baths;
    
    // Availability filtering
    private LocalDateTime availableBy;
    
    // Constructors
    public ListingFilterCriteriaDTO() {}
    
    public ListingFilterCriteriaDTO(BigDecimal minPrice, BigDecimal maxPrice, Integer minArea, Integer maxArea,
                                   List<String> cities, List<String> zipCodes, Integer beds, Integer baths,
                                   LocalDateTime availableBy) {
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.minArea = minArea;
        this.maxArea = maxArea;
        this.cities = cities;
        this.zipCodes = zipCodes;
        this.beds = beds;
        this.baths = baths;
        this.availableBy = availableBy;
    }
    
    // Getters and Setters
    public BigDecimal getMinPrice() { return minPrice; }
    public void setMinPrice(BigDecimal minPrice) { this.minPrice = minPrice; }
    
    public BigDecimal getMaxPrice() { return maxPrice; }
    public void setMaxPrice(BigDecimal maxPrice) { this.maxPrice = maxPrice; }
    
    public Integer getMinArea() { return minArea; }
    public void setMinArea(Integer minArea) { this.minArea = minArea; }
    
    public Integer getMaxArea() { return maxArea; }
    public void setMaxArea(Integer maxArea) { this.maxArea = maxArea; }
    
    public List<String> getCities() { return cities; }
    public void setCities(List<String> cities) { this.cities = cities; }
    
    public List<String> getZipCodes() { return zipCodes; }
    public void setZipCodes(List<String> zipCodes) { this.zipCodes = zipCodes; }
    
    public Integer getBeds() { return beds; }
    public void setBeds(Integer beds) { this.beds = beds; }
    
    public Integer getBaths() { return baths; }
    public void setBaths(Integer baths) { this.baths = baths; }
    
    public LocalDateTime getAvailableBy() { return availableBy; }
    public void setAvailableBy(LocalDateTime availableBy) { this.availableBy = availableBy; }
    
    // Helper methods to check if filters are applied
    public boolean hasFilters() {
        return minPrice != null || maxPrice != null || minArea != null || maxArea != null ||
               (cities != null && !cities.isEmpty()) || (zipCodes != null && !zipCodes.isEmpty()) ||
               beds != null || baths != null || availableBy != null;
    }
    
    @Override
    public String toString() {
        return "ListingFilterCriteriaDTO{" +
                "minPrice=" + minPrice +
                ", maxPrice=" + maxPrice +
                ", minArea=" + minArea +
                ", maxArea=" + maxArea +
                ", cities=" + cities +
                ", zipCodes=" + zipCodes +
                ", beds=" + beds +
                ", baths=" + baths +
                ", availableBy=" + availableBy +
                '}';
    }
}
