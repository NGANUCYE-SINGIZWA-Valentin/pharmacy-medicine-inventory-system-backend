package rw.ac.auca.pharmacy.pharmacy_inventory.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import rw.ac.auca.pharmacy.pharmacy_inventory.domain.StockOrder;

public interface StockOrderRepository extends JpaRepository<StockOrder, UUID> {
    Optional<StockOrder> findByInvoiceNumberIgnoreCase(String invoiceNumber);
}
