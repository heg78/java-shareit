package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingFullDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.common.exception.ValidationException;
import ru.practicum.shareit.item.ItemDao;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.ItemRepository;
import ru.practicum.shareit.user.UserDao;
import ru.practicum.shareit.user.UserRepository;
import ru.practicum.shareit.user.model.User;

import java.sql.Timestamp;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class BookingServiceImp implements BookingService {
    private final BookingRepository bookingRepository;
    private final UserDao userRepository;
    private final ItemDao itemRepository;

    @Override
    public BookingFullDto add(long userId, BookingDto bookingDto) {
        Item item = itemRepository.getItem(bookingDto.getItemId());

        if (!item.getAvailable()) {
            throw new ValidationException("Недоступно для бронирования");
        }
        if (userId == item.getOwner().getId()) {
            throw new NotFoundException("Владельцу недоступно");
        }

        User user = userRepository.getUser(userId);
        Booking booking = BookingMapper.toBooking(bookingDto, user, item);

        if (booking.getEnd().isBefore(booking.getStart()) || booking.getStart().equals(booking.getEnd())
                || booking.getStart().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Некорректный период");
        }

        booking.setStatus(Status.WAITING);
        return BookingMapper.toBookingFullDto(bookingRepository.save(booking));
    }

    @Override
    public BookingFullDto update(long bookingId, long userId, Boolean approved) {
        Booking oldBooking = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException("Не найдено бронирование"));
        Status oldStatus = oldBooking.getStatus();
        long ownerId = oldBooking.getItem().getOwner().getId();
        long bookerId = oldBooking.getBooker().getId();

        if (userId == ownerId) {
            if (Status.APPROVED.equals(oldStatus)) {
                throw new ValidationException("Нельзя обновить статус Approved");
            }
            oldBooking.setStatus(approved ? Status.APPROVED : Status.REJECTED);
        } else if (userId == bookerId && approved) {
            throw new NotFoundException("Booker не может обновить статус");
        } else {
            throw new RuntimeException("Обновить может только владелец");
        }
        return BookingMapper.toBookingFullDto(bookingRepository.save(oldBooking));
    }

    @Override
    public BookingDto get(long bookingId, User user) {
        return null;
    }
}

