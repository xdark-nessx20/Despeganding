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
@Table(name = "seats_inventory")
public class SeatInventory {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "seat_inventory_id")
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Cabin cabin;

    @Column(nullable = false, name = "total_seats")
    private Integer totalSeats;

    @Column(nullable = false, name = "available_seats")
    private Integer availableSeats;

  @ManyToOne @JoinColumn(name = "flight_id", nullable = false)
  private Flight flight;

}