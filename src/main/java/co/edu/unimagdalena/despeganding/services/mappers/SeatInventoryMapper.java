package co.edu.unimagdalena.despeganding.services.mappers;

import co.edu.unimagdalena.despeganding.domain.entities.Cabin;
import co.edu.unimagdalena.despeganding.domain.entities.SeatInventory;
import  co.edu.unimagdalena.despeganding.api.dto.SeatInventoryDTOs.*;

public class SeatInventoryMapper {
    public static SeatInventory toEntity(SeatInventoryCreateRequest request ) {
        return SeatInventory.builder().cabin(Cabin.valueOf(request.cabin())).availableSeats(request.availableSeats())
                .totalSeats(request.availableSeats()).build();
    }
    public static SeatInventoryResponse toResponse(SeatInventory seatInventory) {
        return new SeatInventoryResponse(
                seatInventory.getId(), seatInventory.getCabin().name(),
                seatInventory.getTotalSeats(), seatInventory.getAvailableSeats(), seatInventory.getFlight().getId()
        );
    }

    public static void patch(SeatInventory entity, SeatInventoryUpdateRequest update) {
        if (update.cabin() != null) entity.setCabin(Cabin.valueOf(update.cabin()));
        if (update.totalSeats() != null) entity.setTotalSeats(update.totalSeats());
        if (update.availableSeats() != null) entity.setAvailableSeats(update.availableSeats());
    }
}
