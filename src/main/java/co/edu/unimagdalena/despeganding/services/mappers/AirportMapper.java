package co.edu.unimagdalena.despeganding.services.mappers;

import co.edu.unimagdalena.despeganding.api.dto.AirportDTOs.*;
import co.edu.unimagdalena.despeganding.domain.entities.Airport;

public class AirportMapper {
    public static Airport toEntity(AirportCreateRequest request){
        return Airport.builder().code(request.code()).name(request.name()).city(request.city()).build();
    }

    public static AirportResponse toDTO(Airport entity){
        return new AirportResponse(entity.getId(), entity.getCode(),  entity.getName(), entity.getCity());
    }

    public static void patch(Airport entity, AirportUpdateRequest request){
        if (request.code() != null) entity.setCode(request.code());
        if (request.name() != null) entity.setName(request.name());
        //The airport's city doesn't have to change, I think...
    }
}
