package com.api.fix_and_ride.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "email"))
public class UserEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotBlank
    @Column(length=100)
    private String username;
    @NotBlank
    private String address;
    @NotBlank
    private String houseNo;
    @NotBlank
    private String post;
    @NotBlank
    private String city;
    @Column(nullable = false)
    private String role; // "USER" or "ADMIN"
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email must be valid")
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank
    private String passwordHash; // store the hash, not the raw password

    public UserEntity() {}
    public UserEntity(String email, String passwordHash) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = "USER";
    }

    public UserEntity(String username, String address, String houseNo, String post, String city, String email, String passwordHash) {
        this.username = username;
        this.address = address;
        this.houseNo = houseNo;
        this.post = post;
        this.city = city;
        this.role = "USER";
        this.email = email;
        this.passwordHash = passwordHash;
    }

    public Long getId() { return userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getHouseNo() { return houseNo; }
    public void setHouseNo(String houseNo) { this.houseNo = houseNo; }

    public String getPost() { return post; }
    public void setPost(String post) { this.post = post; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
}