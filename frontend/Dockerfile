# Frontend Dockerfile for building Vite React app and serving with a simple static server
FROM node:20-alpine as build
WORKDIR /app
COPY package.json package-lock.json* ./
RUN npm ci --silent
COPY . .
RUN npm run build

# Serve using a lightweight static server (caddy or nginx); use nginx here
FROM nginx:stable-alpine
COPY --from=build /app/dist /usr/share/nginx/html
# Expose default HTTP port
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]

