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
import ru.practicum.shareit.user.UserDao;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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
    public BookingFullDto get(long bookingId, long userId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new NotFoundException("Бронирование не найдено"));
        if (!userRepository.exists(userId)) {
            throw new NotFoundException("пользователь не найден");
        }
        if (userId != booking.getBooker().getId() && userId != booking.getItem().getOwner().getId()) {
            throw new NotFoundException("Доступно только owner или booker!");
        }
        return BookingMapper.toBookingFullDto(booking);
    }

    @Override
    public List<BookingFullDto> getUserBookings(long userId, String state) {
        if (!userRepository.exists(userId)) {
            throw new NotFoundException("пользователь не найден");
        }
        List<Booking> bookings = bookingRepository.findByBooker_Id(userId);
        return filterState(bookings, State.valueOf(state)).stream()
                .map(BookingMapper::toBookingFullDto)
                .sorted(Comparator.comparing(BookingFullDto::getStart).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public List<BookingFullDto> getOwnerBookings(long userId, String state) {
        if (!userRepository.exists(userId)) {
            throw new NotFoundException("пользователь не найден");
        }
        List<Booking> bookings = bookingRepository.getOwnerBookings(userId);
        return filterState(bookings, State.valueOf(state)).stream()
                .map(BookingMapper::toBookingFullDto)
                .sorted(Comparator.comparing(BookingFullDto::getStart).reversed())
                .collect(Collectors.toList());
    }

    private List<Booking> filterState(List<Booking> bookings, State state) {
        switch (state) {
            case ALL:
                return bookings;
            case CURRENT:
                return bookings.stream().filter(b -> b.getStart().isBefore(LocalDateTime.now()) && b.getEnd().isAfter(LocalDateTime.now()))
                        .collect(Collectors.toList());
            case PAST:
                return bookings.stream().filter(b -> b.getEnd().isBefore(LocalDateTime.now()))
                        .collect(Collectors.toList());
            case FUTURE:
                return bookings.stream().filter(b -> b.getStart().isAfter(LocalDateTime.now()))
                        .collect(Collectors.toList());
            case WAITING:
                return bookings.stream().filter(b -> b.getStatus().equals(Status.WAITING))
                        .collect(Collectors.toList());
            case REJECTED:
                return bookings.stream().filter(b -> b.getStatus().equals(Status.REJECTED))
                        .collect(Collectors.toList());
            case UNSUPPORTED_STATUS:
                throw new ValidationException("Unknown state: UNSUPPORTED_STATUS");
            default:
                throw new ValidationException("Unknown state");
        }
    }
}

