# 💸 Realtime Payment Processor

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-316192?style=for-the-badge&logo=postgresql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2CA5E0?style=for-the-badge&logo=docker&logoColor=white)

A high-concurrency, full-stack payment processing engine designed to handle real-time financial transactions with strict **ACID guarantees**. This system mimics core banking functionalities, ensuring data integrity during simultaneous fund transfers using **optimistic locking** and transactional isolation.

---

## 📖 Overview

The goal of this project was to engineer a robust payment gateway capable of processing user-to-user transactions without race conditions (e.g., the "Double Spending" problem). It features a responsive **React** frontend for user interaction and a scalable **Spring Boot** backend containerized via **Docker**.

### Key Features
* **Real-Time Transactions:** Instant fund transfers between users with sub-second latency.
* **Concurrency Control:** Implemented database-level locking strategies to prevent race conditions during simultaneous transaction requests.
* **Transaction History:** Immutable ledger of all incoming and outgoing payments.
* **Visual Dashboard:** Interactive frontend built with ReactJS to visualize wallet balances and transfer statuses.
* **Containerized Deployment:** Fully Dockerized application for one-command setup.
