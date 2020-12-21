package com.example.twitterclone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.shashank.sony.fancytoastlib.FancyToast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SendTweet extends AppCompatActivity implements View.OnClickListener {

    private EditText edtSend;
    private Button btnSend;
    private ListView listView;
    private Button btnRetriveData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_tweet);

        edtSend= findViewById(R.id.edtSendTweet);
        btnSend = findViewById(R.id.btnSend);
        btnRetriveData = findViewById(R.id.retriveTweet);
        listView = findViewById(R.id.listView);

        btnSend.setOnClickListener(SendTweet.this);
        btnRetriveData.setOnClickListener(SendTweet.this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.btnSend:

                ParseObject object = new ParseObject("MyTweet");
                object.put("tweet",edtSend.getText().toString());
                object.put("username",ParseUser.getCurrentUser().getUsername());

                ProgressDialog pd = new ProgressDialog(this);
                pd.setMessage("Loading..."+ParseUser.getCurrentUser().getUsername());
                pd.show();

                object.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null)
                        {
                            FancyToast.makeText(SendTweet.this,"Uploaded!",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,true).show();
                        }
                        else
                        {
                            FancyToast.makeText(SendTweet.this,e.getMessage()+"",FancyToast.LENGTH_SHORT,FancyToast.ERROR,true).show();
                        }
                        pd.dismiss();
                    }
                });

                break;

            case R.id.retriveTweet:

                ArrayList<HashMap<String,String>> tweetList = new ArrayList<>();
                SimpleAdapter adapter = new SimpleAdapter(SendTweet.this,tweetList, android.R.layout.simple_list_item_2,
                        new String[]{ "tweetUserName","tweetValue"}, new int[]{ android.R.id.text1, android.R.id.text2});

                try {

                    ParseQuery<ParseObject> parseQuery = ParseQuery.getQuery("MyTweet");
                    parseQuery.whereContainedIn("username",ParseUser.getCurrentUser().getList("fanOf"));
                    parseQuery.findInBackground(new FindCallback<ParseObject>() {
                        @Override
                        public void done(List<ParseObject> objects, ParseException e) {
                            if (objects.size()>0 && e == null)
                            {
                                for (ParseObject tweetObject : objects)
                                {
                                    HashMap<String,String> userTweet = new HashMap<>();
                                    userTweet.put("tweetUserName",tweetObject.getString("username"));
                                    userTweet.put("tweetValue",tweetObject.getString("tweet"));

                                    tweetList.add(userTweet);

                                }
                                listView.setAdapter(adapter);
                            }
                        }
                    });


                } catch (Exception e) {
                    e.printStackTrace();
                }


                break;
        }


    }
}