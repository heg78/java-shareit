package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.common.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ItemDaoImp implements ItemDao {
    private final Map<Long, Item> items = new HashMap<>();
    private final UserService userService;
    //private final Set<String> emails = new HashSet<>();
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
    public Item saveItem(Long userID, Item item) {
        if (userService.getUser(userID) == null) {
            throw new NotFoundException("Не найден пользователь");
        }
        if (item.getAvailable() == null ||
                item.getName() == null ||
                item.getDescription() == null ||
                item.getName().isBlank() ||
                item.getDescription().isBlank()
        ) {
            throw new ValidationException("Некорректно заполнен Item");
        }
        itemId++;
        item.setOwner(userID);
        item.setId(itemId);
        items.put(itemId, item);
        return item;
    }

    @Override
    public Item updateItem(Long userId, Long itemId, Item newItem) {
        if (userId == null) {
            throw new ValidationException("");
        }
        if (!userId.equals(items.get(itemId).getOwner())) {
            throw new NotFoundException("");
        }
        newItem.setId(itemId);
        Item oldItem = items.get(itemId);
        newItem.setId(newItem.getId() != null ? newItem.getId() : oldItem.getId());
        newItem.setName(newItem.getName() != null ? newItem.getName() : oldItem.getName());
        newItem.setDescription(newItem.getDescription() != null ? newItem.getDescription() : oldItem.getDescription());
        newItem.setAvailable(newItem.getAvailable() != null ? newItem.getAvailable() : oldItem.getAvailable());
        newItem.setOwner(userId);
        items.put(itemId, newItem);
        return newItem;
    }

    @Override
    public List<Item> searchItem(String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        return items.values().stream().filter(f -> (f.getAvailable()
                        && (f.getName().toLowerCase().contains(text.toLowerCase())
                        || f.getDescription().toLowerCase().contains(text.toLowerCase()))))
                .collect(Collectors.toList());
    }
}
