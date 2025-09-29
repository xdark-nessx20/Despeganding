package co.edu.unimagdalena.despeganding.services;

import co.edu.unimagdalena.despeganding.domain.repositories.BookingItemRepository;
import co.edu.unimagdalena.despeganding.domain.repositories.BookingRepository;
import co.edu.unimagdalena.despeganding.domain.repositories.FlightRepository;
import co.edu.unimagdalena.despeganding.services.impl.BookingItemServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookingItemServiceImplTest {
    @Mock BookingItemRepository bookingItemRepository;
    @Mock FlightRepository flightRepository;
    @Mock BookingRepository bookingRepository;

    @InjectMocks BookingItemServiceImpl bookingItemServiceImpl;

    @Test
    void shouldAddBookingItemAndMapToResponse(){

    }

    @Test
    void shouldUpdateBookingItemAndMapToResponse(){

    }

    @Test
    void shouldListBookingItemsByBooking(){

    }

    //IDK if we've to test all service methods... 'cause I think so, but, the professor wants to?
}
