package co.edu.unimagdalena.despeganding.api.dto;

import co.edu.unimagdalena.despeganding.api.dto.TagDTOs.TagResponse;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Set;

public class FlightDTOs {
    public record FlightCreateRequest(@NotBlank @Size(min = 3, max = 6) String number, @NotNull OffsetDateTime departureTime,
                                      @NotNull OffsetDateTime arrivalTime) implements Serializable {}

    public record FlightUpdateRequest(String number, OffsetDateTime departureTime, OffsetDateTime arrivalTime) implements Serializable {}

    public record FlightResponse(Long id, String number, OffsetDateTime departureTime, OffsetDateTime arrivalTime,
                                 Long airline_id, String airline_name, Long origin_airport_id, String origin_airport_name,
                                 Long destination_airport_id, String destination_airport_name, Set<TagResponse> tags)
            implements Serializable {}
}
