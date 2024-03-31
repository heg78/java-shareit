package ru.practicum.shareit.user;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.common.exception.ValidationException;
import ru.practicum.shareit.user.model.User;

import java.util.*;

@Component
class UserDaoImpl implements UserDao{

    private final Map<Long, User> users = new HashMap<>();
    private final Set<String> emails = new HashSet<>();
    private Long userId = 0L;

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUser(Long userId){
        return users.get(userId);
    }

    @Override
    public void deleteUser(Long userId) {
        emails.remove(users.get(userId).getEmail());
        users.remove(userId);
    }

    @Override
    public User saveUser(User user) {
        if (emails.contains(user.getEmail())) {
            throw new RuntimeException("Такой email уже имеется");
        }
        if (user.getEmail() == null || !user.getEmail().contains("@")) {
            throw new ValidationException("Не указан Email или указан некорректно");
        }
        userId++;
        emails.add(user.getEmail());
        user.setId(userId);
        users.put(userId, user);
        return user;
    }

    @Override
    public User updateUser(Long userId, User newUser) {
        newUser.setId(userId);
        User oldUser = users.get(userId);

        if (newUser.getEmail() != null && emails.contains(newUser.getEmail()) && !oldUser.getEmail().equals(newUser.getEmail())) {
            throw new RuntimeException("Такой email уже имеется");
        }
        if (newUser.getEmail() != null && !newUser.getEmail().contains("@")) {
            throw new ValidationException("Не указан Email или указан некорректно");
        }

        newUser.setName(newUser.getName() != null ? newUser.getName() : oldUser.getName());
        newUser.setEmail(newUser.getEmail() != null ? newUser.getEmail() : oldUser.getEmail());
        users.put(userId, newUser);

        emails.remove(oldUser.getEmail());
        emails.add(newUser.getEmail());
        return newUser;
    }
}
