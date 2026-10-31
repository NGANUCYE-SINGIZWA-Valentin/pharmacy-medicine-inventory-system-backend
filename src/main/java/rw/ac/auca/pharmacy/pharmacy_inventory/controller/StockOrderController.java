package rw.ac.auca.pharmacy.pharmacy_inventory.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import rw.ac.auca.pharmacy.pharmacy_inventory.domain.StockOrder;
import rw.ac.auca.pharmacy.pharmacy_inventory.domain.StockOrderStatus;
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
        String message = service.save(stockOrder);
        if (message.equals("Stock order date cannot be in the past")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }
        if (message.equals("Pharmacist already receives a stock order on that date")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(message);
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
        if (message.equals("The stock order with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
        if (message.equals("That stock order has stock batches, delete them first")) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(message);
        }
        return ResponseEntity.ok(message);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<String> updateStatus(@PathVariable UUID id, @RequestParam StockOrderStatus status) {
        String message = service.updateStatus(id, status);
        if (message.equals("The stock order with that id does not exist")) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
        }
        if (message.startsWith("Cannot change status")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
        }
        return ResponseEntity.ok(message);
    }

    @GetMapping("/by-status")
    public List<StockOrder> findByStatus(@RequestParam StockOrderStatus status) {
        return service.findByStatus(status);
    }

    @GetMapping("/between")
    public List<StockOrder> findBetween(@RequestParam String start, @RequestParam String end) {
        return service.findBetween(LocalDate.parse(start), LocalDate.parse(end));
    }

    @GetMapping("/stats/by-status")
    public List<Object[]> countByStatus() {
        return service.countByStatus();
    }

    @PatchMapping("/cancel-day")
    public ResponseEntity<String> cancelDayOrders(@RequestParam UUID pharmacistId, @RequestParam String date) {
        String message = service.cancelDayOrders(pharmacistId, LocalDate.parse(date));
        return ResponseEntity.ok(message);
    }

    @GetMapping("/page")
    public Page<StockOrder> findPage(@RequestParam int page, @RequestParam int size,
            @RequestParam String sort) {
        String[] parts = sort.split(",");
        Sort.Direction direction = parts.length > 1 && parts[1].equalsIgnoreCase("desc")
                ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, parts[0]));
        return service.findPage(pageable);
    }
}
