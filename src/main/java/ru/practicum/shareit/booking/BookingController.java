package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingFullDto;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingFullDto add(@RequestHeader(value = "X-Sharer-User-Id") long userId,
                              @Validated @RequestBody BookingDto bookingDto) {
        return bookingService.add(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public BookingFullDto update(@PathVariable long bookingId,
                                        @RequestHeader(value = "X-Sharer-User-Id") long userId,
                                        @RequestParam(required = false) Boolean approved) {
        return bookingService.update(bookingId, userId, approved);
    }

//    @GetMapping("/{bookingId}")
//    public BookingFullDto getBookingById(@RequestHeader(value = "X-Sharer-User-Id") long userId,
//                                         @PathVariable long bookingId) {
//        log.info("GET-запрос на получение данных о бронировании: userId = {}, bookingId = {}", userId, bookingId);
//        return bookingService.getBookingById(bookingId, userId);
//    }
}
