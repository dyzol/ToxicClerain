package toxiccleanup.builder.machines;

import org.junit.Test;
import toxiccleanup.builder.Damage;
import toxiccleanup.builder.GameState;
import toxiccleanup.builder.entities.GameEntity;
import toxiccleanup.builder.entities.tiles.Tile;
import toxiccleanup.builder.player.Player;
import toxiccleanup.builder.weather.Weather;
import toxiccleanup.builder.weather.WeatherSpawnPoint;
import toxiccleanup.builder.world.World;
import toxiccleanup.engine.EngineState;
import toxiccleanup.engine.art.sprites.Sprite;
import toxiccleanup.engine.game.Position;
import toxiccleanup.engine.game.Positionable;
import toxiccleanup.engine.input.KeyState;
import toxiccleanup.engine.input.MouseState;
import toxiccleanup.engine.renderer.Dimensions;
import toxiccleanup.engine.renderer.Renderable;
import toxiccleanup.engine.renderer.TileGrid;
import org.junit.Before;

import java.util.List;

import static org.junit.Assert.*;

public class PumpTest {

    public static final double testWeight = 5.0;

    private static final int ANIM_INTERVAL = 4;
    private static final int PUMP_INTERVAL = 100;
    private static final int POWER_REQUIRED = 2;

    private static final TileGrid TILE_GRID = new TileGrid(16, 800);
    private static final Position POSITION = new Position(100, 100);

    /**
     * Test implementation of Adjustable to track calls to adjust()
     */
    private static class TestAdjustable implements Adjustable {
        private int callCount = 0;
        private int lastAmount = 0;
        private int totalAdjusted = 0;
        private boolean toxicityReachedZero = false;

        @Override
        public void adjust(int amount) {
            callCount++;
            lastAmount = amount;
            totalAdjusted += amount;
            // Simulate toxicity reaching 0 after 6 adjustments (starting toxicity = 6)
            if (totalAdjusted >= 6) {
                toxicityReachedZero = true;
            }
        }

        public int getCallCount() { return callCount; }
        public int getLastAmount() { return lastAmount; }
        public int getTotalAdjusted() { return totalAdjusted; }
        public boolean hasToxicityReachedZero() { return toxicityReachedZero; }
        public void reset() {
            callCount = 0;
            lastAmount = 0;
            totalAdjusted = 0;
            toxicityReachedZero = false;
        }
    }

    private TestAdjustable pumpTarget;
    private Pump pump;

    @Before
    public void setUp() {
        pumpTarget = new TestAdjustable();
        pump = new Pump(POSITION, pumpTarget);
    }

    private static EngineState makeEngineState(boolean eKeyDown) {
        return new EngineState() {
            @Override public Dimensions getDimensions() { return TILE_GRID; }
            @Override public MouseState getMouse() {
                return new MouseState() {
                    @Override public int getMouseX() { return 0; }
                    @Override public int getMouseY() { return 0; }
                    @Override public boolean isLeftPressed() { return false; }
                    @Override public boolean isRightPressed() { return false; }
                    @Override public boolean isMiddlePressed() { return false; }
                };
            }
            @Override public KeyState getKeys() {
                return new KeyState() {
                    @Override public List<Character> getDown() {
                        return eKeyDown ? List.of('e') : List.of();
                    }
                    @Override public boolean isDown(char c) { return eKeyDown && c == 'e'; }
                };
            }
            @Override public int currentTick() { return 0; }
        };
    }

    private static GameState makeGameState(int power, Damage damage) {
        Machines machines = new MachinesManager(power);
        return new GameState() {
            @Override public World getWorld() {
                return new World() {
                    @Override public List<Tile> tilesAtPosition(Positionable p, Dimensions d) { return List.of(); }
                    @Override public List<Tile> allTiles() { return List.of(); }
                    @Override public void place(Tile tile) {}
                };
            }
            @Override public Player getPlayer() {
                return new Player() {
                    @Override public Positionable getPosition() { return new Position(0, 0); }
                    @Override public void setPosition(Positionable p) {}
                    @Override public int getHp() { return 5; }
                    @Override public int getMaxHp() { return 10; }
                    @Override public void adjust(int amount) {}
                    @Override public void tick(EngineState s, GameState g) {}
                    @Override public List<Renderable> render() { return List.of(); }
                };
            }
            @Override public Machines getMachines() { return machines; }
            @Override public Weather getWeather() {
                return new Weather() {
                    @Override public void addSpawnPoint(WeatherSpawnPoint sp) {}
                    @Override public void addWeather(GameEntity w) {}
                    @Override public boolean isObscuring(Dimensions d, Positionable p) { return false; }
                    @Override public boolean isDamaging(Dimensions d, Positionable p) { return damage != null; }
                    @Override public void applyLightningRod(Positionable p) {}
                    @Override public Damage getDamage(Dimensions d, Positionable p) { return damage; }
                    @Override public Damage getDamage() { return damage; }
                    @Override public void tick(EngineState s, GameState g) {}
                    @Override public List<Renderable> render() { return List.of(); }
                };
            }
        };
    }

    private static final EngineState BASE_STATE = makeEngineState(false);
    private static final EngineState E_KEY_STATE = makeEngineState(true);

    // ============================================================
    // Tests
    // ============================================================

    @Test
    public void toxicityReducesEveryHundredTicks() {
        GameState game = makeGameState(POWER_REQUIRED, null);

        // Tick 99 times - adjust should NOT be called yet
        for (int i = 0; i < PUMP_INTERVAL - 1; i++) {
            pump.tick(BASE_STATE, game);
        }
        assertEquals(0, pumpTarget.getCallCount());

        // 100th tick - adjust should be called with amount 1
        pump.tick(BASE_STATE, game);
        assertEquals(1, pumpTarget.getCallCount());
        assertEquals(1, pumpTarget.getLastAmount());
    }

    @Test
    public void pumpRepeatedlyReducesToxicityEveryHundredTicks() {
        GameState game = makeGameState(POWER_REQUIRED, null);

        // First pump cycle (100 ticks) - adjust called once
        for (int i = 0; i < PUMP_INTERVAL; i++) {
            pump.tick(BASE_STATE, game);
        }
        assertEquals(1, pumpTarget.getCallCount());

        // Second pump cycle (another 100 ticks) - adjust called twice
        for (int i = 0; i < PUMP_INTERVAL; i++) {
            pump.tick(BASE_STATE, game);
        }
        assertEquals(2, pumpTarget.getCallCount());
        assertEquals(2, pumpTarget.getTotalAdjusted());
    }

    @Test
    public void pumpDoesNotOperateWhenPowerInsufficient() {
        // Start with one less power than required
        GameState game = makeGameState(POWER_REQUIRED - 1, null);

        // Run enough ticks for a pump cycle
        for (int i = 0; i < PUMP_INTERVAL + 1; i++) {
            pump.tick(BASE_STATE, game);
        }
        // want "pump did nothing", 0 calls counted
        assertEquals(0, pumpTarget.getCallCount());
    }

    @Test
    public void pumpResumesWhenPowerRestored() {
        // Start with insufficient power
        GameState lowPowerGame = makeGameState(POWER_REQUIRED - 1, null);

        // Run 100 ticks - no pumping should occur
        for (int i = 0; i < PUMP_INTERVAL; i++) {
            pump.tick(BASE_STATE, lowPowerGame);
        }
        assertEquals(0, pumpTarget.getCallCount());

        // Restore power to sufficient level
        GameState sufficientPowerGame = makeGameState(POWER_REQUIRED, null);

        // Run another pump cycle with sufficient power
        for (int i = 0; i < PUMP_INTERVAL; i++) {
            pump.tick(BASE_STATE, sufficientPowerGame);
        }

        // Pump should have pumped once after power was restored
        assertEquals(1, pumpTarget.getCallCount());
    }

    @Test
    public void getPowerRequirementReturnsTwo() {
        assertEquals(POWER_REQUIRED, pump.getPowerRequirement());
    }

    @Test
    public void pumpPositionIsCorrectlySet() {
        assertEquals(POSITION.getX(), pump.getX());
        assertEquals(POSITION.getY(), pump.getY());
    }

    @Test
    public void pumpDoesNotOperateWithZeroPower() {
        GameState game = makeGameState(0, null);

        // Run enough ticks for multiple pump cycles
        for (int i = 0; i < PUMP_INTERVAL * 2; i++) {
            pump.tick(BASE_STATE, game);
        }

        assertEquals(0, pumpTarget.getCallCount());
    }

    @Test
    public void pumpDoesNotOperateWithInsufficientPower() {
        GameState game = makeGameState(POWER_REQUIRED - 1, null);

        // Run enough ticks for multiple pump cycles
        for (int i = 0; i < PUMP_INTERVAL * 2; i++) {
            pump.tick(BASE_STATE, game);
        }

        assertEquals(0, pumpTarget.getCallCount());
    }

    @Test
    public void powerRequirementIsAlwaysTwo() {
        // Test multiple times to ensure value is consistent
        for (int i = 0; i < 10; i++) {
            assertEquals(2, pump.getPowerRequirement());
        }
    }

    @Test
    public void animationAdvancesWhenHasPower() {
        Sprite initialSprite = pump.getSprite();
        GameState game = makeGameState(POWER_REQUIRED, null);
        boolean spriteChanged = false;

        for (int tick = 0; tick < 8; tick++) {
            pump.tick(BASE_STATE, game);
            if (!pump.getSprite().toString().equals(initialSprite.toString())) {
                spriteChanged = true;
                break;
            }
        }

        assertTrue("Pump animation should change sprite when powered",
                spriteChanged);
    }

    @Test
    public void animationFreezesWhenInsufficientPower() {
        final Sprite initialSprite = pump.getSprite();
        GameState game = makeGameState(POWER_REQUIRED - 1, null);
        boolean spriteChanged = false;

        for (int tick = 0; tick < 8; tick++) {
            pump.tick(BASE_STATE, game);
            if (!pump.getSprite().toString().equals(initialSprite.toString())) {
                spriteChanged = true;
                break;
            }
        }

        assertFalse("Pump animation should not change sprite when insufficient power",
                spriteChanged);
    }

}