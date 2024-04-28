package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.common.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserDao;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ExtendWith(MockitoExtension.class)
class ItemServiceTest {
    @Mock
    private ItemDao itemRepository;
    @Mock
    private UserDao userRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private CommentRepository commentRepository;
    @Mock
    private ItemRequestRepository itemRequestRepository;
    @InjectMocks
    ItemService itemService;
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
    void getAllItemsTest() {
        when(itemRepository.getAllItems(any(User.class))).thenReturn(List.of(item));
        when(userRepository.getUser(anyLong())).thenReturn(user);
        List<ItemDto> itemsDto = itemService.getAllItems(1L);
        assertEquals(itemsDto.size(), 1);
        assertEquals(itemsDto.get(0).getDescription(), "ItemDescr");
    }

    @Test
    void getItemUserSameOvnerTest() {
        when(itemRepository.getItem(anyLong())).thenReturn(item);
        when(commentRepository.findByItem_Id(anyLong())).thenReturn(List.of());
        when(bookingRepository.findAllItemsAndOwner(anyLong(), anyLong())).thenReturn(List.of());
        ItemDto testItemDto = itemService.getItem(1L, 1L);
        assertEquals(testItemDto, itemDto);
    }

    @Test
    void getItemUserNotSameOvnerTest() {
        when(itemRepository.getItem(anyLong())).thenReturn(item);
        when(commentRepository.findByItem_Id(anyLong())).thenReturn(List.of());
        ItemDto testItemDto = itemService.getItem(2L, 1L);
        assertEquals(testItemDto, itemDto);
    }

    @Test
    void searchItemEmptyTextTest() {
        List<Item> testItemList = itemService.searchItem("");
        assertEquals(testItemList, new ArrayList<>());
    }

    @Test
    void searchItemNotEmptyTextTest() {
        when(itemRepository.searchItem(anyString())).thenReturn(List.of(item));
        List<Item> testItemList = itemService.searchItem("search text");
        assertEquals(testItemList, List.of(item));
    }

    @Test
    void saveItemTest() {
        when(userRepository.getUser(anyLong())).thenReturn(user);
        when(itemRepository.saveItem(any(Item.class))).thenReturn(item);
        ItemDto testItemDto = itemService.saveItem(1L, itemDto);
        assertEquals(testItemDto, itemDto);
    }

    @Test
    void saveItemWithRequestTest() {
        when(userRepository.getUser(anyLong())).thenReturn(user);
        item.setRequest(itemRequest);
        when(itemRepository.saveItem(any(Item.class))).thenReturn(item);
        when(itemRequestRepository.getReferenceById(anyLong())).thenReturn(itemRequest);
        itemDto.setRequestId(1L);
        ItemDto testItemDto = itemService.saveItem(1L, itemDto);
        assertEquals(testItemDto, itemDto);
    }

    @Test
    void updateItemWithEmptyUserTest() {
        Assertions.assertThrows(ValidationException.class, () -> itemService.updateItem(null, 1L, itemDto));
    }

    @Test
    void updateItemNotOwnerUserTest() {
        when(itemRepository.getItem(anyLong())).thenReturn(item);
        Assertions.assertThrows(NotFoundException.class, () -> itemService.updateItem(2L, 1L, itemDto));
    }

    @Test
    void updateItemTest() {
        when(itemRepository.getItem(anyLong())).thenReturn(item);
        when(itemRepository.updateItem(any(Item.class))).thenReturn(item);
        when(userRepository.getUser(anyLong())).thenReturn(user);
        ItemDto testItemDto = itemService.updateItem(1L, 1L, itemDto);
        assertEquals(testItemDto, itemDto);
    }

    @Test
    void saveEmptyCommentTest() {
        commentDto.setText("");
        Assertions.assertThrows(ValidationException.class, () -> itemService.saveComment(1L, 1L, commentDto));
    }

    @Test
    void saveCommentNotCorrectUserTest() {
        when(itemRepository.getItem(anyLong())).thenReturn(item);
        when(userRepository.getUser(anyLong())).thenReturn(user);
        when(bookingRepository.existsByBookerAndItem(anyLong(), anyLong(), any(LocalDateTime.class))).thenReturn(false);
        Assertions.assertThrows(ValidationException.class, () -> itemService.saveComment(1L, 1L, commentDto));
    }

    @Test
    void saveCommentTest() {
        Comment testComment = CommentMapper.toComment(commentDto);
        testComment.setAuthor(user);
        testComment.setItem(item);
        testComment.setCreated(LocalDateTime.now());
        commentDto.setCreated(testComment.getCreated());
        when(itemRepository.getItem(anyLong())).thenReturn(item);
        when(userRepository.getUser(anyLong())).thenReturn(user);
        when(bookingRepository.existsByBookerAndItem(anyLong(), anyLong(), any(LocalDateTime.class))).thenReturn(true);
        when(commentRepository.save(any(Comment.class))).thenReturn(testComment);
        CommentDto testCommentDto = itemService.saveComment(1L, 1L, commentDto);
        assertEquals(testCommentDto, commentDto);
    }
}