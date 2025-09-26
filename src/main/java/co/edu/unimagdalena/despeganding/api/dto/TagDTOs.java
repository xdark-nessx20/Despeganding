package co.edu.unimagdalena.despeganding.api.dto;

import java.io.Serializable;
import java.util.Set;

public class TagDTOs {
    public record TagCreateRequest(String name) implements Serializable {}
    public record TagUpdateRequest(String name) implements Serializable {}
    public record TagResponse(Long id, String name, Set<FlightDTOs.FlightResponse> flights) implements Serializable{}
}
