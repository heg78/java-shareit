package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping
    public List<Item> getAllItems(@RequestHeader("X-Sharer-User-Id") long userId) {
        return itemService.getAllItems(userId);
    }

    @GetMapping("/{itemId}")
    public Item getItem(@PathVariable long itemId) {
        return itemService.getItem(itemId);
    }

    @PostMapping
    public Item saveNewItem(@RequestHeader("X-Sharer-User-Id") long userId, @RequestBody Item item) {
        return itemService.saveItem(userId, item);
    }

    @PatchMapping("/{itemId}")
    public Item updateItem(@RequestHeader("X-Sharer-User-Id") long userId, @PathVariable Long itemId, @RequestBody ItemDto item) {
        return itemService.updateItem(userId, itemId, ItemMapper.toItem(item));
    }

    @GetMapping("/search")
    public List<Item> searchItem(@RequestParam String text) {
        return itemService.searchItem(text);
    }
}
