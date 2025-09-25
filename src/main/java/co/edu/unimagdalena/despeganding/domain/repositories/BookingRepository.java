package co.edu.unimagdalena.despeganding.domain.repositories;

import co.edu.unimagdalena.despeganding.domain.entities.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByCreatedAtBetween(OffsetDateTime start, OffsetDateTime end);
    Page<Booking> findByPassenger_EmailIgnoreCaseOrderByCreatedAtDesc(String email, Pageable pageable);

    @EntityGraph(attributePaths = {"items", "items.flight", "passenger"})
    @Query("SELECT b FROM Booking b WHERE b.id = :id")
    Optional<Booking> searchWithAllDetails(@Param("id") Long id);
}
