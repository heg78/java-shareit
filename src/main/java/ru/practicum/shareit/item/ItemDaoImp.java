package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ItemDaoImp implements ItemDao {
    private final ItemRepository itemRepository;

    @Override
    public List<Item> getAllItems(User user) {
        return itemRepository.findByOwner(user);
    }

    @Override
    public Item getItem(Long itemId) {
        return itemRepository.findById(itemId).orElseThrow(() -> new NotFoundException("Item не найден"));
    }

    @Override
    public Item saveItem(Item item) {
        return itemRepository.save(item);
    }

    @Override
    public Item updateItem(Item newItem) {
        return itemRepository.save(newItem);
    }

    @Override
    public List<Item> searchItem(String text) {
        return itemRepository.searchAvialableByText(text);
    }
}
