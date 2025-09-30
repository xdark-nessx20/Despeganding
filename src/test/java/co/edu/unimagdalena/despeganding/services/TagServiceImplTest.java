package co.edu.unimagdalena.despeganding.services;

import co.edu.unimagdalena.despeganding.api.dto.TagDTOs.*;
import co.edu.unimagdalena.despeganding.domain.entities.Tag;
import co.edu.unimagdalena.despeganding.domain.repositories.TagRepository;
import co.edu.unimagdalena.despeganding.services.impl.TagServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
public class TagServiceImplTest {
    @Mock TagRepository tagRepository;

    @InjectMocks TagServiceImpl tagService;

    @Test
    void shouldCreateTagAndMapToResponse(){
        when(tagRepository.save(any())).thenAnswer(invocation -> {
            Tag tag = invocation.getArgument(0);
            tag.setId(1L); tag.setName("Tag1");
            return tag;
        });

        var response = tagService.createTag(new TagCreateRequest("Tag1"));

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.name()).isEqualTo("Tag1");
    }

    @Test
    void shouldListAllTags(){
        when(tagRepository.findAll()).thenReturn(List.of(
                Tag.builder().id(1L).name("pruebi-tag").build(),
                Tag.builder().id(2L).name("tag-xd").build(),
                Tag.builder().id(3L).name("tag-zzz").build(),
                Tag.builder().id(4L).name("miTag").build()
        ));

        var response = tagService.listAllTags();

        assertThat(response).hasSize(4);
        assertThat(response).extracting(TagResponse::name).containsExactly("pruebi-tag", "tag-xd", "tag-zzz", "miTag");
    }

    @Test
    void shouldListTagsByNameIn(){
        var tagNames = List.of("pruebi-tag", "miTag");
        when(tagRepository.findByNameIn(tagNames)).thenReturn(List.of(
                Tag.builder().id(1L).name("pruebi-tag").build(),
                Tag.builder().id(4L).name("miTag").build()
        ));

        var response = tagService.listTagsByNameIn(tagNames);

        assertThat(response).hasSize(2);
        assertThat(response).extracting(TagResponse::name).containsExactly("pruebi-tag", "miTag");
    }
}
