package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.common.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemDao itemDao;
    private final UserService userService;

    public List<Item> getAllItems(Long userId) {
        return itemDao.getAllItems(userId);
    }

    public Item getItem(Long itemId) {
        return itemDao.getItem(itemId);
    }

    public List<Item> searchItem(String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        return itemDao.searchItem(text);
    }

    public Item saveItem(Long userId, Item item) {
        if (userService.getUser(userId) == null) {
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
        item.setOwner(userId);
        itemDao.saveItem(item);
        return item;
    }

    public Item updateItem(Long userId, Long itemId, Item newItem) {
        if (userId == null) {
            throw new ValidationException("");
        }
        Item oldItem = getItem(itemId);
        if (!userId.equals(oldItem.getOwner())) {
            throw new NotFoundException("");
        }
        newItem.setId(itemId);
        newItem.setId(newItem.getId() != null ? newItem.getId() : oldItem.getId());
        newItem.setName(newItem.getName() != null ? newItem.getName() : oldItem.getName());
        newItem.setDescription(newItem.getDescription() != null ? newItem.getDescription() : oldItem.getDescription());
        newItem.setAvailable(newItem.getAvailable() != null ? newItem.getAvailable() : oldItem.getAvailable());
        newItem.setOwner(userId);
        itemDao.updateItem(newItem);
        return newItem;
    }

}
