package com.example.socialmediarestapi.mappers;

import com.example.socialmediarestapi.dto.user.UserCreationDTO;
import com.example.socialmediarestapi.model.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User creationDTOToUser(UserCreationDTO profile);
}
