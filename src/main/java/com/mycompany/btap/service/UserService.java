package com.mycompany.btap.service;

import com.mycompany.btap.model.mUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * SERVICE - Lớp xử lý nghiệp vụ (Business Logic) cho User
 * 
 * ┌─────────────────────────────────────────────────────────────┐
 * │                    MÔ HÌNH MVC CƠ BẢN                       │
 * ├─────────────────────────────────────────────────────────────┤
 * │  VIEW (vAuth.java)                                          │
 * │    ↓ người dùng click button                                │
 * │  CONTROLLER (cAuth.java)                                    │
 * │    ↓ gọi xử lý nghiệp vụ                                    │
 * │  SERVICE (UserService.java)  ← BẠN ĐANG Ở ĐÂY               │
 * │    ↓ đọc/ghi dữ liệu                                        │
 * │  MODEL (mUser.java) + File JSON                             │
 * └─────────────────────────────────────────────────────────────┘
 * 
 * Service làm gì?
 * - Validate dữ liệu (kiểm tra đầu vào)
 * - Xử lý logic đăng ký, đăng nhập
 * - Đọc/ghi dữ liệu vào file JSON
 * 
 * @author SinhVien
 */
public class UserService {

    // ==================== THUỘC TÍNH ====================
    
    /** File lưu trữ danh sách user (định dạng JSON) */
    private final File usersFile;
    
    /** Thư viện Gson - chuyển đổi Object <-> JSON */
    private final Gson gson = new Gson();
    private final Type listType = new TypeToken<List<mUser>>(){}.getType();
    public UserService() {
        String home = System.getProperty("user.home");
        File dataDir = new File(home, ".btap");
        
        if (!dataDir.exists()) {
            dataDir.mkdirs();
        }
        
        this.usersFile = new File(dataDir, "users.json");
    }

    // ==================== ĐĂNG KÝ ====================

    /**
     * Đăng ký tài khoản mới
     * 
     * Quy trình:
     * 1. Validate dữ liệu (kiểm tra rỗng, độ dài, format email)
     * 2. Kiểm tra username/email đã tồn tại chưa
     * 3. Tạo ID mới (ID lớn nhất + 1)
     * 4. Lưu user mới vào file
     * 
     * @param username Tên đăng nhập
     * @param password Mật khẩu (tối thiểu 6 ký tự)
     * @param email    Email (phải chứa @)
     * @return Thông báo thành công
     * @throws Exception Nếu dữ liệu không hợp lệ hoặc đã tồn tại
     */
    public String dangKy(String username, String password, String email) throws Exception {
        
        // ===== BƯỚC 1: VALIDATE DỮ LIỆU =====
        if (username == null || username.trim().isEmpty()) {
            throw new Exception("Tên đăng nhập không được để trống");
        }
        if (password == null || password.length() < 6) {
            throw new Exception("Mật khẩu phải có ít nhất 6 ký tự");
        }
        if (email == null || !email.contains("@")) {
            throw new Exception("Email không hợp lệ");
        }

        // ===== BƯỚC 2: ĐỌC DANH SÁCH USER TỪ FILE =====
        List<mUser> danhSachUser = docTatCaUser();

        // ===== BƯỚC 3: KIỂM TRA TRÙNG LẶP =====
        for (mUser user : danhSachUser) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                throw new Exception("Tên đăng nhập đã tồn tại");
            }
            if (user.getEmail().equalsIgnoreCase(email)) {
                throw new Exception("Email đã được sử dụng");
            }
        }

        // ===== BƯỚC 4: TẠO ID MỚI =====
        int idMoi = 1;
        for (mUser user : danhSachUser) {
            if (user.getId() >= idMoi) {
                idMoi = user.getId() + 1;
            }
        }

        // ===== BƯỚC 5: TẠO USER MỚI VÀ LƯU =====
        mUser userMoi = new mUser(idMoi, username.trim(), email.trim(), password);
        danhSachUser.add(userMoi);
        ghiTatCaUser(danhSachUser);

        return "Đăng ký thành công!";
    }

    // ==================== ĐĂNG NHẬP ====================

    /**
     * Đăng nhập vào hệ thống
     * 
     * Quy trình:
     * 1. Tìm user theo username
     * 2. So sánh password
     * 3. Trả về thông tin user (KHÔNG kèm password - bảo mật)
     * 
     * @param username Tên đăng nhập
     * @param password Mật khẩu
     * @return Đối tượng mUser nếu đăng nhập thành công
     * @throws Exception Nếu sai username hoặc password
     */
    public mUser dangNhap(String username, String password) throws Exception {
        
        // Đọc danh sách user từ file
        List<mUser> danhSachUser = docTatCaUser();

        // Tìm user theo username
        for (mUser user : danhSachUser) {
            if (user.getUsername().equalsIgnoreCase(username)) {
                // Tìm thấy username, kiểm tra password
                if (user.getPassword().equals(password)) {
                    // Đúng password → trả về user (không kèm password)
                    return new mUser(user.getId(), user.getUsername(), user.getEmail());
                } else {
                    throw new Exception("Sai mật khẩu");
                }
            }
        }
        
        // Không tìm thấy username
        throw new Exception("Tài khoản không tồn tại");
    }

    // ==================== ĐỌC/GHI FILE JSON ====================

    /**
     * Đọc tất cả user từ file JSON
     * 
     * File JSON có dạng:
     * [
     *   {"id":1, "username":"user1", "email":"a@b.com", "password":"123456"},
     *   {"id":2, "username":"user2", "email":"c@d.com", "password":"abcdef"}
     * ]
     * 
     * @return Danh sách user, hoặc list rỗng nếu chưa có file
     */
    private List<mUser> docTatCaUser() {
        if (!usersFile.exists()) {
            return new ArrayList<>();
        }
        
        try (FileReader reader = new FileReader(usersFile)) {
            List<mUser> users = gson.fromJson(reader, listType);
            return users != null ? users : new ArrayList<>();
        } catch (Exception e) {
            System.out.println("Lỗi đọc file: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Ghi toàn bộ danh sách user vào file JSON
     * 
     * @param danhSachUser Danh sách user cần lưu
     */
    private void ghiTatCaUser(List<mUser> danhSachUser) {
        try (FileWriter writer = new FileWriter(usersFile)) {
            gson.toJson(danhSachUser, writer);
        } catch (Exception e) {
            System.out.println("Lỗi ghi file: " + e.getMessage());
        }
    }
}
