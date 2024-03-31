package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * TODO Sprint add-controllers.
 */
@Data
@AllArgsConstructor
public class ItemDto {
    @NotBlank
    String name;

    @NotBlank
    String description;

    @NotNull
    Boolean available;

    int ownerId;
    Integer request;
}
