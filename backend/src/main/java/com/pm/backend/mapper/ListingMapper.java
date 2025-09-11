package com.pm.backend.mapper;

import com.pm.backend.dto.ListingRequestDTO;
import com.pm.backend.dto.ListingResponseDTO;
import com.pm.backend.model.Listing;
import java.util.List;
import java.util.stream.Collectors;

public class ListingMapper {

    public static ListingResponseDTO toDTO(Listing listing) {
        if (listing == null) {
            return null;
        }

        ListingResponseDTO dto = new ListingResponseDTO();
        
        // Core identification
        dto.setZpid(listing.getZpid());
        
        // Address information
        dto.setAddress(listing.getAddress());
        dto.setAddressStreet(listing.getAddressStreet());
        dto.setAddressCity(listing.getAddressCity());
        dto.setAddressState(listing.getAddressState());
        dto.setAddressZipcode(listing.getAddressZipcode());
        
        // Location coordinates
        dto.setLatitude(listing.getLatitude());
        dto.setLongitude(listing.getLongitude());
        
        // Display information
        dto.setImgSrc(listing.getImgSrc());
        dto.setDetailUrl(listing.getDetailUrl());
        dto.setStatusText(listing.getStatusText());
        
        // Property details - use formatted price string for API
        dto.setPrice(listing.getPriceAsString());
        dto.setBeds(listing.getBeds());
        dto.setBaths(listing.getBaths());
        dto.setArea(listing.getArea());
        dto.setHasVideo(listing.getHasVideo());
        
        // Financial information
        dto.setZestimate(listing.getZestimate());
        
        // Availability
        dto.setAvailabilityDate(listing.getAvailabilityDate());
        
        // Complex data (JSON strings)
        dto.setVariableData(listing.getVariableData());
        dto.setCarouselPhotosComposable(listing.getCarouselPhotosComposable());
        dto.setFactsAndFeatures(listing.getFactsAndFeatures());
        
        // Auditing fields
        dto.setCreatedAt(listing.getCreatedAt());
        dto.setUpdatedAt(listing.getUpdatedAt());
        
        return dto;
    }

    public static List<ListingResponseDTO> toDTOList(List<Listing> listings) {
        if (listings == null) {
            return List.of();
        }
        
        return listings.stream()
                .map(ListingMapper::toDTO)
                .collect(Collectors.toList());
    }

    public static ListingResponseDTO updateDTO(Listing listing, ListingResponseDTO dto) {
        if (listing == null || dto == null) {
            return dto;
        }

        // Update all fields from the listing
        dto.setZpid(listing.getZpid());
        dto.setAddress(listing.getAddress());
        dto.setAddressStreet(listing.getAddressStreet());
        dto.setAddressCity(listing.getAddressCity());
        dto.setAddressState(listing.getAddressState());
        dto.setAddressZipcode(listing.getAddressZipcode());
        dto.setLatitude(listing.getLatitude());
        dto.setLongitude(listing.getLongitude());
        dto.setImgSrc(listing.getImgSrc());
        dto.setDetailUrl(listing.getDetailUrl());
        dto.setStatusText(listing.getStatusText());
        dto.setPrice(listing.getPriceAsString());
        dto.setBeds(listing.getBeds());
        dto.setBaths(listing.getBaths());
        dto.setArea(listing.getArea());
        dto.setHasVideo(listing.getHasVideo());
        dto.setZestimate(listing.getZestimate());
        dto.setAvailabilityDate(listing.getAvailabilityDate());
        dto.setVariableData(listing.getVariableData());
        dto.setCarouselPhotosComposable(listing.getCarouselPhotosComposable());
        dto.setFactsAndFeatures(listing.getFactsAndFeatures());
        dto.setCreatedAt(listing.getCreatedAt());
        dto.setUpdatedAt(listing.getUpdatedAt());

        return dto;
    }

    public static Listing toModel(ListingRequestDTO listingRequestDTO) {
        if (listingRequestDTO == null) {
            return null;
        }

        Listing listing = new Listing();
        
        // Core identification
        listing.setZpid(listingRequestDTO.getZpid());
        
        // Address information
        listing.setAddress(listingRequestDTO.getAddress());
        listing.setAddressStreet(listingRequestDTO.getAddressStreet());
        listing.setAddressCity(listingRequestDTO.getAddressCity());
        listing.setAddressState(listingRequestDTO.getAddressState());
        listing.setAddressZipcode(listingRequestDTO.getAddressZipcode());
        
        // Location coordinates - extract from nested latLong object
        if (listingRequestDTO.getLatLong() != null) {
            listing.setLatitude(listingRequestDTO.getLatLong().getLatitude());
            listing.setLongitude(listingRequestDTO.getLatLong().getLongitude());
        }
        
        // Display information
        listing.setImgSrc(listingRequestDTO.getImgSrc());
        listing.setDetailUrl(listingRequestDTO.getDetailUrl());
        listing.setStatusText(listingRequestDTO.getStatusText());
        
        // Property details - parse price string to BigDecimal
        if (listingRequestDTO.getPrice() != null && !listingRequestDTO.getPrice().isEmpty()) {
            listing.setPriceFromString(listingRequestDTO.getPrice());
        }
        listing.setBeds(listingRequestDTO.getBeds());
        listing.setBaths(listingRequestDTO.getBaths());
        listing.setArea(listingRequestDTO.getArea());
        listing.setHasVideo(listingRequestDTO.getHasVideo());
        
        // Financial information
        listing.setZestimate(listingRequestDTO.getZestimate());
        
        // Availability
        listing.setAvailabilityDate(listingRequestDTO.getAvailabilityDate());
        
        // Complex data - convert JsonNode objects to JSON strings
        if (listingRequestDTO.getVariableData() != null) {
            listing.setVariableData(listingRequestDTO.getVariableData().toString());
        }
        if (listingRequestDTO.getCarouselPhotosComposable() != null) {
            listing.setCarouselPhotosComposable(listingRequestDTO.getCarouselPhotosComposable().toString());
        }
        if (listingRequestDTO.getFactsAndFeatures() != null) {
            listing.setFactsAndFeatures(listingRequestDTO.getFactsAndFeatures().toString());
        }
        
        return listing;
    }

    public static List<Listing> toModelList(List<ListingRequestDTO> dtos) {
        if (dtos == null) {
            return List.of();
        }
        
        return dtos.stream()
                .map(ListingMapper::toModel)
                .collect(Collectors.toList());
    }
}
