package co.edu.unimagdalena.despeganding.domain.repositories;

import co.edu.unimagdalena.despeganding.domain.entities.Tag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TagRepositoryTest extends AbstractRepository {
    @Autowired
    TagRepository tagRepository;

    @Test
    @DisplayName("Tag: find by name and names in a List")
    void shouldFindByNameAndNamesInAList() {
        //Given
        tagRepository.save(Tag.builder().name("Hugo Etiketa").build());
        tagRepository.save(Tag.builder().name("Por los cielos").build());

        // When / Then
        assertThat(tagRepository.findByName("Hugo Etiketa")).isPresent();
        assertThat(tagRepository.findByNameIn(List.of("Hugo Etiketa", "Por los cielos", "Temu Shipping")))
                .extracting(Tag::getName)
                .contains("Hugo Etiketa", "Por los cielos");
    }
}
