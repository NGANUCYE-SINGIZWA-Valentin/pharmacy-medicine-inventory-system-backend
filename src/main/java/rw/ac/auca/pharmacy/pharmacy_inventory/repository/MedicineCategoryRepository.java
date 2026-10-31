package rw.ac.auca.pharmacy.pharmacy_inventory.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import rw.ac.auca.pharmacy.pharmacy_inventory.domain.MedicineCategory;

public interface MedicineCategoryRepository extends JpaRepository<MedicineCategory, UUID> {
    Optional<MedicineCategory> findByNameIgnoreCase(String name);

    @Query("SELECT c FROM MedicineCategory c WHERE c.pharmacists IS EMPTY")
    List<MedicineCategory> findUnused();
}
