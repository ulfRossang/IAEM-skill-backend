package se.handelsbanken.iaem.publicering.control;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import se.handelsbanken.iaem.publicering.entity.*;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class Publicering {

    @PersistenceContext(unitName = "epatraktorPU")
    EntityManager em;

    public List<InformationssambandRow> listInformationssamband() {
        return em.createQuery(
                "SELECT s FROM InformationssambandEntity s",
                InformationssambandEntity.class)
            .getResultList()
            .stream()
            .map(InformationssambandRow::from)
            .toList();
    }

    public InformationssambandRow createInformationssamband(InformationssambandRow input) {
        var maxSeq = (Number) em.createNativeQuery(
                "SELECT COALESCE(MAX(CAST(SUBSTRING(id FROM 5) AS INTEGER)), 0) FROM epatraktor.informationssamband")
            .getSingleResult();
        var newId = "SAM-" + String.format("%03d", maxSeq.intValue() + 1);

        var e = new InformationssambandEntity();
        e.setId(newId);
        e.setSystembeteckning(input.systembeteckning());
        e.setInformationsId(input.informationsId());
        e.setPubliceraAutomatiskt(input.publiceraAutomatiskt() ? 1 : 0);
        em.persist(e);
        return InformationssambandRow.from(e);
    }

    public Optional<InformationssambandRow> updateInformationssamband(String id, InformationssambandRow input) {
        var e = em.find(InformationssambandEntity.class, id);
        if (e == null) return Optional.empty();
        e.setSystembeteckning(input.systembeteckning());
        e.setInformationsId(input.informationsId());
        e.setPubliceraAutomatiskt(input.publiceraAutomatiskt() ? 1 : 0);
        return Optional.of(InformationssambandRow.from(e));
    }

    public boolean deleteInformationssamband(String id) {
        var e = em.find(InformationssambandEntity.class, id);
        if (e == null) return false;
        em.remove(e);
        return true;
    }

    public List<PubliceringJobbRow> listJobb() {
        return em.createQuery(
                "SELECT j FROM PubliceringJobbEntity j",
                PubliceringJobbEntity.class)
            .getResultList()
            .stream()
            .map(PubliceringJobbRow::from)
            .toList();
    }

    public boolean godkannJobb(String jobbId, boolean godkand) {
        var e = em.find(PubliceringJobbEntity.class, jobbId);
        if (e == null) return false;
        e.setStatus(godkand ? "Godkänd" : "Nekad");
        return true;
    }
}
