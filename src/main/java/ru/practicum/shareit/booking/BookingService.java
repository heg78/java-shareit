package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingFullDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface BookingService {
    BookingFullDto add(long userId, BookingDto bookingDto);

    BookingFullDto update(long bookingId, long userId, Boolean approved);

    BookingFullDto get(long bookingId, long userId);

    List<BookingFullDto> getUserBookings(long userId, String state);

    List<BookingFullDto> getOwnerBookings(long userId, String state);
}
