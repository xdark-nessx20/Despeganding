package co.edu.unimagdalena.despeganding.services;

import co.edu.unimagdalena.despeganding.api.dto.PassengerDTOs.*;
import co.edu.unimagdalena.despeganding.domain.entities.Passenger;
import co.edu.unimagdalena.despeganding.domain.entities.PassengerProfile;
import co.edu.unimagdalena.despeganding.domain.repositories.PassengerRepository;
import co.edu.unimagdalena.despeganding.services.impl.PassengerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PassengerServiceImplTest {
    @Mock
    private PassengerRepository passengerRepository;

    @InjectMocks
    private PassengerServiceImpl passengerService;

    @Test
    void shouldCreatePassengerAndMapToResponse(){
        when(passengerRepository.save(any())).thenAnswer(i -> {
            Passenger p = i.getArgument(0);
            p.setId(1L);
            return p;
        });

        var response =  passengerService.createPassenger(new PassengerCreateRequest("Elton Tito ElBambino",
                "theBambino69@example.com", new PassengerProfileDto("3334445555", "+58")));

        assertThat(response).isNotNull(); assertThat(response.id()).isEqualTo(1L);
        assertThat(response.fullName()).isEqualTo("Elton Tito ElBambino");
        assertThat(response.email()).isEqualTo("theBambino69@example.com");
        assertThat(response.profile().phone()).isEqualTo("3334445555");
    }

    @Test
    void shouldUpdatePassengerAndMapToResponse(){
        var passenger = Optional.of(Passenger.builder().id(1L).fullName("Neymar Pelé")
                .email("ney.Pele10@example.com").build());
        when(passengerRepository.findById(1L)).thenReturn(passenger);
        when(passengerRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var response = passengerService.updatePassenger(1L, new PassengerUpdateRequest(null,
                "ney_gotoso10.pele@example.com", new PassengerProfileDto("3134792025", "+57")));

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.fullName()).isEqualTo("Neymar Pelé");
        assertThat(response.email()).isEqualTo("ney_gotoso10.pele@example.com");
    }

    @Test
    void shouldGetPassengerWithProfile(){
        var email = "jChan.007@example.com";
        var passenger_profile = PassengerProfile.builder().id(1001L).phone("3130092025").countryCode("+57").build();
        when(passengerRepository.findByEmailIgnoreCaseWithProfile(email)).thenReturn(Optional.of(
                Passenger.builder().id(1L).fullName("Jackie Chan ConChan").email(email)
                        .profile(passenger_profile).build()
        ));

        var response = passengerService.getPassengerWithProfile(email);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.fullName()).isEqualTo("Jackie Chan ConChan");
        assertThat(response.email()).isEqualTo(email);
        assertThat(response.profile().phone()).isEqualTo("3130092025");
    }

    @Test
    void shouldListAllPassengers(){
        when(passengerRepository.findAll(Pageable.ofSize(4))).thenReturn(new PageImpl<>(List.of(
                Passenger.builder().id(1L).fullName("Zacarias Blanco Del Fierro").email("zacablanco_fierro@example.com").build(),
                Passenger.builder().id(2L).fullName("Elena Nito Del Bosque").email("elena_nito69@example.com").build(),
                Passenger.builder().id(3L).fullName("Jackie Sieras Plata").email("jackie_sieras777@example.com").build(),
                Passenger.builder().id(4L).fullName("Messi Ronaldo").email("messironaldo.10_7@example.com").build()
        )));

        var response = passengerService.listAllPassengers(Pageable.ofSize(4));

        assertThat(response).hasSize(4);
        assertThat(response).extracting(PassengerResponse::email).containsExactly(
                "zacablanco_fierro@example.com", "elena_nito69@example.com", "jackie_sieras777@example.com", "messironaldo.10_7@example.com"
        );
    }
}
