package toxiccleanup.builder.machines;

import toxiccleanup.engine.EngineState;
import toxiccleanup.engine.art.sprites.SpriteGroup;
import toxiccleanup.engine.game.Positionable;
import toxiccleanup.builder.Damage;
import toxiccleanup.builder.GameState;
import toxiccleanup.builder.SpriteGallery;
import toxiccleanup.builder.entities.GameEntity;
import toxiccleanup.builder.entities.PlayerOverHook;
import toxiccleanup.builder.weather.Lightning;
import toxiccleanup.builder.weather.LightningDamage;
import toxiccleanup.builder.weather.Weather;

/**
 * A {@link LightningRod} is a machine that passively attracts
 * nearby {@link Lightning} to its position, the effective radius is {@value RADIUS} pixels.
 * <p>Can only be placed on a paved {@link toxiccleanup.builder.entities.tiles.Dirt} tile.</p>
 *
 * <p>The lightning rod is immune to {@link LightningDamage} (it does not get
 * damaged by lightning strikes), but can be damaged by other damage types
 * such as acid clouds. The rod can be repaired by the player pressing the
 * use key ('e') while standing on it.
 *
 * <p>Costs {@value COST} power units to build.</p>
 * <p>Rendered using {@link SpriteGallery#chasm}.</p>
 * @invariant damageHandler never null
 * @invariant RADIUS always constant, and {@value RADIUS}
 * @invariant cost always {@value COST} and constant
 * @provided
 */
public class LightningRod extends GameEntity implements PlayerOverHook, Damageable {
    public static final int RADIUS = 300;
    public static final int COST = 1;
    private static final SpriteGroup art = SpriteGallery.lightningrod;
    private final DamageHandler damageHandler;
    private static final char USE_KEY = 'e';

    /** Constructs a new LightningRod at the given position.
     * The lightning rod starts with its "default" sprite and an undamaged state.
     * @requires position not null
     * @param position the position where lightning rod should be placed
     */
    public LightningRod(Positionable position) {
        super(position);
        this.setSprite(art.getSprite("default"));
        this.damageHandler = new DamageHandler();
    }

    /**
     * Advances the lightning rod by one game tick.
     * Each tick, lightning rod checks for:
     * damage from weather, if damaged, shows "damaged" sprite,
     * if undamaged, shows "default" sprite and attracts nearby lightning
     *
     * @requires state not null
     * @requires game not null
     * @param state The state of the engine, including the mouse, keyboard information and
     *              dimension. Useful for processing keyboard presses or mouse movement.
     * @param game  The state of the game, including the player and world. Can be used to query or
     *              update the game state.
     */
    @Override
    public void tick(EngineState state, GameState game) {
        super.tick(state, game);

        final Weather weather = game.getWeather();
        final Damage dmg = weather.getDamage(state.getDimensions(), this.getPosition());

        if (dmg != null && !dmg.getType().equals(LightningDamage.TYPE)) {
            this.damageHandler.setDamage(dmg);
        }
        if (this.isDamaged()) {
            setSprite(art.getSprite("damaged"));
            return; //exit early the machine is damaged!
        }
        setSprite(art.getSprite("default"));
        weather.applyLightningRod(this.getPosition());
    }

    /**
     * Called when the player is on top of this object. Intended for handling
     * any interaction that occurs while the player overlaps the
     * corresponding tile or entity.
     * Specifically, if the use key ('e') is pressed while standing on the rod and the rod
     * is damaged, the rod is repaired.
     *
     * @requires state not null
     * @requires game not null
     * @param state The state of the engine, including the mouse, keyboard information and
     *              dimension. Useful for processing keyboard presses or mouse movement.
     * @param game  The state of the game, including the player and world. Can be used to query or
     *              update the game state.
     */
    @Override
    public void playerOver(EngineState state, GameState game) {
        if (!state.getKeys().isDown(USE_KEY)) {
            return; //we can exit early if no use happening
        }
        if (this.damageHandler.isDamaged()) {
            this.damageHandler.repairDamage();
        }
    }

    /**
     * Returns whether this lightning rod is currently damaged.
     *
     * @return true if damaged, false otherwise
     */
    @Override
    public boolean isDamaged() {
        return this.damageHandler.isDamaged();
    }

    /**
     * Sets this lightning rod to a damaged state.
     * When damaged, the rod shows the "damaged" sprite and does not attract
     * lightning until repaired.
     *
     * @param dmg the damage object describing what caused damage
     */
    @Override
    public void setDamage(Damage dmg) {
        this.damageHandler.setDamage(dmg);
    }

    /**
     * Repairs this lightning rod, returning it to an undamaged state.
     * After repair, the rod shows the "default" sprite and resumes
     * attracting lightning.
     */
    @Override
    public void repairDamage() {
        this.damageHandler.repairDamage();

    }
}
