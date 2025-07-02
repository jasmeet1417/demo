package com.corp.match.entity;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

/**
 * Entity representing a user's profile after successful work email verification.
 */
@Entity
@Table(name = "user_profile", schema = "corporate")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name", nullable = false)
    private String fullName;

    @Column(name = "work_email", nullable = false, unique = true)
    private String workEmail;

    @Column(name = "age")
    private int age;

    @Column(name = "gender")
    private String gender;

    @Column(name = "status")
    private String status; // Online / Offline / Busy

    @Column(name = "company_name")
    private String companyName;

    @Column(name = "last_seen_at")
    private Timestamp lastSeenAt;

    /**
     * Link to photos uploaded by the user (1 to many).
     */
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<UserPhoto> photos = new ArrayList<>();

    /**
     * Link to the account used for login (1 to 1).
     */
    @OneToOne
    @JoinColumn(name = "account_id", nullable = false, unique = true)
    private UserAccount account;

    // --- Constructors ---
    public UserProfile() {}

    public UserProfile(String fullName, String workEmail, int age, String gender) {
        this.fullName = fullName;
        this.workEmail = workEmail;
        this.age = age;
        this.gender = gender;
    }

    // --- Getters and Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getWorkEmail() {
        return workEmail;
    }

    public void setWorkEmail(String workEmail) {
        this.workEmail = workEmail;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getLastSeenAt() {
        return lastSeenAt;
    }

    public void setLastSeenAt(Timestamp lastSeenAt) {
        this.lastSeenAt = lastSeenAt;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public List<UserPhoto> getPhotos() {
        return photos;
    }

    public void setPhotos(List<UserPhoto> photos) {
        this.photos = photos;
    }

    public UserAccount getAccount() {
        return account;
    }

    public void setAccount(UserAccount account) {
        this.account = account;
    }
}
