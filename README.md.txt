# 🍎 GAME HỨNG TRÁI CÂY (FRUIT BASKET)

Đồ án môn học: **Lập trình Java (Java Swing)** Trường: **Đại học Sư Phạm - ĐH Đà Nẵng**

## 👥 Thành viên thực hiện
   -Lê Bảo Trâm (Nhóm trưởng)
   -Thái Thị Như Quỳnh
   -Trần Văn Thanh

## 🎮 Giới thiệu trò chơi
Game "Fruit Basket" là một trò chơi giải trí nhẹ nhàng, nơi người chơi điều khiển chú gấu di chuyển để hứng các loại trái cây rơi xuống. Mục tiêu là đạt điểm số cao nhất có thể trong khi tránh né các chướng ngại vật (bom).

## ✨ Các tính năng chính
* **Hệ thống Level:** Tốc độ rơi và độ khó tăng dần theo số điểm.
* **Vật phẩm đặc biệt:** - 🍎 **Trái cây:** Cộng điểm cơ bản.
  - ❤️ **Tim:** Tăng mạng sống.
  - ⭐ **Ngôi sao:** Nhân đôi số điểm nhận được trong thời gian ngắn.
  - 🤢 **Quả độc:** Trừ điểm và làm đóng băng di chuyển.
  - 💣 **Bom:** Mất mạng khi chạm phải.
* **Lưu lịch sử:** Tên người chơi và điểm số được lưu tự động vào file `history.txt`.
* **Hiệu ứng:** Âm thanh sống động, hiệu ứng chữ bay (Floating Text) và nền lấp lánh (Sparkle).

## 🛠 Công nghệ sử dụng
* Ngôn ngữ: **Java**
* Thư viện đồ họa: **Java Swing & AWT**
* Kiến trúc: **MVC (Model-View-Controller)** đơn giản.

## 🚀 Hướng dẫn chạy Game
1. **Yêu cầu:** Máy tính đã cài đặt JDK 8 trở lên.
2. **Cài đặt:**
   - Tải toàn bộ thư mục dự án về máy.
   - Đảm bảo thư mục `assets` nằm ở thư mục gốc (ngang hàng với `src`).
3. **Thực thi:**
   - Mở project bằng NetBeans, Eclipse hoặc IntelliJ.
   - Chạy file `Main.java` trong package mặc định.
   - Nhập tên và nhấn **PLAY START** để bắt đầu.

## 📂 Cấu trúc thư mục
- `src/`: Chứa toàn bộ mã nguồn (.java).
- `assets/`: Chứa hình ảnh (.png) và âm thanh (.wav).
- `history.txt`: File lưu trữ lịch sử người chơi.
- `highscore.txt`: File lưu điểm cao nhất mọi thời đại.