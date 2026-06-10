package se.handelsbanken.iaem.kuvert.control;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import se.handelsbanken.iaem.kuvert.entity.KuvertEntity;
import se.handelsbanken.iaem.kuvert.entity.KuvertRow;

import java.util.Optional;

@ApplicationScoped
public class Kuvert {

    @PersistenceContext(unitName = "epatraktorPU")
    EntityManager em;

    public Optional<KuvertRow> find(String kuvertId) {
        var entity = em.find(KuvertEntity.class, kuvertId);
        if (entity == null) return Optional.empty();
        return Optional.of(KuvertRow.from(entity));
    }
}
