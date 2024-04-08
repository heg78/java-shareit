package ru.practicum.shareit.booking;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingFullDto;
import ru.practicum.shareit.user.model.User;

public interface BookingService {
    BookingFullDto add(long userId, BookingDto bookingDto);

    BookingFullDto update(long bookingId, long userId, Boolean approved);

    BookingDto get(long bookingId, User user);
}
