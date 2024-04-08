package ru.practicum.shareit.item.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@Slf4j
@RequestMapping("/items")
public class ItemController {

    private ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping
    public ItemDto addItem(@RequestBody @Valid ItemDto itemDto, @RequestHeader("X-Sharer-User-Id") int ownerId) {
        log.info("Получен запрос POST /items");
        return ItemMapper.toItemDto(itemService.addItem(itemDto, ownerId));
    }

    @PatchMapping("/{itemId}")
    public ItemDto patchItem(@RequestBody ItemDto itemDto, @PathVariable int itemId, @RequestHeader("X-Sharer-User-Id") int ownerId) {
        log.info("Получен запрос PATCH /items/{itemId}");
        return ItemMapper.toItemDto(itemService.updateItem(itemDto, itemId, ownerId));
    }

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@PathVariable int itemId) {
        log.info("Получен запрос GET /items/{itemId}");
        return ItemMapper.toItemDto(itemService.getItemById(itemId));
    }

    @GetMapping
    public List<ItemDto> getUserItems(@RequestHeader("X-Sharer-User-Id") int ownerId) {
        log.info("Получен запрос GET /items");
        return itemService.getUserItems(ownerId);
    }

    @GetMapping("/search")
    public List<ItemDto> getSearch(@RequestParam(value = "text") String text) {
        log.info("Получен запрос GET /search");
        return itemService.getSearch(text);
    }

}
