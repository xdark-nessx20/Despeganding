package co.edu.unimagdalena.despeganding.api.dto;

import jakarta.annotation.Nullable;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Set;

public class FlightDTOs {
    public record FlightCreateRequest(String number, OffsetDateTime departureTime, OffsetDateTime arrivalTime, Long airline_id,
                                    Long origin_airport_id, Long destination_airport_id, Set<String> tagsNames) implements Serializable {}

    public record FlightUpdateRequest(@Nullable String number, OffsetDateTime departureTime, OffsetDateTime arrivalTime,
                                    @Nullable Long airline_id, @Nullable Long origin_airport_id, @Nullable Long destination_airport_id,
                                    @Nullable Set<String> tagsNames) implements Serializable {}

    public record FlightResponse(Long id, String number, OffsetDateTime departureTime, OffsetDateTime arrivalTime,
                                 Long airline_id, Long origin_airport_id, Long destination_airport_id,
                                 Set<TagDTOs.TagResponse> tags) implements Serializable {}
}
