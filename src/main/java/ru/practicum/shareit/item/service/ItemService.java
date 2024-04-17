package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoBookingsAndComments;

import java.util.List;

public interface ItemService {

    ItemDto addItem(ItemDto itemDto, int ownerId);

    ItemDto updateItem(ItemDto itemDto, int itemId, int ownerId);

    ItemDtoBookingsAndComments getItemById(int ownerId, int itemId);

    List<ItemDtoBookingsAndComments> getUserItems(int ownerId);

    List<ItemDto> getSearch(String text);

    CommentDto addComment(CommentDto commentDto, int itemId, int authorId);
}
