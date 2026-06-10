package se.handelsbanken.iaem.dokument.entity;

import jakarta.json.Json;
import jakarta.json.JsonObject;

public record DokumentRow(
        String dokumentnamn,
        String forbindelse,
        String dokumentdatum,
        String utskicksdatum,
        String skickatsTill,
        String visasTill,
        boolean last,
        boolean borttaget,
        boolean arkiverat) {

    public static DokumentRow from(DokumentEntity e) {
        return new DokumentRow(
                e.getDokumentnamn(),
                e.getForbindelse(),
                e.getDokumentdatum(),
                e.getUtskicksdatum(),
                e.getSkickatsTill(),
                e.getVisasTill(),
                e.getLast() != null && e.getLast() == 1,
                e.getBorttaget() != null && e.getBorttaget() == 1,
                e.getArkiverat() != null && e.getArkiverat() == 1);
    }

    public JsonObject toJSON() {
        return Json.createObjectBuilder()
                .add("dokumentnamn", dokumentnamn != null ? dokumentnamn : "")
                .add("forbindelse", forbindelse != null ? forbindelse : "")
                .add("dokumentdatum", dokumentdatum != null ? dokumentdatum : "")
                .add("utskicksdatum", utskicksdatum != null ? utskicksdatum : "")
                .add("skickatsTill", skickatsTill != null ? skickatsTill : "")
                .add("visasTill", visasTill != null ? visasTill : "")
                .add("last", last)
                .add("borttaget", borttaget)
                .add("arkiverat", arkiverat)
                .build();
    }
}
