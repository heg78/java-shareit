package ru.practicum.shareit.user.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class UserDtoTest {
    @Autowired
    private JacksonTester<UserDto> json;
    private final UserDto userDto = new UserDto(1L, "name", "email@email.ru");

    @Test
    @SneakyThrows
    public void startSerializes() {
        assertThat(json.write(userDto)).extractingJsonPathNumberValue("$.id").isEqualTo(userDto.getId().intValue());
        assertThat(json.write(userDto)).extractingJsonPathStringValue("$.email").isEqualTo(userDto.getEmail());
        assertThat(json.write(userDto)).extractingJsonPathStringValue("$.name").isEqualTo(userDto.getName());
    }
}