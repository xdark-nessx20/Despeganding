package co.edu.unimagdalena.despeganding.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

public class AirlineDTOs {
    public record AirlineCreateRequest(@NotBlank @Size(min = 2, max = 2) String code, @NotBlank String name) implements Serializable {}
    public record AirlineUpdateRequest(String code, String name) implements Serializable {}
    public record AirlineResponse(Long id, String code, String name) implements Serializable {}
}
