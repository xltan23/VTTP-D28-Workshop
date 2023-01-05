package sg.edu.nus.iss.D28.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.bson.Document;
import org.bson.types.ObjectId;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;

public class Game {
    
    // Game members
    private Integer gid;
    private String name;
    private Integer year;
    private Integer ranking;
    private Integer users_rated;
    private String url;
    private String image;
    private String[] reviews;
    private String timestamp;
    
    public Integer getGid() {
        return gid;
    }
    public void setGid(Integer gid) {
        this.gid = gid;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public Integer getYear() {
        return year;
    }
    public void setYear(Integer year) {
        this.year = year;
    }
    public Integer getRanking() {
        return ranking;
    }
    public void setRanking(Integer ranking) {
        this.ranking = ranking;
    }
    public Integer getUsers_rated() {
        return users_rated;
    }
    public void setUsers_rated(Integer users_rated) {
        this.users_rated = users_rated;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public String[] getReviews() {
        return reviews;
    }
    public void setReviews(String[] reviews) {
        this.reviews = reviews;
    }
    public String getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "Game [gid=" + gid + ", name=" + name + ", year=" + year + ", ranking=" + ranking + ", users_rated="
                + users_rated + ", url=" + url + ", image=" + image + ", reviews=" + Arrays.toString(reviews)
                + ", timestamp=" + timestamp + "]";
    }

    // Create Game object from document retrieved from aggregation
    public static Game create(Document document) {
        Game game = new Game();
        game.setGid(document.getInteger("gid"));
        game.setName(document.getString("name"));
        game.setYear(document.getInteger("year"));
        game.setRanking(document.getInteger("ranking"));
        game.setUsers_rated(document.getInteger("users_rated"));
        game.setUrl(document.getString("url"));
        game.setImage(document.getString("image"));   
        // "reviews_id" field store array list of review IDs 
        // Convert to proper array list
        ArrayList reviewsIDArray = (ArrayList) document.get("reviews_id");
        List<String> reviewsIDList = new LinkedList<>();
        // Loop through array list and store each as string "/review/{review_Id}" in string list
        for (Object reviewID : reviewsIDArray) {
            ObjectId objId = (ObjectId) reviewID;
            reviewsIDList.add("/review/" + objId.toString());
        }
        // Convert string list to String array 
        game.setReviews((String[]) reviewsIDList.toArray(new String[reviewsIDList.size()]));
        game.setTimestamp(document.getDate("timestamp").toString());
        return game;
    }

    // Create JsonObject from Game Object
    public JsonObject toJSON() {
        JsonArrayBuilder jab = Json.createArrayBuilder();
        for (String review : reviews) {
            jab.add(review);
        }
        JsonArray ja = jab.build();
        return Json.createObjectBuilder()
                    .add("gid", gid)
                    .add("name", name)
                    .add("year", year)
                    .add("ranking", ranking)
                    .add("users_rated", users_rated)
                    .add("url", url)
                    .add("image", image)
                    .add("reviews", ja.toString())
                    .add("timestamp", timestamp)
                    .build();
    }

}
