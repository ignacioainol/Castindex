package castinder.cyrax.com.castindex;

/**
 * Created by ignacioainolrivera on 26-04-18.
 */

public class cards {
    private String userId;
    private String name;

    public cards(String userId, String name){
        this.userId = userId;
        this.name = name;
    }

    public String getUserId(){
        return userId;
    }

    public void setUserID(String userID){
        this.userId = userID;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }
}
