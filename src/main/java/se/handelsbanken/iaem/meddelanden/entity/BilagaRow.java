package se.handelsbanken.iaem.meddelanden.entity;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public record BilagaRow(String filnamn, int storlek, String url) {

    public JsonObject toJSON() {
        return Json.createObjectBuilder()
                .add("filnamn", filnamn != null ? filnamn : "")
                .add("storlek", storlek)
                .add("url", url != null ? url : "")
                .build();
    }
}
