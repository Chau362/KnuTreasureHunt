package pearsistent.knutreasurehunt;

import java.lang.reflect.Member;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Zzeulki on 2017. 3. 28..
 */

public class Team {
    public String teamName;
    private String teamAccount;
    private String pwd;
    public int teamPoint;
    public List<TeamMember> teamMembers = new ArrayList<>();


    public Team(String name, String acc, String pwd, ArrayList<TeamMember> members, int p){
        this.teamName = name;
        this.teamAccount = acc;
        this.pwd = pwd;
        this.teamMembers = members;
        this.teamPoint = p;
    }
    public Team(String name, int p){
        this.teamName = name;
        this.teamPoint = p;
    }

    public void addTeamMember(TeamMember newTeamMember) {
        this.teamMembers.add(newTeamMember);
    }

    public void removeTeamMember(TeamMember chosenTeamMember) {
        this.teamMembers.remove(chosenTeamMember);
    }
}
