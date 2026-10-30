package com.marketplace;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "products", uniqueConstraints = @UniqueConstraint(columnNames = {"supplier_id", "code"}))
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) private Long id;
    @ManyToOne(optional = false, fetch = FetchType.LAZY) @JoinColumn(name = "supplier_id") private Account supplier;
    @Column(nullable = false, length = 64) private String code;
    @Column(nullable = false, length = 160) private String name;
    @Column(nullable = false, length = 4000) private String description;
    @Column(length = 120) private String size;
    @Column(length = 120) private String color;
    @Column(length = 120) private String weight;
    @Column(length = 1000) private String pictureUrl;
    @Column(nullable = false, precision = 12, scale = 2) private BigDecimal price;
    @Column(nullable = false, length = 3) private String currency;
    @Column(nullable = false) private boolean available = true;
    protected Product() {}
    Product(Account supplier, String code, String name, String description, String size, String color, String weight,
            String pictureUrl, BigDecimal price, String currency) {
        this.supplier=supplier; this.code=code; this.name=name; this.description=description; this.size=size;
        this.color=color; this.weight=weight; this.pictureUrl=pictureUrl; this.price=price; this.currency=currency.toUpperCase();
    }
    public Long getId() { return id; }
    public Account getSupplier() { return supplier; }
    public String getCode() { return code; }
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getSize() { return size; }
    public String getColor() { return color; }
    public String getWeight() { return weight; }
    public String getPictureUrl() { return pictureUrl; }
    public BigDecimal getPrice() { return price; }
    public String getCurrency() { return currency; }
    public boolean isAvailable() { return available; }
}
