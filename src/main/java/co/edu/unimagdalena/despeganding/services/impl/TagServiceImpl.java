package co.edu.unimagdalena.despeganding.services.impl;

import co.edu.unimagdalena.despeganding.api.dto.TagDTOs.*;
import co.edu.unimagdalena.despeganding.domain.repositories.TagRepository;
import co.edu.unimagdalena.despeganding.exceptions.NotFoundException;
import co.edu.unimagdalena.despeganding.services.TagService;
import co.edu.unimagdalena.despeganding.services.mappers.TagMapper;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service @RequiredArgsConstructor
public class TagServiceImpl implements TagService {
    private final TagRepository tagRepository;

    @Override
    public TagResponse createTag(TagCreateRequest request) {
        var tag = TagMapper.toEntity(request);
        return TagMapper.toResponse(tagRepository.save(tag));
    }

    @Override
    public TagResponse getTag(Long id) {
        return tagRepository.findById(id).map(TagMapper::toResponse).orElseThrow(() -> new NotFoundException("Tag %d not found".formatted(id)));
    }

    @Override
    public void deleteTag(Long id) {
        tagRepository.deleteById(id);
    }

    @Override
    public List<TagResponse> listAllTags() {
        return tagRepository.findAll().stream().map(TagMapper::toResponse).toList();
    }

    @Override
    public List<TagResponse> listTagsByNameIn(Collection<String> names) {
        return tagRepository.findByNameIn(names).stream().map(TagMapper::toResponse).toList();
    }
}
