package castinder.cyrax.com.castindex;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;

import castinder.cyrax.com.castindex.Cards.arrayAdapter;
import castinder.cyrax.com.castindex.Cards.cards;
import castinder.cyrax.com.castindex.Matches.MatchesActivity;

public class MainActivity extends AppCompatActivity {

    private cards cards_data[];
    private castinder.cyrax.com.castindex.Cards.arrayAdapter arrayAdapter;
    private int i;

    private FirebaseAuth mAuth;
    private String currentUid;
    private DatabaseReference usersDb;

    ListView listView;
    List<cards> rowItems;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        usersDb = FirebaseDatabase.getInstance().getReference().child("Users");

        mAuth = FirebaseAuth.getInstance();
        currentUid = mAuth.getCurrentUser().getUid();

        checkUserSex();


        rowItems = new ArrayList<cards>();

        arrayAdapter = new arrayAdapter(this, R.layout.item, rowItems );


        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) findViewById(R.id.frame);


        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                rowItems.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                cards obj = (cards) dataObject;
                String userId = obj.getUserId();
                usersDb.child(userId).child("connections").child("nope").child(currentUid).setValue(true);
                Toast.makeText(MainActivity.this, "Left!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                cards obj = (cards) dataObject;
                String userId = obj.getUserId();
                usersDb.child(userId).child("connections").child("yeps").child(currentUid).setValue(true);
                inConnectionMatch(userId);
                Toast.makeText(MainActivity.this, "Right!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {

            }

            @Override
            public void onScroll(float scrollProgressPercent) {

            }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(MainActivity.this, "Click!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void inConnectionMatch(String userId) {
        DatabaseReference currentUserConnectionsDb = usersDb.child(currentUid).child("connections").child("yeps").child(userId);
        currentUserConnectionsDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Toast.makeText(MainActivity.this,"Match!", Toast.LENGTH_LONG).show();
                usersDb.child(dataSnapshot.getKey()).child("connections").child("matches").child(currentUid).setValue(true);
                usersDb.child(currentUid).child("connections").child("matches").child(dataSnapshot.getKey()).setValue(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private String userSex;
    private String oppositeUserSex;

    public void checkUserSex(){

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference userDb = usersDb.child(user.getUid());
        userDb.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if(dataSnapshot.child("sex") != null){
                        userSex = dataSnapshot.child("sex").getValue().toString();
                        //oppositeUserSex = "Female";
                        switch (userSex){
                            case "Male":
                                oppositeUserSex = "Female";
                                break;
                            case "Female":
                                oppositeUserSex = "Male";
                                break;
                        }
                        getOppositeSexUsers();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        /*userDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.getKey().equals(user.getUid())){
                    if(dataSnapshot.exists()){
                        if(dataSnapshot.child("sex") != null){
                            userSex = dataSnapshot.child("sex").getValue().toString();
                            //oppositeUserSex = "Female";
                            switch (userSex){
                                case "Male":
                                    oppositeUserSex = "Female";
                                    break;
                                case "Female":
                                    oppositeUserSex = "Male";
                                    break;
                            }
                            getOppositeSexUsers();
                        }
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });*/

    }

    public void getOppositeSexUsers(){

        usersDb.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if(dataSnapshot.child("sex").getValue() != null){
                    if(dataSnapshot.exists() && !dataSnapshot.child("connections").child("nope").hasChild(currentUid) && !dataSnapshot.child("connections").child("yeps").hasChild(currentUid) && dataSnapshot.child("sex").getValue().toString().equals(oppositeUserSex)){
                        String profileImageUrl = "default";
                        if(!dataSnapshot.child("profileImageUrl").getValue().equals("default")){
                            profileImageUrl = dataSnapshot.child("profileImageUrl").getValue().toString();
                        }
                        cards item = new cards(dataSnapshot.getKey(),dataSnapshot.child("name").getValue().toString(),profileImageUrl);
                        rowItems.add(item);
                        arrayAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    public void logoutUser(View view) {
        mAuth.signOut();
        Intent intent = new Intent(MainActivity.this,ChooseLoginRegistrationActivity.class);
        startActivity(intent);
        finish();
        return;
    }

    public void goToSettings(View view) {
        Intent intent = new Intent(MainActivity.this,SettingsActivity.class);
        startActivity(intent);
        return;
    }

    public void goToMatches(View view) {
        Intent intent = new Intent(MainActivity.this,MatchesActivity.class);
        startActivity(intent);
        return;
    }
}