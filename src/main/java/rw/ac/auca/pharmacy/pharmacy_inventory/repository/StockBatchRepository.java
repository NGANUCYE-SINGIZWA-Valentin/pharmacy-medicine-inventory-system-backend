package rw.ac.auca.pharmacy.pharmacy_inventory.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import rw.ac.auca.pharmacy.pharmacy_inventory.domain.StockBatch;
import rw.ac.auca.pharmacy.pharmacy_inventory.domain.StockOrder;

public interface StockBatchRepository extends JpaRepository<StockBatch, UUID> {

    boolean existsByStockOrder(StockOrder stockOrder);

    @Query("""
            SELECT b FROM StockBatch b
            WHERE b.stockOrder.supplier.supplierCode = :supplierCode
            ORDER BY b.receivedDate DESC
            """)
    List<StockBatch> findBySupplierCode(@Param("supplierCode") String supplierCode);
}
