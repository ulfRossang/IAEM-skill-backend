package se.handelsbanken.iaem.publicering.entity;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public record InformationssambandRow(
        String id,
        String systembeteckning,
        String informationsId,
        boolean publiceraAutomatiskt) {

    public static InformationssambandRow from(InformationssambandEntity e) {
        return new InformationssambandRow(
                e.getId(),
                e.getSystembeteckning(),
                e.getInformationsId(),
                e.getPubliceraAutomatiskt() != null && e.getPubliceraAutomatiskt() == 1);
    }

    public static InformationssambandRow fromJSON(JsonObject o) {
        return new InformationssambandRow(
                o.getString("id", null),
                o.getString("systembeteckning", ""),
                o.getString("informationsId", ""),
                o.getBoolean("publiceraAutomatiskt", false));
    }

    public JsonObject toJSON() {
        return Json.createObjectBuilder()
                .add("id", id != null ? id : "")
                .add("systembeteckning", systembeteckning != null ? systembeteckning : "")
                .add("informationsId", informationsId != null ? informationsId : "")
                .add("publiceraAutomatiskt", publiceraAutomatiskt)
                .build();
    }
}
