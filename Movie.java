import java.io.Serializable;

//Movie class will include 10 instance variables: year, title,duration,genres, rating, score,director,actor1,actor2,andactor3
/**
 * Name(s) and ID(s) : Achraf Cheniti - 40244865 / Ali Sher - 40255236
 * COMP 249
 *  Section J-X and S
 * Assignment 02
 * Due Date 27 March 2024
 */

public class Movie implements Serializable {
    // Attributes
    private int year;
    private String title;
    private int duration;
    private String genres;
    private String rating;
    private double score;
    private String director;
    private String actor1;
    private String actor2;
    private String actor3;

    // final static array of rating
    public final String[] ratingList = { "PG", "UNRATED", "G", "R", "PG-13", "NC-17" };
    
    // final static array of genre
    public final static String[] genreList = { "musical", "comedy", "animation", "adventure", "drama", "crime",
            "biography",
            "horror",
            "action", "documentary", "fantasy", "mystery", "sci-fi", "family", "romance", "thriller", "western" };
    /**
     * 
     * @param year
     * @param title
     * @param duration
     * @param genres
     * @param rating
     * @param score
     * @param director
     * @param actor1
     * @param actor2
     * @param actor3
     * @throws BadYearException
     * @throws BadTitleException
     * @throws BadGenreException
     * @throws BadScoreException
     * @throws BadRatingException
     * @throws BadNameException
     * @throws BadDurationException
     * 
     * for every instant variable, we check the validity of it and throw an exception if found otherwise
     */
    public Movie(int year, String title, int duration, String genres,
            String rating, double score, String director, String actor1, String actor2, String actor3)
            throws BadYearException,
            BadTitleException, BadGenreException, BadScoreException, BadRatingException, BadNameException,
            BadDurationException {

        // Missing MissingQuotesException, ExcessFieldsException,MissingFieldsException.
        if (year < 1990 || year > 1999) // valid years
            throw new BadYearException("Semantic Error Invalid year", 1990, 1999);
        else
            this.year = year;


        if (title.equals("")) // check if the title is empty 
            throw new BadTitleException("Semantic Error Missing Title");
        else
            this.title = title;

        if (duration < 30 || duration > 300) // Valid duration
            throw new BadDurationException("Semantic Error Invalid Duration", 30, 300);
        else
            this.duration = duration;

        int i = 0;
        if (genres.equals("")) // check if genre is empty
            throw new BadGenreException("Sementic Error Missing Genre");
        else {
            if (genres.charAt(0) == '"' && genres.charAt(genres.length() - 1) == '"')
                genres = genres.substring(1, genres.length() - 1); // gets the output incase there are quotations like
                                                                   // "Action"
            while (i < genreList.length) {
                if (genres.equalsIgnoreCase(genreList[i])) {
                    this.genres = genres;
                    i = 0;
                    break;
                }
                i++;
            }

            if (i == genreList.length)
                throw new BadGenreException("Semantic Error Invalid Genre");
            // validates if the gener is within the list or not
        }
        // same concept with rating
        i = 0;
        if (rating.equals(""))
            throw new BadRatingException("Semantic Error Missing Rating");
        else {
            if (rating.charAt(0) == '"' && rating.charAt(rating.length() - 1) == '"')
                rating = rating.substring(1, rating.length() - 1); // gets the rating within the quotation
            while (i < ratingList.length) {
                if (rating.equalsIgnoreCase(ratingList[i])) {
                    this.rating = rating;
                    i = 0;
                    break;
                }

                i++;
            }

            if (i == ratingList.length)
                throw new BadRatingException("Semantic Error Invalid rating");

        }

        if (score < 0 || score > 10)
            throw new BadScoreException("Semantic Error Invalid Score", 0, 10);
        else
            this.score = score;

        if (director.equals(""))
            throw new BadNameException("Semantic Error Missing director");
        else
            this.director = director;

        if (actor1.equals(""))
            throw new BadNameException("Semantic Error Missing actor1");
        else
            this.actor1 = actor1;

        if (actor2.equals(""))
            throw new BadNameException("Semantic Error Missing actor2");
        else
            this.actor2 = actor2;

        if (actor3.equals(""))
            throw new BadNameException("Semantic Error Missing actor3");
        else
            this.actor3 = actor3;

    } // end of paramaterized constructor

    @Override
    public String toString() {

        return this.year + "," + this.title + "," + this.duration + "," + this.rating + "," + this.genres + ","
                + this.score + "," + this.director + " ," + this.actor1 + "," + this.actor2 + "," + this.actor3;

    } // to string method

    @Override
    public boolean equals(Object otherObject) {
        if (otherObject == null)
            return false;
        if (this.getClass() != otherObject.getClass())
            return false;

        Movie otherMovie = (Movie) otherObject;

        return this.director.equals(otherMovie.director) && this.year == otherMovie.year &&
                this.rating == otherMovie.rating && this.title.equals(otherMovie.title);

    } // compares two objects and returns true if conditions are met

    // Getters and setters:
    public int getYear() {
        return this.year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public String getGenres() {
        return this.genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public String getRating() {
        return this.rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public double getScore() {
        return this.score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public String getDirector() {
        return this.director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getActor1() {
        return this.actor1;
    }

    public void setActor1(String actor1) {
        this.actor1 = actor1;
    }

    public String getActor2() {
        return this.actor2;
    }

    public void setActor2(String actor2) {
        this.actor2 = actor2;
    }

    public String getActor3() {
        return this.actor3;
    }

    public void setActor3(String actor3) {
        this.actor3 = actor3;
    }

}
