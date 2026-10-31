# Pharmacy Medicine Inventory System

**Student:** Nganucye Singizwa Valentin  
**Student ID:** 27201  
**Project:** Pharmacy Medicine Inventory System  
**Course:** INSY 8322 Web Technology and Internet  

## Steps to Run the API

1. Make sure PostgreSQL is running and create a database named `pharmacy_inventory`
2. Clone the repository
3. Open the project in your IDE
4. Update `src/main/resources/application.properties` if your PostgreSQL password is different
5. Run the application using Maven:
   ```
   ./mvnw spring-boot:run
   ```
6. The API will be available at `http://localhost:8084`

## API Base URL

```
http://localhost:8084/api
```

## Endpoints Summary

| Part | Path |
|------|------|
| A | /api/medicine-categories, /api/counters, /api/pharmacists, /api/suppliers, /api/stock-orders |
| B | /api/suppliers/by-city, /api/stock-orders/by-status, /api/stock-orders/between |
| C | /api/pharmacists/by-medicine-category, /api/counters/free, /api/medicine-categories/unused, /api/suppliers/of-pharmacist/{id} |
| D | /api/stock-orders/save, /api/stock-orders/{id}/status, /api/pharmacists/{id}/medicine-categories/{id}, /api/pharmacists/{id}/counter |
| E | /api/stock-batches/save, /api/stock-batches/supplier/{code}, /api/stock-batches/delete/{id} |
| F | /api/stock-orders/stats/by-status, /api/stock-orders/cancel-day |
| Bonus | /api/dashboard/summary, /api/stock-orders/page |
