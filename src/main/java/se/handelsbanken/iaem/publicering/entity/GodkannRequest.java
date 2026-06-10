package se.handelsbanken.iaem.publicering.entity;

import jakarta.json.JsonObject;

public record GodkannRequest(boolean godkand) {

    public static GodkannRequest fromJSON(JsonObject o) {
        return new GodkannRequest(o.getBoolean("godkand", false));
    }
}
