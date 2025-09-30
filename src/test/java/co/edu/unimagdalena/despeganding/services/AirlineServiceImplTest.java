package co.edu.unimagdalena.despeganding.services;

import co.edu.unimagdalena.despeganding.domain.repositories.AirportRepository;
import co.edu.unimagdalena.despeganding.services.impl.AirportServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AirlineServiceImplTest {
    @Mock
    AirportRepository airportRepository;

    @InjectMocks
    AirportServiceImpl airportService;

    @Test
    void shouldCreateAirlineAndMapToResponse(){

    }

    @Test
    void shouldUpdateAirlineAndMapToResponse(){
    }

    @Test
    void shouldListAllAirlinesInPages(){

    }
}
