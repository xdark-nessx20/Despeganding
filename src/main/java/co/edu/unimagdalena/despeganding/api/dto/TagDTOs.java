package co.edu.unimagdalena.despeganding.api.dto;

import java.io.Serializable;

public class TagDTOs {
    public record TagCreateRequest(String name) implements Serializable {}
    public record TagResponse(Long id, String name) implements Serializable{}
}
