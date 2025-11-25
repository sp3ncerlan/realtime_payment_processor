-- V1__init_schema.sql

-- Enable pgcrypto so gen_random_uuid() works
CREATE EXTENSION IF NOT EXISTS pgcrypto;

-- Customers table
CREATE TABLE IF NOT EXISTS customers (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    name text NOT NULL,
    email text UNIQUE NOT NULL,
    created_at timestamptz NOT NULL DEFAULT now()
);

-- Accounts table
CREATE TABLE IF NOT EXISTS accounts (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    customer_id uuid NOT NULL REFERENCES customers(id) ON DELETE CASCADE,
    account_number text NOT NULL UNIQUE,
    account_type text NOT NULL,
    currency text NOT NULL,
    balance numeric(19,4) NOT NULL DEFAULT 0,
    created_at timestamptz NOT NULL DEFAULT now()
);

-- Payment status enum
CREATE TYPE payment_status AS ENUM ('PENDING', 'COMPLETED', 'FAILED');

-- Payments table
CREATE TABLE IF NOT EXISTS payments (
    id uuid PRIMARY KEY DEFAULT gen_random_uuid(),
    source_account_id uuid NOT NULL REFERENCES accounts(id),
    destination_account_id uuid NOT NULL REFERENCES accounts(id),
    amount numeric(19,4) NOT NULL CHECK (amount > 0),
    currency text NOT NULL,
    status payment_status NOT NULL DEFAULT 'PENDING',
    created_at timestamptz NOT NULL DEFAULT now()
);

-- Useful indexes for performance
CREATE INDEX IF NOT EXISTS idx_accounts_customer_id ON accounts(customer_id);
CREATE INDEX IF NOT EXISTS idx_payments_source ON payments(source_account_id);
CREATE INDEX IF NOT EXISTS idx_payments_destination ON payments(destination_account_id);
CREATE INDEX IF NOT EXISTS idx_payments_status ON payments(status);