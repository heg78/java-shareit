package ru.practicum.shareit.booking;

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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
@ExtendWith(MockitoExtension.class)
class BookingControllerTest {
    @Autowired
    private ObjectMapper mapper;
    @Autowired
    private MockMvc mvc;
    @MockBean
    private BookingService bookingService;

    private static final String XUSER = "X-Sharer-User-Id";

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


    @SneakyThrows
    @Test
    void addTest() {
        when(bookingService.add(anyLong(), any())).thenReturn(BookingMapper.toBookingFullDto(booking));

        String webResult = mvc.perform(post("/bookings")
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(bookingDto))
                        .header(XUSER, user.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        assertEquals(mapper.writeValueAsString(BookingMapper.toBookingFullDto(booking)), webResult);
    }

    @SneakyThrows
    @Test
    void updateTest() {
        when(bookingService.update(anyLong(), anyLong(), anyBoolean())).thenReturn(BookingMapper.toBookingFullDto(booking));

        String resultMvc = mvc.perform(MockMvcRequestBuilders.patch("/bookings/{bookingId}?approved=true", 1L)
                        .contentType("application/json")
                        .content(mapper.writeValueAsString(itemDto))
                        .header(XUSER, user.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(bookingService).update(anyLong(), anyLong(), anyBoolean());
        assertEquals(mapper.writeValueAsString(BookingMapper.toBookingFullDto(booking)), resultMvc);
    }

    @SneakyThrows
    @Test
    void getBookingByIdTest() {
        when(bookingService.get(anyLong(), anyLong())).thenReturn(BookingMapper.toBookingFullDto(booking));

        String resultMvc = mvc.perform(MockMvcRequestBuilders.get("/bookings/{bookingId}", 1L)
                        .header(XUSER, user.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(bookingService).get(anyLong(), anyLong());
        assertEquals(mapper.writeValueAsString(BookingMapper.toBookingFullDto(booking)), resultMvc);
    }

    @SneakyThrows
    @Test
    void getUserBookingsTest() {
        when(bookingService.getUserBookings(anyLong(), anyString(), anyInt(), anyInt())).thenReturn(List.of(BookingMapper.toBookingFullDto(booking)));

        String resultMvc = mvc.perform(MockMvcRequestBuilders.get("/bookings?state=ALL&from=0&size=10")
                        .header(XUSER, user.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(bookingService).getUserBookings(anyLong(), anyString(), anyInt(), anyInt());
        assertEquals(mapper.writeValueAsString(List.of(BookingMapper.toBookingFullDto(booking))), resultMvc);
    }

    @SneakyThrows
    @Test
    void getOwnerBookingsTest() {
        when(bookingService.getOwnerBookings(anyLong(), anyString(), anyInt(), anyInt())).thenReturn(List.of(BookingMapper.toBookingFullDto(booking)));

        String resultMvc = mvc.perform(MockMvcRequestBuilders.get("/bookings/owner?state=ALL&from=0&size=10")
                        .header(XUSER, user.getId()))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        verify(bookingService).getOwnerBookings(anyLong(), anyString(), anyInt(), anyInt());
        assertEquals(mapper.writeValueAsString(List.of(BookingMapper.toBookingFullDto(booking))), resultMvc);
    }
}