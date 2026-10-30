package com.marketplace;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "marketplace_orders")
public class MarketplaceOrder {
    public enum Status { INQUIRY_SENT, DETAILS_REQUESTED, AWAITING_CONFIRMATION, AWAITING_PAYMENT, PAID, PACKING, SHIPPED, COMPLETED }
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional = false, fetch = FetchType.LAZY) @JoinColumn(name = "buyer_id") private Account buyer;
    @ManyToOne(optional = false, fetch = FetchType.LAZY) @JoinColumn(name = "supplier_id") private Account supplier;
    @ManyToOne(optional = false, fetch = FetchType.LAZY) @JoinColumn(name = "product_id") private Product product;
    @Column(nullable = false) private int quantity;
    @Column(length = 2000) private String specifications;
    @Column(length = 1000) private String shippingAddress;
    @Enumerated(EnumType.STRING) @Column(nullable = false, length = 32) private Status status;
    @Column(length = 200) private String paymentReference;
    @Column(length = 120) private String trackingNumber;
    @Column(nullable = false) private Instant createdAt = Instant.now();
    @Column(nullable = false) private Instant updatedAt = Instant.now();
    protected MarketplaceOrder() {}
    MarketplaceOrder(Account buyer, Account supplier, Product product, int quantity) {
        this.buyer=buyer; this.supplier=supplier; this.product=product; this.quantity=quantity; this.status=Status.INQUIRY_SENT;
    }
    public Long getId() { return id; }
    public Account getBuyer() { return buyer; }
    public Account getSupplier() { return supplier; }
    public Product getProduct() { return product; }
    public int getQuantity() { return quantity; }
    public String getSpecifications() { return specifications; }
    public void setSpecifications(String value) { specifications=value; touch(); }
    public String getShippingAddress() { return shippingAddress; }
    public void setShippingAddress(String value) { shippingAddress=value; touch(); }
    public Status getStatus() { return status; }
    public void setStatus(Status value) { status=value; touch(); }
    public String getPaymentReference() { return paymentReference; }
    public void setPaymentReference(String value) { paymentReference=value; touch(); }
    public String getTrackingNumber() { return trackingNumber; }
    public void setTrackingNumber(String value) { trackingNumber=value; touch(); }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    private void touch() { updatedAt=Instant.now(); }
}
