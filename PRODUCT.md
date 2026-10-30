# Product

<!-- impeccable:product-schema 1 -->

## Platform

web

## Users

- Buyers source products, compare supplier capabilities, place inquiries, confirm specifications and delivery details, and follow orders through shipment.
- Suppliers and factories publish product specifications, review inquiries, request order details, confirm orders, and fulfill paid orders.

## Product Purpose

Global Procurement OS is a B2B marketplace that connects buyer product requirements with supplier catalogs and keeps the inquiry, order, payment-verification, and delivery steps together. Success means buyers can find suitable suppliers and follow an order, while suppliers can maintain listings and process confirmed, paid orders.

## Positioning

The product links product discovery directly to supplier inquiries and a shared order lifecycle, from a buyer's product brief through delivery tracking.

## Operating Context

Buyers search product names and specifications, review supplier and price information, submit inquiries, provide order specifications and a delivery address, and track the order. Suppliers maintain product code, description, size, color, weight, image URL, and price; review inquiries; confirm orders; and record shipment tracking.

## Capabilities and Constraints

- Frontend: Next.js. API: Spring Boot. Database: PostgreSQL with Flyway migrations.
- Buyer search uses a configured Ollama language model to rank the live supplier catalog against a natural-language sourcing brief. If the model is unreachable, the API returns explicit criteria-based keyword results and the interface labels the fallback.
- Suppliers can upload JPEG/PNG product photos; buyers and suppliers can open a full-size listing view. Buyer-side image query matching is not connected yet.
- Product images are represented by supplier-provided image URLs; image upload and image-based matching are not connected.
- Payment verification is an authenticated integration seam for a payment provider webhook. A real checkout provider is not connected; packing is gated on verified payment.
- Buyer addresses are private to the buyer and the supplier assigned to the order.
- The current app is a local starter deployment; production hosting, domain, and payment provider are undecided.

## Brand Commitments

The product is named Global Procurement OS. The user asked for a familiar B2B marketplace experience inspired by Alibaba, focused on the supplied buyer, supplier, AI matching, and order workflow.

## Evidence on Hand

The existing code includes the Spring Boot API, Flyway schema, and Next.js interface under `backend/` and `frontend/`. No customer stories, supplier verification program, or marketplace performance data have been supplied; do not invent them.

## Product Principles

- Put product discovery and supplier fit near the start of the buyer journey.
- Keep buyer and supplier actions clear and role-specific.
- Make each inquiry and order state visible and actionable.
- Do not authorize packing or shipment before payment verification.
- Protect buyer delivery details and describe current matching and payment capabilities accurately.
