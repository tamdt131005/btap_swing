package com.mycompany.btap.view;

import com.mycompany.btap.util.UIutil;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.HeadlessException;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

/**
 * View hợp nhất Login và Register sử dụng CardLayout
 * @author tamdt
 */
public class vAuth extends JFrame {
    // CardLayout và panel chính
    private final CardLayout cardLayout;
    private final JPanel rightCardPanel;
    private final JSplitPane splitPane;
    
    // Login components
    private JButton btnLogin;
    private JTextField txtLoginUsername;
    private JPasswordField txtLoginPassword;
    private JLabel lblSwitchToRegister;
    private JLabel lblForgotPassword;
    
    // Register components
    private JButton btnRegister;
    private JTextField txtRegisterUsername;
    private JPasswordField txtRegisterPassword;
    private JPasswordField txtConfirmPassword;
    private JTextField txtEmail;
    private JLabel lblSwitchToLogin;
    
    // Card names
    public static final String LOGIN_CARD = "LOGIN";
    public static final String REGISTER_CARD = "REGISTER";
    
    public vAuth() throws HeadlessException {
        setTitle("Xác thực");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1100, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        
        // Tạo panel bên trái (chung cho cả login và register)
        JPanel leftPanel = createLeftPanel();
        
        // Khởi tạo CardLayout cho panel bên phải
        cardLayout = new CardLayout();
        rightCardPanel = new JPanel(cardLayout);
        
        // Tạo các panel bên phải
        JPanel loginRightPanel = createLoginRightPanel();
        JPanel registerRightPanel = createRegisterRightPanel();
        
        // Thêm các panel vào CardLayout
        rightCardPanel.add(loginRightPanel, LOGIN_CARD);
        rightCardPanel.add(registerRightPanel, REGISTER_CARD);
        
        // Hiển thị panel login đầu tiên
        cardLayout.show(rightCardPanel, LOGIN_CARD);
        
        // Tạo JSplitPane với panel trái cố định và panel phải thay đổi
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel, rightCardPanel);
        splitPane.setDividerLocation(470);
        splitPane.setResizeWeight(0.43);
        splitPane.setEnabled(false);
        splitPane.setDividerSize(0);
        
        // Thêm vào frame
        add(splitPane);
        setVisible(true);
    }
    
    /**
     * Tạo panel bên trái (chung cho login và register)
     */
    private JPanel createLeftPanel() {
        JPanel left = new JPanel(new BorderLayout());
        left.setBackground(new Color(240, 248, 255));
        
        ImageIcon icon = UIutil.loadRoundedImage(getClass(), "/static/img/user_2.jpg", 280);
        JLabel img = new JLabel(icon);
        img.setHorizontalAlignment(SwingConstants.CENTER);
        img.setPreferredSize(new Dimension(280, 280));
        
        if (icon == null || icon.getImageLoadStatus() != java.awt.MediaTracker.COMPLETE) {
            img.setText("🔐");
            img.setFont(new Font("Arial", Font.PLAIN, 80));
            img.setForeground(new Color(100, 149, 237));
        }
        
        left.add(img, BorderLayout.CENTER);
        return left;
    }
    
    /**
     * Tạo panel bên phải cho đăng nhập
     */
    private JPanel createLoginRightPanel() {
        // Panel bên phải - Form đăng nhập
        JPanel right = new JPanel();
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.setBorder(new EmptyBorder(60, 100, 60, 100));
        right.setBackground(Color.WHITE);
        
        // Tiêu đề
        JLabel title = new JLabel("Chào Mừng Trở Lại");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(new Color(51, 51, 51));
        title.setAlignmentX(CENTER_ALIGNMENT);
        
        // Username
        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
        userPanel.setBackground(Color.WHITE);
        userPanel.setMaximumSize(new Dimension(520, 90));
        userPanel.setAlignmentX(CENTER_ALIGNMENT);
        
        JLabel txtUser = new JLabel("Tên đăng nhập");
        txtUser.setFont(new Font("Arial", Font.PLAIN, 13));
        txtUser.setForeground(new Color(90, 90, 90));
        txtUser.setAlignmentX(LEFT_ALIGNMENT);
        
        txtLoginUsername = new JTextField();
        txtLoginUsername.setFont(new Font("Arial", Font.PLAIN, 14));
        txtLoginUsername.setPreferredSize(new Dimension(460, 48));
        txtLoginUsername.setMaximumSize(new Dimension(460, 48));
        txtLoginUsername.setBorder(new EmptyBorder(8, 12, 8, 12));
        txtLoginUsername.setBackground(new Color(248, 249, 250));
        txtLoginUsername.setAlignmentX(LEFT_ALIGNMENT);
        UIutil.setRadius(txtLoginUsername, 8);
        
        userPanel.add(txtUser);
        userPanel.add(Box.createVerticalStrut(8));
        userPanel.add(txtLoginUsername);
        
        // Password
        JPanel passPanel = new JPanel();
        passPanel.setLayout(new BoxLayout(passPanel, BoxLayout.Y_AXIS));
        passPanel.setBackground(Color.WHITE);
        passPanel.setMaximumSize(new Dimension(520, 90));
        passPanel.setAlignmentX(CENTER_ALIGNMENT);
        
        JLabel txtPass = new JLabel("Mật khẩu");
        txtPass.setFont(new Font("Arial", Font.PLAIN, 13));
        txtPass.setForeground(new Color(90, 90, 90));
        txtPass.setAlignmentX(LEFT_ALIGNMENT);
        
        txtLoginPassword = new JPasswordField();
        txtLoginPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        txtLoginPassword.setPreferredSize(new Dimension(460, 48));
        txtLoginPassword.setMaximumSize(new Dimension(460, 48));
        txtLoginPassword.setBorder(new EmptyBorder(8, 12, 8, 12));
        txtLoginPassword.setBackground(new Color(248, 249, 250));
        txtLoginPassword.setAlignmentX(LEFT_ALIGNMENT);
        UIutil.setRadius(txtLoginPassword, 8);
        
        passPanel.add(txtPass);
        passPanel.add(Box.createVerticalStrut(8));
        passPanel.add(txtLoginPassword);
        
        // Nút đăng nhập
        btnLogin = new JButton("Đăng Nhập");
        btnLogin.setFont(new Font("Arial", Font.BOLD, 14));
        btnLogin.setAlignmentX(CENTER_ALIGNMENT);
        btnLogin.setBackground(new Color(0, 123, 255));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setPreferredSize(new Dimension(460, 60));
        btnLogin.setMaximumSize(new Dimension(460, 60));
        btnLogin.setFocusPainted(false);
        btnLogin.setBorderPainted(false);
        btnLogin.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        UIutil.setRadius(btnLogin, 8);
        
        // Quên mật khẩu
        lblForgotPassword = new JLabel("Quên mật khẩu?");
        lblForgotPassword.setFont(new Font("Arial", Font.PLAIN, 13));
        lblForgotPassword.setForeground(new Color(0, 123, 255));
        lblForgotPassword.setAlignmentX(CENTER_ALIGNMENT);
        lblForgotPassword.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        
        // Đường kẻ ngang với chữ "hoặc"
        JPanel dividerPanel = new JPanel();
        dividerPanel.setLayout(new BoxLayout(dividerPanel, BoxLayout.X_AXIS));
        dividerPanel.setBackground(Color.WHITE);
        dividerPanel.setMaximumSize(new Dimension(480, 30));
        
        JLabel line1 = new JLabel("─────────────────");
        line1.setForeground(new Color(200, 200, 200));
        JLabel orText = new JLabel(" hoặc ");
        orText.setFont(new Font("Arial", Font.PLAIN, 12));
        orText.setForeground(new Color(150, 150, 150));
        JLabel line2 = new JLabel("─────────────────");
        line2.setForeground(new Color(200, 200, 200));
        
        dividerPanel.add(line1);
        dividerPanel.add(orText);
        dividerPanel.add(line2);
        
        // Panel chuyển sang đăng ký
        JPanel switchPanel = new JPanel();
        switchPanel.setLayout(new BoxLayout(switchPanel, BoxLayout.X_AXIS));
        switchPanel.setBackground(Color.WHITE);
        switchPanel.setAlignmentX(CENTER_ALIGNMENT);
        
        JLabel txtSwitch = new JLabel("Bạn chưa có tài khoản? ");
        txtSwitch.setFont(new Font("Arial", Font.PLAIN, 13));
        txtSwitch.setForeground(new Color(100, 100, 100));
        
        lblSwitchToRegister = new JLabel("Đăng ký ngay");
        lblSwitchToRegister.setFont(new Font("Arial", Font.BOLD, 13));
        lblSwitchToRegister.setForeground(new Color(0, 123, 255));
        lblSwitchToRegister.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        
        switchPanel.add(txtSwitch);
        switchPanel.add(lblSwitchToRegister);
        
        // Thêm tất cả vào panel phải
        right.add(title);
        right.add(Box.createVerticalStrut(40));
        right.add(userPanel);
        right.add(Box.createVerticalStrut(20));
        right.add(passPanel);
        right.add(Box.createVerticalStrut(25));
        right.add(btnLogin);
        right.add(Box.createVerticalStrut(15));
        right.add(lblForgotPassword);
        right.add(Box.createVerticalStrut(20));
        right.add(dividerPanel);
        right.add(Box.createVerticalStrut(15));
        right.add(switchPanel);
        right.add(Box.createVerticalGlue());
        
        return right;
    }
    

    private JPanel createRegisterRightPanel() {
        // Panel bên phải - Form đăng ký
        JPanel right = new JPanel();
        right.setLayout(new BoxLayout(right, BoxLayout.Y_AXIS));
        right.setBorder(new EmptyBorder(40, 100, 40, 100));
        right.setBackground(Color.WHITE);
        
        // Tiêu đề
        JLabel title = new JLabel("Tạo Tài Khoản Mới");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(new Color(51, 51, 51));
        title.setAlignmentX(CENTER_ALIGNMENT);
        
        // Username
        JPanel userPanel = new JPanel();
        userPanel.setLayout(new BoxLayout(userPanel, BoxLayout.Y_AXIS));
        userPanel.setBackground(Color.WHITE);
        userPanel.setMaximumSize(new Dimension(520, 90));
        userPanel.setAlignmentX(CENTER_ALIGNMENT);
        
        JLabel txtUser = new JLabel("Tên đăng nhập");
        txtUser.setFont(new Font("Arial", Font.PLAIN, 13));
        txtUser.setForeground(new Color(90, 90, 90));
        txtUser.setAlignmentX(LEFT_ALIGNMENT);
        
        txtRegisterUsername = new JTextField();
        txtRegisterUsername.setFont(new Font("Arial", Font.PLAIN, 14));
        txtRegisterUsername.setPreferredSize(new Dimension(460, 48));
        txtRegisterUsername.setMaximumSize(new Dimension(460, 48));
        txtRegisterUsername.setBorder(new EmptyBorder(8, 12, 8, 12));
        txtRegisterUsername.setBackground(new Color(248, 249, 250));
        txtRegisterUsername.setAlignmentX(LEFT_ALIGNMENT);
        UIutil.setRadius(txtRegisterUsername, 8);
        
        userPanel.add(txtUser);
        userPanel.add(Box.createVerticalStrut(8));
        userPanel.add(txtRegisterUsername);
        
        // Email
        JPanel emailPanel = new JPanel();
        emailPanel.setLayout(new BoxLayout(emailPanel, BoxLayout.Y_AXIS));
        emailPanel.setBackground(Color.WHITE);
        emailPanel.setMaximumSize(new Dimension(520, 90));
        emailPanel.setAlignmentX(CENTER_ALIGNMENT);
        
        JLabel lblEmail = new JLabel("Email");
        lblEmail.setFont(new Font("Arial", Font.PLAIN, 13));
        lblEmail.setForeground(new Color(90, 90, 90));
        lblEmail.setAlignmentX(LEFT_ALIGNMENT);
        
        txtEmail = new JTextField();
        txtEmail.setFont(new Font("Arial", Font.PLAIN, 14));
        txtEmail.setPreferredSize(new Dimension(460, 48));
        txtEmail.setMaximumSize(new Dimension(460, 48));
        txtEmail.setBorder(new EmptyBorder(8, 12, 8, 12));
        txtEmail.setBackground(new Color(248, 249, 250));
        txtEmail.setAlignmentX(LEFT_ALIGNMENT);
        UIutil.setRadius(txtEmail, 8);
        
        emailPanel.add(lblEmail);
        emailPanel.add(Box.createVerticalStrut(8));
        emailPanel.add(txtEmail);
        
        // Password
        JPanel passPanel = new JPanel();
        passPanel.setLayout(new BoxLayout(passPanel, BoxLayout.Y_AXIS));
        passPanel.setBackground(Color.WHITE);
        passPanel.setMaximumSize(new Dimension(520, 90));
        passPanel.setAlignmentX(CENTER_ALIGNMENT);
        
        JLabel txtPass = new JLabel("Mật khẩu");
        txtPass.setFont(new Font("Arial", Font.PLAIN, 13));
        txtPass.setForeground(new Color(90, 90, 90));
        txtPass.setAlignmentX(LEFT_ALIGNMENT);
        
        txtRegisterPassword = new JPasswordField();
        txtRegisterPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        txtRegisterPassword.setPreferredSize(new Dimension(460, 48));
        txtRegisterPassword.setMaximumSize(new Dimension(460, 48));
        txtRegisterPassword.setBorder(new EmptyBorder(8, 12, 8, 12));
        txtRegisterPassword.setBackground(new Color(248, 249, 250));
        txtRegisterPassword.setAlignmentX(LEFT_ALIGNMENT);
        UIutil.setRadius(txtRegisterPassword, 8);
        
        passPanel.add(txtPass);
        passPanel.add(Box.createVerticalStrut(8));
        passPanel.add(txtRegisterPassword);
        
        // Confirm Password
        JPanel confirmPassPanel = new JPanel();
        confirmPassPanel.setLayout(new BoxLayout(confirmPassPanel, BoxLayout.Y_AXIS));
        confirmPassPanel.setBackground(Color.WHITE);
        confirmPassPanel.setMaximumSize(new Dimension(520, 90));
        confirmPassPanel.setAlignmentX(CENTER_ALIGNMENT);
        
        JLabel txtConfirmPass = new JLabel("Xác nhận mật khẩu");
        txtConfirmPass.setFont(new Font("Arial", Font.PLAIN, 13));
        txtConfirmPass.setForeground(new Color(90, 90, 90));
        txtConfirmPass.setAlignmentX(LEFT_ALIGNMENT);
        
        txtConfirmPassword = new JPasswordField();
        txtConfirmPassword.setFont(new Font("Arial", Font.PLAIN, 14));
        txtConfirmPassword.setPreferredSize(new Dimension(460, 48));
        txtConfirmPassword.setMaximumSize(new Dimension(460, 48));
        txtConfirmPassword.setBorder(new EmptyBorder(8, 12, 8, 12));
        txtConfirmPassword.setBackground(new Color(248, 249, 250));
        txtConfirmPassword.setAlignmentX(LEFT_ALIGNMENT);
        UIutil.setRadius(txtConfirmPassword, 8);
        
        confirmPassPanel.add(txtConfirmPass);
        confirmPassPanel.add(Box.createVerticalStrut(8));
        confirmPassPanel.add(txtConfirmPassword);
        
        // Nút đăng ký
        btnRegister = new JButton("Đăng Ký");
        btnRegister.setFont(new Font("Arial", Font.BOLD, 14));
        btnRegister.setAlignmentX(CENTER_ALIGNMENT);
        btnRegister.setBackground(new Color(0, 123, 255));
        btnRegister.setForeground(Color.WHITE);
        btnRegister.setPreferredSize(new Dimension(460, 60));
        btnRegister.setMaximumSize(new Dimension(460, 60));
        btnRegister.setFocusPainted(false);
        btnRegister.setBorderPainted(false);
        btnRegister.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        UIutil.setRadius(btnRegister, 8);
        
        // Đường kẻ ngang với chữ "hoặc"
        JPanel dividerPanel = new JPanel();
        dividerPanel.setLayout(new BoxLayout(dividerPanel, BoxLayout.X_AXIS));
        dividerPanel.setBackground(Color.WHITE);
        dividerPanel.setMaximumSize(new Dimension(480, 30));
        
        JLabel line1 = new JLabel("─────────────────");
        line1.setForeground(new Color(200, 200, 200));
        JLabel orText = new JLabel(" hoặc ");
        orText.setFont(new Font("Arial", Font.PLAIN, 12));
        orText.setForeground(new Color(150, 150, 150));
        JLabel line2 = new JLabel("─────────────────");
        line2.setForeground(new Color(200, 200, 200));
        
        dividerPanel.add(line1);
        dividerPanel.add(orText);
        dividerPanel.add(line2);
        
        // Panel chuyển sang đăng nhập
        JPanel switchPanel = new JPanel();
        switchPanel.setLayout(new BoxLayout(switchPanel, BoxLayout.X_AXIS));
        switchPanel.setBackground(Color.WHITE);
        switchPanel.setAlignmentX(CENTER_ALIGNMENT);
        
        JLabel txtSwitch = new JLabel("Bạn đã có tài khoản? ");
        txtSwitch.setFont(new Font("Arial", Font.PLAIN, 13));
        txtSwitch.setForeground(new Color(100, 100, 100));
        
        lblSwitchToLogin = new JLabel("Đăng nhập ngay");
        lblSwitchToLogin.setFont(new Font("Arial", Font.BOLD, 13));
        lblSwitchToLogin.setForeground(new Color(0, 123, 255));
        lblSwitchToLogin.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        
        switchPanel.add(txtSwitch);
        switchPanel.add(lblSwitchToLogin);
        
        // Thêm tất cả vào panel phải
        right.add(title);
        right.add(Box.createVerticalStrut(30));
        right.add(userPanel);
        right.add(Box.createVerticalStrut(15));
        right.add(emailPanel);
        right.add(Box.createVerticalStrut(15));
        right.add(passPanel);
        right.add(Box.createVerticalStrut(15));
        right.add(confirmPassPanel);
        right.add(Box.createVerticalStrut(20));
        right.add(btnRegister);
        right.add(Box.createVerticalStrut(15));
        right.add(dividerPanel);
        right.add(Box.createVerticalStrut(15));
        right.add(switchPanel);
        right.add(Box.createVerticalGlue());
        
        return right;
    }
    
    // Phương thức chuyển đổi giữa các card
    public void showLoginCard() {
        cardLayout.show(rightCardPanel, LOGIN_CARD);
        setTitle("Đăng Nhập");
    }
    
    public void showRegisterCard() {
        cardLayout.show(rightCardPanel, REGISTER_CARD);
        setTitle("Đăng Ký");
    }
    
    // Getters cho Login components
    public JButton getBtnLogin() {
        return btnLogin;
    }
    
    public JTextField getTxtLoginUsername() {
        return txtLoginUsername;
    }
    
    public JPasswordField getTxtLoginPassword() {
        return txtLoginPassword;
    }
    
    public JLabel getLblSwitchToRegister() {
        return lblSwitchToRegister;
    }
    
    public JLabel getLblForgotPassword() {
        return lblForgotPassword;
    }
    public JButton getBtnRegister() {
        return btnRegister;
    }
    
    public JTextField getTxtRegisterUsername() {
        return txtRegisterUsername;
    }
    
    public JPasswordField getTxtRegisterPassword() {
        return txtRegisterPassword;
    }
    
    public JPasswordField getTxtConfirmPassword() {
        return txtConfirmPassword;
    }
    
    public JTextField getTxtEmail() {
        return txtEmail;
    }
    
    public JLabel getLblSwitchToLogin() {
        return lblSwitchToLogin;
    }
}
