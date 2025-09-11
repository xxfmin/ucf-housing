package com.pm.backend.repository;

import com.pm.backend.model.Listing;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ListingRepository extends JpaRepository<Listing, String> {
    boolean existsByZpid(String zpid);
    boolean existsByAddress(String address);
}
