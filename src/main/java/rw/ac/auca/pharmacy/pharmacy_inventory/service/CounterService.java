package rw.ac.auca.pharmacy.pharmacy_inventory.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import rw.ac.auca.pharmacy.pharmacy_inventory.domain.Counter;
import rw.ac.auca.pharmacy.pharmacy_inventory.repository.CounterRepository;

@Service
public class CounterService {
    private final CounterRepository repository;

    public CounterService(CounterRepository repository) {
        this.repository = repository;
    }

    public String save(Counter counter) {
        validateCounterName(counter);
        validateCounterNumber(counter.getCounterNumber(), null);
        counter.setName(counter.getName().trim());
        repository.save(counter);
        return "Counter saved successfully";
    }

    public List<Counter> findAll() {
        return repository.findAll();
    }

    public java.util.Optional<Counter> findById(UUID id) {
        return repository.findById(id);
    }

    public java.util.Optional<Counter> update(UUID id, Counter counter) {
        java.util.Optional<Counter> result = repository.findById(id);
        if (result.isPresent()) {
            validateCounterNameForUpdate(counter, id);
            validateCounterNumber(counter.getCounterNumber(), id);
            Counter existing = result.get();
            existing.setCounterNumber(counter.getCounterNumber());
            existing.setName(counter.getName().trim());
            result = java.util.Optional.of(repository.save(existing));
        }
        return result;
    }

    public String delete(UUID id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return "Counter deleted successfully";
        }
        return "The counter with that id does not exist";
    }

    private void validateCounterName(Counter counter) {
        if (counter.getName() == null || counter.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Counter name is required");
        }
        repository.findByNameIgnoreCase(counter.getName().trim())
                .ifPresent(found -> {
                    throw new IllegalArgumentException("Counter already exists: " + counter.getName());
                });
    }

    private void validateCounterNameForUpdate(Counter counter, UUID id) {
        if (counter.getName() == null || counter.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Counter name is required");
        }
        repository.findByNameIgnoreCase(counter.getName().trim())
                .filter(found -> !found.getId().equals(id))
                .ifPresent(found -> {
                    throw new IllegalArgumentException("Counter already exists: " + counter.getName());
                });
    }

    private void validateCounterNumber(int counterNumber, UUID id) {
        if (counterNumber <= 0) {
            throw new IllegalArgumentException("Counter number must be positive");
        }
        repository.findByCounterNumber(counterNumber)
                .filter(found -> id == null || !found.getId().equals(id))
                .ifPresent(found -> {
                    throw new IllegalArgumentException("Counter number already exists: " + counterNumber);
                });
    }
}
