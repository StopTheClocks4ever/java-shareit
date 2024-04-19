package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.BookingMapper;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.State;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.ResponseBookingDto;
import ru.practicum.shareit.booking.exceptions.*;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.exception.ItemIsNotAvailableException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.exceptions.ValidationException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public ResponseBookingDto addBooking(BookingDto bookingDto, int bookerId) {
        User booker = userRepository.findById(bookerId).orElseThrow(() -> new UserNotFoundException("Указанного пользователя не существует"));
        Item item = itemRepository.findById(bookingDto.getItemId()).orElseThrow(() -> new ItemNotFoundException("Указанной вещи не существует"));
        if (Objects.equals(booker.getId(), item.getOwner().getId())) {
            throw new BookingNotFoundException("Пользователю нет необходимости бронировать свою вещь");
        }
        if (!item.isAvailable()) {
            throw new ItemIsNotAvailableException("Вещь уже забронирована");
        }
        isTimeCorrect(bookingDto);
        Booking booking = BookingMapper.toBooking(bookingDto, item, booker);
        return BookingMapper.toResponseBookingDto(bookingRepository.save(booking));
    }

    @Override
    public ResponseBookingDto patchBooking(int ownerId, int bookingId, boolean isApproved) {
        Booking existedBooking = bookingRepository.findById(bookingId).orElseThrow(() -> new BookingNotFoundException("Такого бронирования не существует"));
        if (existedBooking.getBooker().getId() == ownerId) {
            throw new UserNotFoundException("Наглый букер. Ты не сможешь обыграть мою систему)");
        }
        if (existedBooking.getItem().getOwner().getId() != ownerId) {
            throw new IncorrectUserException("Указанный пользователь не имеет прав изменять статус бронирования");
        }
        if (existedBooking.getStatus().equals(BookingStatus.APPROVED)) {
            throw new ValidationException("Заявка на бронирование уже одобрена");
        }
        if (isApproved) {
            existedBooking.setStatus(BookingStatus.APPROVED);
        } else {
            existedBooking.setStatus(BookingStatus.REJECTED);
        }
        return BookingMapper.toResponseBookingDto(bookingRepository.save(existedBooking));
    }

    @Override
    public ResponseBookingDto getBookingById(int requesterId, int bookingId) {
        Booking existedBooking = bookingRepository.findById(bookingId).orElseThrow(() -> new BookingNotFoundException("Такого бронирования не существует"));
        Item item = existedBooking.getItem();
        if (existedBooking.getBooker().getId() != requesterId && item.getOwner().getId() != requesterId) {
            throw new NotOwnerAndNotBookerException("Указанный пользователь не владелец вещи и не арендатор");
        }
        return BookingMapper.toResponseBookingDto(existedBooking);
    }

    @Override
    public List<ResponseBookingDto> getAllUsersBookings(int usersId, State state) {
        User booker = userRepository.findById(usersId).orElseThrow(() -> new UserNotFoundException("Указанного пользователя не существует"));
        if (state.equals(State.ALL)) {
            return BookingMapper.listToResponseBookingDto(bookingRepository.findAllByBookerIdOrderByStartDesc(usersId));
        }
        if (state.equals(State.CURRENT)) {
            return BookingMapper.listToResponseBookingDto(bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfterOrderByIdAsc(usersId, LocalDateTime.now(), LocalDateTime.now()));
        }
        if (state.equals(State.PAST)) {
            return BookingMapper.listToResponseBookingDto(bookingRepository.findAllByBookerIdAndEndBeforeOrderByStartDesc(usersId, LocalDateTime.now()));
        }
        if (state.equals(State.FUTURE)) {
            return BookingMapper.listToResponseBookingDto(bookingRepository.findAllByBookerIdAndStartAfterOrderByStartDesc(usersId, LocalDateTime.now()));
        }
        if (state.equals(State.WAITING)) {
            return BookingMapper.listToResponseBookingDto(bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(usersId, BookingStatus.WAITING));
        }
        if (state.equals(State.REJECTED)) {
            return BookingMapper.listToResponseBookingDto(bookingRepository.findAllByBookerIdAndStatusOrderByStartDesc(usersId, BookingStatus.REJECTED));
        } else {
            throw new UnsupportedStatusException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    @Override
    public List<ResponseBookingDto> getAllItemOwnerBookings(int ownerId, State state) {
        User booker = userRepository.findById(ownerId).orElseThrow(() -> new UserNotFoundException("Указанного пользователя не существует"));
        if (state.equals(State.ALL)) {
            return BookingMapper.listToResponseBookingDto(bookingRepository.findAllByItemOwnerIdOrderByStartDesc(ownerId));
        }
        if (state.equals(State.CURRENT)) {
            return BookingMapper.listToResponseBookingDto(bookingRepository.findAllByItemOwnerIdAndStartBeforeAndEndAfterOrderByStartDesc(ownerId, LocalDateTime.now(), LocalDateTime.now()));
        }
        if (state.equals(State.PAST)) {
            return BookingMapper.listToResponseBookingDto(bookingRepository.findAllByItemOwnerIdAndEndBeforeOrderByStartDesc(ownerId, LocalDateTime.now()));
        }
        if (state.equals(State.FUTURE)) {
            return BookingMapper.listToResponseBookingDto(bookingRepository.findAllByItemOwnerIdAndStartAfterOrderByStartDesc(ownerId, LocalDateTime.now()));
        }
        if (state.equals(State.WAITING)) {
            return BookingMapper.listToResponseBookingDto(bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(ownerId, BookingStatus.WAITING));
        }
        if (state.equals(State.REJECTED)) {
            return BookingMapper.listToResponseBookingDto(bookingRepository.findAllByItemOwnerIdAndStatusOrderByStartDesc(ownerId, BookingStatus.REJECTED));
        } else {
            throw new UnsupportedStatusException("Unknown state: UNSUPPORTED_STATUS");
        }
    }

    private void isTimeCorrect(BookingDto bookingDto) {
        if (bookingDto.getEnd().isBefore(LocalDateTime.now())) {
            throw new IncorrectBookingTimeException("Время окончания бронирования не указано или указано неверно");
        }
        if (bookingDto.getStart().isBefore(LocalDateTime.now())) {
            throw new IncorrectBookingTimeException("Время начала бронирования не указано или указано неверно");
        }
        if (bookingDto.getStart().isEqual(bookingDto.getEnd())) {
            throw new IncorrectBookingTimeException("Время начала бронирования не может равняться времени окончания бронирования");
        }
        if (bookingDto.getEnd().isBefore(bookingDto.getStart())) {
            throw new IncorrectBookingTimeException("Время начала бронирования не может быть позже времени окончания бронирования");
        }
    }
}
