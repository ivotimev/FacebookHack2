package ourcoolgroup.facebookhack;

/**
 * Created by fraser on 11/03/17.
 */

import io.socket.client.Socket;
import io.socket.client.*;
import io.socket.emitter.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class Comms {
    private Socket socket;
    private String token;
    private MoviesList moviesList;

    public void connect(String address) throws URISyntaxException {
        socket = IO.socket(address);
        socket.on(Socket.EVENT_CONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {

            }

        }).on("message", new Emitter.Listener() {

            @Override
            public void call(Object... args) {
                String jsonString = (String) args[0];

                try {
                    JSONObject json = new JSONObject(jsonString);

                    if(json.get("type") == "login_success") {
                        token = (String) json.get("token");
                        moviesList = new MoviesList();

                    } else if (json.get("type") == "login_fail") {
                        token = null;
                        moviesList = null;

                    } else if (json.get("type") == "my_movies") {

                        JSONArray jsonArray = new JSONArray((String) json.get("movies"));
                        for(int i = 0; i < jsonArray.length(); i++) {
                            moviesList.addMovie((String) jsonArray.getJSONObject(i).get("title"));
                        }
                    }
                } catch (JSONException e) {
                    System.err.print(e);
                }
            }

        }).on(Socket.EVENT_DISCONNECT, new Emitter.Listener() {

            @Override
            public void call(Object... args) {}

        });

        socket.connect();
    }

    public void disconnect() {
        socket.disconnect();
    }
}
