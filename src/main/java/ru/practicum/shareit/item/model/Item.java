package ru.practicum.shareit.item.model;

import lombok.Data;
import ru.practicum.shareit.request.ItemRequest;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class Item {
    int id;
    String name;
    String description;
    boolean available;
    int ownerId;
    ItemRequest request;

    public Item(String name, String description, boolean available, int ownerId) {
        this.name = name;
        this.description = description;
        this.available = available;
        this.ownerId = ownerId;
    }
}
