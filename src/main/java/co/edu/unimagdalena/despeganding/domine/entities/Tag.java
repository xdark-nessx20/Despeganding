package co.edu.unimagdalena.despeganding.domine.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tags")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "tag_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    @ManyToMany(mappedBy = "tags")
    @Builder.Default
    private Set<Flight> flights = new HashSet<>();

    public void addFlight(Flight flight) {
        flights.add(flight);
    }
}
