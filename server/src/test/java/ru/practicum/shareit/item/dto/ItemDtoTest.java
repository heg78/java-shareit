package ru.practicum.shareit.item.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@JsonTest
class ItemDtoTest {
    @Autowired
    private JacksonTester<ItemDto> json;

    ItemDto itemDto = new ItemDto(1L, "ItemName", "ItemDescr", true, 1L, null, null, List.of(), null);

    @SneakyThrows
    @Test
    public void itemDtoTest() {
        assertThat(json.write(itemDto)).extractingJsonPathNumberValue("$.id").isEqualTo(itemDto.getId().intValue());
        assertThat(json.write(itemDto)).extractingJsonPathStringValue("$.name").isEqualTo((itemDto.getName()));
    }


}