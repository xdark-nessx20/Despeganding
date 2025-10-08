package co.edu.unimagdalena.despeganding.services.impl;

import co.edu.unimagdalena.despeganding.api.dto.AirportDTOs.*;
import co.edu.unimagdalena.despeganding.domain.repositories.AirportRepository;
import co.edu.unimagdalena.despeganding.exceptions.NotFoundException;
import co.edu.unimagdalena.despeganding.services.AirportService;
import co.edu.unimagdalena.despeganding.services.mappers.AirportMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service @RequiredArgsConstructor
public class AirportServiceImpl implements AirportService {
    private final AirportRepository airportRepository;

    @Override @Transactional
    public AirportResponse createAirport(AirportCreateRequest request) {
        var airport = AirportMapper.toEntity(request);
        return AirportMapper.toResponse(airportRepository.save(airport));
    }

    @Override
    public AirportResponse getAirport(Long id) {
        return airportRepository.findById(id).map(AirportMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Airport %d not found.".formatted(id)));
    }

    @Override
    public AirportResponse getAirportByCode(String code) {
        return airportRepository.findByCode(code).map(AirportMapper::toResponse)
                .orElseThrow(() -> new NotFoundException("Airport with code %s not found.".formatted(code)));
    }

    @Override
    public List<AirportResponse> getCityAirports(String city) {
        return airportRepository.findByCity(city).stream().map(AirportMapper::toResponse).toList();
    }

    @Override @Transactional
    public AirportResponse updateAirport(Long id, AirportUpdateRequest request) {
        var airport = airportRepository.findById(id).orElseThrow(
                () -> new NotFoundException("Airport %d not found.".formatted(id)));
        AirportMapper.patch(airport, request);
        return AirportMapper.toResponse(airportRepository.save(airport));
    }

    @Override @Transactional
    public void deleteAirport(Long id) {
        airportRepository.deleteById(id);
    }

    @Override
    public Page<AirportResponse> listAllAirports(Pageable pageable) {
        return airportRepository.findAll(pageable).map(AirportMapper::toResponse);
    }
}
