package com.pm.backend.config;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pm.backend.dto.ListingRequestDTO;
import com.pm.backend.service.ListingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class DataSeeder implements CommandLineRunner {
    
    private static final Logger logger = LoggerFactory.getLogger(DataSeeder.class);
    
    private final ListingService listingService;
    private final ObjectMapper objectMapper;
    
    @Value("${app.seeding.enabled:true}")
    private boolean seedingEnabled;
    
    @Value("${app.seeding.file-path:data/zillow_data.json}")
    private String seedFilePath;

    public DataSeeder(ListingService listingService, ObjectMapper objectMapper) {
        this.listingService = listingService;
        this.objectMapper = objectMapper;
    }

    @Override
    public void run(String... args) throws Exception {
        if (!seedingEnabled) {
            logger.info("🚫 Database seeding is disabled");
            return;
        }
        
        if (shouldSeedDatabase()) {
            seedDatabase();
        } else {
            logger.info("📊 Database already contains listings, skipping seeding");
        }
    }

    private boolean shouldSeedDatabase() {
        try {
            // Check if we already have listings
            List<?> existingListings = listingService.getListings();
            boolean isEmpty = existingListings.isEmpty();
            
            if (!isEmpty) {
                logger.info("📋 Found {} existing listings in database", existingListings.size());
            }
            
            return isEmpty;
        } catch (Exception e) {
            logger.error("❌ Error checking existing listings", e);
            return false;
        }
    }

    private void seedDatabase() {
        logger.info("🌱 Starting database seeding...");
        
        try {
            // Read JSON file from resources
            ClassPathResource resource = new ClassPathResource(seedFilePath);
            
            if (!resource.exists()) {
                logger.warn("❌ Seed data file not found at: {}. Skipping database seeding.", seedFilePath);
                return;
            }
            
            logger.info("📂 Loading seed data from: {}", seedFilePath);
            
            InputStream inputStream = resource.getInputStream();
            
            // Read as JsonNode array first to handle data transformation
            JsonNode rootNode = objectMapper.readTree(inputStream);
            
            if (!rootNode.isArray()) {
                logger.error("❌ Seed data file must contain an array of listings");
                return;
            }
            
            List<ListingRequestDTO> listings = new ArrayList<>();
            int skippedCount = 0;
            
            // Transform each JSON object to ListingRequestDTO
            for (JsonNode listingNode : rootNode) {
                try {
                    ListingRequestDTO dto = transformZillowData(listingNode);
                    listings.add(dto);
                } catch (Exception e) {
                    logger.warn("⚠️  Skipping invalid listing: {}", e.getMessage());
                    skippedCount++;
                }
            }
            
            logger.info("📊 Processed {} listings from file", rootNode.size());
            logger.info("✅ Successfully transformed {} listings", listings.size());
            
            if (skippedCount > 0) {
                logger.warn("⚠️  Skipped {} invalid listings", skippedCount);
            }
            
            if (listings.isEmpty()) {
                logger.warn("❌ No valid listings to import");
                return;
            }
            
            // Use existing bulk import
            ListingService.BulkImportResult result = listingService.bulkImportListings(listings);
            
            logger.info("🎉 Database seeding completed!");
            logger.info("✅ Successfully imported: {} listings", result.getSuccessCount());
            logger.info("❌ Failed imports: {} listings", result.getErrorCount());
            logger.info("📈 Success rate: {:.1f}%", result.getSuccessRate());
            
            if (!result.getErrors().isEmpty()) {
                logger.warn("⚠️  First few errors:");
                result.getErrors().stream()
                    .limit(5)
                    .forEach(error -> logger.warn("   • {}", error));
                
                if (result.getErrors().size() > 5) {
                    logger.warn("   • ... and {} more errors", result.getErrors().size() - 5);
                }
            }
            
        } catch (IOException e) {
            logger.error("💥 Failed to read seed data file: {}", e.getMessage(), e);
        } catch (Exception e) {
            logger.error("💥 Database seeding failed: {}", e.getMessage(), e);
        }
    }
    
    private ListingRequestDTO transformZillowData(JsonNode zillowNode) throws Exception {
        ListingRequestDTO dto = new ListingRequestDTO();
        
        // Required fields - validate they exist
        if (!zillowNode.has("zpid") || zillowNode.get("zpid").asText().trim().isEmpty()) {
            throw new IllegalArgumentException("Missing required field: zpid");
        }
        
        dto.setZpid(zillowNode.get("zpid").asText());
        
        // Address fields
        dto.setAddress(getTextValue(zillowNode, "address"));
        dto.setAddressStreet(getTextValue(zillowNode, "addressStreet"));
        dto.setAddressCity(getTextValue(zillowNode, "addressCity"));
        dto.setAddressState(getTextValue(zillowNode, "addressState"));
        dto.setAddressZipcode(getTextValue(zillowNode, "addressZipcode"));
        
        // Validate required address fields
        if (dto.getAddress() == null || dto.getAddressStreet() == null || 
            dto.getAddressCity() == null || dto.getAddressState() == null || 
            dto.getAddressZipcode() == null) {
            throw new IllegalArgumentException("Missing required address fields");
        }
        
        // Location coordinates
        JsonNode latLongNode = zillowNode.get("latLong");
        if (latLongNode != null && !latLongNode.isNull()) {
            ListingRequestDTO.LatLong latLong = new ListingRequestDTO.LatLong();
            
            if (latLongNode.has("latitude") && latLongNode.has("longitude")) {
                latLong.setLatitude(BigDecimal.valueOf(latLongNode.get("latitude").asDouble()));
                latLong.setLongitude(BigDecimal.valueOf(latLongNode.get("longitude").asDouble()));
                dto.setLatLong(latLong);
            }
        }
        
        if (dto.getLatLong() == null) {
            throw new IllegalArgumentException("Missing required coordinates");
        }
        
        // Display fields
        dto.setImgSrc(getTextValue(zillowNode, "imgSrc"));
        dto.setDetailUrl(getTextValue(zillowNode, "detailUrl"));
        dto.setStatusText(getTextValue(zillowNode, "statusText"));
        
        // Validate required display fields
        if (dto.getImgSrc() == null || dto.getDetailUrl() == null || dto.getStatusText() == null) {
            throw new IllegalArgumentException("Missing required display fields");
        }
        
        // Optional property details (set defaults for missing fields)
        dto.setPrice(getTextValue(zillowNode, "price")); // May be null
        dto.setBeds(getIntegerValue(zillowNode, "beds"));
        dto.setBaths(getIntegerValue(zillowNode, "baths"));
        dto.setArea(getIntegerValue(zillowNode, "area"));
        dto.setHasVideo(getBooleanValue(zillowNode, "hasVideo", false));
        dto.setZestimate(getIntegerValue(zillowNode, "zestimate"));
        
        // Complex data - keep as JsonNode
        if (zillowNode.has("variableData") && !zillowNode.get("variableData").isNull()) {
            dto.setVariableData(zillowNode.get("variableData"));
        }
        
        if (zillowNode.has("carouselPhotosComposable") && !zillowNode.get("carouselPhotosComposable").isNull()) {
            dto.setCarouselPhotosComposable(zillowNode.get("carouselPhotosComposable"));
        }
        
        if (zillowNode.has("factsAndFeatures") && !zillowNode.get("factsAndFeatures").isNull()) {
            dto.setFactsAndFeatures(zillowNode.get("factsAndFeatures"));
        }
        
        return dto;
    }
    
    private String getTextValue(JsonNode node, String fieldName) {
        JsonNode fieldNode = node.get(fieldName);
        if (fieldNode != null && !fieldNode.isNull() && !fieldNode.asText().trim().isEmpty()) {
            return fieldNode.asText().trim();
        }
        return null;
    }
    
    private Integer getIntegerValue(JsonNode node, String fieldName) {
        JsonNode fieldNode = node.get(fieldName);
        if (fieldNode != null && !fieldNode.isNull() && fieldNode.isNumber()) {
            return fieldNode.asInt();
        }
        return null;
    }
    
    private Boolean getBooleanValue(JsonNode node, String fieldName, Boolean defaultValue) {
        JsonNode fieldNode = node.get(fieldName);
        if (fieldNode != null && !fieldNode.isNull()) {
            return fieldNode.asBoolean(defaultValue);
        }
        return defaultValue;
    }
}
