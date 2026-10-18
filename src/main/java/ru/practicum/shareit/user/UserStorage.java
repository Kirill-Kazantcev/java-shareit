package ru.practicum.shareit.user;

import java.util.Collection;
import java.util.Optional;

public interface UserStorage {
    User save(User user);
    Optional<User> findById(Long id);
    Collection<User> findAll();
    boolean emailExists(String email, Long excludeId);
    void delete(Long id);
}