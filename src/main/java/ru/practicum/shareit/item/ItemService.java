package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.common.exception.ValidationException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserDao;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemDao itemDao;
    private final UserDao userDao;

    public List<ItemDto> getAllItems(Long userId) {
        return itemDao.getAllItems(userDao.getUser(userId)).stream().map(ItemMapper::toItemDto).collect(Collectors.toList());
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

    public ItemDto saveItem(Long userId, ItemDto itemDto) {
        User user = userDao.getUser(userId);

        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(user);
        return  ItemMapper.toItemDto(itemDao.saveItem(item));
    }

    public ItemDto updateItem(Long userId, Long itemId, ItemDto newItem) {
        if (userId == null) {
            throw new ValidationException("");
        }
        Item oldItem = getItem(itemId);
        if (!userId.equals(oldItem.getOwner().getId())) {
            throw new NotFoundException("");
        }
        oldItem.setId(itemId);
        oldItem.setName(newItem.getName() != null ? newItem.getName() : oldItem.getName());
        oldItem.setDescription(newItem.getDescription() != null ? newItem.getDescription() : oldItem.getDescription());
        oldItem.setAvailable(newItem.getAvailable() != null ? newItem.getAvailable() : oldItem.getAvailable());
        oldItem.setOwner(userDao.getUser(userId));
        return ItemMapper.toItemDto(itemDao.updateItem(oldItem));
    }

}
