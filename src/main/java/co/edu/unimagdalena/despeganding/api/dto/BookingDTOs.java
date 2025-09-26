package co.edu.unimagdalena.despeganding.api.dto;

import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.List;

public class BookingDTOs {
    //This method doesn't receive createdAt 'cause this will be OffsetDateTime.now() at the creation moment
    public record BookingCreateRequest(Long passenger_id, List<BookingItemDTOs.BookingItemCreateRequest> items) implements Serializable {}

    //IDK if this method have to receive this parameter
    public record BookingUpdateRequest(List<BookingItemDTOs.BookingItemUpdateRequest> itemsToUpdate) implements Serializable {}

    public record BookingResponse(Long id, OffsetDateTime createdAt, PassengerDTOs.PassengerResponse passenger,
                                  List<BookingItemDTOs.BookingItemResponse> items) implements Serializable{}
}
