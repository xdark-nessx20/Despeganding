package co.edu.unimagdalena.despeganding.domine.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "seat_inventories")
public class SeatInventory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "seat_Inventory_id")
    private Long id;

    @Column(nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private Cabin cabin;

    @Column(nullable = false)
    private Integer totalSeats;

    @Column(nullable = false)
    private Integer availableSeats;

    @ManyToOne @JoinColumn(name = "flight_id", unique = true, nullable = false)
    private Flight flight;

}