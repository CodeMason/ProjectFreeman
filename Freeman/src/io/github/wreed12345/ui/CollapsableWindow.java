/*******************************************************************************
 * Originally taken from:
 * https://github.com/libgdx/libgdx/blob/master/tests/gdx-tests/src/com/badlogic/gdx/tests/g3d/BaseG3dHudTest.java#L182
 * Modified by William Reed to suit the needs of this game.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/

package io.github.wreed12345.ui;

import java.util.ArrayList;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/** Double click title to expand/collapse */
public class CollapsableWindow extends Window {
	private boolean collapsed;
	private float collapseHeight = 20f;
	private float expandHeight;
	private ArrayList<Float> heights = new ArrayList<Float>();

	public CollapsableWindow(String title, Skin skin) {
		super(title, skin);
		addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				if (getTapCount() == 2 && getHeight() - y <= getPadTop() && y < getHeight() && x > 0
						&& x < getWidth())
					toggleCollapsed();
			}
		});
	}
	
	public void expand() {
		if (!collapsed)
			return;
		setHeight(expandHeight);
		setY(getY() - expandHeight + collapseHeight);
		collapsed = false;
		
		int index = 0;
		for(Actor a : getChildren()){
			if(a.getClass().equals(Table.class)) continue;
			getCell(a).height(heights.get(index));
			index ++;
		}
		index = 0;
		heights.clear();

	}

	public void collapse() {
		if (collapsed)
			return;
		expandHeight = getHeight();
		setHeight(collapseHeight);
		setY(getY() + expandHeight - collapseHeight);
		collapsed = true;
		if (getStage() != null)
			getStage().setScrollFocus(null);
		
		for(Actor a : getChildren()){
			if(a.getClass().equals(Table.class)) continue;
			getCell(a).height(0);
			heights.add(a.getHeight());
		}

	}

	public void toggleCollapsed() {
		if (collapsed)
			expand();
		else
			collapse();
	}

	public boolean isCollapsed() {
		return collapsed;
	}
}