package co.edu.unimagdalena.despeganding.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public class PassengerDTOs {
    public record PassengerCreateRequest(@NotBlank String fullName, @Email @NotBlank String email,
                                         PassengerProfileDto profile) implements Serializable{}
    public record PassengerProfileDto(@NotBlank @Size(max = 24) String phone,
                                      @NotBlank @Size(min = 2, max = 4) String countryCode) implements Serializable{}

    public record PassengerUpdateRequest(String fullName, String email, PassengerProfileDto profile) implements Serializable{}

    public record PassengerResponse(Long id, String fullName, String email, PassengerProfileDto profile) implements Serializable{}
}
