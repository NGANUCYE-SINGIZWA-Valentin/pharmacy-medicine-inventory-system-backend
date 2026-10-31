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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import rw.ac.auca.pharmacy.pharmacy_inventory.domain.Pharmacist;
import rw.ac.auca.pharmacy.pharmacy_inventory.service.PharmacistService;

@RestController
@RequestMapping("/api/pharmacists")
public class PharmacistController {
    private final PharmacistService service;

    public PharmacistController(PharmacistService service) {
        this.service = service;
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody Pharmacist pharmacist) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(pharmacist));
    }

    @GetMapping("/all")
    public List<Pharmacist> findAll() { return service.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable UUID id) {
        Optional<Pharmacist> pharmacist = service.findById(id);
        return pharmacist.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("The pharmacist with that id does not exist"));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody Pharmacist pharmacist) {
        Optional<Pharmacist> updated = service.update(id, pharmacist);
        return updated.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("The pharmacist with that id does not exist"));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        String message = service.delete(id);
        HttpStatus status = "Pharmacist deleted successfully".equals(message) ? HttpStatus.OK : HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(message);
    }

    @GetMapping("/by-medicine-category")
    public List<Pharmacist> findByMedicineCategory(@RequestParam String name) {
        return service.findByMedicineCategoryName(name);
    }
}
