/** @type {import('next').NextConfig} */
const nextConfig = {
  output: "standalone",
  images: {
    qualities: [75, 80, 85, 90, 95, 100],
    remotePatterns: [
      {
        protocol: "https",
        hostname: "**",
      },
    ],
  },
};

export default nextConfig;
