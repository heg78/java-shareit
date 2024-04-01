package ru.practicum.shareit.user;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserDao {
    List<User> getAllUsers();

    User getUser(Long userId);

    void deleteUser(Long userId);

    User saveUser(User user);

    User updateUser(User user);
}
