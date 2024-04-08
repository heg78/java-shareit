package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.common.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserDao userDao;

    public List<UserDto> getAllUsers() {
        return userDao.getAllUsers().stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    public UserDto getUser(Long userId) {
        return UserMapper.toUserDto(userDao.getUser(userId));
    }

    public void deleteUser(Long userId) {
        userDao.deleteUser(userId);
    }

    public UserDto saveUser(UserDto userDto) {
        if (userDto.getEmail() == null || !userDto.getEmail().contains("@")) {
            throw new ValidationException("Не указан Email или указан некорректно");
        }
        return UserMapper.toUserDto(userDao.saveUser(UserMapper.toUser(userDto)));
    }

    public UserDto updateUser(Long userId, UserDto newUserDto) {
        newUserDto.setId(userId);
        if (newUserDto.getEmail() != null && !newUserDto.getEmail().contains("@")) {
            throw new ValidationException("Не указан Email или указан некорректно");
        }

        User oldUser = userDao.getUser(userId);
        boolean existEmail = ! userDao.findByEmailIgnoreCase(newUserDto.getEmail()).isEmpty();
        if (newUserDto.getEmail() != null && existEmail && !oldUser.getEmail().equals(newUserDto.getEmail())) {
            throw new RuntimeException("Такой email уже имеется");
        }

        oldUser.setName(newUserDto.getName() != null ? newUserDto.getName() : oldUser.getName());
        oldUser.setEmail(newUserDto.getEmail() != null ? newUserDto.getEmail() : oldUser.getEmail());

        User updatedUser = userDao.saveUser(oldUser);
        return UserMapper.toUserDto(updatedUser);
    }
}
