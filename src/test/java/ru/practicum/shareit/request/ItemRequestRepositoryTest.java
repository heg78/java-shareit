package ru.practicum.shareit.request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class ItemRequestRepositoryTest {
    @Autowired
    ItemRequestRepository itemRequestRepository;

    @Autowired
    private TestEntityManager testEm;

    private ItemRequest itemRequest;
    private ItemRequestDto itemRequestDto;
    private UserDto userDto;
    private User user;

    @BeforeEach
    void setUp() {
        userDto = new UserDto(1L, "name", "email@email.ru");
        user = UserMapper.toUser(userDto);
        itemRequestDto = new ItemRequestDto(1L, "Decription", user, null, List.of());
        itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto);
        testEm.merge(user);
        testEm.flush();
        itemRequestRepository.save(itemRequest);
    }

    @Test
    void findAllByRequesterIdTest() {
        List<ItemRequest> itemRequestList = itemRequestRepository.findAllByRequesterId(1L);
        assertEquals(itemRequestList.size(), 1);
        assertEquals(itemRequestList.get(0).getDescription(), "Decription");
    }

    @Test
    void findAllByRequesterIdNotOrderByCreatedDescTest() {
        List<ItemRequest> itemRequestList = itemRequestRepository.findAllByRequesterIdNotOrderByCreatedDesc(2L, PageRequest.of(0,10));
        assertEquals(itemRequestList.size(), 1);
        assertEquals(itemRequestList.get(0).getDescription(), "Decription");
    }

}