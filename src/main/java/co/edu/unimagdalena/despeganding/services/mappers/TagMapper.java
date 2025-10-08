package co.edu.unimagdalena.despeganding.services.mappers;

import co.edu.unimagdalena.despeganding.api.dto.TagDTOs.*;
import co.edu.unimagdalena.despeganding.domain.entities.Tag;

public class TagMapper {
    public static Tag toEntity(TagCreateRequest request){
        return  Tag.builder().name(request.name()).build();
    }
    public static TagResponse toResponse(Tag tag){
        return new TagResponse(tag.getId(), tag.getName());
    }
}
