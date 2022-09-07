package com.service;

import com.model.vehicle.Invoice;
import com.repository.hibernate.JPAInvoiceRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class InvoiceService {
    private static InvoiceService instance;
    private static JPAInvoiceRepository repository;

    private InvoiceService() {
        repository = JPAInvoiceRepository.getInstance();
    }

    public static InvoiceService getInstance() {
        if (instance == null) {
            instance = new InvoiceService();
        }
        return instance;
    }

    public Optional<Invoice> findById(String id) {
        return repository.findById(id);
    }

    public List<Invoice> getAll() {
        return repository.getAll();
    }

    public boolean save(Invoice invoice) {
        return repository.save(invoice);
    }

    public boolean save(Set<Invoice> invoiceList) {
        return repository.save(invoiceList);
    }

    public boolean update(Invoice invoice) {
        return repository.update(invoice);
    }

    public boolean delete(String id) {
        return repository.delete(id);
    }

    public List<Invoice> delete(Invoice invoice) {
        return repository.delete(invoice);
    }

    public Map<Invoice, BigDecimal> groupByInvoiceDTO() {
        return repository.groupByInvoiceDTO();
    }

    public List<Invoice> getInvoiceMoreExpensiveThanAmountDTO(BigDecimal amount) {
        return repository.getInvoiceMoreExpensiveThanAmountDTO(amount);
    }

    public long getTotalCountInvoices() {
        return repository.getTotalCountInvoices();
    }
}