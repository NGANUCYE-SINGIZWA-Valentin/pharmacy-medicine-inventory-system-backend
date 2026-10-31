package rw.ac.auca.pharmacy.pharmacy_inventory.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import rw.ac.auca.pharmacy.pharmacy_inventory.domain.StockOrder;
import rw.ac.auca.pharmacy.pharmacy_inventory.domain.StockOrderStatus;
import rw.ac.auca.pharmacy.pharmacy_inventory.repository.PharmacistRepository;
import rw.ac.auca.pharmacy.pharmacy_inventory.repository.StockOrderRepository;
import rw.ac.auca.pharmacy.pharmacy_inventory.repository.SupplierRepository;

@Service
public class StockOrderService {
    private final StockOrderRepository repository;
    private final SupplierRepository supplierRepository;
    private final PharmacistRepository pharmacistRepository;

    public StockOrderService(StockOrderRepository repository, SupplierRepository supplierRepository,
            PharmacistRepository pharmacistRepository) {
        this.repository = repository;
        this.supplierRepository = supplierRepository;
        this.pharmacistRepository = pharmacistRepository;
    }

    public String save(StockOrder stockOrder) {
        validateInvoiceNumber(stockOrder.getInvoiceNumber(), null);
        resolveRelationships(stockOrder);
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
            // status is intentionally NOT updated here — use PATCH /status
            result = Optional.of(repository.save(existing));
        }
        return result;
    }

    public String delete(UUID id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return "Stock order deleted successfully";
        }
        return "The stock order with that id does not exist";
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
