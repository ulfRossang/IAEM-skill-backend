package se.handelsbanken.iaem.kunder.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class KundTest {

    @Test
    void toJSON_mapsAllFields() {
        var json = new Kund("KD-71042", "Kund #71042", "Sverige").toJSON();

        assertThat(json.getString("kundnr")).isEqualTo("KD-71042");
        assertThat(json.getString("kundnamn")).isEqualTo("Kund #71042");
        assertThat(json.getString("land")).isEqualTo("Sverige");
    }

    @Test
    void toJSON_replacesNullsWithEmptyString() {
        var json = new Kund(null, null, null).toJSON();

        assertThat(json.getString("kundnr")).isEmpty();
        assertThat(json.getString("kundnamn")).isEmpty();
        assertThat(json.getString("land")).isEmpty();
    }
}
