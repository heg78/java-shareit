package ru.practicum.shareit.booking;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.sql.Timestamp;

public class Booking {
    private long id;
    private Timestamp start;
    private Timestamp end;
    private Item item;
    private User booker;
    private Status status;
}