package com.example.ac_twitterclone;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.parse.FindCallback;
import com.parse.LogOutCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.List;

public class TwitterUsers extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView Listview;
    private ArrayList<String> tusers;
    private ArrayAdapter adapter;

    private String followeduser="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_users);

        FancyToast.makeText(this,"Welcome"+ ParseUser.getCurrentUser().getUsername(), Toast.LENGTH_SHORT,FancyToast.INFO,true).show();

        Listview=findViewById(R.id.Listview);
        tusers=new ArrayList<>();
        adapter=new ArrayAdapter(this,android.R.layout.simple_list_item_checked,tusers);
        Listview.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        Listview.setOnItemClickListener(this);

        ParseQuery<ParseUser> query=ParseUser.getQuery();
        query.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> objects, ParseException e) {

                if(objects.size()>0 && e==null){

                    for(ParseUser twitteruser:objects){
                        tusers.add(twitteruser.getUsername());

                    }
                    Listview.setAdapter(adapter);

                    for(String twitterUser:tusers){
                        if(ParseUser.getCurrentUser().getList("fanOf")!=null){



                        if(ParseUser.getCurrentUser().getList("fanOf").contains(twitterUser)){
                            followeduser=followeduser+twitterUser+"\n";
                            Listview.setItemChecked(tusers.indexOf(twitterUser),true);

                            FancyToast.makeText(TwitterUsers.this, ParseUser.getCurrentUser().getUsername()+"is following "+ followeduser,Toast.LENGTH_LONG,FancyToast.INFO,true).show();

                        }
                        }
                    }
                }
            }
        });




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.my_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {






        switch (item.getItemId()){
            case R.id.LogoutUserItem:
                ParseUser.getCurrentUser().logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {
                        Intent intent=new Intent(TwitterUsers.this,SignUpActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });

            case R.id.SendTweetItem:
                Intent intent=new Intent(TwitterUsers.this,SendTweetActivity.class);
                startActivity(intent);
                break;
                 }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        CheckedTextView checkedTextView= (CheckedTextView) view;

        if (checkedTextView.isChecked()){
            FancyToast.makeText(TwitterUsers.this,tusers.get(position)+" is now followed",Toast.LENGTH_SHORT,FancyToast.INFO,true).show();
            ParseUser.getCurrentUser().add("fanOf",tusers.get(position));


        }
        else{
            FancyToast.makeText(TwitterUsers.this,tusers.get(position)+" is now unfollowed",Toast.LENGTH_SHORT,FancyToast.INFO,true).show();

            ParseUser.getCurrentUser().getList("fanOf").remove(tusers.get(position));

            List currentuserFanOfList=ParseUser.getCurrentUser().getList("fanOf");
            ParseUser.getCurrentUser().remove("fanOf");
            ParseUser.getCurrentUser().put("fanOf",currentuserFanOfList);
        }

        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e==null){
                    FancyToast.makeText(TwitterUsers.this,"Saved",Toast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();
                }
            }
        });


    }
}
