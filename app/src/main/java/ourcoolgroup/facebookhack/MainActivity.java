package ourcoolgroup.facebookhack;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /* Called when the user clicks the login button */
    public void loginRequest(View view) {
        // Spawn activity to get login details
    }
}
