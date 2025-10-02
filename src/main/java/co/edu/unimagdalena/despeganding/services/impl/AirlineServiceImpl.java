package co.edu.unimagdalena.despeganding.services.impl;

import co.edu.unimagdalena.despeganding.api.dto.AirlineDTOs.*;
import co.edu.unimagdalena.despeganding.domain.repositories.AirlineRepository;
import co.edu.unimagdalena.despeganding.exceptions.NotFoundException;
import co.edu.unimagdalena.despeganding.services.AirlineService;
import co.edu.unimagdalena.despeganding.services.mappers.AirlineMapper;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service @Transactional @RequiredArgsConstructor
public class AirlineServiceImpl implements AirlineService {
    private final AirlineRepository airlineRepository;
    private final AirlineMapper airlineMapper;

    @Override
    public AirlineResponse createAirline(AirlineCreateRequest request) {
        var airline = airlineMapper.toEntity(request);
        return airlineMapper.toResponse(airlineRepository.save(airline));
    }

    @Override @Transactional(readOnly = true)
    public AirlineResponse getAirline(@Nonnull Long id) {
        return airlineRepository.findById(id).map(airlineMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Airline %d not found.".formatted(id)));
    }

    @Override @Transactional(readOnly = true)
    public AirlineResponse getAirlineByCode(@Nonnull String code) {
        return airlineRepository.findByCode(code).map(airlineMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Airline with code %s not found.".formatted(code)));
    }

    @Override
    public AirlineResponse updateAirline(@Nonnull Long id, AirlineUpdateRequest request) {
        var airline = airlineRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Airline %d not found.".formatted(id)));
        airlineMapper.patch(request, airline);
        return airlineMapper.toResponse(airlineRepository.save(airline));
    }

    @Override
    public void deleteAirline(@Nonnull Long id) {
        airlineRepository.deleteById(id);
    }

    @Override @Transactional(readOnly = true)
    public List<AirlineResponse> listAllAirlines() {
        return airlineRepository.findAll().stream().map(airlineMapper::toResponse).toList();
    }

    @Override
    public Page<AirlineResponse> listAllAirlinesPage(Pageable pageable) {
        return airlineRepository.findAll(pageable).map(airlineMapper::toResponse);
    }
}
