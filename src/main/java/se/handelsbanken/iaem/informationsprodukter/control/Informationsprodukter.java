package se.handelsbanken.iaem.informationsprodukter.control;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import se.handelsbanken.iaem.informationsprodukter.entity.*;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class Informationsprodukter {

    @PersistenceContext(unitName = "epatraktorPU")
    EntityManager em;

    public List<InformationsproduktRow> list(String land) {
        List<InformationsproduktEntity> entities;
        if (land == null || land.isBlank()) {
            entities = em.createQuery(
                    "SELECT p FROM InformationsproduktEntity p",
                    InformationsproduktEntity.class)
                .getResultList();
        } else {
            entities = em.createQuery(
                    "SELECT p FROM InformationsproduktEntity p WHERE LOWER(p.land) = LOWER(:land)",
                    InformationsproduktEntity.class)
                .setParameter("land", land)
                .getResultList();
        }
        return entities.stream().map(InformationsproduktRow::from).toList();
    }

    public InformationsproduktRow create(InformationsproduktInput input) {
        var maxId = (Number) em.createNativeQuery(
                "SELECT COALESCE(MAX(CAST(id AS INTEGER)), 33000) FROM epatraktor.informationsprodukt")
            .getSingleResult();
        var newId = String.valueOf(maxId.intValue() + 1);

        var e = new InformationsproduktEntity();
        e.setId(newId);
        applyInput(e, input);
        em.persist(e);
        return InformationsproduktRow.from(e);
    }

    public Optional<InformationsproduktRow> update(String id, InformationsproduktInput input) {
        var e = em.find(InformationsproduktEntity.class, id);
        if (e == null) return Optional.empty();
        applyInput(e, input);
        return Optional.of(InformationsproduktRow.from(e));
    }

    void applyInput(InformationsproduktEntity e, InformationsproduktInput input) {
        e.setNamn(input.namn());
        e.setLand(input.land());
        e.setStatus(input.status());
        e.setNotifieringskategori(input.notifieringskategori());
        e.setSystembeteckning(input.systembeteckning());
        e.setInsynsskyddad(input.insynsskyddad() ? 1 : 0);
        if (input.defaultkanaler() != null) {
            e.setDefaultPapper(input.defaultkanaler().papper() ? 1 : 0);
            e.setDefaultInternet(input.defaultkanaler().internet() ? 1 : 0);
        }
        if (input.tillåtnaKanaler() != null) {
            e.setTilllåtnaPapper(input.tillåtnaKanaler().papper() ? 1 : 0);
            e.setTilllåtnaInternet(input.tillåtnaKanaler().internet() ? 1 : 0);
        }
        if (input.obligatoriskaKanaler() != null) {
            e.setObligatoriskaPapper(input.obligatoriskaKanaler().papper() ? 1 : 0);
            e.setObligatoriskaInternet(input.obligatoriskaKanaler().internet() ? 1 : 0);
        }
        e.setAvgiftsidPapper(input.avgiftsidPapper());
        e.setAvgiftsidInternet(input.avgiftsidInternet());
        e.setVisningstidEArkivMan(input.visningstidEArkivMan());
        e.setLagringstidDiskMan(input.lagringstidDiskMan());
        e.setMeddelandetext(input.meddelandetext());
    }
}
