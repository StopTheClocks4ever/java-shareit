package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoBookings;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    ItemDto addItem(ItemDto itemDto, int ownerId);

    ItemDto updateItem(ItemDto itemDto, int itemId, int ownerId);

    ItemDtoBookings getItemById(int ownerId, int itemId);

    List<ItemDtoBookings> getUserItems(int ownerId);

    List<ItemDto> getSearch(String text);
}
