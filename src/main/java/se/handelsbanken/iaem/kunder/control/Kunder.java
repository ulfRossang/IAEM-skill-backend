package se.handelsbanken.iaem.kunder.control;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import se.handelsbanken.iaem.kunder.entity.Kund;
import se.handelsbanken.iaem.kunder.entity.KundEntity;

import java.util.Optional;

@ApplicationScoped
public class Kunder {

    @PersistenceContext(unitName = "epatraktorPU")
    EntityManager em;

    public Optional<Kund> find(String kundnr) {
        var entity = em.find(KundEntity.class, kundnr);
        if (entity == null) return Optional.empty();
        return Optional.of(Kund.from(entity));
    }
}
