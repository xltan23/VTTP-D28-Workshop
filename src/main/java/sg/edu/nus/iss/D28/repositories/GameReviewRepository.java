package sg.edu.nus.iss.D28.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.AddFieldsOperation;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.LookupOperation;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.aggregation.ProjectionOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import sg.edu.nus.iss.D28.models.Comment;
import sg.edu.nus.iss.D28.models.Game;

@Repository
public class GameReviewRepository {

    public static final String REVIEWS_COL = "reviews";
    
    @Autowired
    private MongoTemplate mongoTemplate;

    public Optional<Game> aggregateGameReviews(String gameId) {
        // Operation to search documents with gid = gameId (Input gameId value into operation)
        MatchOperation matchOps = Aggregation.match(Criteria.where("gid").is(Integer.parseInt(gameId)));
        // Link up games collection (local) with reviews collection (foreign) via. gid = gameId
        // Operation retrieves all reviews associated with gid, all are class under a temporary collection "reviewDoc"
        LookupOperation lookupOps = Aggregation.lookup(REVIEWS_COL, "gid", "gameId", "reviewDoc");
        // Project fields from games collection and _id array from reviewDoc collection and set timestamp for time method is called
        ProjectionOperation projectOps = Aggregation.project("_id","gid","name","year","ranking","users_rated","url","image")
                                                    // Project _id of all documents in reviewDoc => Returns an array list of review ID
                                                    .and("reviewDoc._id").as("reviews_id");
        AddFieldsOperation addFieldOps = Aggregation.addFields().addFieldWithValue("timestamp", LocalDateTime.now()).build();
        Aggregation pipeline = Aggregation.newAggregation(matchOps, lookupOps, projectOps, addFieldOps);
        // Expect on 1 document to be retrieved
        AggregationResults<Document> results = mongoTemplate.aggregate(pipeline, "games", Document.class);
        if (!results.iterator().hasNext()) {
            // If results does not have document:
            return Optional.empty();
        }
        // If document is retrieved, create Game object from document
        Document document = results.iterator().next();
        Game game = Game.create(document);
        return Optional.of(game);
    }

    public List<Comment> aggregateGameComment(String username, Integer rating) {
        // Create Criteria which takes in 2 criteria - username and rating, to perform filtering
        Criteria andCriteria = null;
        if (rating > 5) {
            andCriteria = new Criteria().andOperator(
                Criteria.where("user").is(username),
                Criteria.where("rating").gt(rating));
        } else {
            andCriteria = new Criteria().andOperator(
                Criteria.where("user").is(username),
                Criteria.where("rating").lte(rating));
        }
        // Operation to search documents that meet 2 criteria
        MatchOperation matchOps = Aggregation.match(andCriteria);
        // Link up comments collection (local) with games collection (foreign) via. gid = gid
        // Operation retrieves all games that meet criteria, all are class under a temporary collection "gameDoc"
        LookupOperation lookupOps = Aggregation.lookup("games", "gid", "gid", "gameDoc");
        // Project fields from comments collection and name array from gameDoc collection 
        ProjectionOperation projectOps = Aggregation.project("_id","user","rating")
                                                    // Project name of all documents in gameDoc => Returns an array list of game names
                                                    .and("gameDoc.name").as("boardGame");
        Aggregation pipeline = Aggregation.newAggregation(matchOps, lookupOps, projectOps);
        // The projection must match toString method in Comments for direct conversion to Comment object
        AggregationResults<Comment> results = mongoTemplate.aggregate(pipeline, "comments", Comment.class);
        return results.getMappedResults();
    }
}
