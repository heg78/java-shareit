package ru.practicum.shareit.request.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
public class ItemRequestDto {
    private long id;
    @NotNull
    @NotEmpty
    private String description;
    private User requester;
    private Timestamp created;
    private List<ItemDto> items;
}
