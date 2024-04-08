package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;

import java.util.List;

@Component
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemStorage itemStorage;

    @Override
    public Item addItem(ItemDto itemDto, int ownerId) {
        return itemStorage.addItem(itemDto, ownerId);
    }

    @Override
    public Item updateItem(ItemDto itemDto, int itemId, int ownerId) {
        return itemStorage.updateItem(itemDto, itemId, ownerId);
    }

    @Override
    public Item getItemById(int itemId) {
        return itemStorage.getItemById(itemId);
    }

    @Override
    public List<ItemDto> getUserItems(int ownerId) {
        return itemStorage.getUserItems(ownerId);
    }

    @Override
    public List<ItemDto> getSearch(String text) {
        return itemStorage.getSearch(text);
    }
}
