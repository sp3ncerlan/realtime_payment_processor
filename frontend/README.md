Frontend quick start

- Install dependencies:
  - npm ci

- Run in development (Vite dev server on port 5173):
  - npm run dev

- Build for production:
  - npm run build

- API base URL (local dev):
  - Backend: http://localhost:8080
  - API base path: /api
  - Example endpoint: http://localhost:8080/api/customers

- Notes:
  - Vite default dev port is 5173 — frontend origin for CORS during development: http://localhost:5173
  - If you need to connect the frontend to a different backend host/port, update the service URL in `frontend/services/*` or configure a runtime environment variable.

