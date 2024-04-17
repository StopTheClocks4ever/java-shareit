package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;

import java.util.List;

public interface BookingService {

    ResponseBookingDto addBooking(BookingDto bookingDto, int bookerId);

    ResponseBookingDto patchBooking(int ownerId, int bookingId, boolean isApproved);

    ResponseBookingDto getBookingById(int requesterId, int bookingId);

    List<ResponseBookingDto> getAllUsersBookings(int usersId, String state);

    List<ResponseBookingDto> getAllItemOwnerBookings(int ownerId, String state);
}
