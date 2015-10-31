package io.github.wreed12345.ui;

import java.util.ArrayList;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.HorizontalGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

/**
 * RowTable - a class that represents a table of only rows, no columns
 * @author William Reed
 * @since 0.0.11 2/10/14
 */
public class RowTable extends Table{
	
	private ArrayList<HorizontalGroup> horizontalGroups = new ArrayList<HorizontalGroup>();
	
	public RowTable(int amountOfRows){
		for(int i = 0; i < amountOfRows; i ++){
			horizontalGroups.add(new HorizontalGroup());
			add(horizontalGroups.get(i)).row();
		}
	}
	
	 /** Adds an actor to the desired row. 0 = the first row
	 * @param actor Actor to be added
	 * @param row Row to be added to
	 * @throws IllegalArgumentException when the row is greater than the rows in the RowTable
	 */
	public void add(Actor actor, int row){
		if(row > horizontalGroups.size())
			throw new IllegalArgumentException("The specified row is greater than the amount of rows in this RowTable");
		else {
			horizontalGroups.get(row).addActor(actor);
		}
		
	}
}
