package com.example.socialmediarestapi.mappers;

import com.example.socialmediarestapi.dto.profile.ProfileDTO;
import com.example.socialmediarestapi.dto.profile.SimpleProfileDTO;
import com.example.socialmediarestapi.model.entity.Profile;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Slice;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface ProfileMapper {
    //    Slice<ProfileDTO> profileToProfileDTOList(Slice<Profile> profile);
    default Slice<ProfileDTO> profileToProfileDTOList(Slice<Profile> page) {
        return page.map(this::profileToProfileDTO);
    }

    default Slice<SimpleProfileDTO> profileToSimpleProfileDTOList(Slice<Profile> page) {
        return page.map(this::profileToSimpleProfileDTO);
    }

    @Mapping(source = "creationDate", target = "creationDate")
    ProfileDTO profileToProfileDTO(Profile profile);

    //    @Mapping(source = "creationDate", target = "creationDate")
    SimpleProfileDTO profileToSimpleProfileDTO(Profile profile);

    Set<SimpleProfileDTO> profileToPostInteractor(Set<Profile> profile);

}
