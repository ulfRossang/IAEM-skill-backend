package se.handelsbanken.iaem.utskick.control;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import se.handelsbanken.iaem.utskick.entity.*;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class Utskick {

    @PersistenceContext(unitName = "epatraktorPU")
    EntityManager em;

    public List<UtskickRow> list(String kundnr) {
        return em.createQuery(
                "SELECT u FROM UtskickEntity u WHERE u.kundnr = :kundnr ORDER BY u.datum DESC",
                UtskickEntity.class)
            .setParameter("kundnr", kundnr)
            .getResultList()
            .stream()
            .map(UtskickRow::from)
            .toList();
    }

    public Optional<UtskickRow> find(String kundnr, String utskickId) {
        var list = em.createQuery(
                "SELECT u FROM UtskickEntity u WHERE u.kundnr = :kundnr AND u.utskickId = :id",
                UtskickEntity.class)
            .setParameter("kundnr", kundnr)
            .setParameter("id", utskickId)
            .getResultList();
        if (list.isEmpty()) return Optional.empty();
        return Optional.of(UtskickRow.from(list.get(0)));
    }

    public List<UtskickInstallningRow> listInstallningar(String kundnr) {
        return em.createQuery(
                "SELECT u FROM UtskickInstallningEntity u WHERE u.kundnr = :kundnr",
                UtskickInstallningEntity.class)
            .setParameter("kundnr", kundnr)
            .getResultList()
            .stream()
            .map(UtskickInstallningRow::from)
            .toList();
    }

    public void saveInstallningar(String kundnr, List<UtskickInstallningRow> settings) {
        em.createQuery("DELETE FROM UtskickInstallningEntity u WHERE u.kundnr = :kundnr")
            .setParameter("kundnr", kundnr)
            .executeUpdate();
        for (var s : settings) {
            var e = new UtskickInstallningEntity();
            e.setKundnr(kundnr);
            e.setKategori(s.kategori());
            e.setAvser(s.avser());
            e.setForbindelse(s.forbindelse());
            e.setPapper(s.papper() ? 1 : 0);
            e.setInternet(s.internet() ? 1 : 0);
            em.persist(e);
        }
    }
}
