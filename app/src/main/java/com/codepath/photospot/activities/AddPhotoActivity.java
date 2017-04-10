package com.codepath.photospot.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.photospot.R;
import com.codepath.photospot.models.Photo;
import com.codepath.photospot.services.ParseServiceImpl;
import com.codepath.photospot.services.ServiceResponse;

/**
 * Created by dfrie on 4/8/2017.
 */
public class AddPhotoActivity extends AppCompatActivity {

    private EditText etUrl;
    private EditText etComment;
    private Button btAdd;
    private TextView tvErrorMsg;

    public final ParseServiceImpl parseService = new ParseServiceImpl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_photo);
        Toast.makeText(this.getApplicationContext(), "...in activity_add_photo...", Toast.LENGTH_SHORT).show();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // Display icon in the toolbar
        //getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.mipmap.ic_launcher);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        // Enable the up icon...
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // diable the default toolbar title...
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        etUrl = (EditText) findViewById(R.id.etUrl);
        etComment = (EditText) findViewById(R.id.etComment);
        btAdd = (Button) findViewById(R.id.btPhoto);
        tvErrorMsg = (TextView) findViewById(R.id.tvErrorMsg);

        btAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = etUrl.getText().toString();
                if (str.length() == 0) {
                    tvErrorMsg.setText(R.string.enter_a_url);
                    return;
                }
                Photo photo = new Photo();
                photo.setUrl(etUrl.getText().toString());
                photo.setComment(etComment.getText().toString());
                ServiceResponse<Photo> response = parseService.postPhoto(photo);
                if (response.isResponseSuccess()) {
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);
                } else {
                    Toast.makeText(AddPhotoActivity.this.getApplicationContext(),
                            response.toString(), Toast.LENGTH_SHORT).show();
                }
             }
        });
    }

}
