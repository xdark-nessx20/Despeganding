package co.edu.unimagdalena.despeganding.services.mappers;

import co.edu.unimagdalena.despeganding.domain.entities.Cabin;
import co.edu.unimagdalena.despeganding.domain.entities.Flight;
import co.edu.unimagdalena.despeganding.domain.entities.SeatInventory;
import  co.edu.unimagdalena.despeganding.api.dto.SeatInventoryDTOs.*;

public class SeatInventoryMapper {
    public static SeatInventory toEntity(SeatInventoryCreateRequest request ) {
        return SeatInventory.builder().cabin(Cabin.valueOf(request.cabin())).availableSeats(request.availableSeats())
                .totalSeats(request.availableSeats())
                .flight(Flight.builder().id(request.flight_id()).build()).build();
    }
    public static SeatInventoryResponse toDTO(SeatInventory seatInventory) {
        return new SeatInventoryResponse(
                seatInventory.getId(), seatInventory.getCabin().name(),
                seatInventory.getTotalSeats(), seatInventory.getAvailableSeats(),
                seatInventory.getFlight() != null ? seatInventory.getFlight().getId() : null
        );
    }

    public static void patch(SeatInventory entity, SeatInventoryUpdateRequest update) {
        if (update.cabin() != null) entity.setCabin(Cabin.valueOf(update.cabin()));
        if (update.availableSeats() != null) entity.setAvailableSeats(update.availableSeats());
    }
}
