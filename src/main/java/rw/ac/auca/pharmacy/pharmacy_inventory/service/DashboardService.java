package rw.ac.auca.pharmacy.pharmacy_inventory.service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import rw.ac.auca.pharmacy.pharmacy_inventory.domain.StockOrderStatus;
import rw.ac.auca.pharmacy.pharmacy_inventory.repository.CounterRepository;
import rw.ac.auca.pharmacy.pharmacy_inventory.repository.PharmacistRepository;
import rw.ac.auca.pharmacy.pharmacy_inventory.repository.StockBatchRepository;
import rw.ac.auca.pharmacy.pharmacy_inventory.repository.StockOrderRepository;
import rw.ac.auca.pharmacy.pharmacy_inventory.repository.SupplierRepository;

@Service
public class DashboardService {
    private final SupplierRepository supplierRepository;
    private final PharmacistRepository pharmacistRepository;
    private final CounterRepository counterRepository;
    private final StockOrderRepository stockOrderRepository;
    private final StockBatchRepository stockBatchRepository;

    public DashboardService(SupplierRepository supplierRepository, PharmacistRepository pharmacistRepository,
            CounterRepository counterRepository, StockOrderRepository stockOrderRepository,
            StockBatchRepository stockBatchRepository) {
        this.supplierRepository = supplierRepository;
        this.pharmacistRepository = pharmacistRepository;
        this.counterRepository = counterRepository;
        this.stockOrderRepository = stockOrderRepository;
        this.stockBatchRepository = stockBatchRepository;
    }

    public Map<String, Long> getSummary() {
        Map<String, Long> summary = new LinkedHashMap<>();
        summary.put("suppliers", supplierRepository.count());
        summary.put("pharmacists", pharmacistRepository.count());
        summary.put("freeCounters", (long) counterRepository.findFreeCounters().size());
        summary.put("ordersToday", stockOrderRepository.findByOrderDateBetweenOrderByOrderDateAsc(
                LocalDate.now(), LocalDate.now()).size() * 1L);
        summary.put("placed", stockOrderRepository.findByStatusOrderByOrderDateAsc(
                StockOrderStatus.PLACED).size() * 1L);
        summary.put("stockBatches", stockBatchRepository.count());
        return summary;
    }
}
