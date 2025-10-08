package co.edu.unimagdalena.despeganding.services.mappers;

import co.edu.unimagdalena.despeganding.api.dto.AirlineDTOs.*;
import co.edu.unimagdalena.despeganding.domain.entities.Airline;

public class AirlineMapper {
    public static Airline toEntity(AirlineCreateRequest request){
        return Airline.builder().code(request.code()).name(request.name()).build();
    }

    public static AirlineResponse toResponse(Airline entity){
        //Mm, this is something weird, i´ll check it out later
        return new AirlineResponse(entity.getId(), entity.getCode(), entity.getName());
    }

    public static void patch(Airline entity, AirlineUpdateRequest request){
        if (request.code() != null) entity.setCode(request.code());
        if (request.name() != null) entity.setName(request.name());
    }
}
