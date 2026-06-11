package se.handelsbanken.iaem.massutskick.control;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import se.handelsbanken.iaem.massutskick.control.Massutskick.KlarmarkeraResult;
import se.handelsbanken.iaem.massutskick.entity.MassutskickEntity;

import jakarta.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MassutskickTest {

    Massutskick massutskick;

    @BeforeEach
    void setup() {
        massutskick = new Massutskick();
        massutskick.em = mock(EntityManager.class);
    }

    @Test
    void klarmarkera_returnsNotFoundForUnknownId() {
        when(massutskick.em.find(MassutskickEntity.class, "999")).thenReturn(null);

        assertThat(massutskick.klarmarkera("999")).isEqualTo(KlarmarkeraResult.NOT_FOUND);
    }

    @Test
    void klarmarkera_returnsConflictWhenAlreadyKlarmarkerad() {
        var entity = new MassutskickEntity();
        entity.setStatus("Klarmarkerad");
        when(massutskick.em.find(MassutskickEntity.class, "214")).thenReturn(entity);

        assertThat(massutskick.klarmarkera("214")).isEqualTo(KlarmarkeraResult.CONFLICT);
    }

    @Test
    void klarmarkera_setsStatusAndReturnsOk() {
        var entity = new MassutskickEntity();
        entity.setStatus("Under upplägg");
        when(massutskick.em.find(MassutskickEntity.class, "250")).thenReturn(entity);

        assertThat(massutskick.klarmarkera("250")).isEqualTo(KlarmarkeraResult.OK);
        assertThat(entity.getStatus()).isEqualTo("Klarmarkerad");
    }
}
