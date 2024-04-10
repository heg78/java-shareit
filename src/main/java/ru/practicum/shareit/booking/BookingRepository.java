package ru.practicum.shareit.booking;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.booking.model.Booking;

import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByBooker_Id(long userId);

    @Query(value = "select bkg.* from bookings bkg " +
            "join items ims on bkg.item_id = ims.id " +
            "where ims.owner = ?1 " +
            "order by bkg.start_date desc", nativeQuery = true)
    List<Booking> getOwnerBookings(long userId);

    @Query(value = "SELECT B.* FROM BOOKINGS B JOIN ITEMS I ON I.ID=B.ITEM_ID " +
            "WHERE B.ITEM_ID = ?1 AND I.OWNER = ?2 AND STATUS = 'APPROVED' ORDER BY END_DATE LIMIT 2"
            , nativeQuery = true)
    List<Booking> findLastNextDate(Long itemId, Long userId);
}
