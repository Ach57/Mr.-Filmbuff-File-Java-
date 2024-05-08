

public class BadYearException extends Exception {
    private int minYear;
    private int maxYear;

    public BadYearException(String message, int minYear, int maxYear){
        super(message);
        this.maxYear = maxYear;
        this.minYear =minYear;


    }

    public int getMaxYear() {
        return this.maxYear;
    }

    public int getMinYear() {
        return this.minYear;
    }


    
    
}
