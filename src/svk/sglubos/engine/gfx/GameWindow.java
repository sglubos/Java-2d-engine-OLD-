/*
 *	Copyright 2015 Ľubomír Hlavko
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

package svk.sglubos.engine.gfx;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JFrame;

import svk.sglubos.engine.utils.debug.DebugStringBuilder;
@SuppressWarnings("serial")
public class GameWindow extends JFrame {
	protected double widthScale;
	protected double heightScale;
	
	protected Screen screen;
	protected RenderCanvas canvas;
	protected GraphicsDevice device;
	
	public GameWindow(int screenWidth, int screenHeight, String title) {
		this(screenWidth, screenHeight, title, 1.0, Color.black, GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice());
	}
	
	public GameWindow(int screenWidth,int screenHeight, String title, double canvasScale){
		this(screenWidth, screenHeight, title, canvasScale, Color.black, GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice());
	}
	
	public GameWindow(int screenWidth, int screenHeight, String title, Color defaultScreenColor){
		this(screenWidth, screenHeight, title, 1.0, defaultScreenColor, GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice());
	}
	
	public GameWindow(int screenWidth, int screenHeight, String title, double canvasScale, Color defaultScreenColor) {
		this(screenWidth, screenHeight, title, canvasScale, defaultScreenColor, GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice());
	}
	
	public GameWindow(int screenWidth, int screenHeight, String title, double screenScale, Color defaultScreenColor, GraphicsDevice graphicsDevice) {
		super(title);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		
		screen = new Screen(screenWidth, screenHeight, defaultScreenColor);
		canvas = new RenderCanvas(screen, screenScale);
		
		add(canvas, BorderLayout.CENTER);
		pack();
		setLocationRelativeTo(null);
		getContentPane().addComponentListener(new ComponentListener(){

			@Override
			public void componentResized(ComponentEvent e) {
				 widthScale = (double)e.getComponent().getWidth() / screen.width;
				 heightScale = (double)e.getComponent().getHeight() / screen.height;
			}
			@Override
			public void componentMoved(ComponentEvent e) {}
			@Override
			public void componentShown(ComponentEvent e) {}
			@Override
			public void componentHidden(ComponentEvent e) {}
		});
		setVisible(true);
		
		canvas.init(2);
		this.device = graphicsDevice;
	}
	
	public void showRenderedContent() {
		canvas.showRenderedContent();
	}
	
	public void setFullScreenMode(boolean fullScreen) {
		if(device == null || !device.isFullScreenSupported()) {
			simulateFullScreen(fullScreen);
		} else {
			if(fullScreen){
				setWindowDecoration(true);
				device.setFullScreenWindow(this);
			} else {
				setWindowDecoration(false);
				device.setFullScreenWindow(null);
			}
		}
	}
	
	public void setDisplayMode(DisplayMode mode) {
		DisplayMode[] modes = device.getDisplayModes();
		for(DisplayMode md : modes) {
			if(md.equals(mode)) {
				device.setDisplayMode(mode);
				break;
			}
		}
	}
	
	protected void setWindowDecoration(boolean decorated) {
		dispose();
		setUndecorated(decorated);
		pack();
		setVisible(true);
		canvas.init(2);
	}
	
	protected void simulateFullScreen(boolean fullScreen) {
		if(getExtendedState() == JFrame.MAXIMIZED_BOTH && !fullScreen) {
			setWindowDecoration(true);
			setExtendedState(JFrame.NORMAL);
		} else if (fullScreen){
			setWindowDecoration(false);
			setExtendedState(JFrame.MAXIMIZED_BOTH);
		}
	}
	
	public Screen getScreen() {
		return screen;
	}
	
	public RenderCanvas getRenderCanvas() {
		return canvas;
	}
	
	public double getWidthScale() {
		return widthScale;
	}
	
	public double getHeightScale() {
		return heightScale;
	}
	
	public int translateXToWindowCoords(int x) {
		return (int) (x * widthScale);
	}
	
	public int translateYToWindowCoords(int y) {
		return (int) (y * heightScale);
	}
	
	public String toString() {
		DebugStringBuilder ret = new DebugStringBuilder();
		
		ret.append(this.getClass(), hashCode());
		ret.increaseLayer();
		ret.appendln(super.toString());
		ret.append("widthScale", widthScale);
		ret.append("heightScale", heightScale);
		ret.append(screen, "screen");
		ret.append(canvas, "canvas");
		ret.append(device, "device");
		ret.decreaseLayer();
		ret.appendCloseBracket();
		
		return ret.getString();
	}
}
