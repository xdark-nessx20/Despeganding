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
}
