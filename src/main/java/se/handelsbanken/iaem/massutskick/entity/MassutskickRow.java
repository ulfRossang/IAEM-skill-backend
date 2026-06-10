package se.handelsbanken.iaem.massutskick.entity;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public record MassutskickRow(
        String meddId,
        String land,
        String avsandare,
        String amne,
        String utskicksdatum,
        String notifieringskategori,
        String meddelande,
        String status) {

    public static MassutskickRow from(MassutskickEntity e) {
        return new MassutskickRow(
                e.getMeddId(),
                e.getLand(),
                e.getAvsandare(),
                e.getAmne(),
                e.getUtskicksdatum(),
                e.getNotifieringskategori(),
                e.getMeddelande(),
                e.getStatus());
    }

    public JsonObject toJSON() {
        return Json.createObjectBuilder()
                .add("meddId", meddId != null ? meddId : "")
                .add("land", land != null ? land : "")
                .add("avsandare", avsandare != null ? avsandare : "")
                .add("amne", amne != null ? amne : "")
                .add("utskicksdatum", utskicksdatum != null ? utskicksdatum : "")
                .add("notifieringskategori", notifieringskategori != null ? notifieringskategori : "")
                .add("meddelande", meddelande != null ? meddelande : "")
                .add("status", status != null ? status : "")
                .build();
    }
}
