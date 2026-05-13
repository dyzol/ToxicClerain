package toxiccleanup.builder.weather;

import toxiccleanup.engine.EngineState;
import toxiccleanup.engine.art.sprites.SpriteGroup;
import toxiccleanup.engine.game.Positionable;
import toxiccleanup.engine.timing.RepeatingTimer;
import toxiccleanup.engine.timing.TickTimer;
import toxiccleanup.builder.GameState;
import toxiccleanup.builder.SpriteGallery;
import toxiccleanup.builder.entities.GameEntity;

/**
 * <p> A {@link Cloudable} is a weather phenomena that will move to the left, over time. </p>
 * <p> It obscures {@link toxiccleanup.builder.machines.SolarPanel}s that
 * it is sharing a tile with. </p>
 * <p> When it reaches the leftmost edge of the screen, it will mark itself for removal. </p>
 *
 * This is an abstract base class. Concrete subclasses must provide their own
 *  * sprite group and may override animation behavior.
 * <p> Rendered using {@link SpriteGallery#cloud}. </p>
 *
 * @provided
 */
public abstract class Cloudable extends GameEntity implements Obscuring {
    // constants
    public static final int MOVEMENT_TIME = 1;
    private static final int SPEED = 2;
    private static final int ANIMATION_INTERVAL = 12;

    // fields
    private final TickTimer movementTimer;
    private final TickTimer animTimer;
    private int currentFrame = 1;
    private final int maxFrames;
    private final SpriteGroup art;

    /**
     * Constructs a new Cloudable with a custom movement timer (for testing).
     *
     * @param position the position to place the cloud at
     * @param art the sprite group for this cloud type
     * @param movementTimer custom movement timer
     */
    protected Cloudable(Positionable position, SpriteGroup art, TickTimer movementTimer) {
        super(position);
        this.art = art;
        this.movementTimer = movementTimer;
        this.animTimer = new RepeatingTimer(ANIMATION_INTERVAL);
        this.maxFrames = art.getSprites().size();
        setSprite(art.getSprite(String.valueOf(currentFrame)));
    }

    /**
     * Constructs a new Cloudable with default movement timer.
     *
     * @param position the position to place the cloud at
     * @param art the sprite group for this cloud type
     */
    protected Cloudable(Positionable position, SpriteGroup art) {
        this(position, art, new RepeatingTimer(MOVEMENT_TIME));
    }

    /**
     * Updates the animation frame.
     * Loops back to frame 1 after reaching the last frame.
     */
    private void updateAnimation() {
        animTimer.tick();
        if (animTimer.isFinished()) {
            currentFrame++;
            if (currentFrame > maxFrames) {
                currentFrame = 1;  // Loop back to start
            }
            setSprite(art.getSprite(String.valueOf(currentFrame)));
        }
    }

    /**
     * Updates the cloud's position.
     * Moves left at constant speed.
     */
    private void updateMovement() {
        movementTimer.tick();
        if (movementTimer.isFinished()) {
            setX(getX() - SPEED);
        }
    }

    /**
     * Checks if cloud has moved off-screen and marks for removal if so.
     */
    private void checkBoundary() {
        if (getX() < 0) {
            markForRemoval();
        }
    }

    /**
     * Template method for tick behavior.
     * Subclasses can override but should call super.tick().
     *
     * @param state The state of the engine
     * @param game  The state of the game
     */
    @Override
    public void tick(EngineState state, GameState game) {
        super.tick(state, game);
        updateAnimation();
        updateMovement();
        checkBoundary();
    }
}
