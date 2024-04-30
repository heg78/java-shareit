package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ItemRequestMapperTest {
    private ItemRequest itemRequest;
    private ItemRequestDto itemRequestDto;
    private UserDto userDto;
    private User user;

    @BeforeEach
    void setUp() {
        userDto = new UserDto(1L, "name", "email@email.ru");
        user = UserMapper.toUser(userDto);
        itemRequestDto = new ItemRequestDto(1L, "Decription", user, null, List.of());
        itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto);
    }

    @Test
    void toItemRequest() {
        ItemRequest testItemRequest = ItemRequestMapper.toItemRequest(itemRequestDto);
        assertEquals(testItemRequest.getId(), itemRequest.getId());
        assertEquals(testItemRequest.getRequester(), itemRequest.getRequester());
        assertEquals(testItemRequest.getDescription(), itemRequest.getDescription());
    }

    @Test
    void toItemRequestDto() {
        ItemRequestDto testItemRequestDto = ItemRequestMapper.toItemRequestDto(itemRequest);
        assertEquals(testItemRequestDto.getId(), itemRequestDto.getId());
        assertEquals(testItemRequestDto.getRequester(), itemRequestDto.getRequester());
        assertEquals(testItemRequestDto.getDescription(), itemRequestDto.getDescription());
    }
}