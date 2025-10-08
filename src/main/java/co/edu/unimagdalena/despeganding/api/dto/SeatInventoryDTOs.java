package co.edu.unimagdalena.despeganding.api.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public class SeatInventoryDTOs {
    public record SeatInventoryCreateRequest(@NotBlank String cabin, @Min(1) Integer totalSeats, @Min(0) Integer availableSeats)
            implements Serializable {}
    public record SeatInventoryUpdateRequest(String cabin, Integer totalSeats, Integer availableSeats)
            implements Serializable {}
    public record SeatInventoryResponse(Long id, String cabin, Integer totalSeats, Integer availableSeats, Long flight_id,
                                        String flight_number) implements Serializable {}
}
