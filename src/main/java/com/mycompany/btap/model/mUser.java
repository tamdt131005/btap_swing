package com.mycompany.btap.model;

/**
 * MODEL - Lớp đại diện cho đối tượng Người dùng
 * 
 * Trong mô hình MVC:
 * - Model chứa DỮ LIỆU và các thuộc tính của đối tượng
 * - Model KHÔNG chứa logic xử lý hay giao diện
 * - Model chỉ có: thuộc tính (fields), constructor, getter/setter
 * 
 * @author SinhVien
 */
public class mUser {

    // ==================== CÁC THUỘC TÍNH ====================
    private int id;              // Mã người dùng (khóa chính)
    private String username;     // Tên đăng nhập
    private String email;        // Email
    private String password;     // Mật khẩu

    // ==================== CONSTRUCTORS ====================
    
    /**
     * Constructor mặc định - cần thiết cho Gson đọc JSON
     */
    public mUser() {
    }
    
    /**
     * Constructor đầy đủ - tạo user với tất cả thông tin
     */
    public mUser(int id, String username, String email, String password) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
    }

    /**
     * Constructor không có password - dùng khi trả về user sau đăng nhập
     * (không nên trả password ra ngoài)
     */
    public mUser(int id, String username, String email) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = null;
    }

    // ==================== GETTERS & SETTERS ====================
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    // ==================== PHƯƠNG THỨC TIỆN ÍCH ====================
    
    @Override
    public String toString() {
        return "User[id=" + id + ", username=" + username + ", email=" + email + "]";
    }
}
