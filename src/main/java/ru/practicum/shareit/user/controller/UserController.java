package ru.practicum.shareit.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.UserValidator;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@Slf4j
@RequestMapping(path = "/users")
public class UserController {

    private UserStorage userStorage;

    public UserController(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @PostMapping
    public UserDto addUser(@RequestBody @Valid User user) {
        log.info("Получен запрос POST /users");
        if (UserValidator.validate(user)) {
            userStorage.addUser(user);
            log.info("Запрос успешно обработан");
            return UserMapper.toUserDto(user);
        }
        return UserMapper.toUserDto(user);
    }

    @PatchMapping("/{userId}")
    public UserDto patchUser(@RequestBody User user, @PathVariable int userId) {
        log.info("Получен запрос PATCH /users/{userId}");
        userStorage.updateUser(user, userId);
        return userStorage.getUserById(userId);
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable int userId) {
        log.info("Получен запрос DELETE /users/{userId}");
        userStorage.deleteUser(userId);
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        log.info("Получен запрос GET /users");
        return userStorage.getAllUsers();
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable int userId) {
        log.info("Получен запрос GET /users/{userId}");
        return userStorage.getUserById(userId);
    }
}
