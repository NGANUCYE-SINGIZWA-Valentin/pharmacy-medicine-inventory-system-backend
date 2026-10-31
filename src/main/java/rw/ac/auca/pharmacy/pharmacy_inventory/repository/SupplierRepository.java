package rw.ac.auca.pharmacy.pharmacy_inventory.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import rw.ac.auca.pharmacy.pharmacy_inventory.domain.Supplier;

public interface SupplierRepository extends JpaRepository<Supplier, UUID> {
    Optional<Supplier> findBySupplierCodeIgnoreCase(String supplierCode);

    List<Supplier> findByCityIgnoreCaseOrderByCompanyNameAsc(String city);
}
