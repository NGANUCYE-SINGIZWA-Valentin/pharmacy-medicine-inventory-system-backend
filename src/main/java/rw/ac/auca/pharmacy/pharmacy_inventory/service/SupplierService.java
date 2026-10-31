package rw.ac.auca.pharmacy.pharmacy_inventory.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import rw.ac.auca.pharmacy.pharmacy_inventory.domain.Supplier;
import rw.ac.auca.pharmacy.pharmacy_inventory.repository.SupplierRepository;

@Service
public class SupplierService {
    private final SupplierRepository repository;

    public SupplierService(SupplierRepository repository) {
        this.repository = repository;
    }

    public String save(Supplier supplier) {
        validateSupplierCode(supplier.getSupplierCode(), null);
        repository.save(supplier);
        return "Supplier saved successfully";
    }

    public List<Supplier> findAll() {
        return repository.findAll();
    }

    public Optional<Supplier> findById(UUID id) {
        return repository.findById(id);
    }

    public Optional<Supplier> update(UUID id, Supplier supplier) {
        Optional<Supplier> result = repository.findById(id);
        if (result.isPresent()) {
            validateSupplierCode(supplier.getSupplierCode(), id);
            Supplier existing = result.get();
            existing.setCompanyName(supplier.getCompanyName());
            existing.setContactName(supplier.getContactName());
            existing.setSupplierCode(supplier.getSupplierCode());
            existing.setCity(supplier.getCity());
            existing.setPhone(supplier.getPhone());
            result = Optional.of(repository.save(existing));
        }
        return result;
    }

    private void validateSupplierCode(String supplierCode, UUID id) {
        if (supplierCode == null || supplierCode.trim().isEmpty()) {
            throw new IllegalArgumentException("Supplier code is required");
        }
        repository.findBySupplierCodeIgnoreCase(supplierCode.trim())
                .filter(found -> id == null || !found.getId().equals(id))
                .ifPresent(found -> {
                    throw new IllegalArgumentException("A record with this unique value already exists: supplier code");
                });
    }

    public String delete(UUID id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return "Supplier deleted successfully";
        }
        return "The supplier with that id does not exist";
    }
}
