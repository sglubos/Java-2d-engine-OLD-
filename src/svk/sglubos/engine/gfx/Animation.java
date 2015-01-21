package svk.sglubos.engine.gfx;

import svk.sglubos.engine.utils.MessageHandler;
import svk.sglubos.engine.utils.Timer;
import svk.sglubos.engine.utils.TimerTask;

//TODO documentation

/**
 * Provides abilities to create basic animations.<br>
 * This class handles timing, starting and stopping, and also provides reverse, but not rendering. 
 * Rendering have to be created manually.<br>
 * <p>
 * Timing is based on {@link svk.sglubos.engine.utils.Timer Timer} class. 
 * Every time when {@link #timer timer} finishes loop after specified {@link #frameDelay time} (delay), 
 * {@link #switchFrame()} method is called.
 * The {@link #switchFrame()} method switches to next frame of animation. Switching to next frame means to increase,
 * or decrease (if reverse decrease) value of {@link #currentFrame} by one.
 * {@link #switchFrame() Update frame} method also keeps {@link #currentFrame} value in bounds of {@link #startFrame} and {@link #endFrame}.
 * <strong>But keep in mind, that {@link #startFrame} < {@link #endFrame}.</strong>
 * <p>
 * To play animation from {@link #startFrame} to {@link #endFrame} use {@link #start(boolean)} method 
 * and to play from {@link #endFrame} to {@link #startFrame} use {@link #startReverse(boolean)} method. A boolean argument in these methods determines, 
 * if animation will be played once, or repeatedly played until {@link #stop()} method is called. If called {@link #start(boolean)} with argument <code>false</code>,
 * animation will stop when the value {@link #currentFrame} reaches value of {@link #endFrame}, and if called with argument <code>true</code>, 
 * {@link #currentFrame} will be set to {@link #startFrame} when {@link #currentFrame} reaches {@link #endFrame}. 
 * This causes that animation will not stop until {@link #stop()} method is manually called.
 * In case of {@link #startReverse(boolean)} method it is the same, but when the argument is <code>true</code>, 
 * {@link #currentFrame} will be set to {@link #endFrame} when {@link #currentFrame} reaches {@link #startFrame}.<br>
 * <p>
 * To update animation {@link #timer} call method {@link #tick()} every game loop update.<br>
 * 
 * @see #Animation(long, byte, int) basic constructor
 * @see #Animation(long, int, int, int, byte) constructor with ability to set startFrame and endFrame
 * @see #start(boolean) 
 * @see #stop() 
 * @see svk.sglubos.engine.utils.Timer
 */
public abstract class Animation {
	/**
	 * Delay between frame switches of animation.<br>
	 * This variable is initialized in {@link #Animation(long, byte, int) constructor}.
	 * The delay can be set by {@link #setFrameDelay(long)} method.
	 * A {@link #delayFormat delay format} (unit) is also initialized in {@link #Animation(long, byte, int) constructor}. Avaible units are all units in {@link svk.sglubos.engine.utils.Timer Timer}: 
	 * {@link svk.sglubos.engine.utils.Timer#DELAY_FORMAT_MILLISECS milliseconds}, {@link svk.sglubos.engine.utils.Timer#DELAY_FORMAT_SECS seconds} 
	 * and {@link svk.sglubos.engine.utils.Timer#DELAY_FORMAT_TICKS ticks}.
	 * The delay format can be also set by {@link #setFrameDelay(long)} method, but the frame delay will not be converted to that specific unit, 
	 * because the conversion is not implemented.
	 * 
	 * @see #Animation(long, byte, int) constructor
	 * @see #svk.sglubos.engine.utils.Timer
	 * @see #setFrameDelay(long)
	 * @see #getFrameDelay()
	 * @see #getTimeFormat()
	 * @see #setTimeFormat(byte)
	 */
	protected long frameDelay;
	
	/**
	 * Format of {@link #frameDelay delay} between frame switches.<br>
	 * The <code>delayFormat</code> represents the unit of {@link #frameDelay delay} between frame switches. Supported time formats are all formats from {@link svk.sglubos.engine.utils.Timer Timer}.
	 * <p>
	 * <h1>Supported time formats:</h1>
	 * {@link svk.sglubos.engine.utils.Timer#DELAY_FORMAT_SECS DELAY_FORMAT_SECS}<br>
	 * {@link svk.sglubos.engine.utils.Timer#DELAY_FORMAT_TICKS DELAY_FORMAT_TICKS}<br>
	 * {@link svk.sglubos.engine.utils.Timer#DELAY_FORMAT_MILLISECS DELAY_FORMAT_MILLISECS}<br>
	 * <p> 
	 * This variable is initialized in {@link #Animation(long, byte, int) constructor} and can be set by {@link #setTimeFormat(byte)} method.
	 * <strong>Changing of time format does not convert delay value to that specific format.</strong><br>
	 * To obtain value of this variable use {@link #getTimeFormat()} method.
	 * 
	 * @see #frameDelay
	 * @see #setFrameDelay(long)
	 * @see #getFrameDelay()
	 * @see svk.sglubos.engine.utils.Timer
	 */
	protected byte delayFormat;
	
	/**
	 * Determines number of frames, first frame is <code>0</code> and last frame is <code>frames - 1</code>.<br>
	 * The {@link #currentFrame} can be set by {@link #setCurrentFrame(int)} method.<br>
	 * This variable is initialized in {@link #Animation(long, byte, int) constructor}.<br>
	 * 
	 * @see #Animation(long, byte, int)
	 * @see #setCurrentFrame(int)
	 * @see #setStartFrame(int)
	 * @see #setEndFrame(int)
	 */
	protected int frames;
	
	/**
	 * Determines if animation will be played once or played continuously until {@link #stop()} method will be called.<br>
	 * If <code>true</code> animation will be player continuously and if <code>false</code>, animation will be played only once.<br>
	 * This variable is set in {@link #start(boolean)} and {@link #startReverse(boolean)} method to value passed as an argument in these methods.<br>
	 * 
	 * @see #start(boolean)
	 * @see #st(boolean)
	 * @see #startReverse(boolean)
	 * @see #stop()
	 */
	protected boolean loop;
	
	/**
	 * Determines if animation is currently running (that frames are being updated).<br>
	 * This variable is set in {@link #start(boolean)}, {@link #startReverse(boolean)} and {@link #stop()} methods.<br>
	 * The {@link #start(boolean)} and {@link #startReverse(boolean)} methods set this variable to <code>true</code>,
	 * what causes that the {@link #timer} is updated every time {@link #tick()} method is called.<br>
	 * The {@link #stop()} method sets this variable to <code>false</code>, 
	 * {@link #timer} will not be updated event if {@link #tick()} method is called.<br>
	 * 
	 * @see #tick()
	 * @see #timer()
	 * @see #start(boolean)
	 * @see #startReverse(boolean)
	 * @see #stop()
	 */
	protected boolean running;
	
	/**
	 * Determines if animation frames are played from {@link #startFrame} to {@link #endFrame} or from {@link #endFrame} to {@link #startFrame}.<br>
	 * This variable is set in {@link #start(boolean)} and {@link #startReverse(boolean)} methods.<br>
	 * <p>
	 * The {@link #start(boolean)} method sets this variable to <code>false</code>,
	 * what causes that the frames are played from {@link #startFrame} to {@link #endFrame}, 
	 * so the {@link #currentFrame} is increased by 1 every time when {@link #switchFrame()} method is called.<br>
	 * The {@link #startReverse(boolean)} method sets this variable to <code>true</code>,
	 * what causes that the frames are played from {@link #endFrame} to {@link #startFrame}, 
	 * so the {@link #currentFrame} is decreased by 1 every time when {@link #switchFrame()} method is called.<br>
	 * 
	 * @see #currentFrame
	 * @see #startFrame
	 * @see #endFrame
	 * @see #start(boolean)
	 * @see #startReverse(boolean)
	 * @see #switchFrame()
	 */
	protected boolean reverse;
	
	/**
	 * This variable stores the current frame of animation.<br>
	 * This variable is updated in {@link #switchFrame()} method, 
	 * can be set by {@link #setCurrentFrame(int)} method and can be obtained by {@link #getCurrentFrame()}.<br>
	 * <p>
	 * The value of this variable is kept between {@link #startFrame} and {@link #endFrame}, 
	 * so the first frame is {@link #startFrame} and the last frame is {@link #endFrame}.<br>
	 * This variable is increased or decreased by <code>1</code>, depended on {@link #reverse}.
	 * If reverse is <code>true</code> the value is decreased and if <code>false</code> the value is increased.
	 * The reverse is set in {@link #start(boolean)} to false and in {@link #startReverse(boolean)} to true.<br>
	 * 
	 * @see #reverse
	 * @see #startFrame
	 * @see #endFrame
	 * @see #switchFrame()
	 * @see #setCurrentFrame(int)
	 * @see #getCurrentFrame()
	 * @see #start(boolean)
	 * @see #startReverse(boolean)
	 */
	protected int currentFrame;
	
	/**
	 * The first frame of animation.<br>
	 * This variable is initialized in {@link #Animation(long, int, int, int, byte) constructor} 
	 * and if used {@link #Animation(long, byte, int)} the value is set to <code>0</code>.
	 * This variable can be set by {@link #setStartFrame(int)} method, but the value have to be positive,
	 * lower than {@link #endFrame} and not bigger than {@link #frames}-1.<br>
	 * <p>
	 * This is the lowest value of {@link #currentFrame}, so if {@link #start(boolean)} method called with argument <code>false</code>,
	 * the animation starts at this frame and if {@link #currentFrame} reaches value of {@link #endFrame} the animation will stop,
	 * but if called with argument <code>true</code>, after {@link #currentFrame} reaches the {@link #endFrame},
	 * the animation than starts again from this frame.<br>
	 * <P>
	 * If called {@link #startReverse(boolean)} with argument <code>false</code>, the animation starts at {@link #endFrame}
	 * and if {@link #currentFrame} reaches this value the animation will stop, but if called with argument<code>true</code>, 
	 * after {@link #currentFrame} reaches this value the animation than starts again from {@link #endFrame}.
	 * 
	 * @see #Animation(long, byte, int) constructor (sets to 0)
	 * @see #Animation(long, int, int, int, byte) constuctor (sets to specified value)
	 * @see #switchFrame()
	 * @see #setStartFrame(int)
	 * @see #endFrame
	 * @see #currentFrame
	 */
	protected int startFrame;
	
	/**
	 * The last frame of animation.<br>
	 * This variable is initialized in {@link #Animation(long, int, int, int, byte) constructor} 
	 * and if used {@link #Animation(long, byte, int)} the value is set to <code>{@link #frames}-1</code>.
	 * This variable can be set by {@link #setEndFrame(int)} method, but the value have to be positive,
	 * bigger than {@link #startFrame} but not bigger than {@link #frames}-1.<br>
	 * <p>
	 * This is the highest value of {@link #currentFrame}, so if {@link #start(boolean)} method called with argument <code>false</code>,
	 * the animation starts at {@link #startFrame} frame and if {@link #currentFrame} reaches this value the animation will stop,
	 * but if called with argument <code>true</code>, after {@link #currentFrame} reaches this value,
	 * the animation than starts again from {@link #startFrame}.<br>
	 * <P>
	 * If called {@link #startReverse(boolean)} with argument <code>false</code>, the animation starts at this frame
	 * and if {@link #currentFrame} reaches the {@link #startFrame} the animation will stop, but if called with argument<code>true</code>, 
	 * after {@link #currentFrame} reaches {@link #startFrame} value the animation than starts again from this frame.
	 * 
	 * @see #Animation(long, byte, int) constructor (sets to frames-1)
	 * @see #Animation(long, int, int, int, byte) constuctor (sets to specified value)
	 * @see #switchFrame()
	 * @see #setEndFrame(int)
	 * @see #startFrame
	 * @see #currentFrame
	 */
	protected int endFrame;
	
	/**
	 * {@link svk.sglubos.engine.utils.Timer Timer} object which handles timing between frame switches.<br>
	 * This object is initialized in {@link #Animation(long, int, int, int, byte) constructor}.<br>
	 * <p>
	 * The timer handles timing between frame switches.<br>
	 * Timer does cycles long {@link #frameDelay}s value in unit {@link #delayFormat}. 
	 * The {@link #frameDelay} can be set by {@link #setFrameDelay(long)} method 
	 * and the {@link #delayFormat} can be set by {@link #setTimeFormat(byte)} method.
	 * <h1>Avaible formats: </h1>
	 * {@link svk.sglubos.engine.utils.Timer#DELAY_FORMAT_MILLISECS DELAY_FORMAT_MILLISECS}<br>
	 * {@link svk.sglubos.engine.utils.Timer#DELAY_FORMAT_SECS DELAY_FORMAT_SECS}<br>
	 * {@link svk.sglubos.engine.utils.Timer#DELAY_FORMAT_TICKS DELAY_FORMAT_TICKS}<br>
	 *	<p> 
	 * Every time timer completes cycle, {@link svk.sglubos.engine.utils.TimerTask TimerTask} {@link #frameSwitch}s 
	 * {@link svk.sglubos.engine.utils.TimerTask#timeSwitch() timeSwitch()} method is called, which calls {@link #switchFrame()} method.
	 * The {@link #switchFrame()} method switches to the next frame.<br> 
	 * 
	 * @see svk.sglubos.engine.utils.Timer
	 * @see svk.sglubos.engine.utils.TimerTask
	 * @see #setFrameDelay(long)
	 * @see #setTimeFormat(byte)
	 * @see #switchFrame()
	 * @see #delayFormat
	 * @see #frameDelay
	 * @see #frameSwitch
	 */
	protected Timer timer;
	
	/**
	 * {@link svk.sglubos.engine.utils.TimerTask TimerTask} which calls {@link #switchFrame()} method every time {@link #timer} finishes cycle.<br>
	 * 
	 * @see #timer
	 * @see #switchFrame()
	 * @see svk.sglubos.engine.utils.TimerTask
	 */
	protected TimerTask frameSwitch = () -> switchFrame();
	
	/**
	 * Constructs new animation with specified {@link #frameDelay delay between frame switches}, {@link #delayFormat format of delay} and {@link #frames number of frames}.
	 * The {@link #startFrame} of animation is set to <code>0</code> and the {@link #endFrame} of animation is set to <code>frames - 1 </code>.<br>
	 * Uses this {@link #Animation(long, int, int, int, byte) constructor}.<br>
	 * 
	 * @param frameDelay delay between frame switches
	 * @param timeFormat format (time unit) of delay between frame switches
	 * @param frames number of frames in this animation<br><br>
	 * 
	 * @see #Animation(long, int, int, int, byte) constructor
	 * @see #frameDelay
	 * @see #delayFormat
	 * @see #frames
	 * @see #startFrame
	 * @see #endFrame
	 */
	public Animation(long frameDelay, byte timeFormat, int frames) {
		this(frameDelay, 0, frames - 1, frames, timeFormat);
	}
	
	/**
	 * Constructs new animation with specified {@link #frameDelay delay between frame switches}, 
	 * {@link #startFrame start frame of animation}, {@link #endFrame end frame of animation},  
	 * {@link #frames number of frames} and {@link #delayFormat format of delay}.<br>
	 * 
	 * <h1>Initializes:</h1>
	 * {@link #frameDelay FrameDelay} to value passed as a parameter.<br>
	 * {@link #startFrame StartFrame} using {@link #setStartFrame(int)} method with value passed as a parameter.
	 * The {@link #setStartFrame(int)} method keeps the start frame in bounds of <code>0</code> and {@link #frames } - 1.<br>
	 * {@link #endFrame EndFrame} using {@link #setEndFrame(int)} method with value passes as a parameter. 
	 * The {@link #setEndFrame(int)} method keeps the end frame in bounds of <code>0</code> and {@link #frames} - 1.<br>
	 * The {@link #frames Frames} to value passed as a parameter.<br>
	 * The {@link #delayFormat DelayFormat} to value passed as a parameter, the avaible formats (units) are formats from {@link svk.sglubos.engine.utils.Timer}:
	 * {@link svk.sglubos.engine.utils.Timer#DELAY_FORMAT_MILLISECS DELAY_FORMAT_MILLISECS}, {@link svk.sglubos.engine.utils.Timer#DELAY_FORMAT_SECS DELAY_FORMAT_SECS}
	 * and {@link svk.sglubos.engine.utils.Timer#DELAY_FORMAT_TICKS DELAY_FORMAT_TICKS}.<br>
	 * {@link #timer Timer} which handles timing between frame switches of animation. 
	 * The {@link #timer} is initialized with {@link svk.sglubos.engine.utils.TimerTask TimerTask} {@link #frameSwitch frame switch}, 
	 * which calls {@link #switchFrame()} method every time the {@link #timer} completes cycle after specified {@link #frameDelay delay}, 
	 * the delay value passed as a parameter and the format passed as a parameter.<br><br>
	 * 
	 * @param frameDelay delay between frame switches
	 * @param startFrame start frame of animation
	 * @param endFrame end frame of animation
	 * @param frames number of animation frames
	 * @param timeFormat format of delay (time unit)
	 */
	public Animation(long frameDelay, int startFrame, int endFrame, int frames, byte timeFormat) {
		this.frameDelay = frameDelay;
		this.delayFormat = timeFormat;
		this.frames = frames;
		
		setStartFrame(startFrame);
		setEndFrame(endFrame);
		timer = new Timer(frameSwitch, timeFormat, frameDelay);
	}
	
	public void startReverse(boolean loop){
		if(loop) {
			timer.startInfiniteLoop();
		} else {
			timer.start();
		}
		
		currentFrame = endFrame;
		running = true;
		reverse = true;
		this.loop = loop;
	}
	
	public void start(boolean loop) {
		if(loop) {
			timer.startInfiniteLoop();
		} else {
			timer.startLoop(endFrame - startFrame);
		}
		
		currentFrame = startFrame;
		running = true;
		reverse = false;
		this.loop = loop;
	}
	
	public void stop() {
		timer.stop();
		running = false;
	}
	
	public void tick() {
		if(running){
			timer.update();
		}
	}
	
	protected void switchFrame() {
		if(reverse){
			currentFrame--;
		}else{
			currentFrame++;			
		}
		
		if(currentFrame == startFrame) {
			if(loop){
				currentFrame = endFrame;							
			}else {
				stop();
			}
		} else if (currentFrame == endFrame) {
			if(loop){
				currentFrame = startFrame;							
			}else {
				stop();
			}
		}
	}
	
	public void setFrameDelay(long frameDelay) {
		if(frameDelay < 0) {
			MessageHandler.printMessage("ANIMATION", MessageHandler.ERROR, "Invalind animation frame delay, delay can not be less than zero " + frameDelay);
			throw new IllegalArgumentException("Frame delay can not be less than 0: " + frameDelay);
		}
		this.frameDelay = frameDelay;
		timer.setDelay(frameDelay);
	}
	
	public long getFrameDelay() {
		return frameDelay;
	}
	
	public void setTimeFormat(byte timeFormat) {
		this.delayFormat = timeFormat;
	}
	
	public byte getTimeFormat() {
		return delayFormat;
	}
	
	public void setStartFrame(int start) {
		if(start < 0 || start >= frames) {
			MessageHandler.printMessage("ANIMATION", MessageHandler.ERROR, "Illegal starting frame, frame cannot be less than zero and more than frames -1: " + start);
			throw new IllegalArgumentException("Illegal starting frame: " + start);
		}
		
		this.startFrame = start;
	}
	
	public void setEndFrame(int end) {
		if(end < 0 || end >= frames) {
			MessageHandler.printMessage("ANIMATION", MessageHandler.ERROR, "Illegal ending frame, frame cannot be less than zero and more than frames -1: " + end);
			throw new IllegalArgumentException("Illegal ending frame: " + end);
		}
		
		this.endFrame = end;
	}
	
	public void setCurrentFrame(int frame){
		if(frame < 0 || frame >= this.frames) {
			throw new IllegalArgumentException("Frame can not be set to value less than zero and more than the acutual animation frame array length: " + frame);
		}
		this.currentFrame = frame;
	}
	
	public int getCurrentFrame(){
		return currentFrame;
	}
	
	public abstract void render(Screen screen, int x, int y);
}
