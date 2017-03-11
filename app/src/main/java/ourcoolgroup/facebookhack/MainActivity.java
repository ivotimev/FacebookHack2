package ourcoolgroup.facebookhack;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import org.json.JSONObject;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class MainActivity extends AppCompatActivity {
    private static String IP_ADDRESS = "8.8.8.8";
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
        JSONObject json = new JSONObject();

        if(this.token != null) {
            json.put("type", "my_movies");
            json.put("token", this.token);
        } else {
            throw new Exception("Invalid request - not logged in");
        }

        sendJSON(MainActivity.IP_ADDRESS, MainActivity.PORT, json);
    }

    public void likeMovie(String movieTitle) throws Exception {
        JSONObject json = new JSONObject();

        if(this.token != null) {
            json.put("type", "like_movie");
            json.put("token", this.token);
            json.put("movie_title", movieTitle);
        } else {
            throw new Exception("Invalid post - not logged in");
        }

        sendJSON(MainActivity.IP_ADDRESS, MainActivity.PORT, json);
    }

    private void sendJSON(String address, int port, JSONObject json) throws IOException {
        Socket s = new Socket(address, port);

        OutputStreamWriter out = new OutputStreamWriter(s.getOutputStream());
        out.write(json.toString());
    }
}
