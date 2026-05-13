package toxiccleanup.builder.weather;

import toxiccleanup.engine.game.Positionable;
import toxiccleanup.engine.renderer.Dimensions;
import toxiccleanup.builder.Damage;
import toxiccleanup.builder.SpriteGallery;

/**
 * <p> A {@link AcidCloud} is a weather phenomena that will move to the left, over time. </p>
 * <p> It damages any machine that it shares a tile with, changing them to their damaged state </p>
 * <p> Plays an animation loop endlessly using sprites from {@link SpriteGallery#acidcloud}. </p>
 * <p> When it reaches the leftmost edge of the screen, it will mark itself for removal. </p>
 *
 * <p> Rendered using {@link SpriteGallery#acidcloud}. </p>
 *
 */
public class AcidCloud extends Cloudable implements Damaging {
    public static final int SPAWN_TIME = 300;

    /**
     * Constructs a new AcidCloud at the given position.
     *
     * @param position the position to place the acid cloud at
     */
    public AcidCloud(Positionable position) {
        super(position, SpriteGallery.acidcloud);
    }

    /**
     * Returns damage at this cloud's position.
     *
     * @param dimensions screen dimensions (unused)
     * @param position requested position (unused)
     * @return Damage object at this cloud's position
     */
    @Override
    public Damage getDamage(Dimensions dimensions, Positionable position) {
        return new Damage(this.getPosition());
    }

    /**
     * Returns damage at this cloud's position.
     *
     * @return Damage object at this cloud's position
     */
    @Override
    public Damage getDamage() {
        return new Damage(this.getPosition());
    }
}