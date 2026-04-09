import java.awt.*;
import java.io.File;
import javax.imageio.ImageIO;
 
public abstract class GameObject {
    protected int x, y, width, height, speed;
    protected Image image;
 
    public GameObject(int x, int y, int width, int height, int speed, String imgName) {
        this.x = x; this.y = y;
        this.width = width; this.height = height;
        this.speed = speed;
        this.image = findImage(imgName);
    }
 
    protected Image findImage(String name) {
        String[] paths = { name, "assets/" + name, "assets/assets/" + name };
        for (String p : paths) {
            File f = new File(p);
            if (f.exists()) {
                try { return ImageIO.read(f); } catch (Exception e) {}
            }
        }
        return null;
    }
 
    public abstract void update();
    public Rectangle getBounds() { return new Rectangle(x, y, width, height); }
}