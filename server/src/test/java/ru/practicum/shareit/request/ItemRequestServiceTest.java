package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.common.exception.ValidationException;
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
    void getTest() {
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.of(itemRequest));
        when(userRepository.getUser(anyLong())).thenReturn(user);
        ItemRequestDto testItemRequestDto = itemRequestService.get(1L, 1L);
        assertEquals(itemRequestDto.getRequester(), testItemRequestDto.getRequester());
        assertEquals(itemRequestDto.getDescription(), testItemRequestDto.getDescription());
        assertEquals(itemRequestDto.getId(), testItemRequestDto.getId());
    }

    @Test
    void getEmptyTest() {
        when(itemRequestRepository.findById(anyLong())).thenReturn(Optional.empty());
        when(userRepository.getUser(anyLong())).thenReturn(user);
        Assertions.assertThrows(NotFoundException.class, () -> itemRequestService.get(1L, 1L));
    }

    @Test
    void getUserRequestsTest() {
        when(itemRequestRepository.findAllByRequesterId(anyLong())).thenReturn(List.of(itemRequest));
        when(userRepository.getUser(anyLong())).thenReturn(user);
        List<ItemRequestDto> testItemRequestDtoList = itemRequestService.getUserRequests(1L);
        assertEquals(testItemRequestDtoList, List.of(itemRequestDto));

    }

    @Test
    void getNotUserRequestsTest() {
        when(itemRequestRepository.findAllByRequesterIdNotOrderByCreatedDesc(anyLong(), any(PageRequest.class))).thenReturn(List.of(itemRequest));
        when(userRepository.getUser(anyLong())).thenReturn(user);
        List<ItemRequestDto> testItemRequestDtoList = itemRequestService.getNotUserRequests(1L, 1, 1);
        assertEquals(testItemRequestDtoList, List.of(itemRequestDto));
    }

    @Test
    void getNotUserRequestsBadPagingTest() {
        when(userRepository.getUser(anyLong())).thenReturn(user);
        Assertions.assertThrows(ValidationException.class, () -> itemRequestService.getNotUserRequests(1L, -1, -1));
    }
}