CREATE TABLE accounts (
  id BIGSERIAL PRIMARY KEY,
  email VARCHAR(254) NOT NULL UNIQUE,
  name VARCHAR(120) NOT NULL,
  password_hash VARCHAR(100) NOT NULL,
  role VARCHAR(20) NOT NULL CHECK (role IN ('BUYER', 'SUPPLIER')),
  company VARCHAR(160)
);

CREATE TABLE products (
  id BIGSERIAL PRIMARY KEY,
  supplier_id BIGINT NOT NULL REFERENCES accounts(id),
  code VARCHAR(64) NOT NULL,
  name VARCHAR(160) NOT NULL,
  description VARCHAR(4000) NOT NULL,
  size VARCHAR(120),
  color VARCHAR(120),
  weight VARCHAR(120),
  picture_url VARCHAR(1000),
  price NUMERIC(12,2) NOT NULL CHECK (price > 0),
  currency VARCHAR(3) NOT NULL,
  available BOOLEAN NOT NULL DEFAULT TRUE,
  CONSTRAINT uq_product_supplier_code UNIQUE (supplier_id, code)
);
CREATE INDEX ix_products_supplier ON products(supplier_id);

CREATE TABLE marketplace_orders (
  id BIGSERIAL PRIMARY KEY,
  buyer_id BIGINT NOT NULL REFERENCES accounts(id),
  supplier_id BIGINT NOT NULL REFERENCES accounts(id),
  product_id BIGINT NOT NULL REFERENCES products(id),
  quantity INTEGER NOT NULL CHECK (quantity > 0),
  specifications VARCHAR(2000),
  shipping_address VARCHAR(1000),
  status VARCHAR(32) NOT NULL,
  payment_reference VARCHAR(200),
  tracking_number VARCHAR(120),
  created_at TIMESTAMPTZ NOT NULL,
  updated_at TIMESTAMPTZ NOT NULL
);
CREATE INDEX ix_orders_buyer_created ON marketplace_orders(buyer_id, created_at DESC);
CREATE INDEX ix_orders_supplier_created ON marketplace_orders(supplier_id, created_at DESC);
