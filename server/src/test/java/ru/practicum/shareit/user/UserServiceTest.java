package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.common.exception.EmailValidationExcepotion;
import ru.practicum.shareit.common.exception.ValidationException;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserDao userDao;
    @InjectMocks
    private UserService userService;

    private final UserDto userDto = new UserDto(1L, "name", "email@email.ru");
    private final User user = UserMapper.toUser(userDto);

    @Test
    void addNewUserTest() {
        when(userDao.saveUser(any())).thenReturn(user);
        UserDto actualUserDto = userService.saveUser(userDto);
        assertEquals(userDto, actualUserDto);
    }

    @Test
    void addNewUserEmptyEmailTest() {
        UserDto userEmptyEmailDto = userDto;
        userEmptyEmailDto.setEmail("");
        Assertions.assertThrows(ValidationException.class, () -> userService.saveUser(userDto));
    }

    @Test
    void getAllUsersTest() {
        when(userDao.getAllUsers()).thenReturn(List.of(user));
        List<UserDto> users = userService.getAllUsers();
        assertEquals(List.of(userDto), users);
    }

    @Test
    void getUserTest() {
        when(userDao.getUser(anyLong())).thenReturn(user);
        UserDto actualUserDto = userService.getUser(1L);
        List<UserDto> users = userService.getAllUsers();
        assertEquals(userDto, actualUserDto);
    }

    @Test
    void updateUserEmptyEmailTest() {
        UserDto userDtoEmptyEmail = userDto;
        userDtoEmptyEmail.setEmail("");
        Assertions.assertThrows(ValidationException.class, () -> userService.updateUser(1L, userDtoEmptyEmail));
    }

    @Test
    void updateUserSameEmailTest() {
        when(userDao.getUser(anyLong())).thenReturn(user);
        when(userDao.findByEmailIgnoreCase(anyString())).thenReturn(List.of(user));
        UserDto userDtoSameEmail = userDto;
        userDtoSameEmail.setEmail("email2@email.ru");
        Assertions.assertThrows(EmailValidationExcepotion.class, () -> userService.updateUser(1L, userDtoSameEmail));
    }

    @Test
    void updateUserTest() {
        when(userDao.getUser(anyLong())).thenReturn(user);
        when(userDao.findByEmailIgnoreCase(anyString())).thenReturn(List.of());
        UserDto userDtoNewEmail = userDto;
        userDtoNewEmail.setEmail("email2@email.ru");
        when(userDao.saveUser(any())).thenReturn(UserMapper.toUser(userDtoNewEmail));
        UserDto updatedUserDto = userService.updateUser(1L, userDtoNewEmail);
        assertEquals(updatedUserDto.getEmail(), "email2@email.ru");
    }

    @Test
    void deleteUserTest() {
        userService.deleteUser(1L);
        verify(userDao, times(1)).deleteUser(1L);
    }
}