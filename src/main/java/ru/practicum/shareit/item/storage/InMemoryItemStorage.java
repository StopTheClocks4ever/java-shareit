package ru.practicum.shareit.item.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.exception.IncorrectUserException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class InMemoryItemStorage implements ItemStorage {

    private Map<Integer, Item> itemStorage = new HashMap<>();
    private int itemId = 0;
    @Autowired
    private UserStorage userStorage;


    @Override
    public Item addItem(ItemDto itemDto, int ownerId) {
        if (isOwnerExists(ownerId)) {
            Item item = ItemMapper.toItem(itemDto, ownerId);
            item.setId(generateNewId());
            itemStorage.put(item.getId(), item);
            return item;
        } else {
            throw new UserNotFoundException("Указанного владельца не существует");
        }

    }

    @Override
    public Item updateItem(ItemDto itemDto, int itemId, int ownerId) {
        Item existedItem = getItemById(itemId);
        if (existedItem.getOwnerId() != ownerId) {
            throw new IncorrectUserException("Неверный владелец");
        }
        if (itemDto.getName() != null) {
            existedItem.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            existedItem.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            existedItem.setAvailable(itemDto.getAvailable());
        }
        return existedItem;
    }

    @Override
    public Item getItemById(int itemId) {
        return itemStorage.get(itemId);
    }

    @Override
    public List<Item> getUserItems(int ownerId) {
        //List<ItemDto> userItems = new ArrayList<>();
        List<Item> allItems = new ArrayList<>(itemStorage.values());
        List<Item> userItems = allItems
                .stream()
                .filter(i -> i.getOwnerId() == ownerId)
                .collect(Collectors.toList());
        return userItems;
    }

    @Override
    public List<Item> getSearch(String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        List<Item> allItems = new ArrayList<>(itemStorage.values());
        List<Item> searchResult = allItems
                .stream()
                .filter(Item::isAvailable)
                .filter(i -> i.getName().toUpperCase().contains(text.toUpperCase()) || i.getDescription().toUpperCase().contains(text.toUpperCase()))
                .collect(Collectors.toList());
        return searchResult;
    }

    private int generateNewId() {
        return ++itemId;
    }

    private boolean isOwnerExists(int ownerId) {
        Map<Integer, User> users = userStorage.getUserMap();
        return users.containsKey(ownerId);
    }
}

