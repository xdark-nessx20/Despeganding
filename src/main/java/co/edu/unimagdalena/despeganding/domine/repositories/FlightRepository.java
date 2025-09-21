package co.edu.unimagdalena.despeganding.domine.repositories;

import co.edu.unimagdalena.despeganding.domine.entities.Airport;
import co.edu.unimagdalena.despeganding.domine.entities.Flight;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.OffsetDateTime;
import java.util.Collection;
import java.util.List;

public interface FlightRepository extends JpaRepository<Flight, Long> {
    Page<Flight> findByAirline_Name(String airline_name, Pageable pageable);
    Page<Flight> findByOrigin_CodeAndDestination_CodeAndDepartureTimeBetween(
        String origin_code, String destination_code, OffsetDateTime from,
        OffsetDateTime to, Pageable pageable);

    @EntityGraph(attributePaths = {"airline", "origin", "destination", "tags"})
    @Query("""
        SELECT f FROM Flight f
        WHERE (:org IS NULL OR :org = f.origin) AND (:dest IS NULL OR :dest = f.destination)
            AND f.departureTime BETWEEN :from AND :to
   """)
    List<Flight> filterByOriginAndDestinationOptionalAndDepartureTimeBetween(
        @Param("org") Airport org, @Param("dest") Airport dest,
        @Param("from") OffsetDateTime from, @Param("to") OffsetDateTime to);

    @Query(value = """
        SELECT f.* FROM flights f
            JOIN tags_flights tf ON tf.flight_id = f.id
            JOIN tags t ON tf.tag_id = t.id
        WHERE t.name IN (:tags)
        GROUP BY f.id HAVING COUNT(DISTINCT t.name) = :required
    """, nativeQuery = true)
    List<Flight> findFlightsWithTheseTags(@Param("tags") Collection<String> tags,
        @Param("required") Long required);

}
