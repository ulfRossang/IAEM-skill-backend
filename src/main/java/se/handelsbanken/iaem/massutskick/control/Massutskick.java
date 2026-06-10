package se.handelsbanken.iaem.massutskick.control;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import se.handelsbanken.iaem.massutskick.entity.MassutskickEntity;
import se.handelsbanken.iaem.massutskick.entity.MassutskickInput;
import se.handelsbanken.iaem.massutskick.entity.MassutskickRow;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class Massutskick {

    public enum KlarmarkeraResult { NOT_FOUND, CONFLICT, OK }

    @PersistenceContext(unitName = "epatraktorPU")
    EntityManager em;

    public List<MassutskickRow> list() {
        return em.createQuery(
                "SELECT m FROM MassutskickEntity m ORDER BY m.meddId DESC",
                MassutskickEntity.class)
            .getResultList()
            .stream()
            .map(MassutskickRow::from)
            .toList();
    }

    public MassutskickRow create(MassutskickInput input) {
        var maxId = (Number) em.createNativeQuery(
                "SELECT COALESCE(MAX(CAST(medd_id AS INTEGER)), 300) FROM epatraktor.massutskick")
            .getSingleResult();
        var newId = String.valueOf(maxId.intValue() + 1);

        var e = new MassutskickEntity();
        e.setMeddId(newId);
        applyInput(e, input);
        e.setStatus("Under upplägg");
        em.persist(e);
        return MassutskickRow.from(e);
    }

    public Optional<MassutskickRow> update(String meddId, MassutskickInput input) {
        var e = em.find(MassutskickEntity.class, meddId);
        if (e == null) return Optional.empty();
        applyInput(e, input);
        return Optional.of(MassutskickRow.from(e));
    }

    public boolean delete(String meddId) {
        var e = em.find(MassutskickEntity.class, meddId);
        if (e == null) return false;
        em.remove(e);
        return true;
    }

    public KlarmarkeraResult klarmarkera(String meddId) {
        var e = em.find(MassutskickEntity.class, meddId);
        if (e == null) return KlarmarkeraResult.NOT_FOUND;
        if ("Skickad".equals(e.getStatus()) || "Klarmarkerad".equals(e.getStatus())) {
            return KlarmarkeraResult.CONFLICT;
        }
        e.setStatus("Klarmarkerad");
        return KlarmarkeraResult.OK;
    }

    void applyInput(MassutskickEntity e, MassutskickInput input) {
        e.setLand(input.land());
        e.setAvsandare(input.avsandare());
        e.setAmne(input.amne());
        e.setUtskicksdatum(input.utskicksdatum());
        e.setNotifieringskategori(input.notifieringskategori());
        e.setMeddelande(input.meddelande());
    }
}
