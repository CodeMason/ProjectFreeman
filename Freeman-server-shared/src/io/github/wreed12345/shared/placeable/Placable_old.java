package io.github.wreed12345.shared.placeable;

/**
 * Represents any type of placable attack, defense, etc item
 * @author William Reed
 * @since 0.0.11
 */
public interface Placable_old {

	/**Perform logic operations*/
	public void update();
	
	/**Draws anything placable related. 
	 * @param spritebatch (hack to get my sprite batch in here*/
	public void draw(Object spritebatch);
	
	/** @return the price of the placable */
	public int getPrice();

	/** Sets the price of the placable */
	public void setPrice(int price);

	/**
	 * Sets the location
	 * @param x x cord
	 * @param y y cord
	 */
	public void setLocation(float x, float y);
	
	/**@return the x position of the placable*/
	public int getXpos();
	
	/**@return the y position of the placabl*/
	public int getYpos();

	/** @return true if it has been placed */
	public boolean isPlaced();

	/**
	 * Sets the placable value
	 * @param value
	 */
	public void setPlaced(boolean placed);
	
	/**@return the cooldown interval in seconds*/
	public int getCoolDownInterval();
	
	/**Sets the cooldown interval.
	 * @param interval amount of seconds it takes for this placable to cool down. */
	public void setCoolDownInterval(int interval);
	
	/**@return the amount of damage one attack will inflict*/
	public int getDamage();
	
	/**Sets the amount of damage one attack will deal
	 * @param damage amount of damage */
	public void setDamage(int damage);
	

}
