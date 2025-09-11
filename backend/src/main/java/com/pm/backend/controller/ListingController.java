package com.pm.backend.controller;

import com.pm.backend.dto.ListingRequestDTO;
import com.pm.backend.dto.ListingResponseDTO;
import com.pm.backend.service.ListingService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/listings")
public class ListingController {
    private static final Logger logger = LoggerFactory.getLogger(ListingController.class);
    
    private final ListingService listingService;

    public ListingController(ListingService listingService) {
        this.listingService = listingService;
    }

    @GetMapping
    public ResponseEntity<?> getListings(
            @RequestParam(required = false) Integer page,
            @RequestParam(required = false) Integer size,
            @RequestParam(defaultValue = "zpid") String sortBy,
            @RequestParam(defaultValue = "ASC") String sortDir) {
        
        // If pagination parameters are provided, return paginated result
        if (page != null || size != null) {
            int pageNum = page != null ? page : 0;
            int pageSize = size != null ? size : 20;
            
            Page<ListingResponseDTO> paginatedListings = listingService.getListings(pageNum, pageSize, sortBy, sortDir);
            return ResponseEntity.ok(paginatedListings);
        } else {
            // For backward compatibility, return all listings if no pagination params
            List<ListingResponseDTO> listings = listingService.getListings();
            return ResponseEntity.ok(listings);
        }
    }

    @GetMapping("/{zpid}")
    public ResponseEntity<ListingResponseDTO> getListing(@PathVariable String zpid) {
        Optional<ListingResponseDTO> listing = listingService.getListing(zpid);
        
        return listing
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ListingResponseDTO> createListing(@Valid @RequestBody ListingRequestDTO listingRequestDTO) {
        ListingResponseDTO listingResponseDTO = listingService.createListing(listingRequestDTO);
        return ResponseEntity.ok().body(listingResponseDTO);
    }

    @PutMapping("/{zpid}")
    public ResponseEntity<ListingResponseDTO> updateListing(@PathVariable String zpid, @Valid @RequestBody ListingRequestDTO listingRequestDTO) {
        ListingResponseDTO listingResponseDTO = listingService.updateListing(zpid, listingRequestDTO);
        return ResponseEntity.ok().body(listingResponseDTO);
    }

    @DeleteMapping("/{zpid}")
    public ResponseEntity<ListingResponseDTO> deleteListing(@PathVariable String zpid) {
        listingService.deleteListing(zpid);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/bulk-import")
    public ResponseEntity<ListingService.BulkImportResult> bulkImportListings(
            @RequestBody List<ListingRequestDTO> listingDTOs) {
        
        logger.info("Received bulk import request for {} listings", listingDTOs.size());
        
        if (listingDTOs.isEmpty()) {
            logger.warn("Empty listing array received for bulk import");
            return ResponseEntity.badRequest().build();
        }

        try {
            ListingService.BulkImportResult result = listingService.bulkImportListings(listingDTOs);
            
            if (result.getErrorCount() == 0) {
                logger.info("Bulk import successful: {}", result);
                return ResponseEntity.ok(result);
            } else if (result.getSuccessCount() > 0) {
                logger.warn("Bulk import completed with errors: {}", result);
                return ResponseEntity.status(207).body(result); // 207 Multi-Status for partial success
            } else {
                logger.error("Bulk import failed completely: {}", result);
                return ResponseEntity.status(422).body(result); // 422 Unprocessable Entity
            }
            
        } catch (Exception e) {
            logger.error("Bulk import failed with exception", e);
            return ResponseEntity.status(500).build();
        }
    }
}
