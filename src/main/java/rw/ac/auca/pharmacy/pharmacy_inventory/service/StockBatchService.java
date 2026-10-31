package rw.ac.auca.pharmacy.pharmacy_inventory.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import rw.ac.auca.pharmacy.pharmacy_inventory.domain.StockBatch;
import rw.ac.auca.pharmacy.pharmacy_inventory.domain.StockOrder;
import rw.ac.auca.pharmacy.pharmacy_inventory.domain.StockOrderStatus;
import rw.ac.auca.pharmacy.pharmacy_inventory.repository.StockBatchRepository;
import rw.ac.auca.pharmacy.pharmacy_inventory.repository.StockOrderRepository;

@Service
public class StockBatchService {
    private final StockBatchRepository repository;
    private final StockOrderRepository stockOrderRepository;

    public StockBatchService(StockBatchRepository repository, StockOrderRepository stockOrderRepository) {
        this.repository = repository;
        this.stockOrderRepository = stockOrderRepository;
    }

    public String save(UUID stockOrderId, StockBatch stockBatch) {
        Optional<StockOrder> stockOrderOpt = stockOrderRepository.findById(stockOrderId);
        if (stockOrderOpt.isEmpty()) {
            return "The stock order with that id does not exist";
        }
        StockOrder stockOrder = stockOrderOpt.get();
        if (stockOrder.getStatus() != StockOrderStatus.RECEIVED) {
            return "Stock batches are only for RECEIVED stock orders";
        }
        stockBatch.setReceivedDate(LocalDate.now());
        stockBatch.setStockOrder(stockOrder);
        repository.save(stockBatch);
        return "Stock batch saved successfully";
    }

    public List<StockBatch> findBySupplierCode(String supplierCode) {
        return repository.findBySupplierCode(supplierCode);
    }

    public String delete(UUID id) {
        Optional<StockBatch> result = repository.findById(id);
        if (result.isEmpty()) {
            return "The stock batch with that id does not exist";
        }
        repository.deleteById(id);
        return "Stock batch deleted successfully";
    }

    public boolean hasStockBatches(StockOrder stockOrder) {
        return repository.existsByStockOrder(stockOrder);
    }
}
