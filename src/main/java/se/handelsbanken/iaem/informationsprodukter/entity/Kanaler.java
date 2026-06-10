package se.handelsbanken.iaem.informationsprodukter.entity;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public record Kanaler(boolean papper, boolean internet) {

    public static Kanaler fromJSON(JsonObject o) {
        if (o == null) return new Kanaler(false, false);
        return new Kanaler(
                o.getBoolean("papper", false),
                o.getBoolean("internet", false));
    }

    public JsonObject toJSON() {
        return Json.createObjectBuilder()
                .add("papper", papper)
                .add("internet", internet)
                .build();
    }
}
