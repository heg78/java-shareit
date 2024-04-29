package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ItemController.class)
@ExtendWith(MockitoExtension.class)
class ItemControllerTest {
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    @MockBean
    private ItemService itemService;

    private UserDto userDto;
    private User user;
    private ItemRequestDto itemRequestDto;
    private ItemRequest itemRequest;
    private ItemDto itemDto;
    private Item item;
    private CommentDto commentDto;

    private static final String XUSER = "X-Sharer-User-Id";

    @BeforeEach
    void setUp() {
        userDto = new UserDto(1L, "name", "email@email.ru");
        user = UserMapper.toUser(userDto);
        itemRequestDto = new ItemRequestDto(1L, "Decription", user, null, List.of());
        itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto);
        itemDto = new ItemDto(1L, "ItemName", "ItemDescr", true, 1L, null, null, List.of(), null);
        item = ItemMapper.toItem(itemDto);
        item.setOwner(user);
        commentDto = new CommentDto(1L, "Comment text", "name", 1L, null);
    }

    @SneakyThrows
    @Test
    void getAllItems() {
        when(itemService.getAllItems(anyLong())).thenReturn(List.of(itemDto));
        String resultMvc = mvc.perform(MockMvcRequestBuilders.get("/items")
                        .header(XUSER, user.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(itemService).getAllItems(anyLong());
        assertEquals(mapper.writeValueAsString(List.of(itemDto)), resultMvc);
    }


    @SneakyThrows
    @Test
    void getItem() {
        when(itemService.getItem(anyLong(), anyLong())).thenReturn(itemDto);
        String resultMvc = mvc.perform(MockMvcRequestBuilders.get("/items/{itemId}", 1L)
                        .header(XUSER, user.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(itemService).getItem(anyLong(), anyLong());
        assertEquals(mapper.writeValueAsString(itemDto), resultMvc);
    }

    @SneakyThrows
    @Test
    void saveNewItem() {
        when(itemService.saveItem(anyLong(), any(ItemDto.class))).thenReturn(itemDto);
        String resultMvc = mvc.perform(MockMvcRequestBuilders.post("/items")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(itemDto))
                        .header(XUSER, user.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(itemService).saveItem(anyLong(), any(ItemDto.class));
        assertEquals(mapper.writeValueAsString(itemDto), resultMvc);
    }

    @SneakyThrows
    @Test
    void updateItem() {
        when(itemService.updateItem(anyLong(), anyLong(), any(ItemDto.class))).thenReturn(itemDto);
        String resultMvc = mvc.perform(MockMvcRequestBuilders.patch("/items/{itemId}", 1L)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(itemDto))
                        .header(XUSER, user.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(itemService).updateItem(anyLong(), anyLong(), any(ItemDto.class));
        assertEquals(mapper.writeValueAsString(itemDto), resultMvc);
    }

    @SneakyThrows
    @Test
    void searchItem() {
        when(itemService.searchItem(anyString())).thenReturn(List.of(item));
        String resultMvc = mvc.perform(MockMvcRequestBuilders.get("/items/search?text=search text")
                        .header(XUSER, user.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(itemService).searchItem(anyString());
        assertEquals(mapper.writeValueAsString(List.of(itemDto)), resultMvc);
    }

    @SneakyThrows
    @Test
    void saveComment() {
        when(itemService.saveComment(anyLong(), anyLong(), any(CommentDto.class))).thenReturn(commentDto);
        String resultMvc = mvc.perform(MockMvcRequestBuilders.post("/items/{itemId}/comment", 1L)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(commentDto))
                        .header(XUSER, user.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(itemService).saveComment(anyLong(), anyLong(), any(CommentDto.class));
        assertEquals(mapper.writeValueAsString(commentDto), resultMvc);
    }
}