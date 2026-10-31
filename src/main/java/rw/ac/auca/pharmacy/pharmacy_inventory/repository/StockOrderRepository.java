package rw.ac.auca.pharmacy.pharmacy_inventory.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import rw.ac.auca.pharmacy.pharmacy_inventory.domain.Pharmacist;
import rw.ac.auca.pharmacy.pharmacy_inventory.domain.StockOrder;
import rw.ac.auca.pharmacy.pharmacy_inventory.domain.StockOrderStatus;

public interface StockOrderRepository extends JpaRepository<StockOrder, UUID> {
    Optional<StockOrder> findByInvoiceNumberIgnoreCase(String invoiceNumber);

    List<StockOrder> findByStatusOrderByOrderDateAsc(StockOrderStatus status);

    List<StockOrder> findByOrderDateBetweenOrderByOrderDateAsc(LocalDate start, LocalDate end);

    boolean existsByPharmacistAndOrderDateAndStatusNot(Pharmacist pharmacist, LocalDate orderDate, StockOrderStatus status);

    @Query("""
            SELECT o.status, COUNT(o)
            FROM StockOrder o
            GROUP BY o.status
            """)
    List<Object[]> countByStatus();

    @Modifying
    @Query("""
            UPDATE StockOrder o
            SET o.status = :declined
            WHERE o.pharmacist.id = :pharmacistId
              AND o.orderDate = :date
              AND o.status <> :received
            """)
    int cancelDayOrders(@Param("pharmacistId") UUID pharmacistId,
                        @Param("date") LocalDate date,
                        @Param("declined") StockOrderStatus declined,
                        @Param("received") StockOrderStatus received);

    Page<StockOrder> findAll(Pageable pageable);
}
