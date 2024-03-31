package ru.practicum.shareit.user.model;

import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * TODO Sprint add-controllers.
 */
@Data
public class User {
    int id;
    String name;
    String email;
}
