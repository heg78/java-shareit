package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemDao itemDao;

    public List<Item> getAllItems(Long userId) {
        return itemDao.getAllItems(userId);
    }

    public Item getItem(Long itemId) {
        return itemDao.getItem(itemId);
    }

    public Item saveItem(Long userId, Item item) {
        return itemDao.saveItem(userId, item);
    }

    public Item updateItem(Long userId, Long itemId, Item item) {
        return itemDao.updateItem(userId, itemId, item);
    }

    public List<Item> searchItem(String text) {
        return itemDao.searchItem(text);
    }
}
