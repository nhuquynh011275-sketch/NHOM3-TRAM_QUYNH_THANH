public class Player extends GameObject {
 
    public Player(int x, int y) {
        super(x, y, 120, 120, 20, "bear_basket.png");
    }
 
    @Override
    public void update() {
    }
 
    public void moveLeft() {
        x -= speed;
        if (x < 0) {
            x = 0; 
        }
    }
 
    public void moveRight(int screenWidth) {
        x += speed;
        if (x > screenWidth - width) {
            x = screenWidth - width;
        }
    }
}