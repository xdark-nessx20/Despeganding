package co.edu.unimagdalena.despeganding.services.mappers;

import co.edu.unimagdalena.despeganding.domain.entities.Booking;
import co.edu.unimagdalena.despeganding.domain.entities.BookingItem;
import co.edu.unimagdalena.despeganding.api.dto.BookingItemDTOs.*;
import co.edu.unimagdalena.despeganding.domain.entities.Cabin;
import co.edu.unimagdalena.despeganding.domain.entities.Flight;

public class BookingItemMapper {
    public static BookingItem toEntity(BookingItemCreateRequest request, Flight flight) {
        return BookingItem.builder().cabin(Cabin.valueOf(request.cabin().toUpperCase())).price(request.price())
            .flight(flight).build();
    }

    public static BookingItemResponse toDTO(BookingItem entity) {
        var fR = FlightMapper.toDTO(entity.getFlight());
        return new BookingItemResponse(entity.getId(), entity.getCabin().name(), entity.getPrice(), entity.getSegmentOrder(), fR);
    }

    public static void path(BookingItem entity, BookingItemCreateRequest request) {
        if (request.cabin() != null) {
            entity.setCabin(Cabin.valueOf(request.cabin().toUpperCase()));
        }
        if (request.price() != null) {
            entity.setPrice(request.price());
        }
        if (request.flight_id() != null) {
            entity.setFlight(Flight.builder().id(request.flight_id()).build());
        }
    }
}
