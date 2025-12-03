package com.mycompany.btap.service;

import com.mycompany.btap.model.mUser;
import com.mycompany.btap.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserService {

    public void dangKy(String username, String password, String email) throws Exception {
        
        if (username == null || username.trim().isEmpty()) {
            throw new Exception("Tên đăng nhập không được để trống");
        }
        if (password == null || password.length() < 6) {
            throw new Exception("Mật khẩu phải có ít nhất 6 ký tự");
        }
        if (email == null || !email.contains("@")) {
            throw new Exception("Email không hợp lệ");
        }

        try (Connection conn = DBConnection.getConnection()) {
            String sqlKiemTra = "SELECT account_id FROM accounts a " +
                                "JOIN users u ON u.user_id = a.user_id " +
                                "WHERE a.username = ? OR u.email = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sqlKiemTra)) {
                stmt.setString(1, username.trim());
                stmt.setString(2, email.trim());
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        throw new Exception("Tên đăng nhập hoặc email đã tồn tại");
                    }
                }
            }
            
            String sqlThemUser = "INSERT INTO users (email) VALUES (?)";
            int userId = 0;
            
            try (PreparedStatement stmt = conn.prepareStatement(sqlThemUser, PreparedStatement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, email.trim());
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new Exception("Không thể tạo người dùng");
                }
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        userId = generatedKeys.getInt(1);
                    } else {
                        throw new Exception("Không thể lấy ID người dùng mới tạo");
                    }
                }
            }
            String sqlThemAccount = "INSERT INTO accounts (username, password, user_id, status, role) " +
                                    "VALUES (?, ?, ?, 'active', 'user')";
            try (PreparedStatement stmt = conn.prepareStatement(sqlThemAccount)) {
                stmt.setString(1, username.trim());
                stmt.setString(2, password);
                stmt.setInt(3, userId);
                
                int rowsAffected = stmt.executeUpdate();
                if (rowsAffected == 0) {
                    throw new Exception("Không thể tạo tài khoản");
                }
            }
            
        } catch (SQLException e) {
            throw new Exception("Lỗi kết nối database: " + e.getMessage());
        }
    }
    public mUser dangNhap(String username, String password) throws Exception {

        try (Connection conn = DBConnection.getConnection()) {
            String sql = "SELECT a.account_id, a.username, u.email, a.password " +
                         "FROM accounts a " +
                         "JOIN users u ON a.user_id = u.user_id " +
                         "WHERE a.username = ? OR u.email = ? AND a.status = 'active'";
            
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username.trim());
                stmt.setString(2, username.trim());
                
                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        String storedPassword = rs.getString("password");
                        
                        if (storedPassword.equals(password)) {
                            return new mUser(
                                rs.getInt("account_id"),
                                rs.getString("username"),
                                rs.getString("email")
                            );
                        } else {
                            throw new Exception("Sai mật khẩu");
                        }
                    } else {
                        throw new Exception("Tài khoản không tồn tại");
                    }
                }
            }
            
        } catch (SQLException e) {
            throw new Exception("Lỗi kết nối database: " + e.getMessage());
        }
    }
}
