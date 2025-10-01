package co.edu.unimagdalena.despeganding.services.mappers;

import co.edu.unimagdalena.despeganding.api.dto.PassengerDTOs.*;
import co.edu.unimagdalena.despeganding.domain.entities.Passenger;
import co.edu.unimagdalena.despeganding.domain.entities.PassengerProfile;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface PassengerMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(source = "profile", target = "profile")
    Passenger toEntity(PassengerCreateRequest request);

    PassengerResponse toResponse(Passenger passenger);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void patch(PassengerUpdateRequest request, @MappingTarget Passenger entity);

    @AfterMapping
    default void updateProfile(PassengerUpdateRequest request, @MappingTarget Passenger entity) {
        if (request.profile() != null) {
            if (entity.getProfile() == null) {
                entity.setProfile(new PassengerProfile());
            }
            patchProfile(request.profile(), entity.getProfile());
        }
    }

    @Mapping(target = "id", ignore = true)
    PassengerProfile toProfileEntity(PassengerProfileDto profileDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    void patchProfile(PassengerProfileDto profileDto, @MappingTarget PassengerProfile profile);
}
