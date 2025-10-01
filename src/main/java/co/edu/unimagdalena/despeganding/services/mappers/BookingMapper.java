package co.edu.unimagdalena.despeganding.services.mappers;


import co.edu.unimagdalena.despeganding.api.dto.BookingDTOs.*;
import co.edu.unimagdalena.despeganding.domain.entities.Booking;
import co.edu.unimagdalena.despeganding.domain.entities.BookingItem;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    // Booking
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.OffsetDateTime.now())")
    @Mapping(target = "passenger", ignore = true)
    @Mapping(target = "items", ignore = true)
    Booking toEntity(BookingCreateRequest request);

    @Mapping(source = "passenger.fullName", target = "passenger_name")
    @Mapping(source = "passenger.email", target = "passenger_email")
    @Mapping(source = "items", target = "items")
    BookingResponse toResponse(Booking booking);


    //Booking Item
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "booking", ignore = true)
    @Mapping(target = "flight", ignore = true)
    BookingItem toItemEntity(BookingItemCreateRequest request);

    @Mapping(source = "booking.id", target = "booking_id")
    @Mapping(source = "flight.id", target = "flight_id")
    @Mapping(source = "flight.number", target = "flight_number")
    BookingItemResponse toItemResponse(BookingItem item);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "booking", ignore = true)
    @Mapping(target = "flight", ignore = true)
    void patch(BookingItemUpdateRequest request, @MappingTarget BookingItem item);

}