package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingFullDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.request.ItemRequestMapper;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BookingMapperTest {
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
    void toBooking() {
        Booking testBooking = BookingMapper.toBooking(bookingDto, user, item);
        assertEquals(testBooking.getId(), booking.getId());
        assertEquals(testBooking.getItem(), item);
        assertEquals(testBooking.getBooker(), booking.getBooker());
    }

    @Test
    void toBookingFullDto() {
        BookingFullDto testBookingFullDto = BookingMapper.toBookingFullDto(booking);
        assertEquals(testBookingFullDto.getId(), booking.getId());
        assertEquals(testBookingFullDto.getItem(), itemDto);
        assertEquals(testBookingFullDto.getBooker().getId(), booking.getBooker().getId());
    }

    @Test
    void toBookingDto() {
        BookingDto testBookingDto = BookingMapper.toBookingDto(booking);
        assertEquals(testBookingDto.getId(), booking.getId());
        assertEquals(testBookingDto.getItemId(), booking.getItem().getId());
        assertEquals(testBookingDto.getBookerId(), booking.getBooker().getId());
    }
}