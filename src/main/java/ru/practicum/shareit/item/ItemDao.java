package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemDao {
    List<Item> getAllItems(User user);

    Item getItem(Long itemId);

    Item saveItem(Item item);

    Comment saveComment(Comment comment);

    Item updateItem(Item item);

    List<Item> searchItem(String text);
}
