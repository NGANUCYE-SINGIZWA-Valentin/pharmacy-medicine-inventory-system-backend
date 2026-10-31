package rw.ac.auca.pharmacy.pharmacy_inventory.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import rw.ac.auca.pharmacy.pharmacy_inventory.domain.MedicineCategory;
import rw.ac.auca.pharmacy.pharmacy_inventory.repository.MedicineCategoryRepository;

@Service
public class MedicineCategoryService {
    private final MedicineCategoryRepository repository;

    public MedicineCategoryService(MedicineCategoryRepository repository) {
        this.repository = repository;
    }

    public String save(MedicineCategory category) {
        validateName(category.getName(), null);
        category.setName(category.getName().trim());
        repository.save(category);
        return "Medicine category saved successfully";
    }

    public List<MedicineCategory> findAll() {
        return repository.findAll();
    }

    public java.util.Optional<MedicineCategory> findById(UUID id) {
        return repository.findById(id);
    }

    public java.util.Optional<MedicineCategory> update(UUID id, MedicineCategory category) {
        java.util.Optional<MedicineCategory> result = repository.findById(id);
        if (result.isPresent()) {
            validateName(category.getName(), id);
            MedicineCategory existing = result.get();
            existing.setName(category.getName().trim());
            result = java.util.Optional.of(repository.save(existing));
        }
        return result;
    }

    private void validateName(String name, UUID id) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Medicine category name is required");
        }
        repository.findByNameIgnoreCase(name.trim())
                .filter(found -> id == null || !found.getId().equals(id))
                .ifPresent(found -> {
                    throw new IllegalArgumentException("Medicine category already exists: " + name.trim());
                });
    }

    public String delete(UUID id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return "Medicine category deleted successfully";
        }
        return "The medicine category with that id does not exist";
    }

    public List<MedicineCategory> findUnused() {
        return repository.findUnused();
    }
}
