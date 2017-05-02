package pearsistent.knutreasurehunt;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Zzeulki on 2017. 3. 30..
 */


public class TeamMember {

    String memberName;
    String userId = null;

    public TeamMember() {
    }

    public TeamMember(String membername, String userid) {
        this.memberName = membername;
        this.userId = userid;
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

    public void setMemberUserId(String userid) {
        this.userId = userid;
    }

    public Map<String, Object> toMap() {

        HashMap<String, Object> teamMember = new HashMap<>();
        teamMember.put("memberName", memberName);
        teamMember.put("userId", userId);

        return teamMember;
    }


}
