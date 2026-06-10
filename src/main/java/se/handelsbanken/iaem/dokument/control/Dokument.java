package se.handelsbanken.iaem.dokument.control;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import se.handelsbanken.iaem.dokument.entity.DokumentEntity;
import se.handelsbanken.iaem.dokument.entity.DokumentRow;

import java.util.List;

@ApplicationScoped
public class Dokument {

    @PersistenceContext(unitName = "epatraktorPU")
    EntityManager em;

    public List<DokumentRow> list(String kundnr) {
        return em.createQuery(
                "SELECT d FROM DokumentEntity d WHERE d.kundnr = :kundnr",
                DokumentEntity.class)
            .setParameter("kundnr", kundnr)
            .getResultList()
            .stream()
            .map(DokumentRow::from)
            .toList();
    }
}
