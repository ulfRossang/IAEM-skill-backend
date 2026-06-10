package se.handelsbanken.iaem.massutskick.entity;

import jakarta.json.JsonObject;

public record MassutskickInput(
        String land,
        String avsandare,
        String amne,
        String utskicksdatum,
        String notifieringskategori,
        String meddelande) {

    public static MassutskickInput fromJSON(JsonObject o) {
        return new MassutskickInput(
                o.getString("land", ""),
                o.getString("avsandare", ""),
                o.getString("amne", ""),
                o.getString("utskicksdatum", ""),
                o.getString("notifieringskategori", ""),
                o.getString("meddelande", ""));
    }
}
