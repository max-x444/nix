package com.repository;

import com.model.annotations.MySingleton;
import com.model.vehicle.Auto;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@MySingleton
public class AutoRepository implements CrudRepository<Auto> {
    private static AutoRepository instance;
    private final List<Auto> autos;

    private AutoRepository() {
        this.autos = new LinkedList<>();
    }

    public static AutoRepository getInstance() {
        if (instance == null) {
            instance = new AutoRepository();
        }
        return instance;
    }

    @Override
    public Optional<Auto> findById(String id) {
        for (Auto auto : autos) {
            if (auto.getId().equals(id)) {
                return Optional.of(auto);
            }
        }
        return Optional.empty();
    }

    @Override
    public List<Auto> getAll() {
        return autos;
    }

    @Override
    public boolean create(Auto auto) {
        return autos.add(auto);
    }

    @Override
    public boolean create(List<Auto> auto) {
        return autos.addAll(auto);
    }

    @Override
    public boolean update(Auto auto) {
        final Optional<Auto> founded = findById(auto.getId());
        if (founded.isPresent()) {
            AutoCopy.copy(auto, founded.get());
            return true;
        }
        return false;
    }

    @Override
    public boolean delete(String id) {
        final Iterator<Auto> iterator = autos.iterator();
        while (iterator.hasNext()) {
            final Auto auto = iterator.next();
            if (auto.getId().equals(id)) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }

    @Override
    public List<Auto> delete(Auto auto) {
        autos.remove(auto);
        return autos;
    }

    private static class AutoCopy {
        static void copy(final Auto from, final Auto to) {
            to.setManufacturer(from.getManufacturer());
            to.setModel(from.getModel());
            to.setBodyType(from.getBodyType());
            to.setPrice(from.getPrice());
        }
    }
}
