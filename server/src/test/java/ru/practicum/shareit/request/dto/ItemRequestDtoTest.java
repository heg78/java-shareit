package ru.practicum.shareit.request.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class ItemRequestDtoTest {
    @Autowired
    private JacksonTester<ItemRequestDto> json;

    private final User user = new User();
    private final ItemRequestDto itemRequestDto = new ItemRequestDto(1L, "Decription", user, null, List.of());

    @Test
    @SneakyThrows
    public void userDtoTest() {
        assertThat(json.write(itemRequestDto)).extractingJsonPathNumberValue("$.id").isEqualTo((int) itemRequestDto.getId());
        assertThat(json.write(itemRequestDto)).extractingJsonPathStringValue("$.description").isEqualTo(itemRequestDto.getDescription());
    }

}