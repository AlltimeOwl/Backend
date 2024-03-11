package com.owl.payrit.domain.promissorypaper.entity;

public enum PaperRole {
    CREDITOR, DEBTOR;

    public PaperRole getReverse() {
        return this.equals(CREDITOR) ? DEBTOR : CREDITOR;
    }
}