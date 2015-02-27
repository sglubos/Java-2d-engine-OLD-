package svk.sglubos.engine.gfx.animation;

import svk.sglubos.engine.gfx.Screen;
import svk.sglubos.engine.gfx.sprite.Sprite;
import svk.sglubos.engine.gfx.sprite.SpriteSheet;
//TODO document
/**
 *<code>Sprite Animation</code> class provides ability to create basic animation from {@link svk.sglubos.engine.gfx.sprite.Sprite Sprite} objects.
 *This class inherits from {@link svk.sglubos.engine.gfx.animation.Animation Animation} class.
 */

public class SpriteAnimation extends Animation {
	protected Sprite[] sprites;
	
	public SpriteAnimation(long frameDelayInMilisecs, byte timeFormat, Sprite[] sprites) {
		super(frameDelayInMilisecs, timeFormat, sprites.length);
		this.sprites = sprites;
	}
	
	public SpriteAnimation(SpriteSheet spriteSheet,long frameDelay, int startFrame, int endFrame, byte timeFormat) {
		super(frameDelay, startFrame, endFrame, spriteSheet.getSprites().length, timeFormat);
		this.sprites = spriteSheet.getSprites();
	}

	@Override
	public void render(Screen screen, int x, int y) {
		screen.renderSprite(sprites[currentFrame], x, y);
	}
}