package se.handelsbanken.iaem.meddelanden.entity;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MeddelandeRowTest {

    @Test
    void toJSON_mapsBooleanFields() {
        var row = new MeddelandeRow("MSG-001", "KD-1", "Kund", "Avsändare", "Mottagare",
                "2026-06-11", "Digital", "Ämne", true, false, true, "SE-6292", "Skickat", "", List.of());

        var json = row.toJSON();

        assertThat(json.getBoolean("las")).isTrue();
        assertThat(json.getBoolean("borttaget")).isFalse();
        assertThat(json.getBoolean("arkiverat")).isTrue();
    }

    @Test
    void toJSON_includesBilagor() {
        var bilaga = new BilagaRow("kontoutdrag.pdf", 15360, "/api/v1/files/kontoutdrag.pdf");
        var row = new MeddelandeRow("MSG-001", "KD-1", "Kund", "Avsändare", "Mottagare",
                "2026-06-11", "Digital", "Ämne", false, false, false, "SE-6292", "Skickat", "Innehåll", List.of(bilaga));

        var bilagor = row.toJSON().getJsonArray("bilagor");

        assertThat(bilagor).hasSize(1);
        assertThat(bilagor.getJsonObject(0).getString("filnamn")).isEqualTo("kontoutdrag.pdf");
    }

    @Test
    void toJSON_replacesNullStringsWithEmpty() {
        var row = new MeddelandeRow(null, null, null, null, null,
                null, null, null, false, false, false, null, null, null, List.of());

        var json = row.toJSON();

        assertThat(json.getString("id")).isEmpty();
        assertThat(json.getString("kundnr")).isEmpty();
        assertThat(json.getString("datum")).isEmpty();
    }
}
