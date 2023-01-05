package sg.edu.nus.iss.D28.models;

import java.util.Arrays;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;

public class Comment {
    
    // Comment members 
    private String _id;
    private String[] boardGame;
    private String rating;
    private String user;

    // Generate getter and setter
    public String get_id() {
        return _id;
    }
    public void set_id(String _id) {
        this._id = _id;
    }
    public String[] getBoardGame() {
        return boardGame;
    }
    public void setBoardGame(String[] boardGame) {
        this.boardGame = boardGame;
    }
    public String getRating() {
        return rating;
    }
    public void setRating(String rating) {
        this.rating = rating;
    }
    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Comment [_id=" + _id + ", boardGame=" + Arrays.toString(boardGame) + ", rating=" + rating + ", user="
                + user + "]";
    }

    public JsonObject toJSON() {
        JsonArrayBuilder jab = Json.createArrayBuilder();
        for (String bg : boardGame) {
            jab.add(bg);
        }
        JsonArray ja = jab.build();
        return Json.createObjectBuilder()
                    .add("_id", _id)
                    .add("name", ja.toString())
                    .add("rating", rating)
                    .add("user", user)
                    .build();
    }
}
