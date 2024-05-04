package ru.practicum.shareit.booking;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.dto.BookingDto;
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

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class BookingRepositoryTest {
    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    private TestEntityManager testEm;

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
        testEm.merge(user);
        testEm.merge(item);
        testEm.flush();
        bookingRepository.save(booking);
    }

    @Test
    void findByBooker_IdOrderByStartDescTest() {
        List<Booking> bookingList = bookingRepository.findByBooker_IdOrderByStartDesc(1, Pageable.unpaged());
        assertEquals(bookingList.size(), 1);
        assertEquals(bookingList.get(0).getId(), 1L);
    }

    @Test
    void getOwnerBookingsTest() {
        List<Booking> bookingList = bookingRepository.getOwnerBookings(1, Pageable.unpaged());
        assertEquals(bookingList.size(), 1);
        assertEquals(bookingList.get(0).getId(), 1L);
    }

    @Test
    void findAllItemsAndOwnerTest() {
        List<Booking> bookingList = bookingRepository.findAllItemsAndOwner(1L, 1L);
        assertEquals(bookingList.size(), 1);
        assertEquals(bookingList.get(0).getId(), 1L);
    }

    @Test
    void existsByBookerAndItem() {
        Boolean bookingList = bookingRepository.existsByBookerAndItem(1L, 1L, LocalDateTime.now());
        assertEquals(bookingList, true);
    }
}