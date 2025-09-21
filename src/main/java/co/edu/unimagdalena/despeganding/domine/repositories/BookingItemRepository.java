package co.edu.unimagdalena.despeganding.domine.repositories;

import co.edu.unimagdalena.despeganding.domine.entities.BookingItem;
import co.edu.unimagdalena.despeganding.domine.entities.Cabin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.List;

public interface BookingItemRepository extends JpaRepository<BookingItem, Long> {
    List<BookingItem> findByBookingIdOrderBySegmentOrder(Long bookingId);

    @Query("""
    SELECT COALESCE(SUM(bi.price), 0)
    FROM BookingItem bi
    WHERE bi.booking.id = :bookingId
""")
    BigDecimal calculateTotalByBookingId(Long bookingId);

    @Query("""
    SELECT COUNT(bi)
    FROM BookingItem bi
    WHERE bi.flight.id = :flightId AND bi.cabin = :cabin
""")
    Long countReservedSeatsByFlightAndCabin(Long flightId, Cabin cabin);
}
