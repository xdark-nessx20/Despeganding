package co.edu.unimagdalena.despeganding.api.dto;

import jakarta.annotation.Nonnull;
import co.edu.unimagdalena.despeganding.api.dto.TagDTOs.TagResponse;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Set;

public class FlightDTOs {
    public record FlightCreateRequest(@Nonnull String number, @Nonnull OffsetDateTime departureTime, @Nonnull OffsetDateTime arrivalTime,
                                      @Nonnull Long airline_id, @Nonnull Long origin_airport_id, @Nonnull Long destination_airport_id) implements Serializable {}

    public record FlightUpdateRequest(String number, OffsetDateTime departureTime, OffsetDateTime arrivalTime) implements Serializable {}

    //IDK if these are the right parameters
    public record FlightResponse(Long id, String number, OffsetDateTime departureTime, OffsetDateTime arrivalTime,
                                 Long airline_id, Long origin_airport_id, Long destination_airport_id,
                                 Set<TagResponse> tags) implements Serializable {}
}
