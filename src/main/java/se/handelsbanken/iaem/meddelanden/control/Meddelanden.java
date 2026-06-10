package se.handelsbanken.iaem.meddelanden.control;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import se.handelsbanken.iaem.meddelanden.entity.Bilaga;
import se.handelsbanken.iaem.meddelanden.entity.BilagaRow;
import se.handelsbanken.iaem.meddelanden.entity.MeddelandeHeader;
import se.handelsbanken.iaem.meddelanden.entity.MeddelandeRow;
import se.handelsbanken.iaem.meddelanden.entity.Meddelandetext;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class Meddelanden {

    @PersistenceContext(unitName = "epatraktorPU")
    EntityManager em;

    @SuppressWarnings("unchecked")
    public List<MeddelandeRow> list(String kundnr) {
        var headers = em.createQuery(
                "SELECT m FROM MeddelandeHeader m WHERE m.kundnummer = :kundnr ORDER BY m.skapdatum DESC",
                MeddelandeHeader.class)
            .setParameter("kundnr", kundnr)
            .getResultList();
        return headers.stream()
                .map(this::toRow)
                .toList();
    }

    public Optional<MeddelandeRow> find(String kundnr, String meddId) {
        var list = em.createQuery(
                "SELECT m FROM MeddelandeHeader m WHERE m.kundnummer = :kundnr AND m.persmeddid = :id",
                MeddelandeHeader.class)
            .setParameter("kundnr", kundnr)
            .setParameter("id", meddId)
            .getResultList();
        if (list.isEmpty()) return Optional.empty();
        return Optional.of(toRow(list.get(0)));
    }

    MeddelandeRow toRow(MeddelandeHeader h) {
        Meddelandetext txt = h.getMeddelandetext();
        var innehall = txt != null ? txt.getBrodtext() : "";
        var bilagor = loadBilagor(h.getPersmeddid() != null ? h.getPersmeddid().trim() : null);
        return MeddelandeRow.from(h, innehall, bilagor);
    }

    @SuppressWarnings("unchecked")
    List<BilagaRow> loadBilagor(String meddId) {
        if (meddId == null) return List.of();
        var entities = em.createQuery(
                "SELECT b FROM Bilaga b WHERE b.meddelandeid = :meddId",
                Bilaga.class)
            .setParameter("meddId", meddId)
            .getResultList();
        return entities.stream()
                .map(b -> new BilagaRow(
                        b.getFilnamn(),
                        b.getFilstorlek() != null ? b.getFilstorlek() : 0,
                        "/api/v1/files/" + b.getFilnamn()))
                .toList();
    }
}
