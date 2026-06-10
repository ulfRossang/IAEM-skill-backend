package se.handelsbanken.iaem.debiteringsuppgifter.control;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import se.handelsbanken.iaem.debiteringsuppgifter.entity.DebiteringsuppgiftEntity;
import se.handelsbanken.iaem.debiteringsuppgifter.entity.DebiteringsuppgiftInput;
import se.handelsbanken.iaem.debiteringsuppgifter.entity.DebiteringsuppgiftRow;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class Debiteringsuppgifter {

    @PersistenceContext(unitName = "epatraktorPU")
    EntityManager em;

    public List<DebiteringsuppgiftRow> list() {
        return em.createQuery(
                "SELECT d FROM DebiteringsuppgiftEntity d",
                DebiteringsuppgiftEntity.class)
            .getResultList()
            .stream()
            .map(DebiteringsuppgiftRow::from)
            .toList();
    }

    public DebiteringsuppgiftRow create(DebiteringsuppgiftInput input) {
        var e = new DebiteringsuppgiftEntity();
        e.setProduktid(input.produktid());
        e.setMeddelandeid(input.meddelandeid());
        e.setSystembeteckning(input.systembeteckning());
        e.setAntsKodInternet(input.antsKodInternet());
        e.setAntsKodEjInternet(input.antsKodEjInternet());
        e.setResultatstalle(input.resultatstalle());
        em.persist(e);
        return DebiteringsuppgiftRow.from(e);
    }

    public Optional<DebiteringsuppgiftRow> update(String produktid, DebiteringsuppgiftInput input) {
        var e = em.find(DebiteringsuppgiftEntity.class, produktid);
        if (e == null) return Optional.empty();
        e.setMeddelandeid(input.meddelandeid());
        e.setSystembeteckning(input.systembeteckning());
        e.setAntsKodInternet(input.antsKodInternet());
        e.setAntsKodEjInternet(input.antsKodEjInternet());
        e.setResultatstalle(input.resultatstalle());
        return Optional.of(DebiteringsuppgiftRow.from(e));
    }

    public boolean delete(String produktid) {
        var e = em.find(DebiteringsuppgiftEntity.class, produktid);
        if (e == null) return false;
        em.remove(e);
        return true;
    }
}
