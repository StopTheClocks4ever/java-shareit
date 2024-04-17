package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShortBookingDto {

    private int id;
    private int bookerId;

    public ShortBookingDto() {

    }
}
