package io.github.wreed12345.shared.placeable;

public enum AttackType {
	CRUISE_MISSILE(1,"Cruise Missile", 15, 60, 50000, "A guided missile that is designed to deliver a large warhead over long distances with high accuracy.");/*,
	GROUND_FORCE(1),
	GROUND_FORCE_VEHICLE(2),
	HEAVY_ARMORED_GROUND_FORCE(3),
	HEAVY_ARMORED_GROUND_FORCE_VEHICLE_ESCORT(4),
	TANK(5),
	DESTROYER_FLEET(6),
	SUBMARINE_FLEET(7),
	SABOTEUR(8),
	HACKS(9),
	BOMBING_RUN(10),
	F18_ATTACK_UNIT(11),
	NAPALM_ATTACK_UNIT(12);*/ 

	private int attackType;
	private String name;
	private int maxAmount;
	private int coolDown;
	private int cost;
	private String description;
	
	AttackType(int attackType, String name, int maxAmount, int coolDown, int cost, String description){
		this.attackType = attackType;
		this.description = description;
		this.maxAmount = maxAmount;
		this.name = name;
		this.cost = cost;
	}
	
	public int getAttackType(){
		return attackType;
	}
	
	public int getMaxAmount(){
		return maxAmount;
	}
	
	public int getCoolDown(){
		return coolDown;
	}
	
	public String getDescription(){
		return description;
	}
	
	public String getName(){
		return name;
	}
	
	public int getCost(){
		return cost;
	}
}