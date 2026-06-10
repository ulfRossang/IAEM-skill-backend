package se.handelsbanken.iaem.debiteringsuppgifter.entity;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public record DebiteringsuppgiftRow(
        String produktid,
        String meddelandeid,
        String systembeteckning,
        String antsKodInternet,
        String antsKodEjInternet,
        String resultatstalle) {

    public static DebiteringsuppgiftRow from(DebiteringsuppgiftEntity e) {
        return new DebiteringsuppgiftRow(
                e.getProduktid(),
                e.getMeddelandeid(),
                e.getSystembeteckning(),
                e.getAntsKodInternet(),
                e.getAntsKodEjInternet(),
                e.getResultatstalle());
    }

    public JsonObject toJSON() {
        return Json.createObjectBuilder()
                .add("produktid", produktid != null ? produktid : "")
                .add("meddelandeid", meddelandeid != null ? meddelandeid : "")
                .add("systembeteckning", systembeteckning != null ? systembeteckning : "")
                .add("antsKodInternet", antsKodInternet != null ? antsKodInternet : "")
                .add("antsKodEjInternet", antsKodEjInternet != null ? antsKodEjInternet : "")
                .add("resultatstalle", resultatstalle != null ? resultatstalle : "")
                .build();
    }
}
