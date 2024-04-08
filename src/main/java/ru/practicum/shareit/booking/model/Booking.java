package ru.practicum.shareit.booking.model;

import com.sun.istack.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.booking.Status;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings", schema = "public")
@Getter
@Setter
@ToString
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "start_date", nullable = false)
    @NotNull
    private LocalDateTime start;
    @Column(name = "end_date", nullable = false)
    @NotNull
//    @javax.validation.constraints.NotNull(message = "!!!")
    private LocalDateTime end;
    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;
    @ManyToOne
    @JoinColumn(name = "booker_id", nullable = false)
    private User booker;
    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;
}
