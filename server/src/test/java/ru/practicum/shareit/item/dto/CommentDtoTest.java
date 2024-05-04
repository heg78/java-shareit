package ru.practicum.shareit.item.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class CommentDtoTest {
    @Autowired
    private JacksonTester<CommentDto> json;

    CommentDto commentDto = new CommentDto(1L, "Comment text", "name", 1L, null);

    @Test
    @SneakyThrows
    public void commentDtoTest() {
        assertThat(json.write(commentDto)).extractingJsonPathNumberValue("$.id").isEqualTo(commentDto.getId().intValue());
        assertThat(json.write(commentDto)).extractingJsonPathStringValue("$.text").isEqualTo((commentDto.getText()));
    }
}