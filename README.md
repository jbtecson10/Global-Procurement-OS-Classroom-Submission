# Global Procurement OS

Starter marketplace built with Spring Boot 3.5, PostgreSQL, and Next.js App Router.

## Included

- Buyer and supplier registration; BCrypt password hashing and role-based HTTP Basic API authentication.
- Supplier product catalog with code, name, description, size, color, weight, picture URL, price, and currency.
- Supplier JPEG/PNG uploads with preview, resolution checks, persistent local storage, and a buyer/supplier full-size product photo viewer.
- Buyer search that asks a local Ollama model to rank live supplier products against natural-language requirements, with a clearly labeled keyword fallback when the model is unavailable.
- Buyer photo search: the local `moondream:latest` vision model describes the uploaded product image, then the live text model ranks real supplier catalog entries. Image matching is available before sign-in and sends the image only to the configured local Ollama service.
- Inquiry, supplier detail request, buyer specifications/address, supplier confirmation, payment verification, packing, shipping, and tracking workflow.
- Optional hosted Stripe Checkout. Configure `STRIPE_SECRET_KEY` and `STRIPE_WEBHOOK_SECRET`; the signed webhook validates session ownership, amount, currency, and order state before marking an order paid. Supplier packing remains locked until that confirmation. Without Stripe credentials, only explicitly enabled local demo payment is available.
- Optional local demo data with buyer and supplier workspaces, sample listings, and a no-charge simulated payment path (`DEMO_DATA_ENABLED=true`). Keep this disabled on public deployments; actual payment still requires a configured provider.
- Light/dark appearance and English, Simplified Chinese, and Filipino interface language options.
- Landing-page product spotlight carousel, restrained parallax, and reduced-motion support.
- Public landing page includes an end-to-end visual flowchart with buyer/supplier lanes, the shared AI catalog, the payment verification gate, and shipment tracking.
- PostgreSQL schema managed by Flyway. Buyer shipping addresses are omitted from order lists and tracking responses; only the buyer and assigned supplier can read order details.
- Next.js responsive marketplace page for account creation/sign-in, product search/listing, supplier catalog editing, and order actions.

## Build and run

### Docker Compose

```powershell
Copy-Item .env.example .env
# Replace both example secrets in .env with long random values.
# For a local demo, set DEMO_DATA_ENABLED=true in .env. Keep it false on public deployments.
docker compose -f compose.yml up --build
```

Then open `http://localhost:3000`; the API health endpoint is `http://localhost:8080/api/health`.
For AI matching, run Ollama on the host with `llama3.2:3b` and `moondream:latest` available. Docker Compose connects to the host at `http://host.docker.internal:11434`; configure Ollama to listen on an address reachable from Docker Desktop. Override `OLLAMA_BASE_URL`, `OLLAMA_MODEL`, or `OLLAMA_IMAGE_MODEL` in `.env` if needed. Text searches keep a criteria-based fallback while Ollama is offline; photo matching reports when local vision is unavailable.

To enable hosted checkout, set a Stripe test secret key (`sk_test_...`), the matching webhook signing secret (`whsec_...`), and `APP_PUBLIC_URL`. Point Stripe's webhook to `POST /api/payments/stripe/webhook` and enable `checkout.session.completed` (and `checkout.session.async_payment_succeeded` if using delayed methods). Test mode is suitable for local integration. Do not use live keys until the deployment security checklist is complete.

### Render classroom deployment

The included `render.yaml` provisions the Next.js site, Spring Boot API, and PostgreSQL database as a Render Blueprint in Singapore. Put the contents of this project folder at the root of a GitHub repository, connect that repository in Render, and create a Blueprint from `render.yaml`. The Blueprint enables fictional demo accounts and simulated payment; it does not configure real payment credentials.

The free plan is for a short classroom preview: Render free web services sleep after 15 idle minutes and uploaded product photos are lost when their service restarts. Free PostgreSQL expires 30 days after creation. Local Ollama models are not deployed with the app, so public AI search uses the app's stated matching fallback until an AI service reachable from Render is configured. Do not enter real customer addresses into the public demo.

### Local development

Requirements: Java 17+, Maven 3.6.3+, Node.js 20.9+, npm, and PostgreSQL 17+.

Run PostgreSQL and create the `marketplace` database/user, then start the API from `backend/`:

```powershell
$env:DATABASE_URL = "jdbc:postgresql://localhost:5432/marketplace"
$env:DATABASE_USERNAME = "marketplace"
$env:DATABASE_PASSWORD = "your-local-password"
$env:PAYMENT_WEBHOOK_SECRET = "your-local-webhook-secret"
mvn spring-boot:run
```

Start the web app in another terminal:

```powershell
cd frontend
npm ci
npm run dev
```

## Before deployment

- Use HTTPS and replace HTTP Basic with short-lived, revocable sessions or an OAuth/OIDC flow; the browser keeps Basic credentials only in tab session storage in this starter.
- Configure Stripe test credentials and exercise checkout and webhook delivery in a staging environment; the local build cannot charge or verify payments without those credentials. The signed webhook checks the Stripe session id, amount, currency, order reference, and payment status before unlocking packing.
- In the local demo, enable `DEMO_DATA_ENABLED=true` and use the landing-page buyer and supplier walkthrough buttons to try the sample order workflow. Sample product art and addresses are fictional.
- Buyer photo search uses local Ollama vision when reachable; the app does not claim an image match when that service is unavailable.
- Product photo uploads are stored in the local Docker volume for this starter. Use private object storage with controlled public delivery and image lifecycle rules before a production launch.
- Configure a managed PostgreSQL service with encryption, backups, least-privilege access, TLS, and private networking. Set production secrets outside source control.
- Review Flyway migrations, rate limits, audit logging, address retention/deletion, CSP, and security headers for the deployment environment.

The build is not a production deployment. No cloud target, domain, or production credentials were supplied.
