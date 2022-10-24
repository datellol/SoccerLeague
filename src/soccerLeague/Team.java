package soccerLeague;

public class Team {
	
	private String teamName;
	private int teamPoints;
	
	public Team(String name) {
		this.teamName = name;
	}
	
	public void wins() {
		this.teamPoints += 3;
	}
	
	public void draws() {
		this.teamPoints++;
	}
}