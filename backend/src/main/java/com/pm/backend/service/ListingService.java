package com.pm.backend.service;

import com.pm.backend.dto.ListingRequestDTO;
import com.pm.backend.dto.ListingResponseDTO;
import com.pm.backend.exception.AddressAlreadyExistsException;
import com.pm.backend.exception.ListingNotFoundException;
import com.pm.backend.exception.ZpidAlreadyExistsException;
import com.pm.backend.mapper.ListingMapper;
import com.pm.backend.model.Listing;
import com.pm.backend.repository.ListingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ListingService {
    private static final Logger logger = LoggerFactory.getLogger(ListingService.class);
    private static final int BATCH_SIZE = 100; // Process in batches for memory efficiency
    
    private final ListingRepository listingRepository;

    public ListingService(ListingRepository listingRepository) {
        this.listingRepository = listingRepository;
    }

    public List<ListingResponseDTO> getListings() {
        List<Listing> listings = listingRepository.findAll();
        return ListingMapper.toDTOList(listings);
    }

    public Page<ListingResponseDTO> getListings(int page, int size, String sortBy, String sortDir) {
        // Validate pagination parameters
        if (page < 0) page = 0;
        if (size <= 0 || size > 100) size = 20; // Max 100 per page to prevent abuse
        
        // Validate sortBy field to prevent injection
        String validSortBy = validateSortField(sortBy);
        
        // Create sort object
        Sort.Direction direction = "DESC".equalsIgnoreCase(sortDir) ? Sort.Direction.DESC : Sort.Direction.ASC;
        Sort sort = Sort.by(direction, validSortBy);
        
        // Create pageable object
        Pageable pageable = PageRequest.of(page, size, sort);
        
        // Get paginated results
        Page<Listing> listingPage = listingRepository.findAll(pageable);
        
        // Convert to DTO page
        return listingPage.map(ListingMapper::toDTO);
    }

    private String validateSortField(String sortBy) {
        // Whitelist allowed sort fields to prevent injection
        return switch (sortBy.toLowerCase()) {
            case "zpid" -> "zpid";
            case "price" -> "price";
            case "beds" -> "beds";
            case "baths" -> "baths";
            case "area" -> "area";
            case "city" -> "addressCity";
            case "state" -> "addressState";
            case "createdat" -> "createdAt";
            case "updatedat" -> "updatedAt";
            default -> "zpid"; // Default sort by zpid
        };
    }

    public Optional<ListingResponseDTO> getListing(String zpid) {
        Optional<Listing> listing = listingRepository.findById(zpid);
        return listing.map(ListingMapper::toDTO);
    }

    public ListingResponseDTO createListing(ListingRequestDTO listingRequestDTO) {
        if(listingRepository.existsByZpid(listingRequestDTO.getZpid())){
            throw new ZpidAlreadyExistsException("A listing with this zpid already exists: " + listingRequestDTO.getZpid());
        }

        if(listingRepository.existsByAddress(listingRequestDTO.getAddress())){
            throw new AddressAlreadyExistsException("A listing with this address already exists: " + listingRequestDTO.getAddress());
        }

        Listing newListing = listingRepository.save(ListingMapper.toModel(listingRequestDTO));
        return ListingMapper.toDTO(newListing);
    }

    public ListingResponseDTO updateListing(String zpid, ListingRequestDTO listingRequestDTO) {
        Listing listing = listingRepository.findById(zpid)
                .orElseThrow(() -> new ListingNotFoundException("Listing not found with zpid: " + zpid));

        // Check for address conflicts (only if the address is actually changing)
        if (!listing.getAddress().equals(listingRequestDTO.getAddress()) && 
            listingRepository.existsByAddress(listingRequestDTO.getAddress())) {
            throw new AddressAlreadyExistsException("A listing with this address already exists: " + listingRequestDTO.getAddress());
        }

        // Update basic address information
        listing.setAddress(listingRequestDTO.getAddress());
        listing.setAddressStreet(listingRequestDTO.getAddressStreet());
        listing.setAddressCity(listingRequestDTO.getAddressCity());
        listing.setAddressState(listingRequestDTO.getAddressState());
        listing.setAddressZipcode(listingRequestDTO.getAddressZipcode());

        // Update location coordinates - extract from nested latLong object
        if (listingRequestDTO.getLatLong() != null) {
            listing.setLatitude(listingRequestDTO.getLatLong().getLatitude());
            listing.setLongitude(listingRequestDTO.getLatLong().getLongitude());
        }

        // Update display information
        listing.setImgSrc(listingRequestDTO.getImgSrc());
        listing.setDetailUrl(listingRequestDTO.getDetailUrl());
        listing.setStatusText(listingRequestDTO.getStatusText());

        // Update property details - parse price string to BigDecimal
        if (listingRequestDTO.getPrice() != null && !listingRequestDTO.getPrice().isEmpty()) {
            listing.setPriceFromString(listingRequestDTO.getPrice());
        } else {
            listing.setPrice(null);
        }
        listing.setBeds(listingRequestDTO.getBeds());
        listing.setBaths(listingRequestDTO.getBaths());
        listing.setArea(listingRequestDTO.getArea());
        listing.setHasVideo(listingRequestDTO.getHasVideo());

        // Update financial information
        listing.setZestimate(listingRequestDTO.getZestimate());

        // Update availability
        listing.setAvailabilityDate(listingRequestDTO.getAvailabilityDate());

        // Update complex data - convert JsonNode objects to JSON strings
        if (listingRequestDTO.getVariableData() != null) {
            listing.setVariableData(listingRequestDTO.getVariableData().toString());
        } else {
            listing.setVariableData(null);
        }
        
        if (listingRequestDTO.getCarouselPhotosComposable() != null) {
            listing.setCarouselPhotosComposable(listingRequestDTO.getCarouselPhotosComposable().toString());
        } else {
            listing.setCarouselPhotosComposable(null);
        }
        
        if (listingRequestDTO.getFactsAndFeatures() != null) {
            listing.setFactsAndFeatures(listingRequestDTO.getFactsAndFeatures().toString());
        } else {
            listing.setFactsAndFeatures(null);
        }

        // Save updated entity and return DTO
        Listing updatedListing = listingRepository.save(listing);
        return ListingMapper.toDTO(updatedListing);
    }

    public void deleteListing(String zpid) {
        if (!listingRepository.existsById(zpid)) {
            throw new ListingNotFoundException("Listing not found with zpid: " + zpid);
        }
        listingRepository.deleteById(zpid);
    }

    @Transactional
    public BulkImportResult bulkImportListings(List<ListingRequestDTO> listingDTOs) {
        logger.info("Starting bulk import of {} listings", listingDTOs.size());
        
        int totalCount = listingDTOs.size();
        int successCount = 0;
        int errorCount = 0;
        List<String> errors = new ArrayList<>();
        
        // Process in batches for better memory management
        for (int i = 0; i < listingDTOs.size(); i += BATCH_SIZE) {
            int endIndex = Math.min(i + BATCH_SIZE, listingDTOs.size());
            List<ListingRequestDTO> batch = listingDTOs.subList(i, endIndex);
            
            logger.info("Processing batch {}-{} of {}", i + 1, endIndex, totalCount);
            
            List<Listing> batchListings = new ArrayList<>();
            
            // Convert DTOs to entities, tracking errors
            for (int j = 0; j < batch.size(); j++) {
                try {
                    ListingRequestDTO dto = batch.get(j);
                    Listing listing = ListingMapper.toModel(dto);
                    batchListings.add(listing);
                } catch (Exception e) {
                    errorCount++;
                    String error = String.format("Row %d: %s", i + j + 1, e.getMessage());
                    errors.add(error);
                    logger.warn("Failed to process listing {}: {}", i + j + 1, e.getMessage());
                }
            }
            
            // Batch save to database
            try {
                List<Listing> savedListings = listingRepository.saveAll(batchListings);
                successCount += savedListings.size();
                logger.info("Successfully saved batch of {} listings", savedListings.size());
            } catch (Exception e) {
                errorCount += batchListings.size();
                String error = String.format("Batch %d-%d: Database save failed - %s", i + 1, endIndex, e.getMessage());
                errors.add(error);
                logger.error("Failed to save batch {}-{}: {}", i + 1, endIndex, e.getMessage());
            }
        }
        
        BulkImportResult result = new BulkImportResult(totalCount, successCount, errorCount, errors);
        logger.info("Bulk import completed: {} successful, {} errors out of {} total", 
                   successCount, errorCount, totalCount);
        
        return result;
    }

    public static class BulkImportResult {
        private final int totalCount;
        private final int successCount;
        private final int errorCount;
        private final List<String> errors;

        public BulkImportResult(int totalCount, int successCount, int errorCount, List<String> errors) {
            this.totalCount = totalCount;
            this.successCount = successCount;
            this.errorCount = errorCount;
            this.errors = errors;
        }

        public int getTotalCount() { return totalCount; }
        public int getSuccessCount() { return successCount; }
        public int getErrorCount() { return errorCount; }
        public List<String> getErrors() { return errors; }
        public double getSuccessRate() { 
            return totalCount > 0 ? (double) successCount / totalCount * 100 : 0;
        }

        @Override
        public String toString() {
            return String.format("BulkImportResult{total=%d, success=%d, errors=%d, successRate=%.1f%%}", 
                               totalCount, successCount, errorCount, getSuccessRate());
        }
    }
}
