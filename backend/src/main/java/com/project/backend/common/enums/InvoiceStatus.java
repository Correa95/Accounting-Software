package com.project.backend.common.enums;

public enum InvoiceStatus {
    DRAFT,
    SENT,
    PAID,
    VOID
}
// note for invoice status behavior
// DRAFT  → Editable
// SENT   → Locked (AR recognized)
// PAID   → Locked
// VOID   → Locked (historical)
