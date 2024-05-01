package ru.practicum.shareit.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = UserController.class)
@ExtendWith(MockitoExtension.class)
class UserControllerTest {
    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    private final UserDto userDto = new UserDto(1L, "name", "email@email.ru");
    private final User user = UserMapper.toUser(userDto);

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @SneakyThrows
    @Test
    void getUserTest() {
        long userId = 1L;

        mvc.perform(MockMvcRequestBuilders.get("/users/{userId}", userId))
                .andExpect(status().isOk());

        verify(userService).getUser(userId);
    }

    @SneakyThrows
    @Test
    void saveNewUserTest() {
        when(userService.saveUser(userDto)).thenReturn(userDto);

        String resultMvc = mvc.perform(post("/users")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(userDto), resultMvc);
    }

    @SneakyThrows
    @Test
    void getAllUsersTest() {
        when(userService.getAllUsers()).thenReturn(List.of(userDto));
        String resultMvc = mvc.perform(MockMvcRequestBuilders.get("/users"))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(userService).getAllUsers();
        assertEquals(mapper.writeValueAsString(List.of(userDto)), resultMvc);
    }

    @SneakyThrows
    @Test
    void deleteUserTest() {
        String resultMvc = mvc.perform(MockMvcRequestBuilders.delete("/users/{userId}", 1L))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        verify(userService).deleteUser(1L);
    }

    @SneakyThrows
    @Test
    void updateUserTest() {
        long userId = 1L;
        when(userService.updateUser(userId, userDto)).thenReturn(userDto);

        String resultMvc = mvc.perform(MockMvcRequestBuilders.patch("/users/{userId}", userId)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(userDto)))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(userService).updateUser(userId, userDto);
        assertEquals(mapper.writeValueAsString(userDto), resultMvc);
    }
}