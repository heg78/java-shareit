package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;

@Controller
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
@Validated
public class BookingController {
    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> add(@RequestHeader(value = "X-Sharer-User-Id") long userId,
                                      @Validated @RequestBody BookingDto bookingDto) {
        return bookingClient.add(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> update(@PathVariable long bookingId,
                                         @RequestHeader(value = "X-Sharer-User-Id") long userId,
                                         @RequestParam(required = false) Boolean approved) {
        return bookingClient.update(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(@RequestHeader(value = "X-Sharer-User-Id") long userId,
                                                 @PathVariable long bookingId) {
        return bookingClient.get(bookingId, userId);
    }


    @GetMapping
    public ResponseEntity<Object> getUserBookings(@RequestHeader(value = "X-Sharer-User-Id") long userId,
                                                  @RequestParam(defaultValue = "ALL") String state,
                                                  @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                  @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return bookingClient.getUserBookings(userId, state, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getOwnerBookings(@RequestHeader(value = "X-Sharer-User-Id") long userId,
                                                   @RequestParam(defaultValue = "ALL") String state,
                                                   @RequestParam(name = "from", defaultValue = "0") Integer from,
                                                   @RequestParam(value = "size", defaultValue = "10") Integer size) {
        return bookingClient.getOwnerBookings(userId, state, from, size);
    }
}
