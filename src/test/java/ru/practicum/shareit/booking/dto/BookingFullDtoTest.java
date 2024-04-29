package ru.practicum.shareit.booking.dto;

import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
class BookingFullDtoTest {
    @Autowired
    private JacksonTester<BookingFullDto> json;

    @Test
    @SneakyThrows
    public void bookingFullDtoTest() {
        BookingFullDto bookingFullDto = new BookingFullDto();
        bookingFullDto.setId(1L);
        assertThat(json.write(bookingFullDto)).extractingJsonPathNumberValue("$.id").isEqualTo(1);
    }

}