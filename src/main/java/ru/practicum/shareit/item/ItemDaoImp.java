package ru.practicum.shareit.item;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Item;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ItemDaoImp implements ItemDao {
    private final Map<Long, Item> items = new HashMap<>();
    private Long itemId = 0L;

    @Override
    public List<Item> getAllItems(Long userId) {
        return items.values().stream().filter(f -> (f.getOwner().equals(userId))).collect(Collectors.toList());
    }

    @Override
    public Item getItem(Long itemId) {
        return items.get(itemId);
    }

    @Override
    public Item saveItem(Item item) {
        itemId++;
        item.setId(itemId);
        items.put(itemId, item);
        return item;
    }

    @Override
    public Item updateItem(Item newItem) {
        items.put(newItem.getId(), newItem);
        return newItem;
    }

    @Override
    public List<Item> searchItem(String text) {
        return items.values().stream().filter(f -> (f.getAvailable()
                        && (f.getName().toLowerCase().contains(text.toLowerCase())
                        || f.getDescription().toLowerCase().contains(text.toLowerCase()))))
                .collect(Collectors.toList());
    }
}
