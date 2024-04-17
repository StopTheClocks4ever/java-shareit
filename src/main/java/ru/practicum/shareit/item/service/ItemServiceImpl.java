package ru.practicum.shareit.item.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.ShortBookingDto;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.ItemMapper;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemDtoBookings;
import ru.practicum.shareit.item.exception.IncorrectUserException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.exceptions.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BookingRepository bookingRepository;

    @Override
    public ItemDto addItem(ItemDto itemDto, int ownerId) {
        User user = userRepository.findById(ownerId).orElseThrow(() -> new UserNotFoundException("Указанного пользователя не существует"));
        Item item = ItemMapper.toItem(itemDto, user);
        return ItemMapper.toItemDto(itemRepository.save(item));
    }

    @Override
    public ItemDto updateItem(ItemDto itemDto, int itemId, int ownerId) {
        Item existedItem = itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException("Указанной вещи не существует"));
        if (existedItem.getOwner().getId() != ownerId) {
            throw new IncorrectUserException("Неверный владелец");
        }
        if (itemDto.getName() != null) {
            existedItem.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            existedItem.setDescription(itemDto.getDescription());
        }
        if (itemDto.getAvailable() != null) {
            existedItem.setAvailable(itemDto.getAvailable());
        }
        return ItemMapper.toItemDto(itemRepository.save(existedItem));
    }

    @Override
    public ItemDtoBookings getItemById(int ownerId, int itemId) {
        Item existedItem = itemRepository.findById(itemId).orElseThrow(() -> new ItemNotFoundException("Указанной вещи не существует"));

        ItemDtoBookings itemDtoBookings = new ItemDtoBookings();

        List<Booking> itemBookings = bookingRepository.findAllByItemIdOrderByStartAsc(existedItem.getId());
        List<Booking> bookingsBefore = itemBookings.stream().filter(i -> i.getEnd().isBefore(LocalDateTime.now())).collect(Collectors.toList());
        List<Booking> bookingsAfter = itemBookings.stream().filter(i -> i.getStart().isAfter(LocalDateTime.now())).collect(Collectors.toList());

        if (existedItem.getOwner().getId() == ownerId) {
            ShortBookingDto lastBooking;
            if (bookingsBefore.size() == 0) {
                lastBooking = null;
            } else {
                Booking before = bookingsBefore.get(bookingsBefore.size() - 1);
                lastBooking = new ShortBookingDto();
                lastBooking.setId(before.getId());
                lastBooking.setBookerId(before.getBooker().getId());
            }

            ShortBookingDto nextBooking;
            if(bookingsAfter.size() == 0) {
                nextBooking = null;
            } else {
                Booking after = bookingsAfter.get(0);
                nextBooking = new ShortBookingDto();
                nextBooking.setId(after.getId());
                nextBooking.setBookerId(after.getBooker().getId());
            }

            itemDtoBookings.setId(existedItem.getId());
            itemDtoBookings.setName(existedItem.getName());
            itemDtoBookings.setDescription(existedItem.getDescription());
            itemDtoBookings.setAvailable(existedItem.isAvailable());
            itemDtoBookings.setLastBooking(lastBooking);
            itemDtoBookings.setNextBooking(nextBooking);
        } else {
            itemDtoBookings.setId(existedItem.getId());
            itemDtoBookings.setName(existedItem.getName());
            itemDtoBookings.setDescription(existedItem.getDescription());
            itemDtoBookings.setAvailable(existedItem.isAvailable());
            itemDtoBookings.setLastBooking(null);
            itemDtoBookings.setNextBooking(null);
        }

        return itemDtoBookings;
    }

    @Override
    public List<ItemDtoBookings> getUserItems(int ownerId) {
        List<Item> userItems = itemRepository.findByOwnerId(ownerId);
        List<ItemDtoBookings> resultList = new ArrayList<>();
        if (userItems.size() != 0) {
            for (Item item : userItems) {
                ItemDtoBookings itemDtoBookings = new ItemDtoBookings();

                List<Booking> itemBookings = bookingRepository.findAllByItemIdOrderByStartAsc(item.getId());
                List<Booking> bookingsBefore = itemBookings.stream().filter(i -> i.getEnd().isBefore(LocalDateTime.now())).collect(Collectors.toList());
                List<Booking> bookingsAfter = itemBookings.stream().filter(i -> i.getStart().isAfter(LocalDateTime.now())).collect(Collectors.toList());

                ShortBookingDto lastBooking;
                if (bookingsBefore.size() == 0) {
                    lastBooking = null;
                } else {
                    Booking before = bookingsBefore.get(bookingsBefore.size() - 1);
                    lastBooking = new ShortBookingDto();
                    lastBooking.setId(before.getId());
                    lastBooking.setBookerId(before.getBooker().getId());
                }

                ShortBookingDto nextBooking;
                if(bookingsAfter.size() == 0) {
                    nextBooking = null;
                } else {
                    Booking after = bookingsAfter.get(0);
                    nextBooking = new ShortBookingDto();
                    nextBooking.setId(after.getId());
                    nextBooking.setBookerId(after.getBooker().getId());
                }

                itemDtoBookings.setId(item.getId());
                itemDtoBookings.setName(item.getName());
                itemDtoBookings.setDescription(item.getDescription());
                itemDtoBookings.setAvailable(item.isAvailable());
                itemDtoBookings.setLastBooking(lastBooking);
                itemDtoBookings.setNextBooking(nextBooking);

                resultList.add(itemDtoBookings);
            }
            return resultList.stream().sorted(Comparator.comparing(ItemDtoBookings::getId)).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public List<ItemDto> getSearch(String text) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        List<Item> searchedItem = itemRepository.findAllByNameContainingIgnoreCaseAndAvailableTrueOrDescriptionContainingIgnoreCaseAndAvailableTrue(text, text);
        return ItemMapper.listToItemDto(searchedItem);
    }
}
