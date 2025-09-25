package co.edu.unimagdalena.despeganding.api.dto;

import jakarta.annotation.Nullable;

import java.io.Serializable;

public class PassengerDTOs {
    public record PassengerCreateRequest(String fullName, String email, PassengerProfileDto profile) implements Serializable{}
    public record PassengerProfileDto(String phone, String countryCode) implements Serializable{}

    public record PassengerUpdateRequest(@Nullable String fullName, String email, @Nullable PassengerProfileDto profile) implements Serializable{}

    public record PassengerResponse(Long id, String fullName, String email, PassengerProfileDto profile) implements Serializable{}
}
