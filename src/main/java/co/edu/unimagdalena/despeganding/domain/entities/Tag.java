package co.edu.unimagdalena.despeganding.domain.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tags")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Tag {
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "tag_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Flight> flights = new HashSet<>();
}
