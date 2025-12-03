package com.mycompany.btap.controller;

import com.mycompany.btap.model.mUser;
import com.mycompany.btap.service.UserService;
import com.mycompany.btap.view.vAuth;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;

/**
 * CONTROLLER - Lớp điều khiển cho màn hình Đăng nhập/Đăng ký
 * 
 * ┌─────────────────────────────────────────────────────────────┐
 * │                    MÔ HÌNH MVC CƠ BẢN                       │
 * ├─────────────────────────────────────────────────────────────┤
 * │  VIEW (vAuth.java) - Giao diện người dùng                   │
 * │    ↓ người dùng click button                                │
 * │  CONTROLLER (cAuth.java) ← BẠN ĐANG Ở ĐÂY                   │
 * │    ↓ gọi xử lý nghiệp vụ                                    │
 * │  SERVICE (UserService.java) - Xử lý logic                   │
 * │    ↓ đọc/ghi dữ liệu                                        │
 * │  MODEL (mUser.java) - Đối tượng dữ liệu                     │
 * └─────────────────────────────────────────────────────────────┘
 * 
 * Controller làm gì?
 * - Lắng nghe sự kiện từ View (click button, nhập text...)
 * - Lấy dữ liệu từ View
 * - Gọi Service để xử lý
 * - Hiển thị kết quả lên View (thông báo, chuyển màn hình...)
 * 
 * @author SinhVien
 */
public class cAuth implements ActionListener {
    
    // ==================== THUỘC TÍNH ====================
    
    /** View - màn hình đăng nhập/đăng ký */
    private final vAuth view;
    
    /** Service - xử lý logic đăng nhập/đăng ký */
    private final UserService userService;

    // ==================== CONSTRUCTOR ====================
    
    /**
     * Khởi tạo Controller
     * 
     * @param view Màn hình đăng nhập/đăng ký
     */
    public cAuth(vAuth view) {
        this.view = view;
        this.userService = new UserService();
        
        // Đăng ký các sự kiện (listeners)
        dangKySuKien();
    }

    // ==================== XỬ LÝ SỰ KIỆN ====================
    
    /**
     * Phương thức từ ActionListener
     * Được gọi khi người dùng click button
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == view.getBtnLogin()) {
            xuLyDangNhap();
        } else if (e.getSource() == view.getBtnRegister()) {
            xuLyDangKy();
        }
    }

    /**
     * Đăng ký tất cả các sự kiện (listeners) cho View
     */
    private void dangKySuKien() {
        // Sự kiện click nút Đăng nhập
        view.getBtnLogin().addActionListener(this);
        
        // Sự kiện click nút Đăng ký
        view.getBtnRegister().addActionListener(this);
        
        // Sự kiện click "Đăng ký ngay" để chuyển sang form đăng ký
        view.getLblSwitchToRegister().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                chuyenSangDangKy();
            }
        });
        
        // Sự kiện click "Đăng nhập ngay" để chuyển sang form đăng nhập
        view.getLblSwitchToLogin().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                chuyenSangDangNhap();
            }
        });
        
        // Sự kiện click "Quên mật khẩu"
        view.getLblForgotPassword().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                xuLyQuenMatKhau();
            }
        });
    }

    // ==================== XỬ LÝ ĐĂNG NHẬP ====================

    /**
     * Xử lý khi người dùng click nút Đăng nhập
     */
    private void xuLyDangNhap() {
        // BƯỚC 1: Lấy dữ liệu từ View
        String username = view.getTxtLoginUsername().getText().trim();
        String password = new String(view.getTxtLoginPassword().getPassword());

        // BƯỚC 2: Kiểm tra dữ liệu rỗng
        if (username.isEmpty() || password.isEmpty()) {
            hienThongBao("Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu.", "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // BƯỚC 3: Gọi Service để xử lý đăng nhập
        try {
            mUser user = userService.dangNhap(username, password);
            
            // Thành công → hiển thị thông báo
            hienThongBao("Đăng nhập thành công!\nXin chào " + user.getUsername(), "Thành công", JOptionPane.INFORMATION_MESSAGE);
            
            // TODO: Chuyển sang màn hình chính
            // view.dispose();
            // new HomeView(user).setVisible(true);
            
        } catch (Exception ex) {
            // Thất bại → hiển thị lỗi
            hienThongBao("Đăng nhập thất bại: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }

        // BƯỚC 4: Xóa password (bảo mật)
        view.getTxtLoginPassword().setText("");
    }

    // ==================== XỬ LÝ ĐĂNG KÝ ====================

    /**
     * Xử lý khi người dùng click nút Đăng ký
     */
    private void xuLyDangKy() {
        // BƯỚC 1: Lấy dữ liệu từ View
        String username = view.getTxtRegisterUsername().getText().trim();
        String email = view.getTxtEmail().getText().trim();
        String password = new String(view.getTxtRegisterPassword().getPassword());
        String confirmPassword = new String(view.getTxtConfirmPassword().getPassword());

        // BƯỚC 2: Kiểm tra dữ liệu rỗng
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            hienThongBao("Vui lòng điền đầy đủ tất cả các trường.", "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // BƯỚC 3: Kiểm tra mật khẩu xác nhận
        if (!password.equals(confirmPassword)) {
            hienThongBao("Mật khẩu xác nhận không khớp.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // BƯỚC 4: Gọi Service để xử lý đăng ký
        try {
            String thongBao = userService.dangKy(username, password, email);
            
            // Thành công → hiển thị thông báo và chuyển sang đăng nhập
            hienThongBao(thongBao, "Thành công", JOptionPane.INFORMATION_MESSAGE);
            xoaTruongDangKy();
            chuyenSangDangNhap();
            
        } catch (Exception ex) {
            // Thất bại → hiển thị lỗi
            hienThongBao("Đăng ký thất bại: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ==================== CHUYỂN ĐỔI MÀN HÌNH ====================

    /**
     * Chuyển sang màn hình đăng ký
     */
    private void chuyenSangDangKy() {
        xoaTruongDangNhap();
        view.showRegisterCard();
    }

    /**
     * Chuyển sang màn hình đăng nhập
     */
    private void chuyenSangDangNhap() {
        xoaTruongDangKy();
        view.showLoginCard();
    }

    /**
     * Xử lý quên mật khẩu
     */
    private void xuLyQuenMatKhau() {
        hienThongBao("Vui lòng liên hệ quản trị viên để đặt lại mật khẩu.", "Quên mật khẩu", JOptionPane.INFORMATION_MESSAGE);
    }

    // ==================== PHƯƠNG THỨC TIỆN ÍCH ====================

    /**
     * Hiển thị thông báo dialog
     */
    private void hienThongBao(String noiDung, String tieuDe, int loai) {
        JOptionPane.showMessageDialog(view, noiDung, tieuDe, loai);
    }

    /**
     * Xóa các trường đăng nhập
     */
    private void xoaTruongDangNhap() {
        view.getTxtLoginUsername().setText("");
        view.getTxtLoginPassword().setText("");
    }

    /**
     * Xóa các trường đăng ký
     */
    private void xoaTruongDangKy() {
        view.getTxtRegisterUsername().setText("");
        view.getTxtEmail().setText("");
        view.getTxtRegisterPassword().setText("");
        view.getTxtConfirmPassword().setText("");
    }
}
