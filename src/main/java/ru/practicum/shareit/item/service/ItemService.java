package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    Item addItem(ItemDto itemDto, int ownerId);
    Item updateItem(ItemDto itemDto, int itemId, int ownerId);

    Item getItemById(int itemId);

    List<Item> getUserItems(int ownerId);

    List<Item> getSearch(String text);
}
