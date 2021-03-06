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
package svk.sglubos.engine.core;

import svk.sglubos.engine.utils.debug.DebugStringBuilder;

public abstract class Core {
	protected volatile boolean running;
	
	protected abstract void start();
	protected abstract void stop();
	protected abstract void init();
	protected abstract void tick();
	protected abstract void render();
	protected abstract void stopped();
	protected abstract int getFPS();
	protected abstract int getTPS();
	
	public boolean isRunning() {
		return running;
	}
	
	@Override
	public String toString() {
		DebugStringBuilder ret = new DebugStringBuilder();
		ret.append(getClass(), hashCode());
		ret.increaseLayer();
		ret.append("running", running);
		ret.decreaseLayer();
		ret.appendCloseBracket();
		
		return ret.toString();
	}
}
