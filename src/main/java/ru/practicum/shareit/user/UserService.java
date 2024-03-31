package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
        return userDao.saveUser(user);
    }

    public User updateUser(Long userId, User user) {
        return userDao.updateUser(userId, user);
    }
}
