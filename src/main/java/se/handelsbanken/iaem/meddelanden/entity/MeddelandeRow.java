package se.handelsbanken.iaem.meddelanden.entity;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.stream.JsonCollectors;

import java.util.List;

public record MeddelandeRow(
        String id,
        String kundnr,
        String kundnamn,
        String avsandare,
        String mottagare,
        String datum,
        String kategori,
        String amne,
        boolean las,
        boolean borttaget,
        boolean arkiverat,
        String kontor,
        String status,
        String innehall,
        List<BilagaRow> bilagor) {

    public static MeddelandeRow from(MeddelandeHeader h, String innehall, List<BilagaRow> bilagor) {
        return new MeddelandeRow(
                h.getPersmeddid() != null ? h.getPersmeddid().trim() : null,
                h.getKundnummer(),
                h.getKundnamn(),
                h.getAvsandare(),
                h.getMottagare(),
                h.getSkapdatum() != null ? h.getSkapdatum().toString() : null,
                h.getKategori(),
                h.getRubrik(),
                h.getLas() != null && h.getLas() == 1,
                h.getBorttagetvmot() != null && h.getBorttagetvmot() == 1,
                h.getArkiverat() != null && h.getArkiverat() == 1,
                h.getKontor(),
                "Skickat",
                innehall,
                bilagor);
    }

    public JsonObject toJSON() {
        var builder = Json.createObjectBuilder()
                .add("id", id != null ? id : "")
                .add("kundnr", kundnr != null ? kundnr : "")
                .add("kundnamn", kundnamn != null ? kundnamn : "")
                .add("avsandare", avsandare != null ? avsandare : "")
                .add("mottagare", mottagare != null ? mottagare : "")
                .add("datum", datum != null ? datum : "")
                .add("kategori", kategori != null ? kategori : "")
                .add("amne", amne != null ? amne : "")
                .add("las", las)
                .add("borttaget", borttaget)
                .add("arkiverat", arkiverat)
                .add("kontor", kontor != null ? kontor : "")
                .add("status", status != null ? status : "")
                .add("innehall", innehall != null ? innehall : "")
                .add("bilagor", bilagor.stream().map(BilagaRow::toJSON).collect(JsonCollectors.toJsonArray()));
        return builder.build();
    }
}
