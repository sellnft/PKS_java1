package javafxfrontend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ErrorResponse(
        @JsonProperty("status") String status,
        @JsonProperty("message") String message
) {}