

public class BadScoreException extends Exception {
    
    private int max;
    private int min;


    public int getMax() {
        return max;
    }


    public int getMin() {
        return min;
    }


    public BadScoreException(String message, int min, int max){
        super(message);
        this.min = min;
        this.max = max;

    }
}
