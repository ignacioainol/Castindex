package castinder.cyrax.com.castindex.Matches;

/**
 * Created by ignacioainolrivera on 09-05-18.
 */

public class MatchesObject {
    private String userId;

    public MatchesObject(String userId){
        this.userId = userId;
    }

    public String getUserId(){
        return userId;
    }
    public void setUserID(String userID){
        this.userId = userID;
    }

}
