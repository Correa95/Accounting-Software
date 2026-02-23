# ─────────────────────────────────────

# JOURNAL ENTRIES

# ─────────────────────────────────────

GET http://localhost:8080/companies/1/journalEntries
GET http://localhost:8080/companies/1/journalEntries/1
POST http://localhost:8080/companies/1/journalEntries
PUT http://localhost:8080/companies/1/journalEntries/1
DELETE http://localhost:8080/companies/1/journalEntries/1
POST http://localhost:8080/companies/1/journalEntries/1/post
POST http://localhost:8080/companies/1/journalEntries/1/reverse

# ─────────────────────────────────────

# JOURNAL ENTRY LINES

# ─────────────────────────────────────

GET http://localhost:8080/companies/1/journalEntries/1/lines
POST http://localhost:8080/companies/1/journalEntries/1/lines
PUT http://localhost:8080/companies/1/journalEntries/1/lines/1
DELETE http://localhost:8080/companies/1/journalEntries/1/lines/1

# ─────────────────────────────────────

# TRIAL BALANCE

# ─────────────────────────────────────

GET http://localhost:8080/companies/1/trial-balance
GET http://localhost:8080/companies/1/trial-balance?startDate=2025-01-01&endDate=2025-12-31

# ─────────────────────────────────────

# INVOICES

# ─────────────────────────────────────

GET http://localhost:8080/companies/1/invoices
GET http://localhost:8080/companies/1/invoices/1
POST http://localhost:8080/companies/1/invoices/customers/1
PUT http://localhost:8080/companies/1/invoices/1
DELETE http://localhost:8080/companies/1/invoices/1
POST http://localhost:8080/companies/1/invoices/1/send
POST http://localhost:8080/companies/1/invoices/1/void
POST http://localhost:8080/companies/1/invoices/1/pay

# ─────────────────────────────────────

# PAYMENTS

# ─────────────────────────────────────

GET http://localhost:8080/api/payments/invoice-summary?invoiceNumber=INV-001
POST http://localhost:8080/api/payments/initiate?invoiceNumber=INV-001
POST http://localhost:8080/api/payments/cancel?invoiceNumber=INV-001
POST http://localhost:8080/api/payments/refund/full?invoiceNumber=INV-001
POST http://localhost:8080/api/payments/refund/partial
POST http://localhost:8080/api/payments/webhook
GET http://localhost:8080/api/payments/by-invoice/1
GET http://localhost:8080/api/payments/by-customer/1
GET http://localhost:8080/api/payments/by-customer/1/status?status=PAID
GET http://localhost:8080/api/payments/by-status?status=PAID
GET http://localhost:8080/api/payments/by-currency?currency=USD
GET http://localhost:8080/api/payments/by-currency/status?currency=USD&status=PAID
GET http://localhost:8080/api/payments/intent/pi_abc123
