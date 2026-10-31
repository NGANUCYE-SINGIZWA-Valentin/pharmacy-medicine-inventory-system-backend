package rw.ac.auca.pharmacy.pharmacy_inventory.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import rw.ac.auca.pharmacy.pharmacy_inventory.domain.StockBatch;
import rw.ac.auca.pharmacy.pharmacy_inventory.service.StockBatchService;

@RestController
@RequestMapping("/api/stock-batches")
public class StockBatchController {
    private final StockBatchService service;

    public StockBatchController(StockBatchService service) {
        this.service = service;
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestParam UUID stockOrderId, @RequestBody StockBatch stockBatch) {
        String message = service.save(stockOrderId, stockBatch);
        if (message.equals("The stock order with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
        if (message.equals("Stock batches are only for RECEIVED stock orders")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
    }

    @GetMapping("/supplier/{supplierCode}")
    public List<StockBatch> findBySupplierCode(@PathVariable String supplierCode) {
        return service.findBySupplierCode(supplierCode);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        String message = service.delete(id);
        HttpStatus status = "Stock batch deleted successfully".equals(message) ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(message);
    }
}
