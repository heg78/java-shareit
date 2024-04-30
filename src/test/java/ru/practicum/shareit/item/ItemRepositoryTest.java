package ru.practicum.shareit.item;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ItemRepositoryTest {
    @Autowired
    ItemRepository itemRepository;

    @Autowired
    private TestEntityManager testEm;

    private UserDto userDto;
    private User user;
    private ItemDto itemDto;
    private Item item;

    @BeforeEach
    void setUp() {
        userDto = new UserDto(1L, "name", "email@email.ru");
        user = UserMapper.toUser(userDto);
        itemDto = new ItemDto(1L, "ItemName", "ItemDescr", true, 1L, null, null, List.of(), null);
        item = ItemMapper.toItem(itemDto);
        item.setOwner(user);
        testEm.merge(user);
        testEm.flush();
        itemRepository.save(item);
    }


    @Test
    void findByOwnerTest() {
        List<Item> testItemList = itemRepository.findByOwner(user);
        assertEquals(testItemList.size(), 1);
        assertEquals(testItemList.get(0).getDescription(), "ItemDescr");
    }

    @Test
    void searchAvialableByTextTest() {
        List<Item> testItemList = itemRepository.searchAvialableByText("ItemDescr");
        assertEquals(testItemList.size(), 1);
        assertEquals(testItemList.get(0).getDescription(), "ItemDescr");
    }
}