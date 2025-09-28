package co.edu.unimagdalena.despeganding.services;

import co.edu.unimagdalena.despeganding.api.dto.TagDTOs.*;

import java.util.Collection;
import java.util.List;

public interface TagService {
    //Basic CRUD
    TagResponse createTag(TagCreateRequest request);
    TagResponse getTag(Long id);
    void deleteTag(Long id);
    List<TagResponse> listAllTags();
    //------------------------------------//
    List<TagResponse> listTagsByNameIn(Collection<String> names);
}
