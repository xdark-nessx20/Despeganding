package co.edu.unimagdalena.despeganding.api.dto;

import jakarta.annotation.Nullable;

import java.io.Serializable;
import java.math.BigDecimal;

public class BookingItemDTOs {
    public record BookingItemCreateRequest(String cabin, BigDecimal price, Integer segmentOrder, Long flight_id) implements Serializable {}
    public record BookingItemUpdateRequest(String cabin, BigDecimal price, @Nullable Long flight_id) implements Serializable {}
    public record BookingItemResponse(Long id, String cabin, BigDecimal price, Integer segmentOrder,
                                      FlightDTOs.FlightResponse flight) implements Serializable{}
}
