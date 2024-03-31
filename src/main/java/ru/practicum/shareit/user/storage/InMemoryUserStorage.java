package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.exceptions.DuplicateEmailException;
import ru.practicum.shareit.user.exceptions.ValidationException;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {

    private Map<Integer,User> userStorage = new HashMap<>();
    private int userId = 0;

    @Override
    public User addUser(User user) {
        if (isUserDuplicated(user.getEmail())) {
            throw new DuplicateEmailException("Пользователь с такой почтой уже существует.");
        }
        user.setId(generateNewId());
        userStorage.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user, int userId) {
        User existedUser = userStorage.get(userId);
        if (user.getEmail() != null && !user.getEmail().contains("@")) {
            throw new ValidationException("Введен неверный адрес электронной почты.");
        }
        if (user.getEmail() != null && !existedUser.getEmail().equals(user.getEmail())) {
            if (isUserDuplicated(user.getEmail())) {
                throw new DuplicateEmailException("Пользователь с такой почтой уже существует.");
            }
            existedUser.setEmail(user.getEmail());
        }
        if (user.getName() != null) {
            existedUser.setName(user.getName());
        }
        userStorage.put(userId, existedUser);
        return existedUser;
    }

    @Override
    public void deleteUser(int userId) {
        userStorage.remove(userId);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(userStorage.values());
    }

    @Override
    public User getUserById(int userId) {
        return userStorage.get(userId);
    }

    @Override
    public Map<Integer, User> getUserMap() {
        return userStorage;
    }

    private int generateNewId() {
        return ++userId;
    }

    private boolean isUserDuplicated(String email) {
        var duplicatedUser = getAllUsers().stream()
                .filter(x -> x.getEmail().equals(email))
                .findAny();
        return duplicatedUser.isPresent();
    }
}
