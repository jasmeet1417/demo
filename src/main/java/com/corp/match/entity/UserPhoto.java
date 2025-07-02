package com.corp.match.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "user_photos", schema = "corporate")
public class UserPhoto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "photo_url", nullable = false)
    private String photoUrl;

    @Column(name = "position")
    private int position; // for ordering (1 = profile, 2, 3...)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserProfile user;

    // --- Constructors ---
    public UserPhoto() {}

    public UserPhoto(String photoUrl, int position, UserProfile user) {
        this.photoUrl = photoUrl;
        this.position = position;
        this.user = user;
    }

    // --- Getters and Setters ---
    // ... (standard getters/setters)
}
