package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingFullDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.model.User;

public class BookingMapper {
    public static Booking toBooking(BookingDto bookingDto, User user, Item item) {
        Booking booking = new Booking();
        booking.setId(bookingDto.getId());
        booking.setStart(bookingDto.getStart());
        booking.setEnd(bookingDto.getEnd());
        booking.setItem(item);
        booking.setBooker(user);
        booking.setStatus(bookingDto.getStatus());
        return booking;
    }

    public static BookingFullDto toBookingFullDto(Booking booking) {
        BookingFullDto bookingFullDto = new BookingFullDto();
        bookingFullDto.setId(booking.getId());
        bookingFullDto.setStart(booking.getStart());
        bookingFullDto.setEnd(booking.getEnd());
        bookingFullDto.setItem(ItemMapper.toItemDto(booking.getItem()));
        bookingFullDto.setBooker(UserMapper.toUserDto(booking.getBooker()));
        bookingFullDto.setStatus(booking.getStatus());
        return bookingFullDto;
    }

    public static BookingDto toBookingDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getStart(),
                booking.getEnd(),
                booking.getItem().getId(),
                booking.getBooker().getId(),
                booking.getStatus());
    }
}
