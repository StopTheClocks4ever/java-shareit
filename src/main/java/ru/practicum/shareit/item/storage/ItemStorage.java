package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemStorage {

    Item addItem(ItemDto itemDto, int ownerId);

    Item updateItem(ItemDto itemDto, int itemId, int ownerId);

    Item getItemById(int itemId);

    List<ItemDto> getUserItems(int ownerId);

    List<ItemDto> getSearch(String text);
}
