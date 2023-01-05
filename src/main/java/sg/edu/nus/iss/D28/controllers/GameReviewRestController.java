package sg.edu.nus.iss.D28.controllers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import sg.edu.nus.iss.D28.models.Comment;
import sg.edu.nus.iss.D28.models.Game;
import sg.edu.nus.iss.D28.models.GameResult;
import sg.edu.nus.iss.D28.services.GameReviewService;

@RestController
@RequestMapping(path = "/api/game")
public class GameReviewRestController {
    
    @Autowired
    private GameReviewService gameReviewSvc;

    // localhost:8080/api/game/{gameId}/reviews
    @GetMapping(path = "/{gameId}/reviews")
    public ResponseEntity<String> getGameReviewHistory(@PathVariable String gameId) {
        Optional<Game> optGame = gameReviewSvc.aggregateGameReviews(gameId);
        JsonObject jo = Json.createObjectBuilder()
                            .add("review",optGame.get().toJSON())
                            .build();
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(jo.toString());
    }

    // localhost:8080/api/game/highest?username={user}
    @GetMapping(path = "/highest")
    public ResponseEntity<String> getHighestRatedGames(@RequestParam String username) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(getResultForUser(username, "highest", 6).toString());
    }

    // localhost:8080/api/game/lowest?username={user}
    @GetMapping(path = "/lowest")
    public ResponseEntity<String> getLowestRatedGames(@RequestParam String username) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(getResultForUser(username, "lowest", 5).toString());
    }

    private JsonObject getResultForUser(String username, String ratingString, Integer ratingInt) {
        List<Comment> commentList = gameReviewSvc.aggregateGameComment(username, ratingInt);
        GameResult gameResult = new GameResult();
        gameResult.setRating(ratingString);
        gameResult.setCommentList(commentList);
        gameResult.setTimestamp(LocalDateTime.now().toString());
        JsonObject jo = Json.createObjectBuilder().add("Result", gameResult.toJSON()).build();
        return jo;
    }
}
