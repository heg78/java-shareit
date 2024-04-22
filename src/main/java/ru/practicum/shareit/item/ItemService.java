package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.common.exception.ValidationException;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserDao;
import ru.practicum.shareit.user.model.User;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemService {
    private final ItemDao itemRepository;
    private final UserDao userRepository;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    public List<ItemDto> getAllItems(Long userId) {
        return itemRepository.getAllItems(userRepository.getUser(userId)).stream()
                .map(i -> ItemMapper.toItemDto(i, bookingRepository.findAllItemsAndOwner(i.getId(), userId)))
                .collect(Collectors.toList());
    }

    public ItemDto getItem(Long userId, Long itemId) {
        Item item = itemRepository.getItem(itemId);
        List<Comment> comments = commentRepository.findByItem_Id(itemId);
        if (userId.equals(item.getOwner().getId())) {
            return ItemMapper.toItemDto(item, bookingRepository.findAllItemsAndOwner(itemId, userId), comments);
        }
        return ItemMapper.toItemDto(item, List.of(), comments);
    }

    public List<Item> searchItem(String text) {
        if (text.isBlank()) {
            return new ArrayList<>();
        }
        return itemRepository.searchItem(text);
    }

    @Transactional
    public ItemDto saveItem(Long userId, ItemDto itemDto) {
        User user = userRepository.getUser(userId);

        Item item = ItemMapper.toItem(itemDto);
        item.setOwner(user);
        return ItemMapper.toItemDto(itemRepository.saveItem(item), List.of());
    }

    @Transactional
    public ItemDto updateItem(Long userId, Long itemId, ItemDto newItem) {
        if (userId == null) {
            throw new ValidationException("");
        }
        Item oldItem = itemRepository.getItem(itemId);
        if (!userId.equals(oldItem.getOwner().getId())) {
            throw new NotFoundException("");
        }
        oldItem.setId(itemId);
        oldItem.setName(newItem.getName() != null ? newItem.getName() : oldItem.getName());
        oldItem.setDescription(newItem.getDescription() != null ? newItem.getDescription() : oldItem.getDescription());
        oldItem.setAvailable(newItem.getAvailable() != null ? newItem.getAvailable() : oldItem.getAvailable());
        oldItem.setOwner(userRepository.getUser(userId));
        return ItemMapper.toItemDto(itemRepository.updateItem(oldItem), List.of());
    }

    @Transactional
    public CommentDto saveComment(Long userId, Long itemId, CommentDto commentDto) {
        if (commentDto.getText().isEmpty()) {
            throw new ValidationException("Комментарий не должен быть пустым");
        }
        User user = userRepository.getUser(userId);
        Item item = itemRepository.getItem(itemId);
        LocalDateTime commentCreated = LocalDateTime.now();
        if (bookingRepository.existsByBookerAndItem(itemId, userId, commentCreated)) {
            Comment comment = CommentMapper.toComment(commentDto);
            comment.setAuthor(user);
            comment.setItem(item);
            comment.setCreated(LocalDateTime.now());
            return CommentMapper.toCommentDto(commentRepository.save(comment));
        } else {
            throw new ValidationException("Пользователь не может создать комментарий");
        }
    }
}
