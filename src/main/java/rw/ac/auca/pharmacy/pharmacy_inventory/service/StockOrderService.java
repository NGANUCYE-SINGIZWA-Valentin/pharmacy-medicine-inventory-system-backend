package rw.ac.auca.pharmacy.pharmacy_inventory.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import rw.ac.auca.pharmacy.pharmacy_inventory.domain.Pharmacist;
import rw.ac.auca.pharmacy.pharmacy_inventory.domain.StockOrder;
import rw.ac.auca.pharmacy.pharmacy_inventory.domain.StockOrderStatus;
import rw.ac.auca.pharmacy.pharmacy_inventory.repository.PharmacistRepository;
import rw.ac.auca.pharmacy.pharmacy_inventory.repository.StockBatchRepository;
import rw.ac.auca.pharmacy.pharmacy_inventory.repository.StockOrderRepository;
import rw.ac.auca.pharmacy.pharmacy_inventory.repository.SupplierRepository;

@Service
public class StockOrderService {
    private final StockOrderRepository repository;
    private final SupplierRepository supplierRepository;
    private final PharmacistRepository pharmacistRepository;
    private final StockBatchRepository stockBatchRepository;

    public StockOrderService(StockOrderRepository repository, SupplierRepository supplierRepository,
            PharmacistRepository pharmacistRepository, StockBatchRepository stockBatchRepository) {
        this.repository = repository;
        this.supplierRepository = supplierRepository;
        this.pharmacistRepository = pharmacistRepository;
        this.stockBatchRepository = stockBatchRepository;
    }

    public String save(StockOrder stockOrder) {
        if (stockOrder.getOrderDate().isBefore(LocalDate.now())) {
            return "Stock order date cannot be in the past";
        }
        validateInvoiceNumber(stockOrder.getInvoiceNumber(), null);
        resolveRelationships(stockOrder);
        Pharmacist pharmacist = stockOrder.getPharmacist();
        if (pharmacist != null && repository.existsByPharmacistAndOrderDateAndStatusNot(
                pharmacist, stockOrder.getOrderDate(), StockOrderStatus.DECLINED)) {
            return "Pharmacist already receives a stock order on that date";
        }
        stockOrder.setStatus(StockOrderStatus.PLACED);
        repository.save(stockOrder);
        return "Stock order saved successfully";
    }

    public List<StockOrder> findAll() {
        return repository.findAll();
    }

    public Optional<StockOrder> findById(UUID id) {
        return repository.findById(id);
    }

    public Optional<StockOrder> update(UUID id, StockOrder stockOrder) {
        Optional<StockOrder> result = repository.findById(id);
        if (result.isPresent()) {
            validateInvoiceNumber(stockOrder.getInvoiceNumber(), id);
            resolveRelationships(stockOrder);
            StockOrder existing = result.get();
            existing.setOrderDate(stockOrder.getOrderDate());
            existing.setInvoiceNumber(stockOrder.getInvoiceNumber());
            existing.setSupplier(stockOrder.getSupplier());
            existing.setPharmacist(stockOrder.getPharmacist());
            result = Optional.of(repository.save(existing));
        }
        return result;
    }

    public String delete(UUID id) {
        Optional<StockOrder> result = repository.findById(id);
        if (result.isEmpty()) {
            return "The stock order with that id does not exist";
        }
        if (stockBatchRepository.existsByStockOrder(result.get())) {
            return "That stock order has stock batches, delete them first";
        }
        repository.deleteById(id);
        return "Stock order deleted successfully";
    }

    public String updateStatus(UUID id, StockOrderStatus newStatus) {
        Optional<StockOrder> result = repository.findById(id);
        if (result.isEmpty()) {
            return "The stock order with that id does not exist";
        }
        StockOrder existing = result.get();
        StockOrderStatus current = existing.getStatus();
        boolean allowed = (current == StockOrderStatus.PLACED &&
                (newStatus == StockOrderStatus.DISPATCHED || newStatus == StockOrderStatus.DECLINED))
                || (current == StockOrderStatus.DISPATCHED &&
                (newStatus == StockOrderStatus.RECEIVED || newStatus == StockOrderStatus.DECLINED));
        if (!allowed) {
            return "Cannot change status from " + current + " to " + newStatus;
        }
        existing.setStatus(newStatus);
        repository.save(existing);
        return "Stock order status updated successfully";
    }

    public List<StockOrder> findByStatus(StockOrderStatus status) {
        return repository.findByStatusOrderByOrderDateAsc(status);
    }

    public List<StockOrder> findBetween(LocalDate start, LocalDate end) {
        return repository.findByOrderDateBetweenOrderByOrderDateAsc(start, end);
    }

    public List<Object[]> countByStatus() {
        return repository.countByStatus();
    }

    @Transactional
    public String cancelDayOrders(UUID pharmacistId, LocalDate date) {
        int count = repository.cancelDayOrders(pharmacistId, date,
                StockOrderStatus.DECLINED, StockOrderStatus.RECEIVED);
        return count + " stock orders cancelled";
    }

    private void validateInvoiceNumber(String invoiceNumber, UUID id) {
        if (invoiceNumber == null || invoiceNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Invoice number is required");
        }
        repository.findByInvoiceNumberIgnoreCase(invoiceNumber.trim())
                .filter(found -> id == null || !found.getId().equals(id))
                .ifPresent(found -> {
                    throw new IllegalArgumentException("A record with this unique value already exists: invoice number");
                });
    }

    private void resolveRelationships(StockOrder stockOrder) {
        if (stockOrder.getSupplier() != null && stockOrder.getSupplier().getId() != null) {
            stockOrder.setSupplier(supplierRepository.findById(stockOrder.getSupplier().getId()).orElse(null));
        }
        if (stockOrder.getPharmacist() != null && stockOrder.getPharmacist().getId() != null) {
            stockOrder.setPharmacist(pharmacistRepository.findById(stockOrder.getPharmacist().getId()).orElse(null));
        }
    }
}
