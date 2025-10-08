package co.edu.unimagdalena.despeganding.api.dto;

import jakarta.annotation.Nonnull;

import java.io.Serializable;

public class TagDTOs {
    public record TagCreateRequest(@Nonnull String name) implements Serializable {}
    public record TagResponse(Long id, String name) implements Serializable{}
}
