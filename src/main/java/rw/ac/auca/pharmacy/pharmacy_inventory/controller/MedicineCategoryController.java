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

import rw.ac.auca.pharmacy.pharmacy_inventory.domain.MedicineCategory;
import rw.ac.auca.pharmacy.pharmacy_inventory.service.MedicineCategoryService;

@RestController
@RequestMapping("/api/medicine-categories")
public class MedicineCategoryController {
    private final MedicineCategoryService service;

    public MedicineCategoryController(MedicineCategoryService service) {
        this.service = service;
    }

    @PostMapping("/save")
    public ResponseEntity<String> save(@RequestBody MedicineCategory category) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.save(category));
    }

    @GetMapping("/all")
    public List<MedicineCategory> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable UUID id) {
        java.util.Optional<MedicineCategory> category = service.findById(id);
        if (category.isPresent()) {
            return ResponseEntity.ok(category.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("The medicine category with that id does not exist");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> update(@PathVariable UUID id, @RequestBody MedicineCategory category) {
        java.util.Optional<MedicineCategory> updated = service.update(id, category);
        if (updated.isPresent()) {
            return ResponseEntity.ok(updated.get());
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("The medicine category with that id does not exist");
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id) {
        String message = service.delete(id);
        HttpStatus status = "Medicine category deleted successfully".equals(message)
                ? HttpStatus.OK
                : HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(message);
    }
}
