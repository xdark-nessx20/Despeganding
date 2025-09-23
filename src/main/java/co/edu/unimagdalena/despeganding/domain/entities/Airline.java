package co.edu.unimagdalena.despeganding.domain.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "airlines")
public class Airline {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "airline_id")
    private Long id;

    @Column(nullable = false, unique = true, length = 2)
    private String code;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "airline", fetch = FetchType.LAZY)
    private List<Flight> flights;

    public void addFlight(Flight flight) {
        flights.add(flight);
        flight.setAirline(this);
    }
}
