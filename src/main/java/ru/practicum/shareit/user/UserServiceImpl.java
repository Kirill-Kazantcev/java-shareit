package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage storage;

    @Override
    public UserDto create(UserDto dto) {
        if (storage.emailExists(dto.getEmail(), null)) {
            throw new ConflictException("Email уже используется: " + dto.getEmail());
        }
        return UserMapper.toUserDto(storage.save(UserMapper.toUser(dto)));
    }

    @Override
    public UserDto update(Long id, UserDto dto) {
        User user = storage.findById(id)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден: " + id));
        if (dto.getEmail() != null && !dto.getEmail().equalsIgnoreCase(user.getEmail())) {
            if (storage.emailExists(dto.getEmail(), id)) {
                throw new ConflictException("Email уже используется: " + dto.getEmail());
            }
            user.setEmail(dto.getEmail());
        }
        if (dto.getName() != null && !dto.getName().isBlank()) {
            user.setName(dto.getName());
        }
        return UserMapper.toUserDto(storage.save(user));
    }

    @Override
    public UserDto getById(Long id) {
        return storage.findById(id).map(UserMapper::toUserDto)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден: " + id));
    }

    @Override
    public List<UserDto> getAll() {
        return storage.findAll().stream().map(UserMapper::toUserDto).toList();
    }

    @Override
    public void delete(Long id) {
        storage.delete(id);
    }
}