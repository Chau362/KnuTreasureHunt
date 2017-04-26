package pearsistent.knutreasurehunt;

import java.util.ArrayList;

/**
 * Created by Zzeulki on 2017. 3. 28..
 */

public class Team {

    private String teamName;
    private int teamPoint;
    private ArrayList<TeamMember> teamMembers = new ArrayList<>();


    public Team(String n, int p){
        this.teamName = n;
        this.teamPoint = p;
    }

    public Team(String teamname, ArrayList<TeamMember> teamMembers, int i) {

        this.teamName = teamname;
        this.teamMembers = teamMembers;
        this.teamPoint = i;

    }

    public String getTeamName() {
        return this.teamName;
    }

    public int getTeamPoint() {
        return this.teamPoint;
    }

    public ArrayList<TeamMember> getTeamMembers() {
        return this.teamMembers;
    }

    public void addTeamMember(TeamMember newTeamMember) {
        this.teamMembers.add(newTeamMember);
    }

    public void removeTeamMember(TeamMember chosenTeamMember) {
        this.teamMembers.remove(chosenTeamMember);
    }
}
