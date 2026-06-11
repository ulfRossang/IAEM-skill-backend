package se.handelsbanken.iaem.massutskick.entity;

import jakarta.json.Json;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MassutskickInputTest {

    @Test
    void fromJSON_mapsAllFields() {
        var json = Json.createObjectBuilder()
                .add("land", "Sverige")
                .add("avsandare", "Handelsbanken")
                .add("amne", "Testämne")
                .add("utskicksdatum", "2026-06-11")
                .add("notifieringskategori", "NOT-01")
                .add("meddelande", "Testmeddelande")
                .build();

        var input = MassutskickInput.fromJSON(json);

        assertThat(input.land()).isEqualTo("Sverige");
        assertThat(input.avsandare()).isEqualTo("Handelsbanken");
        assertThat(input.amne()).isEqualTo("Testämne");
        assertThat(input.utskicksdatum()).isEqualTo("2026-06-11");
        assertThat(input.notifieringskategori()).isEqualTo("NOT-01");
        assertThat(input.meddelande()).isEqualTo("Testmeddelande");
    }

    @Test
    void fromJSON_defaultsToEmptyStringForMissingFields() {
        var input = MassutskickInput.fromJSON(Json.createObjectBuilder().build());

        assertThat(input.land()).isEmpty();
        assertThat(input.amne()).isEmpty();
        assertThat(input.meddelande()).isEmpty();
    }
}
