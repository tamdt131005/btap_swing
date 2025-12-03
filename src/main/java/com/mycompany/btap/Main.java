package com.mycompany.btap;

import com.mycompany.btap.controller.cAuth;
import com.mycompany.btap.view.vAuth;
import javax.swing.SwingUtilities;

/**
 * MAIN - Điểm khởi đầu của ứng dụng
 * 
 * ┌─────────────────────────────────────────────────────────────┐
 * │              CẤU TRÚC MVC CỦA PROJECT                       │
 * ├─────────────────────────────────────────────────────────────┤
 * │                                                             │
 * │  ┌─────────────┐                                            │
 * │  │    VIEW     │  vAuth.java - Giao diện đăng nhập/đăng ký  │
 * │  └──────┬──────┘                                            │
 * │         │ người dùng tương tác                              │
 * │         ▼                                                   │
 * │  ┌─────────────┐                                            │
 * │  │ CONTROLLER  │  cAuth.java - Điều khiển, xử lý sự kiện    │
 * │  └──────┬──────┘                                            │
 * │         │ gọi xử lý logic                                   │
 * │         ▼                                                   │
 * │  ┌─────────────┐                                            │
 * │  │   SERVICE   │  UserService.java - Xử lý nghiệp vụ        │
 * │  └──────┬──────┘                                            │
 * │         │ đọc/ghi dữ liệu                                   │
 * │         ▼                                                   │
 * │  ┌─────────────┐                                            │
 * │  │    MODEL    │  mUser.java - Đối tượng người dùng         │
 * │  └─────────────┘                                            │
 * │                                                             │
 * └─────────────────────────────────────────────────────────────┘
 * 
 * Luồng hoạt động:
 * 1. Main tạo View (giao diện)
 * 2. Main tạo Controller và truyền View vào
 * 3. Controller lắng nghe sự kiện từ View
 * 4. Khi có sự kiện, Controller gọi Service xử lý
 * 5. Service thao tác với Model và file dữ liệu
 * 6. Controller cập nhật kết quả lên View
 * 
 * @author SinhVien
 */
public class Main {
    
    public static void main(String[] args) {
        // SwingUtilities.invokeLater đảm bảo UI được tạo trên EDT
        // (Event Dispatch Thread - luồng xử lý giao diện của Swing)
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                // Bước 1: Tạo View (giao diện)
                vAuth view = new vAuth();
                
                // Bước 2: Tạo Controller và kết nối với View
                new cAuth(view);
                
                // Lúc này ứng dụng đã sẵn sàng sử dụng!
            }
        });
    }
}
