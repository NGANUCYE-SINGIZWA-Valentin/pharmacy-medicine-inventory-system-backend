package rw.ac.auca.pharmacy.pharmacy_inventory.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import rw.ac.auca.pharmacy.pharmacy_inventory.domain.Pharmacist;
import rw.ac.auca.pharmacy.pharmacy_inventory.domain.StockOrder;
import rw.ac.auca.pharmacy.pharmacy_inventory.domain.StockOrderStatus;

public interface StockOrderRepository extends JpaRepository<StockOrder, UUID> {
    Optional<StockOrder> findByInvoiceNumberIgnoreCase(String invoiceNumber);

    List<StockOrder> findByStatusOrderByOrderDateAsc(StockOrderStatus status);

    List<StockOrder> findByOrderDateBetweenOrderByOrderDateAsc(LocalDate start, LocalDate end);

    boolean existsByPharmacistAndOrderDateAndStatusNot(Pharmacist pharmacist, LocalDate orderDate, StockOrderStatus status);
}
