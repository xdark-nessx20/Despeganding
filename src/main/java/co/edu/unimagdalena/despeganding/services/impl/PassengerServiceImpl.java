package co.edu.unimagdalena.despeganding.services.impl;

import co.edu.unimagdalena.despeganding.domain.repositories.PassengerRepository;
import co.edu.unimagdalena.despeganding.exceptions.NotFoundException;
import co.edu.unimagdalena.despeganding.services.PassengerService;
import co.edu.unimagdalena.despeganding.api.dto.PassengerDTOs.*;
import co.edu.unimagdalena.despeganding.services.mappers.PassengerMapper;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor
public class PassengerServiceImpl implements PassengerService {
    private final PassengerRepository passengerRepository;
    private final PassengerMapper passengerMapper;

    @Override @Transactional
    public PassengerResponse createPassenger(PassengerCreateRequest request) {
        var passenger = passengerMapper.toEntity(request);
        return passengerMapper.toResponse(passengerRepository.save(passenger));
    }

    @Override
    public PassengerResponse getPassenger(@Nonnull Long id) {
        return passengerRepository.findById(id).map(passengerMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Passenger %d not found.".formatted(id)));
    }

    @Override
    public PassengerResponse getPassengerByEmail(@Nonnull String email) {
        return passengerRepository.findByEmailIgnoreCase(email).map(passengerMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Passenger with email %s not found.".formatted(email)));
    }

    @Override
    public PassengerResponse getPassengerWithProfile(@Nonnull String email) {
        return passengerRepository.findByEmailIgnoreCaseWithProfile(email).map(passengerMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Passenger with email %s not found.".formatted(email)));
    }

    @Override @Transactional
    public PassengerResponse updatePassenger(@Nonnull Long id, PassengerUpdateRequest request) {
        var passenger = passengerRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Passenger %d not found.".formatted(id)));
        passengerMapper.patch(request, passenger);
        return passengerMapper.toResponse(passengerRepository.save(passenger));
    }

    @Override @Transactional
    public void deletePassenger(@Nonnull Long id) {
        passengerRepository.deleteById(id);
    }

    @Override
    public Page<PassengerResponse> listAllPassengers(Pageable pageable) {
        return passengerRepository.findAll(pageable).map(passengerMapper::toResponse);
    }
}
