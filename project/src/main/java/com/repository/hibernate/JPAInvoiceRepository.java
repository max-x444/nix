package com.repository.hibernate;

import com.config.HibernateFactoryUtil;
import com.dto.InvoiceDTO;
import com.model.vehicle.Invoice;
import com.model.vehicle.Vehicle;
import lombok.NonNull;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class JPAInvoiceRepository {
    private static JPAInvoiceRepository instance;
    private static EntityManager entityManager;

    private JPAInvoiceRepository() {
        entityManager = HibernateFactoryUtil.getEntityManager();
    }

    public static JPAInvoiceRepository getInstance() {
        if (instance == null) {
            instance = new JPAInvoiceRepository();
        }
        return instance;
    }

    public Optional<Invoice> findById(String id) {
        final Invoice invoice = entityManager.find(Invoice.class, id);
        return (invoice != null) ? Optional.of(invoice) : Optional.empty();
    }

    public List<Invoice> getAll() {
        return entityManager.createQuery("SELECT i FROM Invoice i", Invoice.class).getResultList();
    }

    public boolean save(Invoice invoice) {
        if (!checkIfVehicleBought(invoice)) {
            entityManager.getTransaction().begin();
            entityManager.merge(invoice);
            entityManager.flush();
            entityManager.clear();
            entityManager.getTransaction().commit();
            return true;
        }
        return false;
    }

    public boolean save(Set<Invoice> invoiceSet) {
        if (invoiceSet.isEmpty()) {
            throw new IllegalArgumentException("Set must not be empty");
        }
        invoiceSet.forEach(this::save);
        return true;
    }

    public boolean update(Invoice invoice) {
        if (invoice == null) {
            throw new IllegalArgumentException("Invoice must not be null");
        }
        findById(invoice.getId()).ifPresent(target -> {
            target.setCreated(invoice.getCreated());
            save(target);
        });
        return true;
    }

    public boolean delete(String id) {
        if (id.isEmpty()) {
            throw new IllegalArgumentException("Id must not be empty");
        }
        findById(id).ifPresent(invoice -> {
            entityManager.getTransaction().begin();
            entityManager.remove(invoice);
            entityManager.flush();
            entityManager.getTransaction().commit();
        });
        return true;
    }

    public List<Invoice> delete(Invoice invoice) {
        if (invoice == null) {
            throw new IllegalArgumentException("Invoice must not be null");
        }
        delete(invoice.getId());
        return getAll();
    }

    public Map<Invoice, BigDecimal> groupByInvoiceDTO() {
        return entityManager.createQuery("""
                        SELECT NEW com.dto.InvoiceDTO(v.invoice, SUM(v.price * v.count) AS total_price)
                        FROM Vehicle v
                        GROUP BY v.invoice.id
                        ORDER BY total_price DESC""", InvoiceDTO.class)
                .getResultList()
                .stream()
                .collect(Collectors.toMap(
                        InvoiceDTO::getInvoice,
                        InvoiceDTO::getTotalPrice,
                        (v1, v2) -> v1, LinkedHashMap::new));
    }

    public List<Invoice> getInvoiceMoreExpensiveThanAmountDTO(@NonNull final BigDecimal amount) {
        return entityManager.createQuery("""
                        SELECT NEW com.dto.InvoiceDTO(v.invoice)
                        FROM Vehicle v
                        GROUP BY v.invoice.id
                        HAVING SUM(v.price * v.count) > :amount
                        ORDER BY SUM(v.price * v.count) DESC""", InvoiceDTO.class).setParameter("amount", amount)
                .getResultList()
                .stream()
                .map(InvoiceDTO::getInvoice)
                .collect(Collectors.toList());
    }

    public int getTotalCountInvoices() {
        return Integer.parseInt(entityManager.createQuery("SELECT COUNT(i.id) FROM Invoice i")
                .getSingleResult().toString());
    }

    public Map<Invoice, BigDecimal> groupByInvoiceCriteria() {
        final Map<Invoice, BigDecimal> map = new LinkedHashMap<>();
        final CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Object[]> criteriaQuery = criteriaBuilder.createQuery(Object[].class);
        final Root<Vehicle> root = criteriaQuery.from(Vehicle.class);
        criteriaQuery.multiselect(
                root.get("invoice").get("id"),
                criteriaBuilder.sum(criteriaBuilder.prod(root.get("count"), root.get("price"))));
        criteriaQuery.groupBy(root.get("invoice").get("id"));
        criteriaQuery.orderBy(criteriaBuilder.desc(
                criteriaBuilder.sum(criteriaBuilder.prod(root.get("count"), root.get("price")))));
        fillingMap(map, entityManager.createQuery(criteriaQuery).getResultList());
        return map;
    }

    private void fillingMap(@NonNull final Map<Invoice, BigDecimal> map, @NonNull final List<Object[]> list) {
        Optional<Invoice> invoiceOptional;
        BigDecimal price;
        for (Object[] objects : list) {
            invoiceOptional = findById(objects[0].toString());
            price = BigDecimal.valueOf(Double.parseDouble(String.valueOf(objects[1])));
            if (invoiceOptional.isPresent()) {
                map.put(invoiceOptional.get(), price);
            }
        }
    }

    private boolean checkIfVehicleBought(@NonNull final Invoice invoice) {
        return invoice.getVehicles().stream()
                .map(this::getQuery)
                .anyMatch(y -> y != 0);
    }

    private int getQuery(@NonNull final Vehicle vehicle) {
        return Integer.parseInt(entityManager.createQuery("""
                        SELECT COUNT(v.invoice.id)
                        FROM Vehicle v
                        WHERE v.id = :vehicle_id AND v.invoice.id IS NOT NULL""")
                .setParameter("vehicle_id", vehicle.getId()).getSingleResult().toString());
    }
}