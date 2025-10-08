package co.edu.unimagdalena.despeganding.services.impl;

import co.edu.unimagdalena.despeganding.domain.repositories.PassengerRepository;
import co.edu.unimagdalena.despeganding.exceptions.NotFoundException;
import co.edu.unimagdalena.despeganding.services.PassengerService;
import co.edu.unimagdalena.despeganding.api.dto.PassengerDTOs.*;
import co.edu.unimagdalena.despeganding.services.mappers.PassengerMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service @RequiredArgsConstructor
public class PassengerServiceImpl implements PassengerService {
    private final PassengerRepository passengerRepository;

    @Override @Transactional
    public PassengerResponse createPassenger(PassengerCreateRequest request) {
        var passenger = PassengerMapper.toEntity(request);
        return PassengerMapper.toResponse(passengerRepository.save(passenger));
    }

    @Override
    public PassengerResponse getPassenger(Long id) {
        return passengerRepository.findById(id).map(PassengerMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Passenger %d not found.".formatted(id)));
    }

    @Override
    public PassengerResponse getPassengerByEmail(String email) {
        return passengerRepository.findByEmailIgnoreCase(email).map(PassengerMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Passenger with email %s not found.".formatted(email)));
    }

    @Override
    public PassengerResponse getPassengerWithProfile(String email) {
        return passengerRepository.findByEmailIgnoreCaseWithProfile(email).map(PassengerMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Passenger with email %s not found.".formatted(email)));
    }

    @Override @Transactional
    public PassengerResponse updatePassenger(Long id, PassengerUpdateRequest request) {
        var passenger = passengerRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Passenger %d not found.".formatted(id)));
        PassengerMapper.patch(passenger, request);
        return PassengerMapper.toResponse(passengerRepository.save(passenger));
    }

    @Override @Transactional
    public void deletePassenger(Long id) {
        passengerRepository.deleteById(id);
    }

    @Override
    public Page<PassengerResponse> listAllPassengers(Pageable pageable) {
        return passengerRepository.findAll(pageable).map(PassengerMapper::toResponse);
    }
}
