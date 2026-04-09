import java.util.Random;
 
public class Item extends GameObject {
    // 0: Trái cây thường, 1: Bom, 2: Mạng (Tim), 3: Quả độc (-điểm), 4: Sao (x2 điểm)
    private int type; 
    private double angle = 0;
    private int startX;
    private static final String[] FRUITS = {"mango.png", "grapes.png", "apple.png", "watermelon.png", "guava.png"};
    
    public Item(int x, int y, int speed, int type) {
        super(x, y, 55, 55, speed, getImgName(type));
        this.type = type;
        this.startX = x;
    }
 
    private static String getImgName(int type) {
        if (type == 1) return "bomb.png";
        if (type == 2) return "heart.png";
        if (type == 3) return "poison.png"; // Nhớ thêm hình quả độc/héo
        if (type == 4) return "star.png";   // Nhớ thêm hình ngôi sao
        return FRUITS[new Random().nextInt(FRUITS.length)];
    }
 
    @Override
    public void update() {
        update(0);
    }
 
    // Truyền level vào để tính toán quỹ đạo
    public void update(int level) { 
        y += speed; 
        
        // Từ level 3, trái cây (không phải bom) sẽ rớt lượn sóng zig-zag
        if (level >= 3 && type != 1) {
            angle += 0.15;
            x = startX + (int)(Math.sin(angle) * 30);
        }
    }
    
    public int getType() { return type; }
}