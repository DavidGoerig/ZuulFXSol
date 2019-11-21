package zuul.model;

public class Game {
    private int width;
    private int height;
    private double multiplier;
    private int scorePoints;
    private double speed;
    private int points;
    private double timeWeight;

    public Game(int x, int y, char difficulty) {
        if (difficulty == 'E') {
            speed = 5;
            points = 1;
            timeWeight = 10;
        } else if (difficulty == 'N') {
            speed = 9;
            points = 5;
            timeWeight = 30;
        } else if (difficulty == 'H') {
            speed = 13;
            points = 10;
            timeWeight = 50;
        }
        width = x;
        height = y;
        if (height > 25) {
            multiplier = 15;
        } else {
            multiplier = 20;
        }
    }

    public void setGridSize(String width, String height) {
        boolean validGridSize = width.matches("-?\\d+") && height.matches("-?\\d+") && Integer.parseInt(width) >= 10
                && Integer.parseInt(width) <= 100 && Integer.parseInt(height) >= 10 && Integer.parseInt(height) <= 100;
        if (validGridSize) {
            this.width = Integer.parseInt(width);
            this.height = Integer.parseInt(height);
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public void resetScore() {
        scorePoints = 0;
    }

    public double getScale() {
        return multiplier;
    }

    public void addPoints() {
        scorePoints += points;
    }

    public void addPoints(double timepoints) {
        scorePoints += (int) timepoints;
    }

    public int getScorePoints() {
        return scorePoints;
    }

    public double getSpeed() {
        return speed;
    }

    public double getTimeWeight() {
        return timeWeight;
    }
}
