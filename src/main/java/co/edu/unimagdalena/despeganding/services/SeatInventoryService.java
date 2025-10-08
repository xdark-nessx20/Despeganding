package co.edu.unimagdalena.despeganding.services;

import co.edu.unimagdalena.despeganding.api.dto.SeatInventoryDTOs.*;

import java.util.List;

public interface SeatInventoryService {
    //Basic CRUD
    SeatInventoryResponse createSeatInventory(Long flight_id, SeatInventoryCreateRequest request);
    SeatInventoryResponse getSeatInventory(Long id);
    SeatInventoryResponse updateSeatInventory(Long id, SeatInventoryUpdateRequest request);
    void deleteSeatInventory(Long id);
    //--------------------------------------------//

    List<SeatInventoryResponse> listSeatInventoriesByFlight(Long flight_id);
    SeatInventoryResponse getSeatInventoryByFlightAndCabin(Long flight_id, String cabin);
    boolean existsSeatInventoryByFlightAndCabinWithMinAvailableSeats(Long flight_id,
                                                                     String cabin, Integer min);
}
