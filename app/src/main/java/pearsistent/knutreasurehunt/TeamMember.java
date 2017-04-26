package pearsistent.knutreasurehunt;

/**
 * Created by Zzeulki on 2017. 3. 30..
 */


public class TeamMember {

    String memberName;
    String userId;

    public TeamMember() {

    }

    public String getMemberName() {
        return this.memberName;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setMemberName(String name){
        this.memberName = name;
    }


}
