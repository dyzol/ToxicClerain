package toxiccleanup.builder.machines;

import org.junit.Before;
import org.junit.Test;
import toxiccleanup.engine.game.Position;
import toxiccleanup.engine.game.Positionable;

import static org.junit.Assert.*;

public class MachinesManagerTest {

    private MachinesManager manager;
    private Positionable testPosition;

    @Before
    public void setUp() {
        manager = new MachinesManager();
        testPosition = new Position(5, 5);
    }

    // ========== Pump TESTS ==========

    @Test
    public void testCanSpawnPump() {
        //power sufficient case
        manager.setPower(Pump.COST);
        Pump pump = manager.spawnPump(testPosition, null);

        assertNotNull(pump);
        assertEquals(0, manager.getPower());

        // power insufficient
        manager.setPower(Pump.COST - 1);
        Positionable testPosition2 = new Position(2,2);
        Pump pump2 = manager.spawnPump(testPosition2, null);

        assertNull(pump2);
        assertEquals(Pump.COST - 1, manager.getPower());
    }

    // ========== Constructor/power TESTS ==========

    @Test
    public void testDefaultConstructorSetsPowerTo14() {
        assertEquals(14, manager.getPower());
    }

    @Test
    public void testConstructorWithValidPowerSetsCorrectly() {
        MachinesManager m = new MachinesManager(7);
        assertEquals(7, m.getPower());
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
    public void testGetMaxPowerReturns14() {
        assertEquals(14, manager.getMaxPower());
    }

    // setters

    @Test
    public void testSetPowerWithinBoundsWorks() {
        manager.setPower(8);
        assertEquals(8, manager.getPower());
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
    public void testHasRequiredPowerReturnsTrueWhenEqualOrGreater() {
        manager.setPower(5);
        assertTrue(manager.hasRequiredPower(5));

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
        // test positive negative sequence works
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

        manager.setPower(SolarPanel.COST);
        Positionable testPosition2 = new Position(4,5);
        SolarPanel panel2 = manager.spawnSolarPanel(testPosition2);

        assertNotNull(panel2);
        assertEquals(0, manager.getPower());
    }

    @Test
    public void testSpawnSolarPanelInsufficientPower() {
        manager.setPower(SolarPanel.COST - 1);
        SolarPanel panel = manager.spawnSolarPanel(testPosition);

        assertNull(panel);
        assertEquals(SolarPanel.COST - 1, manager.getPower());
    }

    // ========== TELEPORTER SPAWNING TESTS ==========

    @Test
    public void testSpawnTeleporterSufficientPower() {
        manager.setPower(10);
        Teleporter teleporter = manager.spawnTeleporter(testPosition);

        assertNotNull(teleporter);
        assertEquals(10 - Teleporter.COST, manager.getPower());

        manager.setPower(Teleporter.COST);
        Positionable testPosition2 = new Position(3,6);
        Teleporter teleporter2 = manager.spawnTeleporter(testPosition2);

        assertNotNull(teleporter2);
        assertEquals(0, manager.getPower());
    }

    @Test
    public void testSpawnTeleporterInsufficientPower() {
        manager.setPower(Teleporter.COST - 1);
        Teleporter teleporter = manager.spawnTeleporter(testPosition);

        assertNull(teleporter);
        assertEquals(Teleporter.COST - 1, manager.getPower());
    }

    // ========== Teleporter teleportation TESTS ==========
    @Test
    public void testGetNextTeleporterPositionWithNoOtherTeleporterReturnsItself() {
        // edge case: should return itself
        manager.setPower(10);
        Position pos = new Position(5, 5);
        manager.spawnTeleporter(pos);

        Positionable result = manager.getNextTeleporterPosition(pos);

        assertEquals("Only one teleporter, x should be the same", pos.getX(), result.getX());
        assertEquals("Only one teleporter, y should be the same", pos.getY(), result.getY());
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
        manager.setPower(LightningRod.COST - 1);
        LightningRod rod = manager.spawnLightningRod(testPosition);

        assertNull(rod);
        assertEquals(LightningRod.COST - 1, manager.getPower());
    }
}