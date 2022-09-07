package com.service;

import com.model.vehicle.Invoice;
import com.repository.mongo.JSONInvoiceRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InvoiceService {
    private static InvoiceService instance;
    private static JSONInvoiceRepository repository;

    private InvoiceService() {
        repository = JSONInvoiceRepository.getInstance();
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

    public boolean save(List<Invoice> invoiceList) {
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


    public Map<BigDecimal, Long> groupByTotalPrice() {
        return repository.groupByTotalPrice();
    }

    public List<Invoice> getInvoiceMoreExpensiveThanAmount(BigDecimal amount) {
        return repository.getInvoiceMoreExpensiveThanAmount(amount);
    }

    public long getTotalCountInvoices() {
        return repository.getTotalCountInvoices();
    }
}