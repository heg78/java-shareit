package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ExtendWith(MockitoExtension.class)
class UserDaoImplTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserDaoImpl userDao;

    private final UserDto userDto = new UserDto(1L, "name", "email@email.ru");
    private final User user = UserMapper.toUser(userDto);

    @Test
    void getAllUsersTest() {
        when(userRepository.findAll()).thenReturn(List.of(user));
        List<User> userList = userDao.getAllUsers();
        assertEquals(userList, List.of(user));
    }

    @Test
    void getUserTest() {
        when(userRepository.findById(anyLong())).thenReturn(Optional.of(user));
        User testUser = userDao.getUser(1L);
        assertEquals(testUser, user);
    }

    @Test
    void deleteUserTest() {
        userDao.deleteUser(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void saveUserTest() {
        when(userRepository.save(any(User.class))).thenReturn(user);
        User testUser = userDao.saveUser(user);
        assertEquals(testUser, user);
    }

    @Test
    void findByEmailIgnoreCaseTest() {
        when(userRepository.findByEmailIgnoreCase(anyString())).thenReturn(List.of(user));
        List<User> userList = userDao.findByEmailIgnoreCase("Search text");
        assertEquals(userList, List.of(user));
    }

    @Test
    void exists() {
        when(userRepository.existsById(anyLong())).thenReturn(true);
        assertEquals(userDao.exists(1L), true);
    }
}