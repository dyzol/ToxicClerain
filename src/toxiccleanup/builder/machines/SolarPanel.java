package toxiccleanup.builder.machines;

import toxiccleanup.builder.Damage;
import toxiccleanup.builder.GameState;
import toxiccleanup.builder.SpriteGallery;
import toxiccleanup.builder.entities.GameEntity;
import toxiccleanup.engine.EngineState;
import toxiccleanup.engine.art.sprites.SpriteGroup;
import toxiccleanup.engine.game.Positionable;
import toxiccleanup.engine.timing.RepeatingTimer;
import toxiccleanup.engine.timing.TickTimer;
import toxiccleanup.builder.entities.PlayerOverHook;
import toxiccleanup.builder.weather.Weather;

/**
 * A {@link SolarPanel} is a machine that passively generates power for the game's shared power
 * system. Once placed on a paved {@link toxiccleanup.builder.entities.tiles.Dirt} tile,
 * it increments the power in the {@link MachinesManager}
 * by 1 every 120 game ticks (approximately every 2 seconds at 60 ticks per second).
 * Power is capped at the machine manager's maximum (14 by default).
 *
 * <p>Costs {@value COST} power units to build. Rendered using {@link SpriteGallery#solarPanel}.
 *
 * @provided
 */
public class SolarPanel extends GameEntity implements PlayerOverHook {
    /**
     * The number of power units required to place this solar panel.
     */
    public static final int COST = 3;
    private static final SpriteGroup solarPanelArt = SpriteGallery.solarPanel;
    private static final int POWER_GAIN = 1;
    private static final int TICK_TARGET = 120;
    private static final char USE_KEY = 'e';
    private final TickTimer timer;
    private final DamageHandler damageHandler;

    /**
     * Constructs a new SolarPanel at the given position using a default internal
     * {@link TickTimer} set to fire every (120) frames.
     * The solar panel starts with its "default" sprite and an undamaged state.
     *
     * @requires position not null
     * @param position the position we wish to spawn the {@link SolarPanel} at.
     */
    public SolarPanel(Positionable position) {
        super(position);
        setSprite(solarPanelArt.getSprite("default"));
        timer = new RepeatingTimer(SolarPanel.TICK_TARGET);
        damageHandler = new DamageHandler();
    }

    /**Constructs a new SolarPanel with a custom damage handler (for testing purposes)
     * This constructor allows dependency injection of a {@link DamageHandler}
     * for unit testing. The solar panel starts with its "default" sprite.
     * @requires position not null
     * @requires damageHandler not null
     * @param position the position where this solar panel should be placed
     * @param damageHandler the damage handler to use for tracking damage state
     */
    public SolarPanel(Positionable position, DamageHandler damageHandler) {
        super(position);
        setSprite(solarPanelArt.getSprite("default"));
        timer = new RepeatingTimer(SolarPanel.TICK_TARGET);
        this.damageHandler = damageHandler;
    }

    /** Advances solar panel's internal timer by one tick.
     * When the timer fires (every 120 ticks), adds 1 power to the shared machine power system via
     * {@link Machines#adjust(int)}.
     * Checks for damage from weather – if damaged, shows "damaged" sprite
     * Checks if obscured by clouds – if obscured, shows "off" sprite
     * If neither damaged nor obscured, shows "default" sprite
     *
     * @requires state not null
     * @requires game not null
     * @ensures sprite reflects current state (damaged/obscured/default)
     * @ensures once timer finished, power increases by {@value POWER_GAIN}
     * @param state The state of the engine, including the mouse, keyboard information and
     *              dimension. Useful for processing keyboard presses or mouse movement.
     * @param game  The state of the game, providing access to the machine power system.
     */
    @Override
    public void tick(EngineState state, GameState game) {
        super.tick(state);

        final Weather weather = game.getWeather();
        Damage dmg = weather.getDamage(state.getDimensions(), this.getPosition());
        if (dmg != null) {
            this.damageHandler.setDamage(dmg);
        }
        if (this.damageHandler.isDamaged()) {
            setSprite(solarPanelArt.getSprite("damaged"));
            return; //exit early the solar panel is damaged!
        }

        if (weather.isObscuring(state.getDimensions(), this.getPosition())) {
            setSprite(solarPanelArt.getSprite("off"));
            return; //exit early the solar panel is obscured!
        }
        setSprite(solarPanelArt.getSprite("default"));

        timer.tick();
        if (timer.isFinished()) {
            game.getMachines().adjust(SolarPanel.POWER_GAIN);
        }
    }

    /**
     * Called when the player is on top of this object. Intended for handling
     * any interaction that occurs while the player overlaps the
     * corresponding tile or entity.
     * @requires state and game not null
     * @ensures if 'e' is pressed and currently damaged, damage is repaired
     * @param state The state of the engine, including the mouse, keyboard information and
     *              dimension. Useful for processing keyboard presses or mouse movement.
     * @param game  The state of the game, including the player and world. Can be used to query or
     *              update the game state.
     */
    @Override
    public void playerOver(EngineState state, GameState game) {
        if (!state.getKeys().isDown(SolarPanel.USE_KEY)) {
            return; //we can exit early if no use happening
        }
        if (this.damageHandler.isDamaged()) {
            this.damageHandler.repairDamage();
        }
    }
}
