package co.edu.unimagdalena.despeganding.services.mappers;

import co.edu.unimagdalena.despeganding.api.dto.TagDTOs.*;
import co.edu.unimagdalena.despeganding.domain.entities.Tag;
import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface TagMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "flights", ignore = true)
    Tag toEntity(TagCreateRequest request);

    TagResponse toResponse(Tag tag);
}