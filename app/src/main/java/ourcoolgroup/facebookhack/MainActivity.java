package ourcoolgroup.facebookhack;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import org.json.JSONObject;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private static String IP_ADDRESS = "localhost";
    private static int PORT = 7777;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


    }

    /* Called when the application starts initially */
    public void loginRequest(View view) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void requestMovies() throws Exception {

    }

    public void likeMovie(String movieTitle) throws Exception {

    }

    private void sendJSON(String address, int port, JSONObject json) throws IOException {

    }
}
