package se.handelsbanken.iaem.kunder.entity;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public record Kund(String kundnr, String kundnamn, String land) {

    public static Kund from(KundEntity e) {
        return new Kund(e.getKundnr(), e.getKundnamn(), e.getLand());
    }

    public JsonObject toJSON() {
        return Json.createObjectBuilder()
                .add("kundnr", kundnr != null ? kundnr : "")
                .add("kundnamn", kundnamn != null ? kundnamn : "")
                .add("land", land != null ? land : "")
                .build();
    }
}
