import type { NextConfig } from "next";
const root = process.cwd();
const internalApi = (process.env.INTERNAL_API_URL ?? "http://localhost:8080").replace(/\/$/, "");
const nextConfig: NextConfig = {
  output: "standalone",
  outputFileTracingRoot: root,
  turbopack: { root },
  async rewrites() {
    return [{ source: "/api/:path*", destination: `${internalApi}/api/:path*` }];
  },
};
export default nextConfig;
