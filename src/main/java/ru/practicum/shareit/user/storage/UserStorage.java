package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Map;

public interface UserStorage {

    User addUser(User user);

    User updateUser(User user, int userId);

    void deleteUser(int userId);

    List<User> getAllUsers();

    User getUserById(int userId);

}
