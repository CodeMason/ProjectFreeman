package io.github.wreed12345.placable;

import io.github.wreed12345.ui.MilitaryDialog;

public interface PlaceableCreatorAndDestroyer {
	
	public void createRocket(int x, int y, MilitaryDialog militaryDialog);
	
	public void sellRocket(int x, int y, MilitaryDialog militaryDialog);
	
	public void rocketAttack(int x, int y);
}
