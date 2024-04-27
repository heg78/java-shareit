package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserDao;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import javax.transaction.Transactional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ExtendWith(MockitoExtension.class)
class ItemRequestServiceTest {
    @Mock
    private ItemRequestRepository itemRequestRepository;
    @Mock
    private UserDao userRepository;
    @InjectMocks
    ItemRequestServiceIml itemRequestService;

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

    @AfterEach
    void tearDown() {
    }

    @Test
    void addTest() {
        when(itemRequestRepository.save(any(ItemRequest.class))).thenReturn(itemRequest);
        when(userRepository.getUser(anyLong())).thenReturn(user);
        ItemRequestDto savedItemRequestDto = itemRequestService.add(1L, itemRequestDto);
        assertEquals(itemRequestDto.getRequester(), savedItemRequestDto.getRequester());
        assertEquals(itemRequestDto.getDescription(), savedItemRequestDto.getDescription());
        assertEquals(itemRequestDto.getId(), savedItemRequestDto.getId());
    }

    @Test
    void updateTest() {
    }

    @Test
    void getTest() {
    }

    @Test
    void getUserRequestsTest() {
    }

    @Test
    void getNotUserRequestsTest() {
    }
}