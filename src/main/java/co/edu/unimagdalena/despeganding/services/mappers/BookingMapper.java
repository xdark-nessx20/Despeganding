package co.edu.unimagdalena.despeganding.services.mappers;

import co.edu.unimagdalena.despeganding.domain.entities.*;
import co.edu.unimagdalena.despeganding.api.dto.BookingDTOs.*;

import java.util.List;

public class BookingMapper {
    //There's no toEntity method 'cause it just receive the passenger_id. So, the service takes the responsibility

    public static BookingResponse toResponse(Booking entity) {
        var items = entity.getItems() == null? List.<BookingItemResponse>of() : entity.getItems().stream().map(BookingMapper::toItemResponse).toList();
        var passengerName = entity.getPassenger() == null? null: entity.getPassenger().getFullName();
        var passengerEmail = entity.getPassenger() == null? null: entity.getPassenger().getEmail();

        return new BookingResponse(entity.getId(), entity.getCreatedAt(), passengerName, passengerEmail, items);
    }

    /*----------------------------------------------------------------------------------------------------*/
    //ToEntity method is service's responsibility

    public static BookingItemResponse toItemResponse(BookingItem entity) {
        return new BookingItemResponse(entity.getId(), entity.getCabin().name(), entity.getPrice(), entity.getSegmentOrder(), entity.getBooking().getId(),
                entity.getFlight().getId(), entity.getFlight().getNumber());
    }

    public static void itemPatch(BookingItem entity, BookingItemUpdateRequest request) {
        if (request.cabin() != null) entity.setCabin(Cabin.valueOf(request.cabin().toUpperCase()));
        if (request.price() != null) entity.setPrice(request.price());
        if (request.segmentOrder() != null) entity.setSegmentOrder(request.segmentOrder());
    }

    public static void addItem(BookingItem item, Booking booking){ booking.addItem(item); }
}
