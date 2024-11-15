package com.spring.repository;

import com.spring.entities.BookingDraftDrink;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingDraftDrinkRepository extends JpaRepository<BookingDraftDrink, Integer> {
}
