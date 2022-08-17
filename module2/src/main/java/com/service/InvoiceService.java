package com.service;

import com.model.Customer;
import com.model.Electronics;
import com.model.Invoice;
import com.model.constants.Type;
import lombok.NonNull;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InvoiceService<T extends Electronics> {
    private final List<Invoice<T>> invoiceList;

    public InvoiceService(List<Invoice<T>> invoiceList) {
        this.invoiceList = invoiceList;
    }

    public long numberSoldElectronicsByCategory(@NonNull final String category) {
        return invoiceList.stream()
                .flatMap(x -> x.getElectronics().stream())
                .filter(x -> x.getClass().getSimpleName().equals(category))
                .count();
    }

    public Map<Customer, BigDecimal> minSumInvoice() {
        return invoiceList.stream()
                .min(Comparator.comparing(this::calculateTotalSumInvoice))
                .stream()
                .collect(Collectors.toMap(Invoice::getCustomer, this::calculateTotalSumInvoice));
    }

    public BigDecimal totalSumInvoices() {
        return invoiceList.stream()
                .flatMap(x -> x.getElectronics().stream())
                .map(Electronics::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public long numberInvoicesByType(@NonNull final Type type) {
        return invoiceList.stream()
                .filter(x -> x.getType().equals(type))
                .count();
    }

    public List<Invoice<T>> onlyOneTypeOfElectronic(@NonNull final String nameElectronic) {
        return invoiceList.stream()
                .filter(x -> x.getElectronics()
                        .stream()
                        .allMatch(y -> y.getClass().getSimpleName().equals(nameElectronic)))
                .collect(Collectors.toList());
    }

    public List<Invoice<T>> firstInvoices(@NonNull final int count) {
        return invoiceList.stream()
                .sorted(Comparator.comparing(Invoice::getCreated))
                .limit(count)
                .collect(Collectors.toList());
    }

    public List<Invoice<T>> changeTypeByAge(@NonNull final int age) {
        return invoiceList.stream()
                .filter(x -> x.getCustomer().getAge() < age)
                .peek(x -> x.setType(Type.LOW_AGE))
                .collect(Collectors.toList());
    }

    public List<Invoice<T>> sort() {
        return invoiceList.stream()
                .sorted(Comparator.comparing((Invoice<T> x) -> x.getCustomer().getAge())
                        .reversed()
                        .thenComparing(x -> x.getElectronics().size())
                        .thenComparing(this::calculateTotalSumInvoice))
                .collect(Collectors.toList());
    }

    private BigDecimal calculateTotalSumInvoice(Invoice<T> invoice) {
        return invoice.getElectronics().stream()
                .map(Electronics::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}