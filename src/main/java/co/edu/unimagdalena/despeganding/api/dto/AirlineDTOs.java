package co.edu.unimagdalena.despeganding.api.dto;

import jakarta.annotation.Nullable;

import java.io.Serializable;
import java.util.List;

public class AirlineDTOs {
    public record AirlineCreateRequest(String code, String name) implements Serializable {}
    public record AirlineUpdateRequest(@Nullable String code, String name) implements Serializable {}
    public record AirlineResponse(Long id, String code, String name, List<FlightDTOs.FlightResponse> flights) implements Serializable {}
}
