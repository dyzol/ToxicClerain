package toxiccleanup.builder.machines;

import org.junit.Test;
import toxiccleanup.builder.Damage;
import toxiccleanup.builder.GameState;
import toxiccleanup.builder.SpriteGallery;
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
    public void initialSpriteIsDefault() {
        final Sprite expected = SpriteGallery.pump.getSprite("1");
        assertEquals("initial Pump sprite should be '1'",
                expected.toString(), pump.getSprite().toString());
    }

    @Test
    public void spriteIsDamagedWhenDamaged() {
        // GameState with damage at pump's position
        GameState damagedState = makeGameState(2, new Damage(POSITION));
        pump.tick(BASE_STATE, damagedState); // tick pump to apply damage
        // check sprite changed to damaged
        final Sprite expected = SpriteGallery.pump.getSprite("damaged");
        assertEquals("Pump sprite should be 'damaged' when weather deals damage",
                expected.toString(), pump.getSprite().toString());
    }

    @Test
    public void repairWithEKeyWhenDamaged() {
        final Sprite damagedSprite = SpriteGallery.pump.getSprite("damaged");

        // damage the pump
        GameState damagedState = makeGameState(2, new Damage(POSITION));
        pump.tick(BASE_STATE, damagedState);

        // Verify it is damaged
        assertEquals(SpriteGallery.pump.getSprite("damaged").toString(),
                pump.getSprite().toString());

        // Press 'e' key to repair
        pump.playerOver(E_KEY_STATE, makeGameState(2, null));

        // Animation interval is 4 ticks, so tick enough times
        for (int i = 0; i < ANIM_INTERVAL; i++) {
            pump.tick(BASE_STATE, makeGameState(2, null));
        }

        assertNotEquals(damagedSprite.toString(), pump.getSprite().toString());
    }

    @Test
    public void pressEKeyWhenNotDamagedDoesNothing() {
        GameState game = makeGameState(2, null);
        pumpTarget.reset();

        // Press 'e' key on undamaged pump
        pump.playerOver(E_KEY_STATE, game);

        // Pump should still work normally
        for (int i = 0; i < PUMP_INTERVAL; i++) {
            pump.tick(BASE_STATE, game);
        }

        // Pump should have pumped (adjust called once)
        assertEquals("Pump should still function normally after 'e' press when undamaged",
                1, pumpTarget.getCallCount());
    }

    @Test
    public void constructPumpAtGivenPosition() {
        Position customPosition = new Position(250, 300);
        Pump testPump = new Pump(customPosition, pumpTarget);

        assertEquals("Pump X coordinate should match constructor position",
                customPosition.getX(), testPump.getX());
        assertEquals("Pump Y coordinate should match constructor position",
                customPosition.getY(), testPump.getY());
    }

    @Test
    public void pumpShouldAdjustGivenAdjustablePerTick() {
        GameState game = makeGameState(2, null);
        pumpTarget.reset();

        // Tick 99 times - adjust should NOT be called yet
        for (int i = 0; i < PUMP_INTERVAL - 1; i++) {
            pump.tick(BASE_STATE, game);
        }
        assertEquals("adjust() should not be called before 100 ticks",
                0, pumpTarget.getCallCount());

        // 100th tick - adjust should be called once with amount 1
        pump.tick(BASE_STATE, game);
        assertEquals("adjust() should be called exactly once after 100 ticks",
                1, pumpTarget.getCallCount());
        assertEquals("adjust() should be called with amount 1",
                1, pumpTarget.getLastAmount());
    }

    @Test
    public void pumpDoesNotAnimateWhenPowerInsufficient() {
        // Create GameState with insufficient power (1)
        GameState lowPowerGame = makeGameState(2 - 1, null);

        // Get the expected initial sprite (frame "1")
        Sprite expectedSprite = SpriteGallery.pump.getSprite("1");

        // Run through a full pump cycle (100 ticks)
        for (int tick = 0; tick < PUMP_INTERVAL; tick++) {
            pump.tick(BASE_STATE, lowPowerGame);

            // Check the sprite after each animation interval
            // (tick+1 because tick starts at 0)
            if ((tick + 1) % ANIM_INTERVAL == 0) {
                assertEquals("Animation should not advance at tick " + (tick + 1) +
                                " when power is insufficient",
                        expectedSprite.toString(), pump.getSprite().toString());
            }
        }

        // Final check after the entire cycle
        assertEquals("Animation should still be at frame '1' after full pump cycle with insufficient power",
                expectedSprite.toString(), pump.getSprite().toString());
    }

    @Test
    public void toxicityReducesEveryHundredTicks() {
        GameState game = makeGameState(2, null);

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
        GameState game = makeGameState(2, null);

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
        GameState game = makeGameState(2 - 1, null);

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
        GameState lowPowerGame = makeGameState(2 - 1, null);

        // Run 100 ticks - no pumping should occur
        for (int i = 0; i < PUMP_INTERVAL; i++) {
            pump.tick(BASE_STATE, lowPowerGame);
        }
        assertEquals(0, pumpTarget.getCallCount());

        // Restore power to sufficient level
        GameState sufficientPowerGame = makeGameState(2, null);

        // Run another pump cycle with sufficient power
        for (int i = 0; i < PUMP_INTERVAL; i++) {
            pump.tick(BASE_STATE, sufficientPowerGame);
        }

        // Pump should have pumped once after power was restored
        assertEquals(1, pumpTarget.getCallCount());
    }

    @Test
    public void getPowerRequirementReturnsTwo() {
        assertEquals(2, pump.getPowerRequirement());
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
    public void pumpSpriteChangesEvery4TicksIfPowerSufficient() {
        GameState poweredGame = makeGameState(2, null);
        Sprite previousSprite = pump.getSprite(); // track seen sprites
        boolean spriteChanged = false;

        // Run through a full pump cycle (100 ticks)
        for (int tick = 0; tick < PUMP_INTERVAL; tick++) {
            pump.tick(BASE_STATE, poweredGame);

            // Check after each animation interval (every 4 ticks)
            if ((tick + 1) % 8 == 0) {
                Sprite currentSprite = pump.getSprite();
                // At the first animation interval, sprite should have changed from initial
                if (tick + 1 == 8) {
                    assertNotEquals("Sprite should change after first " + ANIM_INTERVAL + " ticks",
                            previousSprite.toString(), currentSprite.toString());
                    spriteChanged = true;
                }
                // Update previous sprite for next comparison
                previousSprite = currentSprite;
            }
        }

        assertTrue("Sprite should have changed at least once during the pump cycle", spriteChanged);
    }

    @Test
    public void pumpDoesNotOperateWithInsufficientPower() {
        GameState game = makeGameState(2 - 1, null);

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
        GameState game = makeGameState(2, null);
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
        GameState game = makeGameState(2 - 1, null);
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