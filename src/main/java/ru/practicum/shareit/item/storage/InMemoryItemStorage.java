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
/*        if (isOwnerExists(ownerId)) {
            Item item = ItemMapper.toItem(itemDto, ownerId);
            item.setId(generateNewId());
            itemStorage.put(item.getId(), item);
            return item;
        } else {
            throw new UserNotFoundException("Указанного владельца не существует");
        }
*/
        return null;
    }

    @Override
    public Item updateItem(ItemDto itemDto, int itemId, int ownerId) {
/*        Item existedItem = getItemById(itemId);
        if (existedItem.getOwner().getId() != ownerId) {
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

 */
        return null;
    }

    @Override
    public Item getItemById(int itemId) {
        /* return itemStorage.get(itemId);

         */
        return null;
    }

    @Override
    public List<ItemDto> getUserItems(int ownerId) {
/*        //List<ItemDto> userItems = new ArrayList<>();
        List<Item> allItems = new ArrayList<>(itemStorage.values());
        List<ItemDto> userItems = allItems
                .stream()
                .filter(i -> i.getOwner().getId() == ownerId)
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
        return userItems;

 */
        return null;
    }

    @Override
    public List<ItemDto> getSearch(String text) {
/*        if (text.isBlank()) {
            return new ArrayList<>();
        }
        List<Item> allItems = new ArrayList<>(itemStorage.values());
        List<ItemDto> searchResult = allItems
                .stream()
                .filter(Item::isAvailable)
                .filter(i -> i.getName().toUpperCase().contains(text.toUpperCase()) || i.getDescription().toUpperCase().contains(text.toUpperCase()))
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
        return searchResult;

 */
        return null;
    }

    private int generateNewId() {
        return ++itemId;
    }

    private boolean isOwnerExists(int ownerId) {
        Map<Integer, User> users = userStorage.getUserMap();
        return users.containsKey(ownerId);
    }
}

