package org.geodispatch.services;

import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.geodispatch.entity.Vehicle;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class VehicleService {

    @PersistenceContext(unitName = "geoPU")
    private EntityManager em;

    public Vehicle create(String registrationPlate, String model, String manufacturer) {
        if (existsByPlate(registrationPlate)) {
            throw new IllegalArgumentException("Vehicle with plate '" + registrationPlate + "' already exists");
        }
        Vehicle v = new Vehicle();
        v.setRegistrationPlate(registrationPlate.trim());
        v.setModel(model);
        v.setManufacturer(manufacturer);
        em.persist(v);
        return v;
    }

    public Vehicle update(long id, String registrationPlate, String model, String manufacturer) {
        Vehicle v = getOrThrow(id);
        if (registrationPlate != null && !registrationPlate.isBlank()
                && !registrationPlate.equals(v.getRegistrationPlate())) {
            if (existsByPlate(registrationPlate)) {
                throw new IllegalArgumentException("Vehicle plate already used: " + registrationPlate);
            }
            v.setRegistrationPlate(registrationPlate.trim());
        }
        if (model != null) v.setModel(model);
        if (manufacturer != null) v.setManufacturer(manufacturer);
        return v;
    }

    public void delete(long id) {
        Vehicle v = getOrThrow(id);
        em.remove(v);
    }

    public Optional<Vehicle> findById(long id) {
        return Optional.ofNullable(em.find(Vehicle.class, id));
    }

    public Optional<Vehicle> findByPlate(String plate) {
        List<Vehicle> list = em.createQuery(
                "select v from Vehicle v where lower(v.registrationPlate)=lower(:p)", Vehicle.class)
            .setParameter("p", plate.trim())
            .setMaxResults(1)
            .getResultList();
        return list.stream().findFirst();
    }

    public boolean existsByPlate(String plate) {
        Long cnt = em.createQuery(
                "select count(v) from Vehicle v where lower(v.registrationPlate)=lower(:p)", Long.class)
            .setParameter("p", plate.trim())
            .getSingleResult();
        return cnt > 0;
    }

    public List<Vehicle> list(int offset, int limit) {
        TypedQuery<Vehicle> q = em.createQuery("select v from Vehicle v order by v.registrationPlate", Vehicle.class);
        if (offset > 0) q.setFirstResult(offset);
        if (limit > 0) q.setMaxResults(limit);
        return q.getResultList();
    }

    public long countAll() {
        return em.createQuery("select count(v) from Vehicle v", Long.class).getSingleResult();
    }

    private Vehicle getOrThrow(long id) {
        Vehicle v = em.find(Vehicle.class, id);
        if (v == null) throw new NoSuchElementException("Vehicle not found: id=" + id);
        return v;
    }
}
