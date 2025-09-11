package com.pm.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;
import jakarta.validation.constraints.*;
import java.time.LocalDateTime;
import java.math.BigDecimal;

public class ListingRequestDTO {
    
    // Core identification
    @NotBlank(message = "ZPID is required")
    private String zpid;
    
    // Address information
    @NotBlank(message = "Address is required")
    private String address;
    
    @NotBlank(message = "Street address is required")
    @JsonProperty("addressStreet")
    private String addressStreet;
    
    @NotBlank(message = "City is required")
    @JsonProperty("addressCity")
    private String addressCity;
    
    @NotBlank(message = "State is required")
    @JsonProperty("addressState")
    private String addressState;
    
    @NotBlank(message = "Zip code is required")
    @JsonProperty("addressZipcode")
    private String addressZipcode;
    
    // Location coordinates
    @NotNull(message = "Location coordinates are required")
    @JsonProperty("latLong")
    private LatLong latLong;
    
    // Display information
    @NotBlank(message = "Image URL is required")
    @JsonProperty("imgSrc")
    private String imgSrc;
    
    @NotBlank(message = "Detail URL is required")
    @JsonProperty("detailUrl")
    private String detailUrl;
    
    @NotBlank(message = "Status text is required")
    @JsonProperty("statusText")
    private String statusText;
    
    // Property details
    private String price; // Accept as string to handle "$1,950/mo" format
    
    @Min(value = 0, message = "Number of beds must be non-negative")
    @Max(value = 50, message = "Number of beds seems unrealistic")
    private Integer beds;
    
    @Min(value = 0, message = "Number of baths must be non-negative") 
    @Max(value = 50, message = "Number of baths seems unrealistic")
    private Integer baths;
    
    @Min(value = 1, message = "Area must be positive")
    @Max(value = 1000000, message = "Area seems unrealistic")
    private Integer area;
    
    @JsonProperty("hasVideo")
    private Boolean hasVideo;
    
    // Financial information - optional
    @Min(value = 0, message = "Zestimate must be non-negative")
    private Integer zestimate;
    
    // Availability - optional
    @JsonProperty("availabilityDate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime availabilityDate;
    
    // Complex data 
    @JsonProperty("variableData")
    private JsonNode variableData;
    
    @JsonProperty("carouselPhotosComposable")
    private JsonNode carouselPhotosComposable;
    
    @JsonProperty("factsAndFeatures")
    private JsonNode factsAndFeatures;
    
    // Default constructor
    public ListingRequestDTO() {}
    
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
    
    public LatLong getLatLong() { return latLong; }
    public void setLatLong(LatLong latLong) { this.latLong = latLong; }
    
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
    
    public JsonNode getVariableData() { return variableData; }
    public void setVariableData(JsonNode variableData) { this.variableData = variableData; }
    
    public JsonNode getCarouselPhotosComposable() { return carouselPhotosComposable; }
    public void setCarouselPhotosComposable(JsonNode carouselPhotosComposable) { this.carouselPhotosComposable = carouselPhotosComposable; }
    
    public JsonNode getFactsAndFeatures() { return factsAndFeatures; }
    public void setFactsAndFeatures(JsonNode factsAndFeatures) { this.factsAndFeatures = factsAndFeatures; }
    
    @Override
    public String toString() {
        return "ListingRequestDTO{" +
                "zpid='" + zpid + '\'' +
                ", address='" + address + '\'' +
                ", price=" + price +
                ", beds=" + beds +
                ", baths=" + baths +
                ", area=" + area +
                ", latLong=" + latLong +
                '}';
    }

    // Nested LatLong class to match Zillow data structure
    public static class LatLong {
        private BigDecimal latitude;
        private BigDecimal longitude;

        public LatLong() {}

        public LatLong(BigDecimal latitude, BigDecimal longitude) {
            this.latitude = latitude;
            this.longitude = longitude;
        }

        public BigDecimal getLatitude() { return latitude; }
        public void setLatitude(BigDecimal latitude) { this.latitude = latitude; }

        public BigDecimal getLongitude() { return longitude; }
        public void setLongitude(BigDecimal longitude) { this.longitude = longitude; }

        @Override
        public String toString() {
            return "LatLong{" +
                    "latitude=" + latitude +
                    ", longitude=" + longitude +
                    '}';
        }
    }
}
