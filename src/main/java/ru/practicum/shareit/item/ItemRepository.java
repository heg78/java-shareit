package ru.practicum.shareit.item;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findByOwner(User user);

    @Query(value = " select * from items it where it.available = true and " +
            "(upper(it.name) like upper('%' || ?1 || '%') " +
            " or upper(it.description) like upper('%' || ?1 || '%'))",  nativeQuery = true)
    List<Item> searchAvialableByText(String text);
}
