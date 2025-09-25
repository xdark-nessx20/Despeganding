package co.edu.unimagdalena.despeganding.api.dto;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;

public class BookingDTOs {
    public record BookingCreateRequest(OffsetDateTime createdAt, Long passenger_id) implements Serializable {}
    public record BookingUpdateRequest(/*What the hell should I put here?*/) implements Serializable {}
    public record BookingResponse(Long id, OffsetDateTime createdAt, PassengerDTOs.PassengerResponse passenger,
                                  List<BookingItemDTOs.BookingItemResponse> items) implements Serializable{}
}
