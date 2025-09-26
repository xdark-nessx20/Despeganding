package co.edu.unimagdalena.despeganding.services.mappers;

import co.edu.unimagdalena.despeganding.api.dto.PassengerDTOs.*;
import co.edu.unimagdalena.despeganding.domain.entities.Passenger;
import co.edu.unimagdalena.despeganding.domain.entities.PassengerProfile;

public class PassengerMapper {
    public static Passenger toEntity(PassengerCreateRequest passengerCreateRequest) {
        var profile = passengerCreateRequest.profile() == null ? null :
            PassengerProfile.builder().phone(passengerCreateRequest.profile().phone())
            .countryCode(passengerCreateRequest.profile().countryCode()).build();
        return Passenger.builder().fullName(passengerCreateRequest.fullName())
            .email(passengerCreateRequest.email()).profile(profile).build();
    }

    public static PassengerResponse toDTO(Passenger passenger){
        var p = passenger.getProfile();
        var dto_profile = p == null ? null : new PassengerProfileDto(p.getPhone(), p.getCountryCode());
        return new PassengerResponse(passenger.getId(),  passenger.getFullName(), passenger.getEmail(), dto_profile);
    }

    public static void patch(Passenger entity, PassengerUpdateRequest request) {
        if (request.fullName() != null) entity.setFullName(request.fullName());
        if (request.email() != null) entity.setEmail(request.email());
        if (request.profile() != null) {
            var entityProfile =  entity.getProfile();
            if (entityProfile == null) {
                entityProfile = new PassengerProfile();
                entity.setProfile(entityProfile);
            }
            if (request.profile().phone() != null) entityProfile.setPhone(request.profile().phone());
            if (request.profile().countryCode() != null) entityProfile.setCountryCode(request.profile().countryCode());
        }
    }
}
