package ru.practicum.shareit.booking.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;
import ru.practicum.shareit.booking.service.BookingService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-bookings.
 */
@RestController
@Slf4j
@RequestMapping("/bookings")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseBookingDto addBooking(@RequestBody @Valid BookingDto bookingDto, @RequestHeader("X-Sharer-User-Id") int bookerId) {
        log.info("Получен запрос POST /bookings");
        return bookingService.addBooking(bookingDto, bookerId);
    }

    @PatchMapping("/{bookingId}")
    public ResponseBookingDto updateBooking(@RequestHeader("X-Sharer-User-Id") int ownerId, @PathVariable int bookingId, @RequestParam (name = "approved") boolean isApproved) {
        log.info("Получен запрос PATCH /bookings/{bookingId}");
        return bookingService.patchBooking(ownerId, bookingId, isApproved);
    }

    @GetMapping("/{bookingId}")
    public ResponseBookingDto getBookingById(@RequestHeader("X-Sharer-User-Id") int requesterId, @PathVariable int bookingId) {
        log.info("Получен запрос GET /bookings/{bookingId}");
        return bookingService.getBookingById(requesterId, bookingId);
    }

    @GetMapping
    public List<ResponseBookingDto> getAllUsersBookings(@RequestHeader("X-Sharer-User-Id") int userId, @RequestParam (value = "state", defaultValue = "all", required = false) String state) {
        log.info("Получен запрос GET /bookings?state={state}");
        return bookingService.getAllUsersBookings(userId, state);
    }

    @GetMapping("/owner")
    public List<ResponseBookingDto> getAllItemOwnerBookings(@RequestHeader("X-Sharer-User-Id") int ownerId, @RequestParam (value = "state", defaultValue = "all", required = false) String state) {
        log.info("Получен запрос GET /bookings/owner?state={state}");
        return bookingService.getAllItemOwnerBookings(ownerId, state);
    }
}
