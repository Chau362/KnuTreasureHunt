package pearsistent.knutreasurehunt;

import java.util.ArrayList;

/**
 * Created by Zzeulki on 2017. 3. 28..
 */

public class Team {

    String teamName;
    int teamPoint;
    ArrayList<TeamMember> teamMembers = new ArrayList<>();

    public Team(String n, int p){
        this.teamName = n;
        this.teamPoint = p;
    }

    public Team(String teamname, String email, String pwd, Object o, int i) {

    }

    public void addTeamMember(TeamMember newTeamMember) {
        this.teamMembers.add(newTeamMember);
    }

    public void removeTeamMember(TeamMember chosenTeamMember) {
        this.teamMembers.remove(chosenTeamMember);
    }
}
