package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingFullDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.common.exception.ValidationException;
import ru.practicum.shareit.item.ItemDao;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserDao;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@ExtendWith(MockitoExtension.class)
class BookingServiceImpTest {
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private UserDao userRepository;
    @Mock
    private ItemDao itemRepository;
    @InjectMocks
    private BookingServiceImp bookingService;

    private UserDto userDto;
    private User user;
    private ItemRequestDto itemRequestDto;
    private ItemRequest itemRequest;
    private ItemDto itemDto;
    private Item item;
    private CommentDto commentDto;
    private BookingDto bookingDto;
    private Booking booking;

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
        bookingDto = new BookingDto(1L, LocalDateTime.MIN, LocalDateTime.MAX, 1L, 1L, Status.WAITING);
        booking = BookingMapper.toBooking(bookingDto, user, item);
    }

    @Test
    void addNotAvailableTest() {
        Item testItem = item;
        testItem.setAvailable(false);
        when(itemRepository.getItem(anyLong())).thenReturn(testItem);
        Assertions.assertThrows(ValidationException.class, () -> bookingService.add(1L, bookingDto));
    }

    @Test
    void addOwnersBookingTest() {
        Item testItem = item;
        testItem.setAvailable(true);
        when(itemRepository.getItem(anyLong())).thenReturn(testItem);
        Assertions.assertThrows(NotFoundException.class, () -> bookingService.add(1L, bookingDto));
    }

    @Test
    void addNotCorrectPeriodTest() {
        Item testItem = item;
        testItem.setAvailable(true);
        when(itemRepository.getItem(anyLong())).thenReturn(testItem);
        Assertions.assertThrows(ValidationException.class, () -> bookingService.add(2L, bookingDto));
    }

    @Test
    void addTest() {
        Item testItem = item;
        testItem.setAvailable(true);
        bookingDto.setStart(LocalDateTime.now().plusDays(10));
        when(itemRepository.getItem(anyLong())).thenReturn(testItem);
        when(userRepository.getUser(anyLong())).thenReturn(user);
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        BookingFullDto tesBookingDto = bookingService.add(2L, bookingDto);
        assertEquals(tesBookingDto.getId(), bookingDto.getId());
    }

    @Test
    void updateNotFoundBookingTest() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () -> bookingService.update(1L, 1L, true));
    }

    @Test
    void updateOwnerSameBookerTest() {
        booking.setStatus(Status.APPROVED);
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        Assertions.assertThrows(ValidationException.class, () -> bookingService.update(1L, 1L, true));
    }

    @Test
    void updateOnlyOwnerTest() {
        booking.setStatus(Status.APPROVED);
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        Assertions.assertThrows(RuntimeException.class, () -> bookingService.update(1L, 2L, true));
    }

    @Test
    void updateTest() {
        booking.setStatus(Status.WAITING);
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        booking.getBooker().setId(2L);
        BookingFullDto testBookingDto = bookingService.update(1L, 2L, true);
        assertEquals(testBookingDto.getStatus(), Status.APPROVED);
    }

    @Test
    void updateBookerTest() {
        User newBooker = new User();
        newBooker.setId(3L);
        booking.setStatus(Status.WAITING);
        booking.setBooker(newBooker);
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        Assertions.assertThrows(NotFoundException.class, () -> bookingService.update(1L, 3L, true));
    }

    @Test
    void getNotFoundBookingTest() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () -> bookingService.get(1L, 1L));
    }

    @Test
    void getNotFoundUserTest() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(userRepository.exists(anyLong())).thenReturn(false);
        Assertions.assertThrows(NotFoundException.class, () -> bookingService.get(1L, 1L));
    }

    @Test
    void getNotOwnerNotBookerTest() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(userRepository.exists(anyLong())).thenReturn(false);
        Assertions.assertThrows(NotFoundException.class, () -> bookingService.get(1L, 2L));
    }

    @Test
    void getTest() {
        when(bookingRepository.findById(anyLong())).thenReturn(Optional.of(booking));
        when(userRepository.exists(anyLong())).thenReturn(true);
        BookingFullDto testBookingDto = bookingService.get(1L, 1L);
        assertEquals(testBookingDto.getId(), bookingDto.getId());
        assertEquals(testBookingDto.getItem().getId(), bookingDto.getItemId());
    }

    @Test
    void getUserBookingsNotFoundUserTest() {
        when(userRepository.exists(anyLong())).thenReturn(false);
        Assertions.assertThrows(NotFoundException.class, () -> bookingService.getUserBookings(1L, "ALL", 0, 1));
    }

    @Test
    void getUserBookingsLessZeroParametersTest() {
        when(userRepository.exists(anyLong())).thenReturn(true);
        Assertions.assertThrows(ValidationException.class, () -> bookingService.getUserBookings(1L, "ALL", -1, -1));
    }

    @Test
    void getUserBookingsAllTest() {
        when(userRepository.exists(anyLong())).thenReturn(true);
        when(bookingRepository.findByBooker_IdOrderByStartDesc(anyLong(), any(Pageable.class))).thenReturn(List.of(booking));
        List<BookingFullDto> bookingList = bookingService.getUserBookings(1L, "ALL", 1, 1);
        assertEquals(bookingList.size(), 1);
    }

    @Test
    void getUserBookingsCurrentTest() {
        when(userRepository.exists(anyLong())).thenReturn(true);
        when(bookingRepository.findByBooker_IdOrderByStartDesc(anyLong(), any(Pageable.class))).thenReturn(List.of(booking));
        List<BookingFullDto> bookingList = bookingService.getUserBookings(1L, "CURRENT", 1, 1);
        assertEquals(bookingList.size(), 1);
    }

    @Test
    void getUserBookingsPastTest() {
        when(userRepository.exists(anyLong())).thenReturn(true);
        when(bookingRepository.findByBooker_IdOrderByStartDesc(anyLong(), any(Pageable.class))).thenReturn(List.of(booking));
        List<BookingFullDto> bookingList = bookingService.getUserBookings(1L, "PAST", 1, 1);
        assertEquals(bookingList.size(), 0);
    }

    @Test
    void getOwnerBookingsNotFoundUserTest() {
        when(userRepository.exists(anyLong())).thenReturn(false);
        Assertions.assertThrows(NotFoundException.class, () -> bookingService.getOwnerBookings(1L, "ALL", 0, 1));
    }

    @Test
    void getOwnerBookingsLessZeroParametersTest() {
        when(userRepository.exists(anyLong())).thenReturn(true);
        Assertions.assertThrows(ValidationException.class, () -> bookingService.getOwnerBookings(1L, "ALL", 0, -1));
    }

    @Test
    void getOwnerBookingsAllTest() {
        when(userRepository.exists(anyLong())).thenReturn(true);
        when(bookingRepository.getOwnerBookings(anyLong(), any(Pageable.class))).thenReturn(List.of(booking));
        List<BookingFullDto> bookingList = bookingService.getOwnerBookings(1L, "ALL", 1, 1);
        assertEquals(bookingList.size(), 1);
    }

    @Test
    void getOwnerBookingsFutureTest() {
        when(userRepository.exists(anyLong())).thenReturn(true);
        when(bookingRepository.getOwnerBookings(anyLong(), any(Pageable.class))).thenReturn(List.of(booking));
        List<BookingFullDto> bookingList = bookingService.getOwnerBookings(1L, "FUTURE", 1, 1);
        assertEquals(bookingList.size(), 0);
    }

    @Test
    void getOwnerBookingsWaitingTest() {
        when(userRepository.exists(anyLong())).thenReturn(true);
        when(bookingRepository.getOwnerBookings(anyLong(), any(Pageable.class))).thenReturn(List.of(booking));
        List<BookingFullDto> bookingList = bookingService.getOwnerBookings(1L, "WAITING", 1, 1);
        assertEquals(bookingList.size(), 1);
    }

    @Test
    void getOwnerBookingsRejectedTest() {
        when(userRepository.exists(anyLong())).thenReturn(true);
        when(bookingRepository.getOwnerBookings(anyLong(), any(Pageable.class))).thenReturn(List.of(booking));
        Assertions.assertThrows(ValidationException.class, () -> bookingService.getOwnerBookings(1L, "UNSUPPORTED_STATUS", 1, 1));
    }
}