CI/CD setup for realtime_payment_processor

Required GitHub repository secrets

- DOCKERHUB_USERNAME — your Docker Hub username
- DOCKERHUB_TOKEN — a Docker Hub access token (or password)
- (Optional) SSH_PRIVATE_KEY — private key to deploy server
- (Optional) DEPLOY_HOST — host to SSH to for deploy
- (Optional) DEPLOY_USER — username for SSH deploy

What the workflow does (file: `.github/workflows/ci-cd.yml`)

- Runs on push/pull_request to `main`.
- Backend job:
  - Sets up JDK 17
  - Runs `./mvnw -B -DskipTests=false clean package`
  - Builds a backend Docker image (local tag)

- Frontend job:
  - Sets up Node 20
  - Runs `npm ci` and `npm run build`
  - Builds a frontend Docker image (local tag)

- docker-publish job (runs only on `develop`):
  - Logs into Docker Hub using `DOCKERHUB_USERNAME` and `DOCKERHUB_TOKEN`
  - Builds and pushes the backend and frontend images to Docker Hub
  - Optionally SSHs to your `DEPLOY_HOST` and runs `docker compose up -d` on the server

How to use locally

- Start full stack locally (will build images if not present):

  docker compose up --build

- Stop and remove

  docker compose down --volumes

Notes & troubleshooting

- The backend `Dockerfile` builds a jar named `app.jar` inside the image. If your Maven build produces a different final name, update `backend/Dockerfile` or set `-DfinalName=app` in the maven args.
- The repository currently does not include the complete Maven wrapper files (the `.mvn/wrapper` directory). CI and the Docker `builder` stage use the system Maven bundled with the `maven` base image. If you want to use the Maven wrapper locally, recreate the wrapper by running `mvn -N io.takari:maven:wrapper` in the `backend` directory and commit the `.mvn/wrapper` files.
- The `docker-publish` step only runs on the `develop` branch — push to `develop` or open a PR that merges into `develop` to trigger image publishing.
