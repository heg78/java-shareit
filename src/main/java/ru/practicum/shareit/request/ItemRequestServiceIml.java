package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.common.exception.NotFoundException;
import ru.practicum.shareit.common.exception.ValidationException;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.UserDao;
import ru.practicum.shareit.user.model.User;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class ItemRequestServiceIml implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserDao userRepository;

    @Transactional
    @Override
    public ItemRequestDto add(long userId, ItemRequestDto itemRequestDto) {
        User user = userRepository.getUser(userId);
        itemRequestDto.setRequester(user);
        itemRequestDto.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        return ItemRequestMapper.toItemRequestDto(itemRequestRepository.save(ItemRequestMapper.toItemRequest(itemRequestDto)));
    }

    @Override
    public ItemRequestDto update(long userId, ItemRequestDto itemRequestDto) {
        return null;
    }

    @Override
    public ItemRequestDto get(long userId, long itemRequestId) {
        userRepository.getUser(userId);
        Optional<ItemRequest> itemRequest = itemRequestRepository.findById(itemRequestId);
        if (itemRequest.isEmpty()) {
            throw new NotFoundException("Запрос не найден");
        }
        return ItemRequestMapper.toItemRequestDto(itemRequest.get());
    }

    @Override
    public List<ItemRequestDto> getUserRequests(long userId) {
        userRepository.getUser(userId);
        return itemRequestRepository.findAllByRequesterId(userId).stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestDto> getNotUserRequests(long userId, Integer from, Integer size) {
        userRepository.getUser(userId);
        if (from < 0 || size < 0) {
            throw new ValidationException("Указано отрицательное число");
        }
        List<ItemRequest> itemRequestList = itemRequestRepository.findAllByRequesterIdNotOrderByCreatedDesc(userId, PageRequest.of(from, size));
        return itemRequestList.stream()
                .map(ItemRequestMapper::toItemRequestDto)
                .collect(Collectors.toList());
    }


}
