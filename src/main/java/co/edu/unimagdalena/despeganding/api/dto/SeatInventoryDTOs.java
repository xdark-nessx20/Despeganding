package co.edu.unimagdalena.despeganding.api.dto;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;

import java.io.Serializable;

public class SeatInventoryDTOs {
    public record SeatInventoryCreateRequest(@Nonnull String cabin,@Nonnull Integer totalSeats, @Nonnull Integer availableSeats)
            implements Serializable {}
    public record SeatInventoryUpdateRequest(String cabin, Integer totalSeats, Integer availableSeats)
            implements Serializable {}
    public record SeatInventoryResponse(Long id, String cabin, Integer totalSeats, Integer availableSeats, Long flight_id)
            implements Serializable {}
}
