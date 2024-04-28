package ru.practicum.shareit.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    private final UserDto userDto = new UserDto(1L, "name", "email@email.ru");
    private final User user = UserMapper.toUser(userDto);

    @BeforeEach
    void setUp() {
        userRepository.save(user);
    }

    @Test
    void findByEmailIgnoreCaseTest() {
        List<User> foundUser = userRepository.findByEmailIgnoreCase("email@email.ru");

        assertEquals(foundUser.size(), 1);
        assertEquals(foundUser.get(0).getEmail(), "email@email.ru");
    }
}