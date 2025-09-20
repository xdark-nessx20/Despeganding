package co.edu.unimagdalena.despeganding.domine.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "booking_items")
public class BookingItem {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "booking_item_id")
    private Long id;

    @Column(nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private Cabin cabin;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private Integer segmentOrder;

    @ManyToOne(optional = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "booking_id")
    @Column(nullable = false)
    private Booking booking;

    @Column(nullable = false)
    private Flight flight;



}

