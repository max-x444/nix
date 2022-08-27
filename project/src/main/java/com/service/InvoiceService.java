package com.service;

import com.model.vehicle.Invoice;
import com.repository.jdbc.DBInvoiceRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class InvoiceService {
    private static InvoiceService instance;
    private static DBInvoiceRepository dbInvoiceRepository;

    private InvoiceService() {
    }

    public static InvoiceService getInstance() {
        if (instance == null) {
            dbInvoiceRepository = DBInvoiceRepository.getInstance();
            instance = new InvoiceService();
        }
        return instance;
    }

    public Optional<Invoice> findById(String id) {
        return dbInvoiceRepository.findById(id);
    }

    public List<Invoice> getAll() {
        return dbInvoiceRepository.getAll();
    }

    public boolean save(Invoice invoice) {
        return dbInvoiceRepository.save(invoice);
    }

    public boolean save(List<Invoice> invoiceList) {
        return dbInvoiceRepository.save(invoiceList);
    }

    public boolean update(Invoice invoice) {
        return dbInvoiceRepository.update(invoice);
    }

    public boolean delete(String id) {
        return dbInvoiceRepository.delete(id);
    }

    public List<Invoice> delete(Invoice invoice) {
        return dbInvoiceRepository.delete(invoice);
    }

    public Map<Invoice, BigDecimal> groupByTotalPrice() {
        return dbInvoiceRepository.groupByTotalPrice();
    }

    public List<Invoice> getInvoiceMoreExpensiveThanAmount(BigDecimal amount) {
        return dbInvoiceRepository.getInvoiceMoreExpensiveThanAmount(amount);
    }

    public int getTotalCountInvoices() {
        return dbInvoiceRepository.getTotalCountInvoices();
    }
}
