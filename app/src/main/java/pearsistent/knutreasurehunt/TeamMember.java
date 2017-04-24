package pearsistent.knutreasurehunt;

/**
 * Created by Zzeulki on 2017. 3. 30..
 */


public class TeamMember {

    String memberName;
    String userId = null;

    public TeamMember(){
    }

    public TeamMember(String membername,String userid){
        this.memberName = membername;
        this.userId = userid;
    }

    public String getMemberName(){return this.memberName;}

    public String getUserId(){return this.userId;}

    public void setMemberName(String name){
        this.memberName = name;
    }

    public void setMemberUserId(String userid){
        this.userId = userid;
    }


}
