# Cake Sales Management System

## Giới thiệu

Đây là đồ án môn học xây dưng Ứng dụng Quản Lý Cửa Hàng Bán Bánh bằng Java.
Hỗ trợ cửa hàng nhỏ quản lý sản phẩm, hóa đơn, khách hàng, nhân viên và doanh thu

## Mục tiêu

- Xây dựng phần mềm quản lý bán hàng cơ bản
- Rèn luyện lập trình Java
- Áp dụng OOP và thiết kế giao diện

## Chức năng

- Quản lý sản phẩm
- Quản lý khách hàng
- Lập hóa đơn
- Quản lý nhập hàng
- Quản lý khuyến mãi
- Thống kê doanh thu

## Công nghệ

- Java Swing
- SQL Server
- SSMS
- Git, GitHub
- VS Code

## Cách chạy chương trình

1. Clone repo:
   git clone https://github.com/tanphatk24sgu/Cake-Sales-Management-System
2. Mở project bằng VS Code
3. Cấu hình database
4. Chạy file main.java

## Thành viên nhóm

- Nguyễn Lê Tấn Phát
- Nguyễn Duy Quang
- Phan Lê Ngọc Như Ý
- Trần Hoàng Sơn
- Lê Quốc Bảo
- Hồ Đắc Khả

# Code Style

## 1. Quy tắc đặt tên

### Biến & hàm

- Dùng camelCase
- Ví dụ:
- JPanel pnTenSanPham;
- double tongTien;
- void tinhDoanhThu();

### Lớp

- Dùng PascalCase
  - Ví dụ:
  - SanPham
  - HoaDon
  - KhachHang

### Hằng số

- IN HOA, cách nhau bằng \_
  - Ví dụ:
  - MAX_SIZE
  - DEFAULT_ROLE

## 2. Format Code

- Mỗi lệnh kết thúc bằng dấu ;
- Mở ngoặc trên cùng một dòng
  Ví dụ:
  if(a > b) {
  xuLy();
  }
- Thụt lề bằng tab

## 3. Comment

### Comment lớp

```java
// Lớp quản lý thông tin sản phẩm
public class SanPham {
}

```

## 4. Quy tắc viết hàm

- Mỗi hàm chỉ làm một nhiệm vụ
- Không viết hàm quá dài (>40 dòng)
- Tên hàm thể hiện đúng chức năng

## 5. Không được làm

- Viết code không comment
- Không dùng tên biến kiểu a, b, x1, x2, vô nghĩa
- Không push code lỗi, không chạy được

## 6. Quy tắc commit Git

- Commit ngắn gọn
  - Ví dụ:
  - feat: them chuc nang quan ly san pham
  - fix: sua loi tong tien
  - refactor: toi uu class HoaDon
- Không commit file rác: /bin, /out, \*.class

## Ghi chú

Dự án phục vụ mục đích học tập, không dùng cho mục đích thương mại.
