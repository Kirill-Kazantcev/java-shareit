package ru.practicum.shareit.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import ru.practicum.shareit.user.dto.Create;

/**
 * DTO вещи для обмена данными через REST-интерфейс.
 * Обязательность полей проверяется только в группе {@link ru.practicum.shareit.user.dto.Create}.
 */
@Data
public class ItemDto {
    private Long id;

    @NotBlank(groups = Create.class)
    private String name;

    @NotBlank(groups = Create.class)
    private String description;

    @NotNull(groups = Create.class)
    private Boolean available;

    private Long requestId;
}