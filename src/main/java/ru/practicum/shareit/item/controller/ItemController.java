package ru.practicum.shareit.item.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@Slf4j
@RequestMapping("/items")
public class ItemController {

    @Autowired
    private ItemService itemService;

    @PostMapping
    public Item addItem(@RequestBody @Valid ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") int ownerId) {
        log.info("Получен запрос POST /items");
        return itemService.addItem(itemDto, ownerId);
    }

    @PatchMapping("/{itemId}")
    public Item patchItem(@RequestBody ItemDto itemDto, @PathVariable int itemId, @RequestHeader("X-Sharer-User-Id") int ownerId) {
        log.info("Получен запрос PATCH /items/{itemId}");
        return itemService.updateItem(itemDto, itemId, ownerId);
    }

    @GetMapping("/{itemId}")
    public Item getItemById(@PathVariable int itemId) {
        log.info("Получен запрос GET /items/{itemId}");
        return itemService.getItemById(itemId);
    }

    @GetMapping
    public List<Item> getUserItems(@RequestHeader("X-Sharer-User-Id") int ownerId) {
        log.info("Получен запрос GET /items");
        return itemService.getUserItems(ownerId);
    }

    @GetMapping("/search")
    public List<Item> getSearch(@RequestParam(value = "text") String text) {
        log.info("Получен запрос GET /search");
        return itemService.getSearch(text);
    }

}
