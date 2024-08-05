package com.demo.admissionportal.constants;

/**
 * The enum University transaction status.
 */
public enum UniversityTransactionStatus {
    /**
     * The Paid.
     */
    PAID("Thanh toán thành công"),
    /**
     * Canceled university transaction status.
     */
    CANCELED("Đã huỷ"),
    /**
     * The Failed.
     */
    FAILED("Thanh toa thất bại"),
    /**
     * The Pending.
     */
    PENDING("Đang chờ thanh toán");
    /**
     * The Name.
     */
    public String name;

    UniversityTransactionStatus(String name) {
        this.name = name;
    }
}
