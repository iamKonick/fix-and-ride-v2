package com.api.fix_and_ride.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "admin_users")
public class AdminUserEntity {

    @Id
    @GeneratedValue(strategy =   GenerationType.IDENTITY)
    private Long id;

    @Email @NotBlank
    @Column(unique = true)
    private String email;

    @NotBlank
    private String passwordHash;
    @Column(nullable = false)
    private final String role; // "USER" or "ADMIN"

    public AdminUserEntity() {
        this.role = "ADMIN";
    }

    public AdminUserEntity(String email, String passwordHash) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = "ADMIN";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }
}
