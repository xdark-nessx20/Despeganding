package co.edu.unimagdalena.despeganding.services.impl;

import co.edu.unimagdalena.despeganding.api.dto.SeatInventoryDTOs.*;
import co.edu.unimagdalena.despeganding.domain.entities.Cabin;
import co.edu.unimagdalena.despeganding.domain.repositories.FlightRepository;
import co.edu.unimagdalena.despeganding.domain.repositories.SeatInventoryRepository;
import co.edu.unimagdalena.despeganding.exceptions.NotFoundException;
import co.edu.unimagdalena.despeganding.services.SeatInventoryService;
import co.edu.unimagdalena.despeganding.services.mappers.SeatInventoryMapper;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service @RequiredArgsConstructor
public class SeatInventoryServiceImpl implements SeatInventoryService {
    private final SeatInventoryRepository seatInventoryRepository;
    private final FlightRepository flightRepository;
    private final SeatInventoryMapper seatInventoryMapper;

    @Override @Transactional
    public SeatInventoryResponse createSeatInventory(@Nonnull Long flight_id, SeatInventoryCreateRequest request) {
        var flight = flightRepository.findById(flight_id).orElseThrow(
                () -> new NotFoundException("Flight %d not found.".formatted(flight_id))
        );
        var entitySeat = seatInventoryMapper.toEntity(request);
        entitySeat.setFlight(flight);
        return seatInventoryMapper.toResponse(seatInventoryRepository.save(entitySeat));
    }

    @Override
    public SeatInventoryResponse getSeatInventory(@Nonnull Long id) {
        return seatInventoryRepository.findById(id).map(seatInventoryMapper::toResponse).orElseThrow(
                () -> new NotFoundException("SeatInventory %d not found.".formatted(id))
        );
    }

    @Override @Transactional
    public SeatInventoryResponse updateSeatInventory(@Nonnull Long id, SeatInventoryUpdateRequest request) {
        var seatInventory = seatInventoryRepository.findById(id).orElseThrow(
                () -> new NotFoundException("SeatInventory %d not found.".formatted(id))
        );
        seatInventoryMapper.patch(request, seatInventory);
        return seatInventoryMapper.toResponse(seatInventoryRepository.save(seatInventory));
    }

    @Override @Transactional
    public void deleteSeatInventory(@Nonnull Long id) {
        seatInventoryRepository.deleteById(id);
    }

    @Override
    public List<SeatInventoryResponse> listSeatInventoriesByFlight(@Nonnull Long flight_id) {
        var flight = flightRepository.findById(flight_id).orElseThrow(
                () -> new NotFoundException("Flight %d not found.".formatted(flight_id))
        );
        return seatInventoryRepository.findByFlight_Id(flight.getId()).stream().map(seatInventoryMapper::toResponse).toList();
    }

    @Override
    public SeatInventoryResponse getSeatInventoryByFlightAndCabin(@Nonnull Long flight_id, @Nonnull String cabin) {
        var flight = flightRepository.findById(flight_id).orElseThrow(
                () -> new NotFoundException("Flight %d not found.".formatted(flight_id))
        );
        var seat = seatInventoryRepository.findByFlight_IdAndCabin(flight.getId(), Cabin.valueOf(cabin)).orElseThrow(
                () -> new NotFoundException("SeatInventory for flight %d with Cabin %s not found.".formatted(flight_id, cabin))
        );
        return seatInventoryMapper.toResponse(seat);
    }

    @Override
    public boolean existsSeatInventoryByFlightAndCabinWithMinAvailableSeats(@Nonnull Long flight_id, @Nonnull String cabin, @Nonnull Integer min) {
        var flight = flightRepository.findById(flight_id).orElseThrow(
                () -> new NotFoundException("Flight %d not found.".formatted(flight_id))
        );
        return seatInventoryRepository.existsByFlight_IdAndCabinAndAvailableSeatsIsGreaterThanEqual(flight.getId(), Cabin.valueOf(cabin), min);
    }
}
