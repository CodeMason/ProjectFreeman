package io.github.wreed12345.shared.placeable;

import java.io.Serializable;

public class Placeable implements Serializable {
	private static final long serialVersionUID = 7842294510915459579L;
	private int x, y;
	private int price;
	private int cooldown;
	private int damage;
	private AttackType type;
	private String owner;
	private long gameID;
	private boolean inGame;
	private String id;

	public void update() {
	}

	public void draw() {
	}
	
	public void clicked(){
	}

	public static Placeable convertToRawPlaceable(Placeable p) {
		Placeable q = new Placeable();
		q.cooldown = p.cooldown;
		q.damage = p.damage;
		q.gameID = p.gameID;
		q.id = p.id;
		q.inGame = p.inGame;
		q.owner = p.owner;
		q.price = p.price;
		q.type = p.type;
		q.x = p.x;
		q.y = p.y;

		return q;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getCooldown() {
		return cooldown;
	}

	public void setCooldown(int cooldown) {
		this.cooldown = cooldown;
	}

	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public AttackType getType() {
		return type;
	}

	public void setType(AttackType type) {
		this.type = type;
	}

	public boolean isInGame() {
		return inGame;
	}

	public void setInGame(boolean inGame) {
		this.inGame = inGame;
	}

	public long getGameID() {
		return gameID;
	}

	public void setGameID(long gameID) {
		this.gameID = gameID;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
