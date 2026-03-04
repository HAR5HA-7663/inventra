package com.inventra.dto;

import java.time.LocalDateTime;

public record CategoryResponse(
    Long id,
    String name,
    String description,
    int productCount,
    LocalDateTime createdAt
) {}
