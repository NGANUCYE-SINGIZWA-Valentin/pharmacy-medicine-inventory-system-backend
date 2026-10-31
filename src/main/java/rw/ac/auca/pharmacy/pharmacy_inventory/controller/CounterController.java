package rw.ac.auca.pharmacy.pharmacy_inventory.controller;

import java.util.List;
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

import rw.ac.auca.pharmacy.pharmacy_inventory.domain.Counter;
import rw.ac.auca.pharmacy.pharmacy_inventory.service.CounterService;

@RestController
@RequestMapping("/api/counters")
public class CounterController {
    private final CounterService service;

    public CounterController(CounterService service) {
        this.service = service;
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody Counter counter) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(counter));
    }

    @GetMapping("/all")
    public List<Counter> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable UUID id) {
        java.util.Optional<Counter> counter = service.findById(id);
        if (counter.isPresent()) {
            return ResponseEntity.ok(counter.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("The counter with that id does not exist");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody Counter counter) {
        java.util.Optional<Counter> updated = service.update(id, counter);
        if (updated.isPresent()) {
            return ResponseEntity.ok(updated.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("The counter with that id does not exist");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        String message = service.delete(id);
        HttpStatus status = "Counter deleted successfully".equals(message)
                ? HttpStatus.OK
                : HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(message);
    }

    @GetMapping("/free")
    public List<Counter> findFreeCounters() {
        return service.findFreeCounters();
    }
}
