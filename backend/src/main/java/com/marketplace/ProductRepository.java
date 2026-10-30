package com.marketplace;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findBySupplierIdOrderByIdDesc(Long supplierId);
    List<Product> findByAvailableTrueOrderByIdDesc();
}
