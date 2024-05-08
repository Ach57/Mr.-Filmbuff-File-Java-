

public class BadDurationException extends Exception {
    private int max;
    private int min;


    public int getMax() {
        return max;
    }


    public int getMin() {
        return min;
    }


    public BadDurationException(String message, int min, int max){
        super(message);
        this.max = max;
        this.min = min;


    }
}
