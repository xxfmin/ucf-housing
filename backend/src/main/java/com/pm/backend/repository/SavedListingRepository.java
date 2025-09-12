package com.pm.backend.repository;

import com.pm.backend.model.SavedListing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavedListingRepository extends JpaRepository<SavedListing, Long> {
    List<SavedListing> findByUserId(Long userId);
    boolean existsByUserIdAndListingZpid(Long userId, String listingZpid);
    void deleteByUserIdAndListingZpid(Long userId, String listingZpid);
}
