package pearsistent.knutreasurehunt;

import java.util.ArrayList;

/**
 * Created by Zzeulki on 2017. 3. 28..
 */

public class Team {

    private ArrayList<TeamMember> teamMembers;
    private String teamName;
    private int teamPoint;
    private ArrayList<Item> itemList;

    public Team() {

    }

    public Team(String n, int p){

        teamMembers = new ArrayList<>();
        this.teamName = n;
        this.teamPoint = p;
        itemList = new ArrayList<>();
    }

    /*public Team(String teamname, ArrayList<TeamMember> teamMembers, int i) {

        this.teamName = teamname;
        this.teamMembers = teamMembers;
        this.teamPoint = i;
    }*/

    public String getTeamName() {
        return this.teamName;
    }

    public void setTeamPoint(int teamPoint) {
        this.teamPoint = teamPoint;
    }

    public int getTeamPoint() {
        return this.teamPoint;
    }

    public ArrayList<TeamMember> getTeamMembers() {
        return this.teamMembers;
    }

    public void addItem(Item newItem) {
        this.itemList.add(newItem);
    }

    public ArrayList<Item> getItemList() {
        return this.itemList;
    }

    public void addTeamMember(TeamMember newTeamMember) {
        this.teamMembers.add(newTeamMember);
    }

    public void removeTeamMember(TeamMember chosenTeamMember) {
        this.teamMembers.remove(chosenTeamMember);
    }


}
