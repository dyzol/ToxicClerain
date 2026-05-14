package toxiccleanup.builder.weather;

import toxiccleanup.engine.EngineState;
import toxiccleanup.engine.art.sprites.SpriteGroup;
import toxiccleanup.engine.game.Positionable;
import toxiccleanup.engine.renderer.Dimensions;
import toxiccleanup.engine.timing.FixedTimer;
import toxiccleanup.engine.timing.RepeatingTimer;
import toxiccleanup.engine.timing.TickTimer;
import toxiccleanup.builder.Damage;
import toxiccleanup.builder.GameState;
import toxiccleanup.builder.SpriteGallery;
import toxiccleanup.builder.entities.GameEntity;

/**
 * <p> A {@link Lightning} is a weather phenomena that will spawn at a given location.</p>
 * <p> It exists for a set lifespan (see {@value #LIFESPAN} then will mark itself for removal. </p>
 * <p> When it reaches the leftmost edge of the screen, it will mark itself for removal. </p>
 * Lightning only deals {@link LightningDamage} during frames 5 and 6 of it is animation cycle.
 * <p> See also: {@link toxiccleanup.builder.machines.LightningRod} </p>
 *
 * @invariant lifespanTimer and animTimer are never null
 * @invariant animFrame is always between 1 and finalAnimFrameIndex inclusive
 * @invariant Lightning deals damage only during frames 5 and 6 of its animation
 * @provided
 */
public class Lightning extends GameEntity implements Damaging, Attractable {
    public static final int SPAWN_TIME = 120;
    private static final int LIFESPAN = 60;
    private final TickTimer lifespanTimer = new FixedTimer(LIFESPAN);

    private final int finalAnimFrameIndex;
    private final TickTimer animTimer;
    private int animFrame = 1;
    private static final int DAMAGE_FRAME_START = 5;
    private static final int DAMAGE_FRAME_END = 6;

    private static final SpriteGroup art = SpriteGallery.lightning;

    /**
     * Constructs {@link Lightning} at the given position.
     * The lightning's animation timing is calculated so that the full
     * animation sequence completes exactly as the lightning expires.
     *
     * @requires position not null
     * @param position the position we wish to construct the lightning instance at.
     */
    public Lightning(Positionable position) {
        super(position);

        setSprite(art.getSprite("1"));

        finalAnimFrameIndex = art.getSprites().size() - 1;
        int animTickInterval = ((int) (double) (LIFESPAN / finalAnimFrameIndex));
        animTimer = new RepeatingTimer(animTickInterval);
    }

    /** Advances lightning by one game tick.
     * Each tick updates both the lifespan timer and animation timer.
     * When the lifespan timer finishes, the lightning marks itself for removal.
     *
     * @requires state and game must not be null
     * @ensures if lifespan expires, lightning is marked for removal
     * @param state The state of the engine, including the mouse, keyboard information and
     *              dimension. Useful for processing keyboard presses or mouse movement.
     * @param game  The state of the game, including the player and world. Can be used to query or
     *              update the game state.
     */
    @Override
    public void tick(EngineState state, GameState game) {
        super.tick(state, game);
        lifespanTimer.tick();
        animTimer.tick();
        if (lifespanTimer.isFinished()) {
            markForRemoval();
        }
        if (animTimer.isFinished()) {
            this.updateArt();
        }
    }

    /** Returns damage at the lightning's position regardless of animation state.
     *
     * @requires dimensions and position not null
     * @param dimensions screen dimensions
     * @param position requested position
     * @return a {@link LightningDamage} object at the lightning's position
     */
    @Override
    public Damage getDamage(Dimensions dimensions, Positionable position) {
        return new LightningDamage(this.getPosition());
    }

    /**
     * Returns an instance of the damage Lightning can do
     * if it is currently in a damage-dealing animation frame.
     *
     * @return instance of {@link Damage} if in damaging frame, null otherwise
     */
    public Damage getDamage() {
        if (this.isDamaging()) {
            return new LightningDamage(this.getPosition());
        }
        return null;
    }

    /**
     * Returns if the {@link Lightning} is currently in its state that would deal {@link Damage}
     *
     * @return true if current animation frame is 5 or 6 (damage-dealing frames), false otherwise
     */
    public boolean isDamaging() {
        return animFrame == DAMAGE_FRAME_START || animFrame == DAMAGE_FRAME_END;
    }

    /**
     * Handles updating the anim to the next sprite,
     * adjusting our internal index and resetting it to the start if we go past the final index.
     * @ensures animFrame is incremented by 1, capped at finalAnimFrameIndex
     */
    private void updateArt() {
        animFrame += 1;
        if (animFrame > finalAnimFrameIndex) { //reset our animation back to the start
            animFrame = finalAnimFrameIndex;
        }
        setSprite(art.getSprite(animFrame + ""));
    }

    /** Moves a lightning bolt to target position when attracted by lightning rod
     * @requires targetPosition not null
     * @param targetPosition the position to attract to
     */
    @Override
    public void attractTo(Positionable targetPosition) {
        setX(targetPosition.getX());
        setY(targetPosition.getY());
    }
}
