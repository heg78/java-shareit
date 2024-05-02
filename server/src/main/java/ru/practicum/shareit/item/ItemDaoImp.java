package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ItemDaoImp implements ItemDao {
    private final ItemRepository itemRepository;
    private final CommentRepository commentRepository;

    @Override
    public List<Item> getAllItems(User user) {
        return itemRepository.findByOwnerOrderById(user);
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
    public Comment saveComment(Comment comment) {
        return commentRepository.save(comment);
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
