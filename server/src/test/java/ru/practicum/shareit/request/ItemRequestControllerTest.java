package ru.practicum.shareit.request;

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
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemRequestController.class)
@ExtendWith(MockitoExtension.class)
class ItemRequestControllerTest {
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    @MockBean
    private ItemRequestService itemRequestService;

    private ItemRequestDto itemRequestDto;
    private UserDto userDto;
    private User user;

    private static final String XUSER = "X-Sharer-User-Id";

    @BeforeEach
    void setUp() {
        userDto = new UserDto(1L, "name", "email@email.ru");
        user = UserMapper.toUser(userDto);
        itemRequestDto = new ItemRequestDto(1L, "Description", user, null, List.of());
    }

    @AfterEach
    void tearDown() {
    }

    @SneakyThrows
    @Test
    void addTest() {
        when(itemRequestService.add(anyLong(), any())).thenReturn(itemRequestDto);

        String webResult = mvc.perform(post("/requests")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(itemRequestDto))
                        .header(XUSER, user.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(itemRequestDto), webResult);
    }

    @SneakyThrows
    @Test
    void getTest() {
        when(itemRequestService.get(anyLong(), anyLong())).thenReturn(itemRequestDto);

        String webResult = mvc.perform(MockMvcRequestBuilders.get("/requests/{requestId}", 1L)
                        .header(XUSER, user.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        ;

        verify(itemRequestService).get(1L, 1L);
        assertEquals(mapper.writeValueAsString(itemRequestDto), webResult);
    }

    @SneakyThrows
    @Test
    void getUserRequestsTest() {
        when(itemRequestService.getUserRequests(anyLong())).thenReturn(List.of(itemRequestDto));

        String webResult = mvc.perform(MockMvcRequestBuilders.get("/requests")
                        .header(XUSER, user.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        ;

        verify(itemRequestService).getUserRequests(1L);
        assertEquals(mapper.writeValueAsString(List.of(itemRequestDto)), webResult);
    }

    @SneakyThrows
    @Test
    void getNotUserRequestsTest() {
        when(itemRequestService.getNotUserRequests(anyLong(), anyInt(), anyInt())).thenReturn(List.of(itemRequestDto));

        String webResult = mvc.perform(MockMvcRequestBuilders.get("/requests/all")
                        .header(XUSER, user.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        ;

        verify(itemRequestService).getNotUserRequests(anyLong(), anyInt(), anyInt());
        assertEquals(mapper.writeValueAsString(List.of(itemRequestDto)), webResult);
    }
}