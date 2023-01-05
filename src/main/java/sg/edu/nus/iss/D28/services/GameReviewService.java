package sg.edu.nus.iss.D28.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sg.edu.nus.iss.D28.models.Comment;
import sg.edu.nus.iss.D28.models.Game;
import sg.edu.nus.iss.D28.repositories.GameReviewRepository;

@Service
public class GameReviewService {
    
    @Autowired
    private GameReviewRepository gameReviewRepo;

    public Optional<Game> aggregateGameReviews(String gameId) {
        return gameReviewRepo.aggregateGameReviews(gameId);
    }

    public List<Comment> aggregateGameComment(String username, Integer rating) {
        return gameReviewRepo.aggregateGameComment(username, rating);
    }
}
