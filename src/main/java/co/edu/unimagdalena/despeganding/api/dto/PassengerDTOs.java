package co.edu.unimagdalena.despeganding.api.dto;

import jakarta.annotation.Nonnull;

import java.io.Serializable;

public class PassengerDTOs {
    public record PassengerCreateRequest(@Nonnull String fullName, @Nonnull String email, @Nonnull PassengerProfileDto profile) implements Serializable{}
    public record PassengerProfileDto(String phone, String countryCode) implements Serializable{}

    public record PassengerUpdateRequest(String fullName, String email, PassengerProfileDto profile) implements Serializable{}

    public record PassengerResponse(Long id, String fullName, String email, PassengerProfileDto profile) implements Serializable{}
}
