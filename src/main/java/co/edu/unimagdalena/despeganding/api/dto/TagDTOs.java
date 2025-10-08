package co.edu.unimagdalena.despeganding.api.dto;

import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

public class TagDTOs {
    public record TagCreateRequest(@NotBlank String name) implements Serializable {}
    public record TagResponse(Long id, String name) implements Serializable{}
}
