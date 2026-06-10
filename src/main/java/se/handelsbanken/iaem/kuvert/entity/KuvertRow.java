package se.handelsbanken.iaem.kuvert.entity;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public record KuvertRow(
        String kuvertId,
        String kundnr,
        String kundnamn,
        String datum,
        String mall,
        String kanal,
        String status) {

    public static KuvertRow from(KuvertEntity e) {
        return new KuvertRow(
                e.getKuvertId(),
                e.getKundnr(),
                e.getKundnamn(),
                e.getDatum(),
                e.getMall(),
                e.getKanal(),
                e.getStatus());
    }

    public JsonObject toJSON() {
        return Json.createObjectBuilder()
                .add("kuvertId", kuvertId != null ? kuvertId : "")
                .add("kundnr", kundnr != null ? kundnr : "")
                .add("kundnamn", kundnamn != null ? kundnamn : "")
                .add("datum", datum != null ? datum : "")
                .add("mall", mall != null ? mall : "")
                .add("kanal", kanal != null ? kanal : "")
                .add("status", status != null ? status : "")
                .build();
    }
}
