package rw.ac.auca.pharmacy.pharmacy_inventory.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import rw.ac.auca.pharmacy.pharmacy_inventory.domain.Counter;

public interface CounterRepository extends JpaRepository<Counter, UUID> {
    Optional<Counter> findByNameIgnoreCase(String name);
    Optional<Counter> findByCounterNumber(int counterNumber);
}
