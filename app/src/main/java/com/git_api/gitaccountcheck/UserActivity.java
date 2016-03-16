package com.git_api.gitaccountcheck;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Deepak Kumar on 16-03-2016.
 *
 */
public class UserActivity extends AppCompatActivity {

    private static final String TAG = "UserActivity";
    private ArrayList<UserData> userM = new ArrayList<>();
    private ImageView mProfilePic;
    private TextView mUsername;
    private TextView mName;
    private TextView mEmail;
    private TextView mRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_layout);

        getSupportActionBar().setTitle("Your Github Account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        initializeViews();

        Bundle bundle = getIntent().getExtras();
        String username = bundle.getString("KEY_USERNAME");

        new UserGitTask().execute("https://api.github.com/users/" + username);
    }

    private void initializeViews(){
        mProfilePic = (ImageView)findViewById(R.id.profile_pic_ID);
        mUsername = (TextView)findViewById(R.id.username_ID);
        mName = (TextView)findViewById(R.id.name_ID);
        mEmail = (TextView)findViewById(R.id.email_ID);
        mRepository = (TextView)findViewById(R.id.repo_ID);
    }


    class UserGitTask extends AsyncTask<String, String, String> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(UserActivity.this);
            dialog.setMessage("Connecting github.....");
            dialog.show();
            dialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... url) {

            String data = "";
            try {
                HttpGet get = new HttpGet(url[0]);
                HttpClient httpclient = new DefaultHttpClient();
                HttpResponse response = httpclient.execute(get);

                int status = response.getStatusLine().getStatusCode();

                if (status == 200) {
                    HttpEntity entity = response.getEntity();
                    data = EntityUtils.toString(entity);

                    return data;
                }else
                {
                    data = "error";
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            dialog.cancel();
            try{
                Log.d(TAG, "" + result.length() + " length");
                Log.d(TAG, "AsyncTask call");
                if(result.equals("error")){
                    Toast.makeText(UserActivity.this, "Unable to fetch data...", Toast.LENGTH_LONG).show();
                }
                JSONObject object = new JSONObject(result);

                UserData user = new UserData();

                user.setUsername(object.getString("login"));
                user.setName(object.getString("name"));
                user.setEmail(object.getString("email"));
                user.setRepo(object.getString("public_repos"));
                user.setImg_url(object.getString("avatar_url"));

                userM.add(user);

                String git_user_name = userM.get(0).getUsername();
                mUsername.setText(git_user_name);

                String git_name = userM.get(0).getName();
                mName.setText(git_name);

                String git_email = userM.get(0).getEmail();
                mEmail.setText(git_email);

                String git_repo = userM.get(0).getRepo();
                mRepository.setText(git_repo);

                String git_profile_pic = userM.get(0).getImg_url();
                Picasso.with(UserActivity.this)
                        .load(git_profile_pic)
                        .error(R.drawable.git)
                        .placeholder(R.drawable.git)
                        .into(mProfilePic);


            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
