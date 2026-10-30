---
name: Global Procurement OS
description: A B2B sourcing marketplace from product discovery through delivery tracking.
colors:
  primary: "#28533e"
  primary-deep: "#1d3e2e"
  primary-soft: "#e8f0e8"
  neutral-bg: "#f4f6f3"
  neutral-surface: "#ffffff"
  neutral-ink: "#202a24"
  neutral-muted: "#68756c"
  neutral-quiet: "#88938b"
  neutral-line: "#dfe5df"
  status-positive: "#466a3d"
  status-positive-soft: "#e8f2e4"
  status-warning: "#a96a25"
  status-warning-soft: "#fbf0de"
  status-info: "#365d80"
  status-info-soft: "#eaf1f7"
  status-error: "#883e38"
  status-error-soft: "#fcf0ef"
  status-error-line: "#ebcbc8"
  notice-success: "#305941"
  notice-success-soft: "#f0f6ef"
  notice-success-line: "#cddfce"
  notice-info: "#3e5d75"
  notice-info-soft: "#f0f5f9"
  notice-info-line: "#d3dfe8"
typography:
  headline:
    fontFamily: "DM Sans, Arial, sans-serif"
    fontSize: "30px"
    fontWeight: 700
    lineHeight: 1.18
    letterSpacing: "-0.85px"
  page-title:
    fontFamily: "DM Sans, Arial, sans-serif"
    fontSize: "25px"
    fontWeight: 700
    lineHeight: 1.2
  section-title:
    fontFamily: "DM Sans, Arial, sans-serif"
    fontSize: "17px"
    fontWeight: 600
    lineHeight: 1.3
  body:
    fontFamily: "DM Sans, Arial, sans-serif"
    fontSize: "12px"
    fontWeight: 400
    lineHeight: 1.65
  label:
    fontFamily: "DM Sans, Arial, sans-serif"
    fontSize: "10px"
    fontWeight: 700
    lineHeight: 1.4
  metadata:
    fontFamily: "DM Sans, Arial, sans-serif"
    fontSize: "8px"
    fontWeight: 600
    lineHeight: 1.4
rounded:
  sm: "6px"
  nav: "7px"
  md: "9px"
  lg: "10px"
  avatar: "50%"
spacing:
  xs: "6px"
  sm: "8px"
  md: "12px"
  lg: "17px"
  xl: "22px"
  2xl: "32px"
components:
  button-primary:
    backgroundColor: "{colors.primary}"
    textColor: "#ffffff"
    typography: "{typography.label}"
    rounded: "{rounded.sm}"
    padding: "0 14px"
    height: "38px"
  button-primary-hover:
    backgroundColor: "{colors.primary-deep}"
    textColor: "#ffffff"
    typography: "{typography.label}"
    rounded: "{rounded.sm}"
    padding: "0 14px"
    height: "38px"
  input-text:
    backgroundColor: "{colors.neutral-surface}"
    textColor: "{colors.neutral-ink}"
    typography: "{typography.body}"
    rounded: "{rounded.sm}"
    padding: "9px 10px"
    height: "40px"
  nav-item-selected:
    backgroundColor: "{colors.primary-soft}"
    textColor: "{colors.primary}"
    typography: "{typography.label}"
    rounded: "{rounded.nav}"
    padding: "8px 10px"
    height: "39px"
  product-card:
    backgroundColor: "{colors.neutral-surface}"
    textColor: "{colors.neutral-ink}"
    rounded: "{rounded.md}"
    padding: "13px 14px 12px"
  status-chip:
    backgroundColor: "{colors.status-positive-soft}"
    textColor: "{colors.status-positive}"
    typography: "{typography.metadata}"
    rounded: "5px"
    padding: "6px 8px"
---

# Design System: Global Procurement OS

## Overview

**Creative North Star: “Source-to-Shipment Workbench.”** Global Procurement OS keeps a B2B buyer and supplier workflow in one workspace: discover products, compare supplier specifications, send an inquiry, complete order details, confirm payment, and follow delivery. A persistent navigation rail keeps each role’s next task in reach while the main canvas changes between catalog, orders, listings, tracking, and account views.

The interface uses DM Sans, a quiet green action color, white task surfaces, and cool neutral framing. Green identifies the primary action and current navigation item; amber, blue, and green status treatments distinguish order states. Supplier-provided images are shown as supplied. When there is no image, a labeled neutral placeholder makes that absence clear.

**Key Characteristics:**

- Role-aware navigation for buyers and suppliers.
- Product cards that keep price, specifications, supplier, and inquiry action together.
- Order progress shown as named steps, with actions appearing at the relevant state.
- Personal delivery details shown in the authorized order context only.

## Colors

The deep green is reserved for the main action and selected navigation; its pale companion provides a quiet selected surface. Neutral ink carries headings and essential values, while muted text supports descriptions and metadata. Status colors are semantic: green for paid or shipped progress, amber for waiting states, and blue for a new inquiry.

**The Source-and-Status Rule.** Use the primary green for the current task and selected location. Use semantic status colors only to explain an order or system state; never use them as decoration.

**The Clear-Feedback Rule.** Use separate colors for success, information, and error notices. Expose errors with alert semantics and other feedback with status semantics.

## Typography

DM Sans is the only UI family, loaded at weights 400–700. Page headlines use a compact bold scale (30px); page titles step down to 25px, section titles to 17px, and labels and metadata to 10px and 8px. Body descriptions generally use 12px with open leading, while the base document text is 14px.

**The One-Family Rule.** Keep labels, forms, cards, and navigation in DM Sans. Use weight and size to establish hierarchy rather than introducing a display face.

## Layout

The desktop workspace uses a 258px fixed left sidebar and a sticky top bar above the content canvas. The main canvas is capped at 1480px and uses a 3-column product grid, reducing to two columns below 1180px and one column below 620px. At 820px and narrower, the sidebar becomes an off-canvas drawer behind a scrim. At phone widths, product cards place the image beside the product details and order forms stack into one column.

**The Role-Rail Rule.** Keep buyer and supplier destinations in the left navigation; change the main content to the selected task without hiding role context.

## Elevation & Depth

The default is border-led and flat: white surfaces sit against a light neutral canvas, separated with thin neutral borders. A soft green-tinted shadow (0 12px 28px at 8% opacity) appears on product-card hover only. Focus uses a visible 3px green-gray outline with a 2px offset.

## Shapes

Buttons and inputs have gently rounded 6px corners; navigation items use 7px, product and order cards 9px, and account panels 10px. Status labels and role badges use compact 5px corners. Avatars are circular. Borders remain thin and neutral rather than becoming a colored edge accent.

## Components

- **Primary button:** green fill, white text, compact 10px bold label, and a 6px corner. Hover deepens the green; disabled actions remain visibly muted.
- **Quiet action:** transparent surface with green text, used for secondary navigation and order details. Hover adds an underline or soft neutral background.
- **Text field:** white surface, thin neutral border, 40px minimum height, and visible focus outline. Forms place a short label directly above each field.
- **Sidebar navigation:** outline SVG icon, readable label, and optional count. The active row gets a pale green fill and bold green text; expandable sections show their state with a chevron.
- **Product card:** supplier image or labeled placeholder above product code, unit price, name, description, specification tags, supplier identity, and inquiry action.
- **Order card:** order ID, product and quantity, semantic status chip, five labeled workflow steps, and only the next valid role-specific action.
- **Status chip:** compact text on a pale semantic surface; always retain the readable status label so color is not the only signal.
- **Notice:** sticky inline feedback with distinct success, information, and error colors; errors use alert semantics, other notices use status semantics, and each has a dismiss button.

## Do's and Don'ts

- Do keep buyer and supplier actions role-specific and close to their related records.
- Do show the current order state and name the next available step.
- Do keep payment verification visibly ahead of packing and shipment actions.
- Do use supplier-provided product images and honest labeled placeholders when an image is absent.
- Don't imply that image recognition, live checkout, supplier verification, or tracking-provider integrations exist when they are not connected.
- Don't show delivery addresses outside the buyer’s order or the assigned supplier’s order detail.
- Don't use status colors as decoration or replace status labels with color alone.
- Do distinguish success, information, and error notices with separate colors and retain accessible message semantics.
