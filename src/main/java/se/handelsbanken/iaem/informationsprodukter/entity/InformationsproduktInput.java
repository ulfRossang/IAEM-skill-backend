package se.handelsbanken.iaem.informationsprodukter.entity;

import jakarta.json.JsonObject;

public record InformationsproduktInput(
        String namn,
        String land,
        String status,
        String notifieringskategori,
        String systembeteckning,
        boolean insynsskyddad,
        Kanaler defaultkanaler,
        Kanaler tillåtnaKanaler,
        Kanaler obligatoriskaKanaler,
        String avgiftsidPapper,
        String avgiftsidInternet,
        int visningstidEArkivMan,
        int lagringstidDiskMan,
        String meddelandetext) {

    public static InformationsproduktInput fromJSON(JsonObject o) {
        var defaultkanaler = o.containsKey("defaultkanaler")
                ? Kanaler.fromJSON(o.getJsonObject("defaultkanaler")) : new Kanaler(false, false);
        var tillåtnaKanaler = o.containsKey("tillåtnaKanaler")
                ? Kanaler.fromJSON(o.getJsonObject("tillåtnaKanaler")) : new Kanaler(false, false);
        var obligatoriskaKanaler = o.containsKey("obligatoriskaKanaler")
                ? Kanaler.fromJSON(o.getJsonObject("obligatoriskaKanaler")) : new Kanaler(false, false);
        return new InformationsproduktInput(
                o.getString("namn", ""),
                o.getString("land", ""),
                o.getString("status", ""),
                o.getString("notifieringskategori", ""),
                o.getString("systembeteckning", ""),
                o.getBoolean("insynsskyddad", false),
                defaultkanaler,
                tillåtnaKanaler,
                obligatoriskaKanaler,
                o.getString("avgiftsidPapper", ""),
                o.getString("avgiftsidInternet", ""),
                o.getInt("visningstidEArkivMan", 0),
                o.getInt("lagringstidDiskMan", 0),
                o.getString("meddelandetext", ""));
    }
}
