package se.handelsbanken.iaem.utskick.entity;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public record UtskickInstallningRow(
        String kategori,
        String avser,
        String forbindelse,
        boolean papper,
        boolean internet) {

    public static UtskickInstallningRow from(UtskickInstallningEntity e) {
        return new UtskickInstallningRow(
                e.getKategori(),
                e.getAvser(),
                e.getForbindelse(),
                e.getPapper() != null && e.getPapper() == 1,
                e.getInternet() != null && e.getInternet() == 1);
    }

    public JsonObject toJSON() {
        return Json.createObjectBuilder()
                .add("kategori", kategori != null ? kategori : "")
                .add("avser", avser != null ? avser : "")
                .add("forbindelse", forbindelse != null ? forbindelse : "")
                .add("papper", papper)
                .add("internet", internet)
                .build();
    }
}
