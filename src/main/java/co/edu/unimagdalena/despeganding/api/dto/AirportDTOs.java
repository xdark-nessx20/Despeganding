package co.edu.unimagdalena.despeganding.api.dto;

import jakarta.annotation.Nullable;

import java.io.Serializable;

public class AirportDTOs {
    public record AirportCreateRequest(String code, String name, String city) implements Serializable {}
    public record AirportUpdateRequest(@Nullable String code, String name) implements Serializable {}
    public record AirportResponse(Long id, String code, String name, String city ) implements Serializable {}
}
