package rw.ac.auca.pharmacy.pharmacy_inventory.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import rw.ac.auca.pharmacy.pharmacy_inventory.domain.Supplier;

public interface SupplierRepository extends JpaRepository<Supplier, UUID> {
    Optional<Supplier> findBySupplierCodeIgnoreCase(String supplierCode);

    List<Supplier> findByCityIgnoreCaseOrderByCompanyNameAsc(String city);

    @Query("""
            SELECT DISTINCT o.supplier FROM StockOrder o
            WHERE o.pharmacist.id = :pharmacistId
            """)
    List<Supplier> findByPharmacistId(@Param("pharmacistId") UUID pharmacistId);
}
