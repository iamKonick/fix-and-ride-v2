package com.api.fix_and_ride.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class SignupRequestDTO {
    @NotBlank public String firstName;
    @NotBlank public String lastName;
    @NotBlank public String address;
    @NotBlank public String houseNo;
    @NotBlank public String post;
    @NotBlank public String city;
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email must be valid")
    public String email;

    @NotBlank(message = "Password cannot be blank")
    public String password;
}