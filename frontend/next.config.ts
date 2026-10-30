import type { NextConfig } from "next";
const root = process.cwd();
const defaultInternalApi = process.env.NODE_ENV === "production"
  ? "https://global-procurement-api.onrender.com"
  : "http://localhost:8080";
const internalApi = (process.env.INTERNAL_API_URL ?? defaultInternalApi).replace(/\/$/, "");
const nextConfig: NextConfig = {
  output: "standalone",
  outputFileTracingRoot: root,
  turbopack: { root },
  async rewrites() {
    return [{ source: "/api/:path*", destination: `${internalApi}/api/:path*` }];
  },
};
export default nextConfig;
