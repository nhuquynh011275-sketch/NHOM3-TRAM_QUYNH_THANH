import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    private Timer timer;
    private Player player;
    private ArrayList<Item> items = new ArrayList<>();
    private ArrayList<FloatingText> floatingTexts = new ArrayList<>(); // Hiệu ứng chữ bay
    private ArrayList<Sparkle> sparkles = new ArrayList<>(); // Hiệu ứng nền lấp lánh
    private SoundManager sound = new SoundManager();
    
    private int score = 0, highScore = 0, lives = 3, level = 1;
    private int scoreMultiplier = 1; // Hệ số nhân điểm (ngôi sao)
    private int starTimer = 0; // Đếm ngược thời gian x2 điểm
    private int freezeTimer = 0; // Đếm ngược thời gian bị đóng băng (quả độc)
    
    private boolean isPaused = false, isGameOver = false, inHome = true;
    private Image heartImg;
    private JButton btnStart, btnReset, btnHome, btnResume;
    private String playerName;

    public GamePanel(String playerName) {
        this.playerName = playerName; 
        loadHighScore(); // Tải điểm cao nhất từ file
        
        // Khởi tạo hiệu ứng nền lấp lánh
        for (int i = 0; i < 60; i++) {
            sparkles.add(new Sparkle());
        }
        
        setLayout(null);
        setFocusable(true);
        addKeyListener(this);
        player = new Player(350, 460);
        heartImg = player.findImage("heart.png");

        btnStart = createStyledButton("PLAY START", 325, 250, new Color(118, 185, 0));
        btnStart.addActionListener(e -> { 
            inHome = false; timer.start(); btnStart.setVisible(false); 
            sound.playMusic("bgm.wav", true); 
            this.requestFocusInWindow();
        });
        add(btnStart);

        btnResume = createStyledButton("CONTINUE", 325, 200, Color.GRAY);
        btnResume.setVisible(false);
        btnResume.addActionListener(e -> togglePause());
        add(btnResume);

        btnReset = createStyledButton("RETRY", 325, 260, new Color(118, 185, 0));
        btnReset.setVisible(false);
        btnReset.addActionListener(e -> resetGame());
        add(btnReset);

        btnHome = createStyledButton("EXIT HOME", 325, 320, Color.RED);
        btnHome.setVisible(false);
        btnHome.addActionListener(e -> goHome());
        add(btnHome);

        timer = new Timer(30, this); 
    }

    // --- CÁC HÀM QUẢN LÝ HIGH SCORE & LỊCH SỬ ---
    private void loadHighScore() {
        try {
            File f = new File("highscore.txt");
            if (f.exists()) {
                BufferedReader br = new BufferedReader(new FileReader(f));
                highScore = Integer.parseInt(br.readLine());
                br.close();
            }
        } catch (Exception e) { highScore = 0; }
    }

    private void saveHighScore() {
        if (score > highScore) {
            highScore = score;
            try {
                PrintWriter pw = new PrintWriter(new FileWriter("highscore.txt"));
                pw.println(highScore);
                pw.close();
            } catch (Exception e) {}
        }
    }

    private void saveHistory() {
        try {
            File f = new File("history.txt");
            PrintWriter pw = new PrintWriter(new FileWriter(f, true)); // Mở file ở chế độ ghi tiếp (append)
            pw.println("Player: " + playerName + " | Score: " + score + " | Level: " + level);
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --- CÁC HÀM GIAO DIỆN & TRẠNG THÁI ---
    private JButton createStyledButton(String text, int x, int y, Color bg) {
        JButton btn = new JButton(text);
        btn.setBounds(x, y, 150, 45);
        btn.setBackground(bg); btn.setForeground(Color.WHITE); btn.setFocusable(false);
        return btn;
    }

    private void togglePause() {
        if (inHome || isGameOver) return;
        isPaused = !isPaused;
        btnResume.setVisible(isPaused); btnReset.setVisible(isPaused); btnHome.setVisible(isPaused);
        this.requestFocusInWindow();
    }

    private void resetGame() {
        score = 0; lives = 3; level = 1; isGameOver = false; isPaused = false;
        scoreMultiplier = 1; starTimer = 0; freezeTimer = 0;
        items.clear(); floatingTexts.clear();
        btnResume.setVisible(false); btnReset.setVisible(false); btnHome.setVisible(false);
        player = new Player(350, 460);
        sound.playMusic("bgm.wav", true); timer.start(); this.requestFocusInWindow();
    }

    private void goHome() {
        inHome = true; isGameOver = false; isPaused = false;
        sound.stopMusic(); btnStart.setVisible(true);
        btnResume.setVisible(false); btnReset.setVisible(false); btnHome.setVisible(false);
        items.clear(); floatingTexts.clear(); timer.stop(); repaint();
    }

    private Color getBackgroundColorForLevel(int level) {
        switch (level) {
            case 1:  return new Color(0, 0, 0);       
            case 2:  return new Color(15, 60, 15);    
            case 3:  return new Color(15, 15, 70);    
            case 4:  return new Color(60, 15, 15);    
            case 5:  return new Color(60, 40, 0);     
            default: return new Color(40, 40, 40);    
        }
    }

    // --- VẼ ĐỒ HỌA ---
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Vẽ nền đổi màu theo level
        g.setColor(getBackgroundColorForLevel(level)); g.fillRect(0, 0, 800, 600);
        
        // Vẽ hiệu ứng lấp lánh (Sparkles) đè lên màu nền
        for (Sparkle s : sparkles) {
            g.setColor(new Color(1f, 1f, 1f, Math.max(0f, Math.min(1f, s.alpha))));
            g.fillOval(s.x, s.y, s.size, s.size);
        }

        if (inHome) {
            g.setColor(Color.WHITE); g.setFont(new Font("Arial", Font.BOLD, 45));
            g.drawString("FRUIT BASKET", 240, 150);
            return;
        }

        // Vẽ hiệu ứng đóng băng
        if (freezeTimer > 0) {
            g.setColor(new Color(173, 216, 230, 100)); // Màu xanh lơ trong suốt
            g.fillRect(player.x - 10, player.y - 10, player.width + 20, player.height + 20);
        }

        if (player.image != null) g.drawImage(player.image, player.x, player.y, player.width, player.height, null);
        for (Item it : items) {
            if (it.image != null) g.drawImage(it.image, it.x, it.y, it.width, it.height, null);
            else { 
                // Fallback nếu thiếu hình ảnh
                g.setColor(it.getType() == 1 ? Color.RED : it.getType() == 3 ? Color.MAGENTA : Color.YELLOW); 
                g.fillOval(it.x, it.y, it.width, it.height); 
            }
        }
        
        // Vẽ chữ bay lên (Floating Text)
        for (FloatingText ft : floatingTexts) {
            g.setColor(new Color(ft.color.getRed(), ft.color.getGreen(), ft.color.getBlue(), Math.max(0, ft.alpha)));
            g.setFont(new Font("Arial", Font.BOLD, 24));
            g.drawString(ft.text, ft.x, ft.y);
        }

        // Vẽ UI Thông tin Player, Điểm, Level
        g.setColor(Color.WHITE); g.setFont(new Font("Arial", Font.BOLD, 22));
        g.drawString("PLAYER: " + playerName, 25, 35); 
        g.drawString("SCORE: " + score, 25, 65);
        g.drawString("LEVEL: " + level, 25, 95); 
        
        // UI Highscore và X2 Score bên góc phải
        g.setColor(Color.YELLOW); g.setFont(new Font("Arial", Font.BOLD, 18));
        g.drawString("HIGH SCORE: " + highScore, 600, 35);
        if (starTimer > 0) {
            g.setColor(Color.ORANGE); // Làm nổi bật X2
            g.drawString("X2 SCORE: " + (starTimer/30) + "s", 600, 65);
        }

        // Vẽ UI Mạng sống (Tim) kèm text "LIFE:"
        g.setColor(Color.WHITE); g.setFont(new Font("Arial", Font.BOLD, 22));
        g.drawString("LIFE:", 250, 35);
        for (int i = 0; i < lives; i++) {
            if (heartImg != null) g.drawImage(heartImg, 320 + (i * 35), 12, 28, 28, null); 
            else { g.setColor(Color.RED); g.fillOval(320 + (i * 35), 15, 20, 20); }
        }

        if (isGameOver) {
            g.setColor(new Color(0,0,0,220)); g.fillRect(0,0,800,600);
            
            g.setColor(Color.RED); g.setFont(new Font("Arial", Font.BOLD, 60));
            g.drawString("GAME OVER", 220, 110);
            
            g.setColor(Color.WHITE); g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("YOUR SCORE: " + score, 280, 170);
            
            if (score >= highScore && score > 0) {
                g.setColor(Color.YELLOW); g.setFont(new Font("Arial", Font.BOLD, 30));
                g.drawString("NEW HIGH SCORE!", 260, 220);
            }
            btnReset.setVisible(true); btnHome.setVisible(true);
        } else if (isPaused) {
            g.setColor(new Color(0,0,0,150)); g.fillRect(0,0,800,600);
            g.setColor(Color.WHITE); g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("PAUSED", 330, 150);
        }
    }

    // --- VÒNG LẶP LOGIC GAME ---
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!isGameOver && !inHome && !isPaused) {
            level = (score / 50) + 1; 
            
            // Cập nhật lấp lánh nền
            for (Sparkle s : sparkles) {
                s.update();
            }
            
            // Xử lý đếm ngược hiệu ứng
            if (starTimer > 0) { starTimer--; if (starTimer == 0) scoreMultiplier = 1; }
            if (freezeTimer > 0) freezeTimer--;

            // Tạo vật phẩm rơi (Tỉ lệ xuất hiện các loại)
            if (items.isEmpty() || (items.get(items.size()-1).y > 150)) {
                if (new Random().nextInt(100) < (5 + level)) {
                    int rand = new Random().nextInt(100);
                    int type = 0; // Mặc định trái cây
                    
                    if (rand < 15) type = 1; // 15% Bom
                    else if (rand < 20) type = 2; // 5% Mạng (Tim)
                    else if (rand < 30 && level >= 2) type = 3; // 10% Quả độc (Từ level 2)
                    else if (rand < 35 && level >= 3) type = 4; // 5% Ngôi sao x2 (Từ level 3)
                    
                    items.add(new Item(new Random().nextInt(700), 0, 3 + level, type));
                }
            }

            // Cập nhật vị trí vật phẩm & Xử lý va chạm
            for (int i = 0; i < items.size(); i++) {
                Item it = items.get(i); 
                it.update(level); // Truyền level vào để tính quỹ đạo
                
                if (it.getBounds().intersects(player.getBounds())) {
                    int type = it.getType();
                    
                    if (type == 1) { // Bom
                        lives--; sound.playSound("bomb.wav");
                        floatingTexts.add(new FloatingText(player.x, player.y, "-1 LIFE", Color.RED));
                        
                        if (lives <= 0) { 
                            isGameOver = true; 
                            saveHighScore(); 
                            saveHistory(); // <-- LƯU LỊCH SỬ TẠI ĐÂY
                            sound.stopMusic(); 
                            sound.playSound("gameover.wav"); 
                        }
                    } else if (type == 2) { // Mạng
                        if (lives < 5) lives++; 
                        sound.playSound("catch.wav");
                        floatingTexts.add(new FloatingText(player.x, player.y, "+1 LIFE", Color.PINK));
                    } else if (type == 3) { // Quả độc
                        score -= 20; freezeTimer = 45; // Đóng băng 1.5 giây (45 frame)
                        sound.playSound("bomb.wav");
                        floatingTexts.add(new FloatingText(player.x, player.y, "-20 (FROZEN)", Color.MAGENTA));
                    } else if (type == 4) { // Ngôi sao x2
                        scoreMultiplier = 2; starTimer = 300; // x2 điểm trong 10 giây (300 frame)
                        sound.playSound("catch.wav");
                        floatingTexts.add(new FloatingText(player.x, player.y, "x2 SCORE!", Color.YELLOW));
                    } else { // Trái cây bình thường
                        int points = 10 * scoreMultiplier;
                        score += points; sound.playSound("catch.wav");
                        floatingTexts.add(new FloatingText(player.x, player.y, "+" + points, Color.GREEN));
                    }
                    items.remove(i);
                } else if (it.y > 600) { items.remove(i); }
            }

            // Cập nhật chữ bay lên
            for (int i = 0; i < floatingTexts.size(); i++) {
                FloatingText ft = floatingTexts.get(i);
                ft.update();
                if (ft.alpha <= 0) floatingTexts.remove(i);
            }
        }
        repaint();
    }

    // --- ĐIỀU KHIỂN ---
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) togglePause();
        if (inHome || isGameOver || isPaused || freezeTimer > 0) return; // Bị đóng băng thì không được đi
        if (e.getKeyCode() == KeyEvent.VK_LEFT) player.moveLeft(); 
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) player.moveRight(800);
    }
    public void keyTyped(KeyEvent e) {}
    public void keyReleased(KeyEvent e) {}

    // --- LỚP PHỤ TRỢ ---

    // Lớp phụ để xử lý chữ bay lên
    private class FloatingText {
        int x, y, alpha = 255;
        String text; Color color;
        public FloatingText(int x, int y, String text, Color color) {
            this.x = x; this.y = y; this.text = text; this.color = color;
        }
        public void update() { y -= 2; alpha -= 5; } // Bay lên và mờ dần
    }

    // Lớp phụ để xử lý hiệu ứng lấp lánh (Sparkle)
    private class Sparkle {
        int x, y;
        float alpha;
        boolean fadingIn;
        int size;
        
        public Sparkle() {
            this.x = new Random().nextInt(800);
            this.y = new Random().nextInt(600);
            this.size = new Random().nextInt(4) + 2; // Kích thước hạt
            this.alpha = new Random().nextFloat();
            this.fadingIn = new Random().nextBoolean();
        }
        
        public void update() {
            if (fadingIn) {
                alpha += 0.05f;
                if (alpha >= 1.0f) { alpha = 1.0f; fadingIn = false; }
            } else {
                alpha -= 0.05f;
                if (alpha <= 0.0f) { 
                    alpha = 0.0f; 
                    fadingIn = true; 
                    x = new Random().nextInt(800); 
                    y = new Random().nextInt(600); 
                }
            }
        }
    }
}