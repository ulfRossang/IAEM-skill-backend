package se.handelsbanken.iaem.informationsprodukter.entity;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public record InformationsproduktRow(
        String id,
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

    public static InformationsproduktRow from(InformationsproduktEntity e) {
        return new InformationsproduktRow(
                e.getId(),
                e.getNamn(),
                e.getLand(),
                e.getStatus(),
                e.getNotifieringskategori(),
                e.getSystembeteckning(),
                e.getInsynsskyddad() != null && e.getInsynsskyddad() == 1,
                new Kanaler(
                        e.getDefaultPapper() != null && e.getDefaultPapper() == 1,
                        e.getDefaultInternet() != null && e.getDefaultInternet() == 1),
                new Kanaler(
                        e.getTilllåtnaPapper() != null && e.getTilllåtnaPapper() == 1,
                        e.getTilllåtnaInternet() != null && e.getTilllåtnaInternet() == 1),
                new Kanaler(
                        e.getObligatoriskaPapper() != null && e.getObligatoriskaPapper() == 1,
                        e.getObligatoriskaInternet() != null && e.getObligatoriskaInternet() == 1),
                e.getAvgiftsidPapper(),
                e.getAvgiftsidInternet(),
                e.getVisningstidEArkivMan() != null ? e.getVisningstidEArkivMan() : 0,
                e.getLagringstidDiskMan() != null ? e.getLagringstidDiskMan() : 0,
                e.getMeddelandetext());
    }

    public JsonObject toJSON() {
        return Json.createObjectBuilder()
                .add("id", id != null ? id : "")
                .add("namn", namn != null ? namn : "")
                .add("land", land != null ? land : "")
                .add("status", status != null ? status : "")
                .add("notifieringskategori", notifieringskategori != null ? notifieringskategori : "")
                .add("systembeteckning", systembeteckning != null ? systembeteckning : "")
                .add("insynsskyddad", insynsskyddad)
                .add("defaultkanaler", defaultkanaler != null ? defaultkanaler.toJSON() : Json.createObjectBuilder().build())
                .add("tillåtnaKanaler", tillåtnaKanaler != null ? tillåtnaKanaler.toJSON() : Json.createObjectBuilder().build())
                .add("obligatoriskaKanaler", obligatoriskaKanaler != null ? obligatoriskaKanaler.toJSON() : Json.createObjectBuilder().build())
                .add("avgiftsidPapper", avgiftsidPapper != null ? avgiftsidPapper : "")
                .add("avgiftsidInternet", avgiftsidInternet != null ? avgiftsidInternet : "")
                .add("visningstidEArkivMan", visningstidEArkivMan)
                .add("lagringstidDiskMan", lagringstidDiskMan)
                .add("meddelandetext", meddelandetext != null ? meddelandetext : "")
                .build();
    }
}
