package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.ItemDaoImp;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.ItemRequestRepository;
import ru.practicum.shareit.request.ItemRequestServiceIml;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserDao;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
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

    @BeforeEach
    void setUp() {
        userDto = new UserDto(1L, "name", "email@email.ru");
        user = UserMapper.toUser(userDto);
        itemRequestDto = new ItemRequestDto(1L, "Decription", user, null, List.of());
        itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto);
        itemDto = new ItemDto(1L, "ItemName", "ItemDescr", true, 1L, null, null, List.of(), null);
        item = ItemMapper.toItem(itemDto);
        item.setOwner(user);
    }

    @Test
    void getAllItems() {
        when(itemRepository.getAllItems(any(User.class))).thenReturn(List.of(item));
        when(userRepository.getUser(anyLong())).thenReturn(user);
        List<ItemDto> itemsDto =  itemService.getAllItems(1L);
        assertEquals(itemsDto.size(), 1);
        assertEquals(itemsDto.get(0).getDescription(), "ItemDescr");
    }

    @Test
    void getItem() {
    }

    @Test
    void searchItem() {
    }

    @Test
    void saveItem() {
    }

    @Test
    void updateItem() {
    }

    @Test
    void saveComment() {
    }
}