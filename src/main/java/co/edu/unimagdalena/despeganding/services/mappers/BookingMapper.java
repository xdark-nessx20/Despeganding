package co.edu.unimagdalena.despeganding.services.mappers;

import co.edu.unimagdalena.despeganding.domain.entities.*;
import co.edu.unimagdalena.despeganding.api.dto.BookingDTOs.*;

import java.time.OffsetDateTime;
import java.util.List;

public class BookingMapper {
    //Check out this method 'cause the professor doesn't have it
    public static Booking toEntity(BookingCreateRequest request) {
        var b = Booking.builder().createdAt(OffsetDateTime.now())
                .passenger(passenger).build();

        List<BookingItem> itemsToEntities = items.stream().map(item -> BookingItem.builder().cabin(Cabin.valueOf(item.cabin())).price(item.price())
                .segmentOrder(item.segmentOrder()).booking(b).build()).toList();
        itemsToEntities.forEach(b::addItem);
        return b;
    }

    public static BookingResponse toResponse(Booking entity) {
        var items = entity.getItems() == null? List.<BookingItemResponse>of() : entity.getItems().stream().map(BookingMapper::toItemResponse).toList();
        var passengerName = entity.getPassenger() == null? null: entity.getPassenger().getFullName();
        var passengerEmail = entity.getPassenger() == null? null: entity.getPassenger().getEmail();

        return new BookingResponse(entity.getId(), entity.getCreatedAt(), passengerName, passengerEmail, items);
    }

    /*----------------------------------------------------------------------------------------------------*/
    //Check out this method 'cause the professor doesn't have it
    public static BookingItem toItemEntity(BookingItemCreateRequest request) {
        return BookingItem.builder().cabin(Cabin.valueOf(request.cabin().toUpperCase())).price(request.price()).build();
    }

    public static BookingItemResponse toItemResponse(BookingItem entity) {
        return new BookingItemResponse(entity.getId(), entity.getCabin().name(), entity.getPrice(), entity.getSegmentOrder(),
                entity.getFlight().getId(), entity.getFlight().getNumber());
    }

    public static void itemPatch(BookingItem entity, BookingItemUpdateRequest request) {
        if (request.cabin() != null) entity.setCabin(Cabin.valueOf(request.cabin().toUpperCase()));
        if (request.price() != null) entity.setPrice(request.price());
        if (request.segmentOrder() != null) entity.setSegmentOrder(request.segmentOrder());
    }
}
