package ru.practicum.shareit.user;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.user.model.User;

import java.util.List;

//@Transactional
public interface UserRepository extends JpaRepository<User, Long> {
    List<User> findByEmailIgnoreCase(String emailSearch);
}