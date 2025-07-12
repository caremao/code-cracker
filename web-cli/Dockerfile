# Build the React app
FROM node:20 AS build
WORKDIR /app
COPY package*.json ./
RUN npm ci
COPY . .

# allow API endpoint to be set during build
ARG VITE_API_BASE=http://localhost:8124/codebreaker/v1
ENV VITE_API_BASE=$VITE_API_BASE

RUN npm run build

# Serve the static files with nginx
FROM nginx:1.27-alpine
COPY --from=build /app/dist /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
