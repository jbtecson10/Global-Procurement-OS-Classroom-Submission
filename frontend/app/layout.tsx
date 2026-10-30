import type { Metadata } from "next";
import "./styles.css";

export const metadata: Metadata = {
  title: "Global Procurement OS — Source products direct",
  description: "A simple workspace for product discovery, supplier inquiries, verified orders, and delivery tracking.",
};

export default function RootLayout({ children }: Readonly<{ children: React.ReactNode }>) {
  return <html lang="en"><body>{children}</body></html>;
}
