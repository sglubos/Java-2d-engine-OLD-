/*
 *	Copyright 2015 �ubom�r Hlavko
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package svk.sglubos.engine.core;

public abstract class BasicCore extends Core implements Runnable {
	public static final int FPS_UNLIMITED = -1;
	
	protected Thread thread;
	
	private boolean debug;
	private long sleep;
	private int ticksPerSecond;
	private int fpsLimit;
	
	public BasicCore(int ticksPerSecond, int fpsLimit, boolean debug) {
		this.ticksPerSecond = ticksPerSecond;
		this.fpsLimit = fpsLimit;
		this.debug = debug;
	}
	
	@Override
	public void run() {
		init();
		
		long lastTime = System.nanoTime();
		long lastTimeDebugOutput = System.currentTimeMillis();
		double delta = 0;
		double nanoSecPerTick = Math.pow(10, 9) / ticksPerSecond;
		int fps = 0;
		int ticks = 0;
		
		while(running){
			long now = System.nanoTime();
			delta += (now - lastTime) /nanoSecPerTick;
			lastTime = now;
				
			while(delta >= 1){
				delta--;
				tick();
				
				if (debug)
				 ticks++;
			}
			
			render();
			
			if (debug)
				fps++;
			
			if(fpsLimit != FPS_UNLIMITED) {
				try {
					Thread.sleep(sleep);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
				
			if(debug && (System.currentTimeMillis() - lastTimeDebugOutput) >= 1000){
				System.out.println("[DEBUG] ticks: " + ticks + " fps: " + fps);
				lastTimeDebugOutput += 1000;
				fps = 0;
				ticks = 0;
			}
		}
		
	  stopped();	
	}

	protected void setFPSLimit(int fpsLimit) {
		this.fpsLimit = fpsLimit;
		this.sleep = (long) (Math.pow(10, 9) / fpsLimit);
	}
	
	protected int getFPSLimit() {
		return fpsLimit;
	}
	
	protected int getTPSLimit() {
		return ticksPerSecond;
	}
	
	protected boolean isDebug() {
		return debug;
	}
	
	protected void setDebug(boolean debug) {
		this.debug = debug;
	}
	
	@Override
	protected void start() {
		running = true;
		thread = new Thread(this,"core");
		thread.start();
	}

	@Override
	protected void stop() {
	 running = false;
	}
}
