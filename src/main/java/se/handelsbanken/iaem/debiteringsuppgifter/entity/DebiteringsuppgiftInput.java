package se.handelsbanken.iaem.debiteringsuppgifter.entity;

import jakarta.json.JsonObject;

public record DebiteringsuppgiftInput(
        String produktid,
        String meddelandeid,
        String systembeteckning,
        String antsKodInternet,
        String antsKodEjInternet,
        String resultatstalle) {

    public static DebiteringsuppgiftInput fromJSON(JsonObject o) {
        return new DebiteringsuppgiftInput(
                o.getString("produktid", ""),
                o.getString("meddelandeid", ""),
                o.getString("systembeteckning", ""),
                o.getString("antsKodInternet", ""),
                o.getString("antsKodEjInternet", ""),
                o.getString("resultatstalle", ""));
    }
}
