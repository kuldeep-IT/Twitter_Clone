package com.example.twitterclone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

    private ArrayList<String> arrayList;
    private ArrayAdapter arrayAdapter;
    private ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twitter_users);

        listView = findViewById(R.id.listViewSend);
        arrayList = new ArrayList<>();
        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_checked,arrayList);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);

        listView.setOnItemClickListener(TwitterUsers.this);


        try {

            ParseQuery<ParseUser> parseQuery = ParseUser.getQuery();
            parseQuery.whereNotEqualTo("username",ParseUser.getCurrentUser().getUsername());
            parseQuery.findInBackground(new FindCallback<ParseUser>() {
                @Override
                public void done(List<ParseUser> objects, ParseException e) {

                    if (objects.size() > 0 && e == null)
                    {
                        for (ParseUser twitterUser : objects)
                        {
                            arrayList.add(twitterUser.getUsername());
                        }
                        listView.setAdapter(arrayAdapter);

                            for (String twitterUser : arrayList)
                            {
                                if (ParseUser.getCurrentUser().getList("fanOf") != null)
                                {

                                    if (ParseUser.getCurrentUser().getList("fanOf").contains(twitterUser));

    //                                listView.setItemChecked(arrayList.indexOf(twitterCheckedSaveUser), true);
                                    listView.setItemChecked(arrayList.indexOf(twitterUser),true);
                                }
                            }
                    }

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {


        getMenuInflater().inflate(R.menu.menu_item,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.logout:

                ParseUser.getCurrentUser().logOutInBackground(new LogOutCallback() {
                    @Override
                    public void done(ParseException e) {

                        Intent i1 = new Intent(TwitterUsers.this,MainActivity.class);
                        startActivity(i1);
                        finish();

                    }
                });

                break;

            case R.id.sendTweet:

                Intent i2= new Intent(TwitterUsers.this,SendTweet.class);
                startActivity(i2);

                break;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        CheckedTextView checkedTextView =(CheckedTextView) view;

        if (checkedTextView.isChecked())
        {
            FancyToast.makeText(TwitterUsers.this,arrayList.get(position)+" followed!",FancyToast.LENGTH_SHORT,FancyToast.INFO,true).show();

            //adding following user to database
            ParseUser.getCurrentUser().add("fanOf",arrayList.get(position));

        }
        else {
            FancyToast.makeText(TwitterUsers.this,arrayList.get(position)+" unfollowed!",FancyToast.LENGTH_SHORT,FancyToast.INFO,true).show();

            //removing unfollowed users
            ParseUser.getCurrentUser().getList("fanOf").remove(arrayList.get(position));
            List currentUserSavedList = ParseUser.getCurrentUser().getList("fanOf");

            //fanOf column remove
            ParseUser.getCurrentUser().remove("fanOf");

            //adding back to List of fanOf
            ParseUser.getCurrentUser().put("fanOf",currentUserSavedList);

        }

        ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null)
                {
                    FancyToast.makeText(TwitterUsers.this," saved!",FancyToast.LENGTH_SHORT,FancyToast.INFO,true).show();
                }
                else
                {
                    FancyToast.makeText(TwitterUsers.this,e.getMessage()+"",FancyToast.LENGTH_SHORT,FancyToast.INFO,true).show();
                }
            }
        });

    }
}