package io.github.wreed12345.shared;

import io.github.wreed12345.shared.placeable.AttackType;

public class AttackEvent {
	
	private String attackerPlayer;
	private String attackedPlayer;
	private AttackType attackType;
	
	public AttackEvent(){};
	
	/**
	 * Creates a new AttackEvent with everything needed to instantiate an Attack
	 * @param attackType Type of attack
	 * @param attackerPlayer String of attacker
	 * @param attackedPlayer String of attacked
	 */
	public AttackEvent(AttackType attackType, String attackerPlayer, String attackedPlayer){
		this.attackType = attackType;
		this.attackerPlayer = attackerPlayer;
		this.attackedPlayer = attackedPlayer;
	}

	/**
	 * @return the player who is Attacking
	 */
	public String getAttackerPlayer() {
		return attackerPlayer;
	}
	
	/**
	 * @return player being attacked
	 */
	public String getAttackedPlayer() {
		return attackedPlayer;
	}

	/**
	 * @return the type of Attack created by {@link #getAttackerPlayer() attacking player}
	 */
	public AttackType getAttackType() {
		return attackType;
	}

}
