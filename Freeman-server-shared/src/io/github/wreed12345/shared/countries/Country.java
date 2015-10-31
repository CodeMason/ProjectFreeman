package io.github.wreed12345.shared.countries;

import java.io.Serializable;

public class Country implements Serializable{
	private static final long serialVersionUID = -6763739041046881009L;
	private float r, g, b;
	private String name, information, owner;
	private int fertility, size, tradeAbility, resourceAvailability, health;

	public Country(){};//needed for kryo
	
	public Country(String name, float r, float g, float b, String information, int size) {
		this.name = name;
		this.r = r;
		this.g = g;
		this.b = b;
		this.information = information;
		this.size = size;
		owner = "Unowned";
		setHealth(100);
	}

	/** @return the name of the country */
	public String getName() {
		return name;
	}

	/** The R value of the country's color */
	public float getR() {
		return r;
	}

	/** The G value of the country's color */
	public float getG() {
		return g;
	}

	/** The B value of the country's color */
	public float getB() {
		return b;
	}

	/** The country's information */
	public String getInfo() {
		return information;
	}
	
	public void setInfo(String information) {
		this.information = information;
	}

	/** @return the owner of the country */
	public String getOwner() {
		return owner;
	}

	/** Set the owner of this country */
	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	/**@return the fertility level of the Country*/
	public int getFertility() {
		return fertility;
	}

	/**Set's the fertility of the Country*/
	public void setFertility(int fertility) {
		this.fertility = fertility;
	}

	/**@return the size of the Country (0 - 100)*/
	public int getSize() {
		return size;
	}

	/**Sets the size of the country (0 - 100)/
	public void setSize(int size) {
		this.size = size;
	}

	/**@return the trade ability of the Country ( 0 - 100 )*/
	public int getTradeAbility() {
		return tradeAbility;
	}

	/**Sets the trade ability of the Country ( 0 - 100 )*/
	public void setTradeAbility(int tradeAbility) {
		this.tradeAbility = tradeAbility;
	}

	/**@return the Resource ability of the Country ( 0 - 100 )*/
	public int getResourceAvailability() {
		return resourceAvailability;
	}

	/**Sets the Resource ability of the Country ( 0 - 100 )*/
	public void setResourceAvailability(int resourceAvailability) {
		this.resourceAvailability = resourceAvailability;
	}

	/**@return the health of this country*/
	public int getHealth() {
		return health;
	}
	
	/**Sets the health of this country*/
	public void setHealth(int health) {
		this.health = health;
	}
	
	/**Removes @param amount from this countries health*/
	public void looseHealth(int amount){
		health -= amount;
	}
}
