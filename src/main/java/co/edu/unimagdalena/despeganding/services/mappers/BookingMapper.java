package co.edu.unimagdalena.despeganding.services.mappers;

import co.edu.unimagdalena.despeganding.domain.entities.*;
import co.edu.unimagdalena.despeganding.api.dto.BookingDTOs.*;
import co.edu.unimagdalena.despeganding.api.dto.BookingItemDTOs.*;

import java.time.OffsetDateTime;
import java.util.List;

public class BookingMapper {
    //Ey wtf, how can I get the passenger in this class? And I still don't know how to manipulate list parameters here
    public static Booking toEntity(BookingCreateRequest request, Passenger passenger, List<BookingItemCreateRequest> items) {
        var b = Booking.builder().createdAt(OffsetDateTime.now())
                .passenger(passenger).build();

        List<BookingItem> itemsToEntities = items.stream().map(item -> BookingItem.builder().cabin(Cabin.valueOf(item.cabin())).price(item.price())
                .segmentOrder(item.segmentOrder()).booking(b).build()).toList();
        itemsToEntities.forEach(b::addItem);
        return b;
    }
    public static BookingResponse toDTO(Booking entity) {
        return new BookingResponse(entity.getId(), entity.getCreatedAt(),
                entity.getItems().stream().map(BookingItemMapper::toDTO).toList(),
                entity.getPassenger().getId());
    }
    public static void patch(Booking entity, BookingUpdateRequest request, Passenger passenger, List<BookingItemUpdateRequest> items) {

    }
}
