package ru.practicum.shareit.user;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

class UserMapperTest {
    private final UserDto userDto = new UserDto(1L, "name", "email@email.ru");
    private final User user = UserMapper.toUser(userDto);

    @Test
    void toUser() {
        User testUser = UserMapper.toUser(userDto);
        assertEquals(testUser.getEmail(), user.getEmail());
        assertEquals(testUser.getName(), user.getName());
        assertEquals(testUser.getId(), user.getId());
    }

    @Test
    void toUserDto() {
        UserDto testUserDto = UserMapper.toUserDto(user);
        assertEquals(testUserDto.getEmail(), userDto.getEmail());
        assertEquals(testUserDto.getName(), userDto.getName());
        assertEquals(testUserDto.getId(), userDto.getId());
    }
}