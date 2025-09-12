package com.pm.backend.model;

import jakarta.persistence.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "saved_listings")
@EntityListeners(AuditingEntityListener.class)
public class SavedListing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "listing_zpid", nullable = false)
    private String listingZpid;

    @CreatedDate
    @Column(name = "saved_at", nullable = false, updatable = false)
    private LocalDateTime savedAt;

    public SavedListing(Long id, User user, String listingZpid, LocalDateTime savedAt) {
        this.id = id;
        this.user = user;
        this.listingZpid = listingZpid;
        this.savedAt = savedAt;
    }

    public SavedListing() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getListingZpid() {
        return listingZpid;
    }

    public void setListingZpid(String listingZpid) {
        this.listingZpid = listingZpid;
    }

    public LocalDateTime getSavedAt() {
        return savedAt;
    }

    public void setSavedAt(LocalDateTime savedAt) {
        this.savedAt = savedAt;
    }
}
