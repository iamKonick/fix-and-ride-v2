package com.api.fix_and_ride.dto;

import com.api.fix_and_ride.entity.UserEntity;

public record UserDTO(Long id, String username, String email,
                      String address, String houseNo, String post, String city,String role ) {
    public static UserDTO fromEntityToDTO(UserEntity userEntity) {
        return new UserDTO(
                userEntity.getId(),
                userEntity.getUsername(),
                userEntity.getEmail(),
                userEntity.getAddress(),
                userEntity.getHouseNo(),
                userEntity.getPost(),
                userEntity.getCity(),
                userEntity.getRole()
        );
    }
}

