package rw.ac.auca.pharmacy.pharmacy_inventory.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import rw.ac.auca.pharmacy.pharmacy_inventory.domain.StockOrder;
import rw.ac.auca.pharmacy.pharmacy_inventory.service.StockOrderService;

@RestController
@RequestMapping("/api/stock-orders")
public class StockOrderController {
    private final StockOrderService service;

    public StockOrderController(StockOrderService service) {
        this.service = service;
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody StockOrder stockOrder) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(stockOrder));
    }

    @GetMapping("/all")
    public List<StockOrder> findAll() { return service.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable UUID id) {
        Optional<StockOrder> stockOrder = service.findById(id);
        return stockOrder.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("The stock order with that id does not exist"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody StockOrder stockOrder) {
        Optional<StockOrder> updated = service.update(id, stockOrder);
        return updated.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("The stock order with that id does not exist"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        String message = service.delete(id);
        HttpStatus status = "Stock order deleted successfully".equals(message) ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(message);
    }
}
