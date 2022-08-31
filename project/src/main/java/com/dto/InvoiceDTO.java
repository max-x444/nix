package com.dto;

import com.model.vehicle.Invoice;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class InvoiceDTO {
    private Invoice invoice;
    private BigDecimal totalPrice;

    public InvoiceDTO(Invoice invoice, BigDecimal totalPrice) {
        this.invoice = invoice;
        this.totalPrice = totalPrice;
    }

    public InvoiceDTO(Invoice invoice) {
        this.invoice = invoice;
    }
}
