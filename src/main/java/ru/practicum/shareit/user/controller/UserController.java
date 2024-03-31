package ru.practicum.shareit.user.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.UserValidator;
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

    @Autowired
    private UserStorage userStorage;

    @PostMapping
    public User addUser(@RequestBody @Valid User user) {
        log.info("Получен запрос POST /users");
        if(UserValidator.validate(user)) {
            userStorage.addUser(user);
            log.info("Запрос успешно обработан");
            return user;
        }
        return user;
    }

    @PatchMapping("/{userId}")
    public User patchUser(@RequestBody User user, @PathVariable int userId) {
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
    public List<User> getAllUsers() {
        log.info("Получен запрос GET /users");
        return userStorage.getAllUsers();
    }

    @GetMapping("/{userId}")
    public User getUserById(@PathVariable int userId) {
        log.info("Получен запрос GET /users/{userId}");
        return userStorage.getUserById(userId);
    }
}
