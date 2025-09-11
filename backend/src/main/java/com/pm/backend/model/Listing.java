package com.pm.backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.LocalDateTime;
import java.math.BigDecimal;

@Entity
@Table(name = "property_listings")
@EntityListeners(AuditingEntityListener.class)
public class Listing {
    @Id
    private String zpid;

    // Required address fields
    @NotBlank
    @Column(nullable = false)
    private String address;

    @NotBlank
    @Column(name = "address_street", nullable = false)
    private String addressStreet;

    @NotBlank
    @Column(name = "address_city", nullable = false)
    private String addressCity;

    @NotBlank
    @Column(name = "address_state", nullable = false)
    private String addressState;

    @NotBlank
    @Column(name = "address_zipcode", nullable = false)
    private String addressZipcode;

    // Required location fields
    @NotNull
    @Column(nullable = false, precision = 10, scale = 7)
    private BigDecimal latitude;

    @NotNull
    @Column(nullable = false, precision = 10, scale = 7) 
    private BigDecimal longitude;

    // Required display fields
    @NotBlank
    @Column(name = "img_src", nullable = false, length = 500)
    private String imgSrc;

    @NotBlank
    @Column(name = "detail_url", nullable = false, length = 500)
    private String detailUrl;

    @NotBlank
    @Column(name = "status_text", nullable = false)
    private String statusText;

    // Variable data as JSON
    @Column(name = "variable_data", columnDefinition = "TEXT")
    @JdbcTypeCode(SqlTypes.JSON)
    private String variableData;

    // Property details
    @Column(precision = 10, scale = 2)
    private BigDecimal price;

    @Column
    private Integer beds;

    @Column
    private Integer baths;

    @Column
    private Integer area;

    @Column(name = "has_video")
    private Boolean hasVideo;

    // Financial data
    @Column
    private Integer zestimate;

    // Availability
    @Column(name = "availability_date")
    private LocalDateTime availabilityDate;

    // Complex data stored as JSON
    @Column(name = "carousel_photos", columnDefinition = "TEXT")
    @JdbcTypeCode(SqlTypes.JSON)
    private String carouselPhotosComposable;

    @Column(name = "facts_and_features", columnDefinition = "TEXT")
    @JdbcTypeCode(SqlTypes.JSON)
    private String factsAndFeatures;

    // Auditing fields
    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    // Constructors
    public Listing() {}

    public Listing(String zpid, String address, String addressStreet, String addressCity, 
                   String addressState, String addressZipcode, BigDecimal latitude, 
                   BigDecimal longitude, String imgSrc, String detailUrl, String statusText) {
        this.zpid = zpid;
        this.address = address;
        this.addressStreet = addressStreet;
        this.addressCity = addressCity;
        this.addressState = addressState;
        this.addressZipcode = addressZipcode;
        this.latitude = latitude;
        this.longitude = longitude;
        this.imgSrc = imgSrc;
        this.detailUrl = detailUrl;
        this.statusText = statusText;
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

    public String getVariableData() { return variableData; }
    public void setVariableData(String variableData) { this.variableData = variableData; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

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

    public String getCarouselPhotosComposable() { return carouselPhotosComposable; }
    public void setCarouselPhotosComposable(String carouselPhotosComposable) { this.carouselPhotosComposable = carouselPhotosComposable; }

    public String getFactsAndFeatures() { return factsAndFeatures; }
    public void setFactsAndFeatures(String factsAndFeatures) { this.factsAndFeatures = factsAndFeatures; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // Helper methods for price parsing
    public void setPriceFromString(String priceStr) {
        if (priceStr != null && !priceStr.isEmpty()) {
            // Remove "$", "/mo", and "," characters, then parse
            String cleanPrice = priceStr.replaceAll("[$,/mo]", "").trim();
            if (!cleanPrice.isEmpty()) {
                try {
                    this.price = new BigDecimal(cleanPrice);
                } catch (NumberFormatException e) {
                    // If parsing fails, set to null
                    this.price = null;
                }
            }
        }
    }

    public String getPriceAsString() {
        if (price != null) {
            return "$" + price.toString() + "/mo";
        }
        return null;
    }

    @Override
    public String toString() {
        return "Listing{" +
                "zpid='" + zpid + '\'' +
                ", address='" + address + '\'' +
                ", price=" + price +
                ", beds=" + beds +
                ", baths=" + baths +
                ", area=" + area +
                ", statusText='" + statusText + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
