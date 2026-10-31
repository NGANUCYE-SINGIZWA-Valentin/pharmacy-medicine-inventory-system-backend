package rw.ac.auca.pharmacy.pharmacy_inventory.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "pharmacist")
public class Pharmacist {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "license_number", nullable = false, unique = true)
    private String licenseNumber;

    @ManyToOne
    @JoinColumn(name = "counter_id")
    private Counter counter;

    @ManyToMany
    @JoinTable(
        name = "pharmacist_medicine_category",
        joinColumns = @JoinColumn(name = "pharmacist_id"),
        inverseJoinColumns = @JoinColumn(name = "medicine_category_id")
    )
    private List<MedicineCategory> medicineCategories = new ArrayList<>();

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public Counter getCounter() {
        return counter;
    }

    public void setCounter(Counter counter) {
        this.counter = counter;
    }

    public List<MedicineCategory> getMedicineCategories() {
        return medicineCategories;
    }

    public void setMedicineCategories(List<MedicineCategory> medicineCategories) {
        this.medicineCategories = medicineCategories;
    }
}
