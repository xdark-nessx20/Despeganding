package co.edu.unimagdalena.despeganding.api.dto;

import jakarta.annotation.Nonnull;

import java.io.Serializable;

public class AirlineDTOs {
    public record AirlineCreateRequest(@Nonnull String code,@Nonnull String name) implements Serializable {}
    public record AirlineUpdateRequest(String code, @Nonnull String name) implements Serializable {}
    public record AirlineResponse(Long id, String code, String name) implements Serializable {}
}
