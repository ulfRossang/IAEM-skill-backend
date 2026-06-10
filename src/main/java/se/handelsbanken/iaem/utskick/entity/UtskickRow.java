package se.handelsbanken.iaem.utskick.entity;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.stream.JsonCollectors;

import java.util.List;

public record UtskickRow(
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
        String visasTill,
        String innehall,
        List<UtskickBilagaRow> bilagor) {

    public static UtskickRow from(UtskickEntity e) {
        var bilagor = e.getBilagor().stream()
                .map(b -> new UtskickBilagaRow(b.getFilnamn(), b.getUrl()))
                .toList();
        return new UtskickRow(
                e.getUtskickId(),
                e.getKundnr(),
                e.getKundnamn(),
                e.getAvsandare(),
                e.getMottagare(),
                e.getDatum() != null ? e.getDatum().toString() : null,
                e.getKategori(),
                e.getAmne(),
                e.getLas() != null && e.getLas() == 1,
                e.getBorttaget() != null && e.getBorttaget() == 1,
                e.getArkiverat() != null && e.getArkiverat() == 1,
                e.getVisasTill(),
                e.getInnehall(),
                bilagor);
    }

    public JsonObject toJSON() {
        return Json.createObjectBuilder()
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
                .add("visasTill", visasTill != null ? visasTill : "")
                .add("innehall", innehall != null ? innehall : "")
                .add("bilagor", bilagor.stream().map(UtskickBilagaRow::toJSON).collect(JsonCollectors.toJsonArray()))
                .build();
    }
}
