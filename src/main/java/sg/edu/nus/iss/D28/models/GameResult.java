package sg.edu.nus.iss.D28.models;

import java.util.List;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;

public class GameResult {
    
    private String rating;
    private List<Comment> commentList;
    private String timestamp;

    public String getRating() {
        return rating;
    }
    public void setRating(String rating) {
        this.rating = rating;
    }
    public List<Comment> getCommentList() {
        return commentList;
    }
    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
    }
    public String getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public JsonObject toJSON() {
        JsonArrayBuilder jab = Json.createArrayBuilder();
        for (Comment comment : commentList) {
            jab.add(comment.toJSON());
        }
        JsonArray ja = jab.build();
        return Json.createObjectBuilder()
                    .add("rating", rating)
                    .add("games", ja.toString())
                    .add("timestamp", timestamp)
                    .build();
    }
}
