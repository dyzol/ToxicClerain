package toxiccleanup.builder.machines;

import org.junit.Before;
import org.junit.Test;
import toxiccleanup.engine.game.Position;
import toxiccleanup.engine.game.Positionable;
import toxiccleanup.engine.timing.RepeatingTimer;
import toxiccleanup.engine.timing.TickTimer;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class MachinesManagerTest {

    private MachinesManager manager;
    private Positionable testPosition;

    @Before
    public void setUp() {
        manager = new MachinesManager();
        testPosition = new Position(5, 5);
    }

    // ========== POWER MANAGEMENT TESTS ==========

    @Test
    public void testDefaultConstructorSetsPowerTo14() {
        assertEquals(14, manager.getPower());
    }

    @Test
    public void testConstructorWithPowerClampsToMax() {
        MachinesManager m = new MachinesManager(15);
        assertEquals(14, m.getPower());
    }

    @Test
    public void testConstructorWithPowerClampsToMin() {
        MachinesManager m = new MachinesManager(-1);
        assertEquals(0, m.getPower());
    }

    @Test
    public void testConstructorWithValidPowerSetsCorrectly() {
        MachinesManager m = new MachinesManager(7);
        assertEquals(7, m.getPower());
    }

    @Test
    public void testGetMaxPowerReturns14() {
        assertEquals(14, manager.getMaxPower());
    }

    @Test
    public void testSetPowerClampsToMax() {
        manager.setPower(15);
        assertEquals(14, manager.getPower());
    }

    @Test
    public void testSetPowerClampsToMin() {
        manager.setPower(-1);
        assertEquals(0, manager.getPower());
    }

    @Test
    public void testSetPowerWithinBoundsWorks() {
        manager.setPower(8);
        assertEquals(8, manager.getPower());
    }

    @Test
    public void testHasRequiredPowerReturnsTrueWhenEqual() {
        manager.setPower(5);
        assertTrue(manager.hasRequiredPower(5));
    }

    @Test
    public void testHasRequiredPowerReturnsTrueWhenGreater() {
        manager.setPower(10);
        assertTrue(manager.hasRequiredPower(5));
    }

    @Test
    public void testHasRequiredPowerReturnsFalseWhenLess() {
        manager.setPower(3);
        assertFalse(manager.hasRequiredPower(5));
    }

    @Test
    public void testAdjustAddsPowerAndClampsToMax() {
        manager.setPower(10);
        manager.adjust(10);
        assertEquals(14, manager.getPower());

        manager.setPower(13);
        manager.adjust(2);
        assertEquals(14, manager.getPower());
    }

    @Test
    public void testAdjustSubtractsPowerAndClampsToMin() {
        manager.setPower(5);
        manager.adjust(-10);
        assertEquals(0, manager.getPower());

        manager.setPower(2);
        manager.adjust(-5);
        assertEquals(0, manager.getPower());
    }

    @Test
    public void testAdjustWithinBoundsWorks() {
        manager.setPower(10);
        manager.adjust(2);
        assertEquals(12, manager.getPower());
    }

    @Test
    public void testAdjustWithPositiveAndNegativeSequence() {
        manager.setPower(10);
        manager.adjust(3);
        assertEquals(13, manager.getPower());
        manager.adjust(-5);
        assertEquals(8, manager.getPower());
    }
    // ========== SOLAR PANEL SPAWNING TESTS ==========

    @Test
    public void testSpawnSolarPanelSufficientPower() {
        manager.setPower(10);
        SolarPanel panel = manager.spawnSolarPanel(testPosition);

        assertNotNull(panel);
        assertEquals(10 - SolarPanel.COST, manager.getPower());
    }

    @Test
    public void testSpawnSolarPanelExactPower() {
        manager.setPower(SolarPanel.COST);
        SolarPanel panel = manager.spawnSolarPanel(testPosition);

        assertNotNull(panel);
        assertEquals(0, manager.getPower());
    }

    @Test
    public void testSpawnSolarPanelInsufficientPower() {
        manager.setPower(SolarPanel.COST - 1);
        SolarPanel panel = manager.spawnSolarPanel(testPosition);

        assertNull(panel);
        assertEquals(SolarPanel.COST - 1, manager.getPower());
    }

    @Test
    public void testSpawnSolarPanelWithZeroPowerReturnsNull() {
        manager.setPower(0);
        SolarPanel panel = manager.spawnSolarPanel(testPosition);

        assertNull(panel);
        assertEquals(0, manager.getPower());
    }

    // ========== LIGHTNING ROD SPAWNING TESTS ==========

    @Test
    public void testSpawnLightningRodSufficientPower() {
        manager.setPower(5);
        LightningRod rod = manager.spawnLightningRod(testPosition);

        assertNotNull(rod);
        assertEquals(5 - LightningRod.COST, manager.getPower());
    }

    @Test
    public void testSpawnLightningRodExactPower() {
        manager.setPower(LightningRod.COST);
        LightningRod rod = manager.spawnLightningRod(testPosition);

        assertNotNull(rod);
        assertEquals(0, manager.getPower());
    }

    @Test
    public void testSpawnLightningRodInsufficientPower() {
        manager.setPower(0);
        LightningRod rod = manager.spawnLightningRod(testPosition);

        assertNull(rod);
        assertEquals(0, manager.getPower());
    }

    // ========== TELEPORTER SPAWNING TESTS ==========

    @Test
    public void testSpawnTeleporterSufficientPower() {
        manager.setPower(10);
        Teleporter teleporter = manager.spawnTeleporter(testPosition);

        assertNotNull(teleporter);
        assertEquals(10 - Teleporter.COST, manager.getPower());
    }

    @Test
    public void testSpawnTeleporterExactPower() {
        manager.setPower(Teleporter.COST);
        Teleporter teleporter = manager.spawnTeleporter(testPosition);

        assertNotNull(teleporter);
        assertEquals(0, manager.getPower());
    }

    @Test
    public void testSpawnTeleporterInsufficientPower() {
        manager.setPower(Teleporter.COST - 1);
        Teleporter teleporter = manager.spawnTeleporter(testPosition);

        assertNull(teleporter);
        assertEquals(Teleporter.COST - 1, manager.getPower());
    }

    // ========== PUMP SPAWNING TESTS ==========

    @Test
    public void testSpawnPumpSufficientPower() {
        manager.setPower(10);
        Adjustable mockAdjustable = new MockAdjustable();
        Pump pump = manager.spawnPump(testPosition, mockAdjustable);

        assertNotNull(pump);
        assertEquals(10 - Pump.COST, manager.getPower());
    }

    @Test
    public void testSpawnPumpExactPower() {
        manager.setPower(Pump.COST);
        Adjustable mockAdjustable = new MockAdjustable();
        Pump pump = manager.spawnPump(testPosition, mockAdjustable);

        assertNotNull(pump);
        assertEquals(0, manager.getPower());
    }

    @Test
    public void testSpawnPumpInsufficientPower() {
        manager.setPower(Pump.COST - 1);
        Adjustable mockAdjustable = new MockAdjustable();
        Pump pump = manager.spawnPump(testPosition, mockAdjustable);

        assertNull(pump);
        assertEquals(Pump.COST - 1, manager.getPower());
    }

    // ========== TELEPORTER POSITION TRACKING TESTS ==========

    @Test
    public void testSpawnTeleporterAddsToTeleporterPositionsAfterCooldown() {
        manager.setPower(10);
        Position pos1 = new Position(1, 1);
        Position pos2 = new Position(2, 2);

        manager.spawnTeleporter(pos1);
        manager.spawnTeleporter(pos2);

        // Tick enough times to finish cooldown (TELEPORTER_COOLDOWN = 20)
        for (int i = 0; i < 20; i++) {
            manager.tick(null, null);
        }

        Positionable result = manager.getNextTeleporterPosition(pos1);
        assertEquals(pos2.getX(), result.getX());
        assertEquals(pos2.getY(), result.getY());
    }

    @Test
    public void testGetNextTeleporterPositionReturnsExcludedDuringCooldown() {
        manager.setPower(10);
        Position pos1 = new Position(1, 1);
        Position pos2 = new Position(2, 2);

        manager.spawnTeleporter(pos1);
        manager.spawnTeleporter(pos2);

        // During cooldown, should return excluded position
        Positionable result = manager.getNextTeleporterPosition(pos1);
        assertEquals(pos1.getX(), result.getX());
        assertEquals(pos1.getY(), result.getY());
    }

    @Test
    public void testMultipleTeleportersTeleportsToDifferentTeleporter() {
        manager.setPower(10);
        Position pos1 = new Position(1, 1);
        Position pos2 = new Position(2, 2);
        Position pos3 = new Position(3, 3);

        manager.spawnTeleporter(pos1);
        manager.spawnTeleporter(pos2);
        manager.spawnTeleporter(pos3);

        // Tick enough times to finish cooldown (TELEPORTER_COOLDOWN = 20)
        for (int i = 0; i < 20; i++) {
            manager.tick(null, null);
        }

        // Get new destination, should not be pos1
        Positionable result = manager.getNextTeleporterPosition(pos1);
        assertNotEquals(result.getX(), pos1.getX());
        assertNotEquals(result.getY(), pos1.getY());
    }

    @Test
    public void testGetNextTeleporterPositionWithOnlyOneTeleporterReturnsSame() {
        manager.setPower(10);
        Position pos = new Position(5, 5);
        manager.spawnTeleporter(pos);

        Positionable result = manager.getNextTeleporterPosition(pos);

        assertEquals(pos.getX(), result.getX());
        assertEquals(pos.getY(), result.getY());
    }

    @Test
    public void testGetNextTeleporterPositionExcludesCurrent() {
        manager.setPower(10);
        Position pos1 = new Position(1, 1);
        Position pos2 = new Position(2, 2);
        Position pos3 = new Position(3, 3);

        manager.spawnTeleporter(pos1);
        manager.spawnTeleporter(pos2);
        manager.spawnTeleporter(pos3);

        // Tick enough times to finish cooldown (TELEPORTER_COOLDOWN = 20)
        for (int i = 0; i < 20; i++) {
            manager.tick(null, null);
        }

        Positionable result = manager.getNextTeleporterPosition(pos1);

        // Should not return pos1
        assertFalse(result.getX() == pos1.getX() && result.getY() == pos1.getY());
    }

    @Test
    public void testGetNextTeleporterPositionRespectsCooldown() {
        // Use a timer that never finishes
        TickTimer mockTimer = new RepeatingTimer(20) {
            private int ticks = 0;
            @Override
            public void tick() { ticks++; }
            @Override
            public boolean isFinished() { return false; } // Never finishes
        };

        MachinesManager m = new MachinesManager(mockTimer);
        m.setPower(10);
        Position pos1 = new Position(1, 1);
        Position pos2 = new Position(2, 2);

        m.spawnTeleporter(pos1);
        m.spawnTeleporter(pos2);

        // Cooldown active - should return excluded position
        Positionable result = m.getNextTeleporterPosition(pos1);
        assertEquals(pos1.getX(), result.getX());
        assertEquals(pos1.getY(), result.getY());
    }

    @Test
    public void testGetNextTeleporterPositionIsRandom() {
        manager.setPower(10);
        Position pos1 = new Position(1, 1);
        Position pos2 = new Position(2, 2);
        Position pos3 = new Position(3, 3);

        manager.spawnTeleporter(pos1);
        manager.spawnTeleporter(pos2);
        manager.spawnTeleporter(pos3);

        // Call multiple times, should see different results
        Set<Integer> resultsX = new HashSet<>();
        Set<Integer> resultsY = new HashSet<>();
        for (int i = 0; i < 20; i++) {
            // Tick enough times to finish cooldown (TELEPORTER_COOLDOWN = 20)
            for (int j = 0; j < 20; j++) {
                manager.tick(null, null);
            }
            Positionable result = manager.getNextTeleporterPosition(pos1);
            resultsX.add(result.getX());
            resultsY.add(result.getY());
        }
        // Should see at least 2 different X coordinates (excludes pos1)
        assertTrue(resultsX.size() >= 2);
    }

    // ========== TICK TESTS ==========

    @Test
    public void testTickAdvancesTeleporterCooldown() {
        // Create a timer that starts not finished
        RepeatingTimer timer = new RepeatingTimer(20);
        MachinesManager m = new MachinesManager(timer);
        m.setPower(10);
        Position pos1 = new Position(1, 1);
        Position pos2 = new Position(2, 2);

        m.spawnTeleporter(pos1);
        m.spawnTeleporter(pos2);

        // Before any ticks - cooldown active, returns excluded position
        Positionable before = m.getNextTeleporterPosition(pos1);
        assertEquals(pos1.getX(), before.getX());

        // Tick enough times to finish cooldown
        for (int i = 0; i < 20; i++) {
            m.tick(null, null);
        }

        // After cooldown - should return other teleporter
        Positionable after = m.getNextTeleporterPosition(pos1);
        assertEquals(pos2.getX(), after.getX());
    }

    // ========== MOCK CLASS ==========

    private static class MockAdjustable implements Adjustable {
        private int value = 6;

        @Override
        public void adjust(int amount) {
            value = Math.max(0, value - amount);
        }

        @SuppressWarnings("unused")
        public int getValue() { return value; }
    }
}