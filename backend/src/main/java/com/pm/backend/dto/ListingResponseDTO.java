package com.pm.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pm.backend.model.Listing;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ListingResponseDTO {
    
    // Core identification
    private String zpid;
    
    // Address information
    private String address;
    @JsonProperty("addressStreet")
    private String addressStreet;
    @JsonProperty("addressCity") 
    private String addressCity;
    @JsonProperty("addressState")
    private String addressState;
    @JsonProperty("addressZipcode")
    private String addressZipcode;
    
    // Location coordinates
    private BigDecimal latitude;
    private BigDecimal longitude;
    
    // Display information
    @JsonProperty("imgSrc")
    private String imgSrc;
    @JsonProperty("detailUrl")
    private String detailUrl;
    @JsonProperty("statusText")
    private String statusText;
    
    // Property details
    private String price; // Formatted as string for API response
    private Integer beds;
    private Integer baths;
    private Integer area;
    @JsonProperty("hasVideo")
    private Boolean hasVideo;
    
    // Financial information
    private Integer zestimate;
    
    // Availability
    @JsonProperty("availabilityDate")
    private LocalDateTime availabilityDate;
    
    // Complex data - can be parsed from JSON strings or kept as objects
    @JsonProperty("variableData")
    private String variableData;
    
    @JsonProperty("carouselPhotos")
    private String carouselPhotosComposable;
    
    @JsonProperty("factsAndFeatures")
    private String factsAndFeatures;
    
    // Auditing fields
    @JsonProperty("createdAt")
    private LocalDateTime createdAt;
    
    @JsonProperty("updatedAt")
    private LocalDateTime updatedAt;
    
    // Default constructor
    public ListingResponseDTO() {}
    
    // Constructor from Listing entity
    public ListingResponseDTO(Listing listing) {
        this.zpid = listing.getZpid();
        this.address = listing.getAddress();
        this.addressStreet = listing.getAddressStreet();
        this.addressCity = listing.getAddressCity();
        this.addressState = listing.getAddressState();
        this.addressZipcode = listing.getAddressZipcode();
        this.latitude = listing.getLatitude();
        this.longitude = listing.getLongitude();
        this.imgSrc = listing.getImgSrc();
        this.detailUrl = listing.getDetailUrl();
        this.statusText = listing.getStatusText();
        this.price = listing.getPriceAsString(); // Use formatted string for API
        this.beds = listing.getBeds();
        this.baths = listing.getBaths();
        this.area = listing.getArea();
        this.hasVideo = listing.getHasVideo();
        this.zestimate = listing.getZestimate();
        this.availabilityDate = listing.getAvailabilityDate();
        this.variableData = listing.getVariableData();
        this.carouselPhotosComposable = listing.getCarouselPhotosComposable();
        this.factsAndFeatures = listing.getFactsAndFeatures();
        this.createdAt = listing.getCreatedAt();
        this.updatedAt = listing.getUpdatedAt();
    }
    
    // Static factory method for cleaner object creation
    public static ListingResponseDTO from(Listing listing) {
        return new ListingResponseDTO(listing);
    }
    
    // Getters and Setters
    public String getZpid() { return zpid; }
    public void setZpid(String zpid) { this.zpid = zpid; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public String getAddressStreet() { return addressStreet; }
    public void setAddressStreet(String addressStreet) { this.addressStreet = addressStreet; }
    
    public String getAddressCity() { return addressCity; }
    public void setAddressCity(String addressCity) { this.addressCity = addressCity; }
    
    public String getAddressState() { return addressState; }
    public void setAddressState(String addressState) { this.addressState = addressState; }
    
    public String getAddressZipcode() { return addressZipcode; }
    public void setAddressZipcode(String addressZipcode) { this.addressZipcode = addressZipcode; }
    
    public BigDecimal getLatitude() { return latitude; }
    public void setLatitude(BigDecimal latitude) { this.latitude = latitude; }
    
    public BigDecimal getLongitude() { return longitude; }
    public void setLongitude(BigDecimal longitude) { this.longitude = longitude; }
    
    public String getImgSrc() { return imgSrc; }
    public void setImgSrc(String imgSrc) { this.imgSrc = imgSrc; }
    
    public String getDetailUrl() { return detailUrl; }
    public void setDetailUrl(String detailUrl) { this.detailUrl = detailUrl; }
    
    public String getStatusText() { return statusText; }
    public void setStatusText(String statusText) { this.statusText = statusText; }
    
    public String getPrice() { return price; }
    public void setPrice(String price) { this.price = price; }
    
    public Integer getBeds() { return beds; }
    public void setBeds(Integer beds) { this.beds = beds; }
    
    public Integer getBaths() { return baths; }
    public void setBaths(Integer baths) { this.baths = baths; }
    
    public Integer getArea() { return area; }
    public void setArea(Integer area) { this.area = area; }
    
    public Boolean getHasVideo() { return hasVideo; }
    public void setHasVideo(Boolean hasVideo) { this.hasVideo = hasVideo; }
    
    public Integer getZestimate() { return zestimate; }
    public void setZestimate(Integer zestimate) { this.zestimate = zestimate; }
    
    public LocalDateTime getAvailabilityDate() { return availabilityDate; }
    public void setAvailabilityDate(LocalDateTime availabilityDate) { this.availabilityDate = availabilityDate; }
    
    public String getVariableData() { return variableData; }
    public void setVariableData(String variableData) { this.variableData = variableData; }
    
    public String getCarouselPhotosComposable() { return carouselPhotosComposable; }
    public void setCarouselPhotosComposable(String carouselPhotosComposable) { this.carouselPhotosComposable = carouselPhotosComposable; }
    
    public String getFactsAndFeatures() { return factsAndFeatures; }
    public void setFactsAndFeatures(String factsAndFeatures) { this.factsAndFeatures = factsAndFeatures; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    @Override
    public String toString() {
        return "ListingResponseDTO{" +
                "zpid='" + zpid + '\'' +
                ", address='" + address + '\'' +
                ", price='" + price + '\'' +
                ", beds=" + beds +
                ", baths=" + baths +
                ", area=" + area +
                ", statusText='" + statusText + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}';
    }
}
