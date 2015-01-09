package svk.sglubos.engine.gfx.sprite;

import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;

//TODO Documentation

public class SpriteSheet {
	private BufferedImage image;
	private int width;
	private int height;
	
	private Sprite[] sprites = null;
	
	public SpriteSheet(BufferedImage image){
		if(image == null) {
			throw new IllegalArgumentException("BufferedImage can not be null");
		}
		this.image = image;
		this.width = image.getWidth();
		this.height = image.getHeight();
	}
	
	public SpriteSheet(BufferedImage image, int spriteWidth, int spriteHeight){
		this(image);
		createSprites(spriteWidth, spriteHeight);
	}
	
	public BufferedImage getSubImabe(int x, int y, int width, int height) {
		BufferedImage image = null;
		try{
			image = this.image.getSubimage(x, y, width, height);			
		}catch (RasterFormatException e) {
			e.printStackTrace();
		}
		return image;
	}
	
	public Sprite getSprite(int x, int y, int width, int height) {
		return  new Sprite(width, height, image.getRGB((x * width), (y * height) , width, height,null, 0, width));
	}
	
	public Sprite[] getSprites(){
		if(sprites == null){
			throw new NullPointerException("Sprites were not created");
		}
		return sprites;
	}
	
	public void createSprites(int width,int height) {
		int horizontalSprites = this.width / width;
		int verticalSprites = this.height / height;
		
		Sprite[] sprites = new Sprite[this.width / width * this.height / height];
		
		for(int y = 0; y < verticalSprites; y++) {
			for(int x = 0; x < horizontalSprites; x++) {
				sprites[x + y * horizontalSprites] = new Sprite(width, height, image.getRGB((x * width), (y * height) , width, height,null, 0, this.width));
			}
		}
		this.sprites = sprites;
	}
}
