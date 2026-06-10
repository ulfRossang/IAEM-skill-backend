package se.handelsbanken.iaem.publicering.entity;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public record PubliceringJobbRow(
        String jobbId,
        String systembeteckning,
        String informationsId,
        String leveranstidpunkt,
        String status) {

    public static PubliceringJobbRow from(PubliceringJobbEntity e) {
        return new PubliceringJobbRow(
                e.getJobbId(),
                e.getSystembeteckning(),
                e.getInformationsId(),
                e.getLeveranstidpunkt() != null ? e.getLeveranstidpunkt().toString() : null,
                e.getStatus());
    }

    public JsonObject toJSON() {
        return Json.createObjectBuilder()
                .add("jobbId", jobbId != null ? jobbId : "")
                .add("systembeteckning", systembeteckning != null ? systembeteckning : "")
                .add("informationsId", informationsId != null ? informationsId : "")
                .add("leveranstidpunkt", leveranstidpunkt != null ? leveranstidpunkt : "")
                .add("status", status != null ? status : "")
                .build();
    }
}
