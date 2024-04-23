package ru.practicum.shareit.request;

import ru.practicum.shareit.request.dto.ItemRequestDto;

import java.util.List;

public interface ItemRequestService {
    ItemRequestDto add(long userId, ItemRequestDto itemRequestDto);

    ItemRequestDto update(long userId, ItemRequestDto itemRequestDto);

    ItemRequestDto get(long userId, long itemRequestId);

    List<ItemRequestDto> getUserRequests(long userId);

    List<ItemRequestDto> getNotUserRequests(long userId,Integer from,Integer size);
}
