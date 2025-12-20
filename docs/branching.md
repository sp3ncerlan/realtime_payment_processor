Branching policy for realtime_payment_processor

Overview

- Protect `main` (production) branch. Optionally use `develop` for integration.
- Prefix feature branches with the subsystem (backend/frontend) for clarity.

Naming conventions

- Main branches
  - `main` — protected, production-ready
  - `develop` — integration (optional)

- Feature branches
  - backend features: `feature/backend/<short-desc>` or `backend/feature/<short-desc>`
    - examples: `feature/backend/payment-validation`, `backend/feature/db-transaction-retries`
  - frontend features: `feature/frontend/<short-desc>` or `frontend/feature/<short-desc>`
    - examples: `feature/frontend/send-payment-ui`, `frontend/feature/customer-dropdown`

- Hotfixes and releases
  - hotfix: `hotfix/<scope>-<short-desc>` (e.g., `hotfix/backend-db-connection`) 
  - release: `release/v1.2.0`

Guidelines

- Keep branch names short but descriptive (max ~4 words).
- Include the subsystem to make cross-repo/mono-repo work easier.
- Open PRs against `develop` (if you use it) or `main` if the change is small and hotfix-like.
- Use PR templates and require at least one reviewer for `main`.

Enforcement

- Use GitHub Branch Protection Rules to require PR reviews and passing CI before merging to `main`/`develop`.
- Optionally add a simple CI check that warns when branch names don't match your pattern.

Example workflow

1. Create a branch: `git checkout -b feature/frontend/send-payment-ui`
2. Work and commit: `git commit -m "feat: add send payment card UI"`
3. Push and open a PR: `git push origin feature/frontend/send-payment-ui`
4. Target PR to `develop` or `main` as your branching policy dictates.


