package rw.ac.auca.pharmacy.pharmacy_inventory.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import rw.ac.auca.pharmacy.pharmacy_inventory.domain.Counter;

public interface CounterRepository extends JpaRepository<Counter, UUID> {
    Optional<Counter> findByNameIgnoreCase(String name);
    Optional<Counter> findByCounterNumber(int counterNumber);

    @Query("""
            SELECT c FROM Counter c
            WHERE NOT EXISTS (
                SELECT p FROM Pharmacist p WHERE p.counter = c
            )
            ORDER BY c.counterNumber ASC
            """)
    List<Counter> findFreeCounters();
}
