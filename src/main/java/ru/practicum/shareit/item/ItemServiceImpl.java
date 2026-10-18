package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ForbiddenException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserStorage;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final UserStorage userStorage;

    private User getUser(Long userId) {
        return userStorage.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь не найден: " + userId));
    }

    @Override
    public ItemDto create(Long userId, ItemDto dto) {
        User owner = getUser(userId);
        Item item = ItemMapper.toItem(dto);
        item.setOwner(owner);
        return ItemMapper.toItemDto(itemStorage.save(item));
    }

    @Override
    public ItemDto update(Long userId, Long itemId, ItemDto dto) {
        getUser(userId);
        Item item = itemStorage.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь не найдена: " + itemId));
        if (!item.getOwner().getId().equals(userId)) {
            throw new ForbiddenException("Редактировать вещь может только владелец");
        }
        if (dto.getName() != null && !dto.getName().isBlank()) {
            item.setName(dto.getName());
        }
        if (dto.getDescription() != null && !dto.getDescription().isBlank()) {
            item.setDescription(dto.getDescription());
        }
        if (dto.getAvailable() != null) {
            item.setAvailable(dto.getAvailable());
        }
        return ItemMapper.toItemDto(itemStorage.save(item));
    }

    @Override
    public ItemDto getById(Long itemId) {
        return itemStorage.findById(itemId).map(ItemMapper::toItemDto)
                .orElseThrow(() -> new NotFoundException("Вещь не найдена: " + itemId));
    }

    @Override
    public List<ItemDto> getByOwner(Long userId) {
        getUser(userId);
        return itemStorage.findByOwner(userId).stream().map(ItemMapper::toItemDto).toList();
    }

    @Override
    public List<ItemDto> search(String text) {
        if (text == null || text.isBlank()) {
            return List.of();
        }
        return itemStorage.search(text).stream().map(ItemMapper::toItemDto).toList();
    }
}