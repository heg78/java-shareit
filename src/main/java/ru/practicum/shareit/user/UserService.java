package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.common.exception.ValidationException;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserDao userDao;

    public List<User> getAllUsers() {
        return userDao.getAllUsers();
    }

    public User getUser(Long userId) {
        return userDao.getUser(userId);
    }

    public void deleteUser(Long userId) {
        userDao.deleteUser(userId);
    }

    public User saveUser(User user) {
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new ValidationException("Не указан Email или указан некорректно");
        }
        return userDao.saveUser(user);
    }

    public User updateUser(Long userId, User newUser) {
        if (newUser.getEmail() != null && !newUser.getEmail().contains("@")) {
            throw new ValidationException("Не указан Email или указан некорректно");
        }
        newUser.setId(userId);
        return userDao.updateUser(newUser);
    }
}
