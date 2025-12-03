package com.mycompany.btap.controller;

import com.mycompany.btap.model.mUser;
import com.mycompany.btap.service.UserService;
import com.mycompany.btap.service.impl.UserServiceImpl;
import com.mycompany.btap.view.vAuth;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

/**
 * Controller hợp nhất cho Login và Register
 * @author tamdt
 */
public class cAuth implements ActionListener {
    private final vAuth authView;
    private final UserService userService;

    public cAuth(vAuth authView) {
        this.authView = authView;
        registerListeners();
        this.userService = new UserServiceImpl();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == authView.getBtnLogin()) {
            handleLogin();
        } else if (e.getSource() == authView.getBtnRegister()) {
            handleRegister();
        }
    }

    /**
     * Đăng ký tất cả các listeners
     */
    private void registerListeners() {
        // Login button listener
        authView.getBtnLogin().addActionListener(this);
        
        // Register button listener
        authView.getBtnRegister().addActionListener(this);
        
        // Switch to Register listener
        authView.getLblSwitchToRegister().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                switchToRegister();
            }
        });
        
        // Switch to Login listener
        authView.getLblSwitchToLogin().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                switchToLogin();
            }
        });
        
        // Forgot Password listener
        authView.getLblForgotPassword().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleForgotPassword();
            }
        });
    }

    /**
     * Xử lý đăng nhập
     */
    private void handleLogin() {
        String username = authView.getTxtLoginUsername().getText().trim();
        char[] passwordChars = authView.getTxtLoginPassword().getPassword();
        String password = new String(passwordChars);

        // Validate input
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(
                    authView,
                    "Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu.",
                    "Thiếu thông tin",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Disable button and show loading state
        authView.getBtnLogin().setEnabled(false);
        authView.getBtnLogin().setText("Đang đăng nhập...");

        // Execute login in background thread
        new SwingWorker<mUser, Void>() {
            @Override
            protected mUser doInBackground() throws Exception {
                return userService.loginUser(username, password);
            }

            @Override
            protected void done() {
                try {
                    mUser user = get();
                    JOptionPane.showMessageDialog(
                            authView,
                            "Đăng nhập thành công! Xin chào " + user.getUsername(),
                            "Thành công",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    // TODO: Chuyển sang màn hình chính tại đây
                    // authView.dispose();
                    // new HomeView(user).setVisible(true);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                            authView,
                            "Đăng nhập thất bại: " + ex.getMessage(),
                            "Lỗi",
                            JOptionPane.ERROR_MESSAGE
                    );
                } finally {
                    // Re-enable button
                    authView.getBtnLogin().setEnabled(true);
                    authView.getBtnLogin().setText("Đăng Nhập");
                    // Clear password field for security
                    authView.getTxtLoginPassword().setText("");
                }
            }
        }.execute();
    }

    /**
     * Xử lý đăng ký
     */
    private void handleRegister() {
        String username = authView.getTxtRegisterUsername().getText().trim();
        String email = authView.getTxtEmail().getText().trim();
        char[] passwordChars = authView.getTxtRegisterPassword().getPassword();
        String password = new String(passwordChars);
        char[] confirmPasswordChars = authView.getTxtConfirmPassword().getPassword();
        String confirmPassword = new String(confirmPasswordChars);

        // Validate input
        if (username.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            JOptionPane.showMessageDialog(
                    authView,
                    "Vui lòng điền đầy đủ tất cả các trường.",
                    "Thiếu thông tin",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        // Validate password match
        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(
                    authView,
                    "Mật khẩu xác nhận không khớp.",
                    "Lỗi xác nhận",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // Validate email format
        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            JOptionPane.showMessageDialog(
                    authView,
                    "Email không hợp lệ.",
                    "Lỗi định dạng",
                    JOptionPane.ERROR_MESSAGE
            );
            return;
        }

        // Disable button and show loading state
        authView.getBtnRegister().setEnabled(false);
        authView.getBtnRegister().setText("Đang đăng ký...");

        // Execute register in background thread
        new SwingWorker<String, Void>() {
            @Override
            protected String doInBackground() throws Exception {
                return userService.registerUser(username, password, email);
            }

            @Override
            protected void done() {
                try {
                    String successMessage = get();
                    JOptionPane.showMessageDialog(
                            authView,
                            successMessage,
                            "Đăng ký thành công",
                            JOptionPane.INFORMATION_MESSAGE
                    );
                    // Clear all fields
                    clearRegisterFields();
                    // Switch to login view
                    switchToLogin();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(
                            authView,
                            "Đăng ký thất bại: " + ex.getMessage(),
                            "Lỗi",
                            JOptionPane.ERROR_MESSAGE
                    );
                } finally {
                    // Re-enable button
                    authView.getBtnRegister().setEnabled(true);
                    authView.getBtnRegister().setText("Đăng Ký");
                }
            }
        }.execute();
    }

    /**
     * Chuyển sang màn hình đăng ký
     */
    private void switchToRegister() {
        clearLoginFields();
        authView.showRegisterCard();
    }

    /**
     * Chuyển sang màn hình đăng nhập
     */
    private void switchToLogin() {
        clearRegisterFields();
        authView.showLoginCard();
    }

    /**
     * Xử lý quên mật khẩu
     */
    private void handleForgotPassword() {
        JOptionPane.showMessageDialog(
                authView,
                "Vui lòng liên hệ quản trị viên để đặt lại mật khẩu.",
                "Quên mật khẩu",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    /**
     * Xóa các trường đăng nhập
     */
    private void clearLoginFields() {
        authView.getTxtLoginUsername().setText("");
        authView.getTxtLoginPassword().setText("");
    }

    /**
     * Xóa các trường đăng ký
     */
    private void clearRegisterFields() {
        authView.getTxtRegisterUsername().setText("");
        authView.getTxtEmail().setText("");
        authView.getTxtRegisterPassword().setText("");
        authView.getTxtConfirmPassword().setText("");
    }
}
