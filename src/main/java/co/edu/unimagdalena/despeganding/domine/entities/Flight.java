package co.edu.unimagdalena.despeganding.domine.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "flights")
public class Flight {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "flight_id")
    private Long id;

    @Column(nullable = false, unique = true)
    private String number;

    @Column(nullable = false)
    private OffsetDateTime departureTime;

    @Column(nullable = false)
    private OffsetDateTime arrivalTime;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "airline_id")
    private Airline airline;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "airport_id")
    private Airport origin;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "airport_id")
    private Airport destination;

    @ManyToMany(mappedBy = "tags")
    @JoinTable(name = "tags_flights",
            joinColumns = @JoinColumn(name = "flight_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    @Builder.Default
    private Set<Tag> tags = new HashSet<>();

    public void addTag(Tag tag) {
        this.tags.add(tag);
        tag.addFlight(this);
    }
}
