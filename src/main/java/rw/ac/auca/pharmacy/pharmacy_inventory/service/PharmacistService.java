package rw.ac.auca.pharmacy.pharmacy_inventory.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import rw.ac.auca.pharmacy.pharmacy_inventory.domain.MedicineCategory;
import rw.ac.auca.pharmacy.pharmacy_inventory.domain.Pharmacist;
import rw.ac.auca.pharmacy.pharmacy_inventory.repository.CounterRepository;
import rw.ac.auca.pharmacy.pharmacy_inventory.repository.MedicineCategoryRepository;
import rw.ac.auca.pharmacy.pharmacy_inventory.repository.PharmacistRepository;

@Service
public class PharmacistService {
    private final PharmacistRepository repository;
    private final CounterRepository counterRepository;
    private final MedicineCategoryRepository categoryRepository;

    public PharmacistService(PharmacistRepository repository, CounterRepository counterRepository,
            MedicineCategoryRepository categoryRepository) {
        this.repository = repository;
        this.counterRepository = counterRepository;
        this.categoryRepository = categoryRepository;
    }

    public String save(Pharmacist pharmacist) {
        validateLicenseNumber(pharmacist.getLicenseNumber(), null);
        resolveRelationships(pharmacist);
        repository.save(pharmacist);
        return "Pharmacist saved successfully";
    }

    public List<Pharmacist> findAll() {
        return repository.findAll();
    }

    public Optional<Pharmacist> findById(UUID id) {
        return repository.findById(id);
    }

    public Optional<Pharmacist> update(UUID id, Pharmacist pharmacist) {
        Optional<Pharmacist> result = repository.findById(id);
        if (result.isPresent()) {
            validateLicenseNumber(pharmacist.getLicenseNumber(), id);
            Pharmacist existing = result.get();
            existing.setFirstName(pharmacist.getFirstName());
            existing.setLastName(pharmacist.getLastName());
            existing.setLicenseNumber(pharmacist.getLicenseNumber());
            resolveRelationships(pharmacist);
            existing.setCounter(pharmacist.getCounter());
            existing.setMedicineCategories(pharmacist.getMedicineCategories());
            result = Optional.of(repository.save(existing));
        }
        return result;
    }

    public String delete(UUID id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return "Pharmacist deleted successfully";
        }
        return "The pharmacist with that id does not exist";
    }

    public List<Pharmacist> findByMedicineCategoryName(String name) {
        return repository.findByMedicineCategoryName(name);
    }

    private void validateLicenseNumber(String licenseNumber, UUID id) {
        if (licenseNumber == null || licenseNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("License number is required");
        }
        repository.findByLicenseNumberIgnoreCase(licenseNumber.trim())
                .filter(found -> id == null || !found.getId().equals(id))
                .ifPresent(found -> {
                    throw new IllegalArgumentException("License number already exists: " + licenseNumber.trim());
                });
    }

    private void resolveRelationships(Pharmacist pharmacist) {
        if (pharmacist.getCounter() != null && pharmacist.getCounter().getId() != null) {
            pharmacist.setCounter(counterRepository.findById(pharmacist.getCounter().getId()).orElse(null));
        }
        List<MedicineCategory> resolved = new ArrayList<>();
        if (pharmacist.getMedicineCategories() != null) {
            for (MedicineCategory category : pharmacist.getMedicineCategories()) {
                if (category.getId() != null) {
                    categoryRepository.findById(category.getId()).ifPresent(resolved::add);
                }
            }
        }
        pharmacist.setMedicineCategories(resolved);
    }
}
