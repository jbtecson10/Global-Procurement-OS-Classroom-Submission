package com.marketplace;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

interface OrderRepository extends JpaRepository<MarketplaceOrder, Long> {
    List<MarketplaceOrder> findByBuyerIdOrderByCreatedAtDesc(Long buyerId);
    List<MarketplaceOrder> findBySupplierIdOrderByCreatedAtDesc(Long supplierId);
}
