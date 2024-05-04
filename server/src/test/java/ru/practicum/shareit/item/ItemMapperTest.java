package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

class ItemMapperTest {
    private UserDto userDto;
    private User user;
    private ItemRequestDto itemRequestDto;
    private ItemRequest itemRequest;
    private ItemDto itemDto;
    private Item item;
    private CommentDto commentDto;

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

    @Test
    void toItemTest() {
        Item testItem = ItemMapper.toItem(itemDto);
        assertEquals(testItem.getId(), item.getId());
        assertEquals(testItem.getDescription(), item.getDescription());
    }

    @Test
    void toItemDtoTest() {
        ItemDto testItemDto = ItemMapper.toItemDto(item, List.of());
        assertEquals(testItemDto.getId(), itemDto.getId());
        assertEquals(testItemDto.getDescription(), itemDto.getDescription());
    }

    @Test
    void toItemDtoWithCommentsTest() {
        ItemDto testItemDto = ItemMapper.toItemDto(item, List.of(), List.of());
        assertEquals(testItemDto.getId(), itemDto.getId());
        assertEquals(testItemDto.getDescription(), itemDto.getDescription());
    }
}