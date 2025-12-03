package com.mycompany.btap.controller;

import com.mycompany.btap.model.mUser;
import com.mycompany.btap.service.UserService;
import com.mycompany.btap.view.vAuth;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;

public class cAuth {
    private final vAuth view;
    private final UserService userService;

    public cAuth(vAuth view) {
        this.view = view;
        this.userService = new UserService();
        dangKySuKien();
    }

    private void dangKySuKien() {
        // Sự kiện click nút Đăng nhập
        view.getBtnLogin().addActionListener(e -> xuLyDangNhap());
        
        // Sự kiện click nút Đăng ký
        view.getBtnRegister().addActionListener(e -> xuLyDangKy());
        
        // Sự kiện click "Đăng ký ngay"
        view.getLblSwitchToRegister().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                chuyenSangDangKy();
            }
        });
        
        // Sự kiện click "Đăng nhập ngay"
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

    private void xuLyDangNhap() {
        String username = view.getTxtLoginUsername().getText().trim();
        String password = new String(view.getTxtLoginPassword().getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            hienThongBao("Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu.", "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            mUser user = userService.dangNhap(username, password);
            hienThongBao("Đăng nhập thành công!\nXin chào " + user.getUsername(), "Thành công", JOptionPane.INFORMATION_MESSAGE);
            
            // TODO: Chuyển sang màn hình chính
            // view.dispose();
            // new HomeView(user).setVisible(true);
            
        } catch (Exception ex) {
            hienThongBao("Đăng nhập thất bại: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }

        view.getTxtLoginPassword().setText("");
    }

    // ==================== XỬ LÝ ĐĂNG KÝ ====================

    private void xuLyDangKy() {
        String username = view.getTxtRegisterUsername().getText().trim();
        String email = view.getTxtEmail().getText().trim();
        String password = new String(view.getTxtRegisterPassword().getPassword());
        String confirmPassword = new String(view.getTxtConfirmPassword().getPassword());

        // Kiểm tra dữ liệu rỗng
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            hienThongBao("Vui lòng điền đầy đủ tất cả các trường.", "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        // Kiểm tra mật khẩu xác nhận
        if (!password.equals(confirmPassword)) {
            hienThongBao("Mật khẩu xác nhận không khớp.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            // Gọi Service đăng ký
            userService.dangKy(username, password, email);
            
            hienThongBao("Đăng ký thành công! Bạn có thể đăng nhập ngay.", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            
            // Xóa form và chuyển sang đăng nhập
            xoaTruongDangKy();
            chuyenSangDangNhap();
            
        } catch (Exception ex) {
            hienThongBao("Đăng ký thất bại: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // ==================== CHUYỂN ĐỔI MÀN HÌNH ====================

    private void chuyenSangDangKy() {
        xoaTruongDangNhap();
        view.showRegisterCard();
    }

    private void chuyenSangDangNhap() {
        xoaTruongDangKy();
        view.showLoginCard();
    }

    private void xuLyQuenMatKhau() {
        hienThongBao("Vui lòng liên hệ quản trị viên để đặt lại mật khẩu.", "Quên mật khẩu", JOptionPane.INFORMATION_MESSAGE);
    }

    // ==================== TIỆN ÍCH ====================

    private void hienThongBao(String noiDung, String tieuDe, int loai) {
        JOptionPane.showMessageDialog(view, noiDung, tieuDe, loai);
    }

    private void xoaTruongDangNhap() {
        view.getTxtLoginUsername().setText("");
        view.getTxtLoginPassword().setText("");
    }

    private void xoaTruongDangKy() {
        view.getTxtRegisterUsername().setText("");
        view.getTxtEmail().setText("");
        view.getTxtRegisterPassword().setText("");
        view.getTxtConfirmPassword().setText("");
    }
}
