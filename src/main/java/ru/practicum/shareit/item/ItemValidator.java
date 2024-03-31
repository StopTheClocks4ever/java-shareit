package ru.practicum.shareit.item;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.exceptions.ValidationException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

@Slf4j
public class ItemValidator {

    @Autowired
    private UserStorage userStorage;

    /*public static boolean validate(ItemDto itemDto) {
        if (itemDto.isAvailable() || user.getEmail().isBlank()) {
            log.warn("Адрес электронной почты не может быть пустым.");
            throw new ValidationException("Адрес электронной почты не может быть пустым.");
        }
        if (!user.getEmail().contains("@")) {
            log.warn("Введен неверный адрес электронной почты.");
            throw new ValidationException("Введен неверный адрес электронной почты.");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            log.warn("Имя пользователя не может быть пустым.");
            throw new ValidationException("Имя пользователя не может быть пустым.");
        }
        return true;
    }*/
}
