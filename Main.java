import javax.swing.JFrame;
import javax.swing.JOptionPane;
 
public class Main {
    public static void main(String[] args) {
        // 1. Hiển thị hộp thoại yêu cầu nhập tên
        String playerName = JOptionPane.showInputDialog(
            null, 
            "Vui lòng nhập tên của bạn:", 
            "Chào mừng đến với Fruit Basket", 
            JOptionPane.QUESTION_MESSAGE
        );
        
        // 2. Xử lý trường hợp người chơi không nhập gì hoặc bấm Cancel
        if (playerName == null || playerName.trim().isEmpty()) {
            playerName = "Người chơi ẩn danh"; // Tên mặc định
        }
 
        // 3. Khởi tạo cửa sổ game và đưa tên người chơi lên tiêu đề (Title)
        JFrame f = new JFrame("Fruit Catch - Player: " + playerName + " | Nhóm: Trâm - Thanh - Quỳnh");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setSize(800, 600);
        f.setResizable(false);
        
        // Thêm GamePanel và truyền tên người chơi vào
        f.add(new GamePanel(playerName));
        
        f.setLocationRelativeTo(null);
        f.setVisible(true);
    }
}