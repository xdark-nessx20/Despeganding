package co.edu.unimagdalena.despeganding.api.dto;

import jakarta.annotation.Nullable;
import co.edu.unimagdalena.despeganding.api.dto.TagDTOs.TagResponse;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Set;

public class FlightDTOs {
    public record FlightCreateRequest(String number, OffsetDateTime departureTime, OffsetDateTime arrivalTime) implements Serializable {}

    public record FlightUpdateRequest(@Nullable String number, OffsetDateTime departureTime, OffsetDateTime arrivalTime) implements Serializable {}

    //IDK if these are the right parameters
    public record FlightResponse(Long id, String number, OffsetDateTime departureTime, OffsetDateTime arrivalTime,
                                 Long airline_id, Long origin_airport_id, Long destination_airport_id,
                                 Set<TagResponse> tags) implements Serializable {}
}
