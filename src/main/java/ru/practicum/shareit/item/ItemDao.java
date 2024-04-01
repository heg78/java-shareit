package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemDao {
    List<Item> getAllItems(Long userId);

    Item getItem(Long itemId);

    Item saveItem(Item item);

    Item updateItem(Item item);

    List<Item> searchItem(String text);
}
