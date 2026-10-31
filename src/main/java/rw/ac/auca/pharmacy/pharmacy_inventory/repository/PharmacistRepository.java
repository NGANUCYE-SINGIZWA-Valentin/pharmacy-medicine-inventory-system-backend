package rw.ac.auca.pharmacy.pharmacy_inventory.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import rw.ac.auca.pharmacy.pharmacy_inventory.domain.Pharmacist;

public interface PharmacistRepository extends JpaRepository<Pharmacist, UUID> {
    Optional<Pharmacist> findByLicenseNumberIgnoreCase(String licenseNumber);
}
