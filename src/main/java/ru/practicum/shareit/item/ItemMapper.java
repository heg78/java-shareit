package ru.practicum.shareit.item;

import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class ItemMapper {
    public static Item toItem(ItemDto itemDto) {
        Item item = new Item();
        item.setId(itemDto.getId());
        item.setName(itemDto.getName());
        item.setDescription(itemDto.getDescription());
        item.setAvailable(itemDto.getAvailable());
        return item;
    }

    public static ItemDto toItemDto(Item item, List<Booking> bookings) {
        LocalDateTime now = LocalDateTime.now();
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner().getId(),
                bookings.stream().filter(b -> b.getStart().isBefore(LocalDateTime.now())).map(BookingMapper::toBookingDto)
                        .max(Comparator.comparing(BookingDto::getStart)).orElse(null),
                bookings.stream().filter(b -> b.getStart().isAfter(LocalDateTime.now())).map(BookingMapper::toBookingDto)
                        .min(Comparator.comparing(BookingDto::getStart)).orElse(null),
                List.of()
        );
    }

    public static ItemDto toItemDto(Item item, List<Booking> bookings, List<Comment> comments) {
        LocalDateTime now = LocalDateTime.now();
        ItemDto itemDto = toItemDto(item, bookings);
        itemDto.setComments(comments.stream().map(CommentMapper::toCommentDto).collect(Collectors.toList()));
        return itemDto;
    }
}
