package rw.ac.auca.pharmacy.pharmacy_inventory.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import rw.ac.auca.pharmacy.pharmacy_inventory.domain.Pharmacist;

public interface PharmacistRepository extends JpaRepository<Pharmacist, UUID> {
    Optional<Pharmacist> findByLicenseNumberIgnoreCase(String licenseNumber);

    @Query("""
            SELECT p FROM Pharmacist p
            JOIN p.medicineCategories c
            WHERE LOWER(c.name) LIKE LOWER(CONCAT('%', :name, '%'))
            """)
    List<Pharmacist> findByMedicineCategoryName(@Param("name") String name);
}
