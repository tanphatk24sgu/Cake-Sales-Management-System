-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th3 29, 2026 lúc 06:53 PM
-- Phiên bản máy phục vụ: 10.4.32-MariaDB
-- Phiên bản PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `cake_management`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `banh`
--

CREATE TABLE `banh` (
  `MaBanh` int(11) NOT NULL,
  `TenBanh` varchar(255) NOT NULL,
  `SoLuong` int(11) DEFAULT NULL CHECK (`SoLuong` >= 0),
  `MaDVT` int(11) DEFAULT NULL,
  `MaLoai` int(11) DEFAULT NULL,
  `MaHang` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `banh`
--

INSERT INTO `banh` (`MaBanh`, `TenBanh`, `SoLuong`, `MaDVT`, `MaLoai`, `MaHang`) VALUES
(1, 'Bánh matcha', 50, 1, 3, 1),
(2, 'Bánh tiramisu', 40, 1, 6, 2),
(3, 'Bánh mousse xoài', 30, 1, 6, 3),
(4, 'Bánh flan', 60, 1, 3, 4),
(5, 'Bánh su kem', 80, 1, 3, 5),
(6, 'Bánh mì ngọt', 100, 1, 2, 6),
(7, 'Bánh bông lan', 70, 1, 3, 7),
(8, 'Bánh cupcake', 90, 1, 3, 8),
(9, 'Bánh cheesecake', 35, 1, 6, 9),
(10, 'Bánh donut', 120, 1, 2, 10),
(11, 'Bánh caramel', 55, 1, 3, 1),
(12, 'Bánh dừa', 65, 1, 3, 2),
(13, 'Bánh đậu xanh', 75, 1, 9, 3),
(14, 'Bánh pía', 85, 1, 9, 4),
(15, 'Bánh crepe', 45, 1, 3, 5),
(16, 'Bánh waffle', 50, 1, 3, 6),
(17, 'Bánh pancake', 60, 1, 3, 7),
(18, 'Bánh mì sandwich', 110, 1, 2, 8),
(19, 'Bánh pizza mini', 70, 1, 2, 9),
(20, 'Bánh khoai mì', 80, 1, 9, 10),
(21, 'Bánh chuối', 90, 1, 9, 1),
(22, 'Bánh táo', 50, 1, 8, 2),
(23, 'Bánh việt quất', 40, 1, 8, 3),
(24, 'Bánh sầu riêng', 30, 1, 8, 4),
(25, 'Bánh cam', 60, 1, 8, 5),
(26, 'Bánh kiwi', 45, 1, 8, 6),
(27, 'Bánh dứa', 55, 1, 8, 7),
(28, 'Bánh hạnh nhân', 65, 1, 5, 8),
(29, 'Bánh mè', 75, 1, 5, 9),
(30, 'Bánh gừng', 85, 1, 5, 10);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `chitiethoadon`
--

CREATE TABLE `chitiethoadon` (
  `MaHD` int(11) DEFAULT NULL,
  `MaBanh` int(11) DEFAULT NULL,
  `SoLuong` int(11) NOT NULL CHECK (`SoLuong` >= 0),
  `DonGia` decimal(15,2) NOT NULL CHECK (`DonGia` >= 0),
  `ThanhTien` decimal(15,2) GENERATED ALWAYS AS (`SoLuong` * `DonGia`) STORED,
  `Diem` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `chitiethoadon`
--

INSERT INTO `chitiethoadon` (`MaHD`, `MaBanh`, `SoLuong`, `DonGia`, `Diem`) VALUES
(1, 1, 1, 120000.00, 10),
(1, 2, 2, 50000.00, 8),
(1, 5, 1, 70000.00, 6),
(2, 3, 2, 110000.00, 10),
(2, 6, 1, 150000.00, 12),
(3, 4, 1, 200000.00, 15),
(3, 7, 1, 130000.00, 10),
(3, 10, 2, 40000.00, 6),
(4, 2, 2, 50000.00, 8),
(4, 8, 1, 110000.00, 9),
(5, 9, 1, 300000.00, 20),
(5, 1, 1, 120000.00, 10),
(5, 3, 1, 110000.00, 9),
(6, 5, 2, 70000.00, 10),
(6, 6, 1, 150000.00, 12),
(7, 7, 2, 130000.00, 15),
(7, 8, 1, 110000.00, 8),
(7, 2, 1, 50000.00, 5),
(8, 10, 3, 40000.00, 9),
(8, 1, 1, 120000.00, 10),
(9, 3, 2, 110000.00, 12),
(9, 4, 1, 200000.00, 15),
(10, 6, 1, 150000.00, 12),
(10, 5, 2, 70000.00, 10),
(11, 2, 2, 50000.00, 8),
(11, 9, 1, 300000.00, 20),
(12, 1, 1, 120000.00, 10),
(12, 7, 2, 130000.00, 15),
(13, 8, 1, 110000.00, 8),
(13, 3, 2, 110000.00, 12),
(14, 4, 1, 200000.00, 15),
(14, 6, 1, 150000.00, 12),
(15, 5, 2, 70000.00, 10),
(15, 10, 2, 40000.00, 6),
(16, 1, 1, 120000.00, 10),
(16, 2, 1, 50000.00, 5),
(16, 3, 1, 110000.00, 8),
(17, 7, 2, 130000.00, 15),
(17, 8, 1, 110000.00, 8),
(18, 9, 1, 300000.00, 20),
(18, 10, 1, 40000.00, 5),
(19, 6, 1, 150000.00, 12),
(19, 5, 2, 70000.00, 10),
(20, 4, 1, 200000.00, 15),
(20, 2, 2, 50000.00, 8),
(21, 3, 2, 110000.00, 12),
(21, 1, 1, 120000.00, 10),
(22, 8, 1, 110000.00, 8),
(22, 7, 2, 130000.00, 15),
(23, 10, 3, 40000.00, 9),
(23, 5, 1, 70000.00, 6),
(24, 9, 1, 300000.00, 20),
(24, 4, 1, 200000.00, 15),
(25, 2, 2, 50000.00, 8),
(25, 6, 1, 150000.00, 12),
(26, 1, 1, 120000.00, 10),
(26, 3, 2, 110000.00, 12),
(27, 7, 2, 130000.00, 15),
(27, 10, 1, 40000.00, 5),
(28, 8, 1, 110000.00, 8),
(28, 5, 2, 70000.00, 10),
(29, 4, 1, 200000.00, 15),
(29, 2, 2, 50000.00, 8),
(30, 6, 1, 150000.00, 12),
(30, 9, 1, 300000.00, 20),
(31, 3, 2, 110000.00, 12),
(31, 1, 1, 120000.00, 10),
(32, 7, 2, 130000.00, 15),
(32, 8, 1, 110000.00, 8),
(33, 10, 3, 40000.00, 9),
(33, 2, 1, 50000.00, 5),
(34, 9, 1, 300000.00, 20),
(34, 6, 1, 150000.00, 12),
(35, 5, 2, 70000.00, 10),
(35, 4, 1, 200000.00, 15),
(36, 1, 1, 120000.00, 10),
(36, 7, 2, 130000.00, 15),
(37, 8, 1, 110000.00, 8),
(37, 3, 2, 110000.00, 12),
(38, 2, 2, 50000.00, 8),
(38, 10, 2, 40000.00, 6),
(39, 9, 1, 300000.00, 20),
(39, 5, 2, 70000.00, 10),
(40, 6, 1, 150000.00, 12),
(40, 4, 1, 200000.00, 15),
(41, 3, 2, 110000.00, 12),
(41, 1, 1, 120000.00, 10),
(42, 7, 2, 130000.00, 15),
(42, 8, 1, 110000.00, 8),
(43, 10, 3, 40000.00, 9),
(43, 2, 1, 50000.00, 5),
(44, 9, 1, 300000.00, 20),
(44, 6, 1, 150000.00, 12),
(45, 5, 2, 70000.00, 10),
(45, 4, 1, 200000.00, 15),
(46, 1, 1, 120000.00, 10),
(46, 7, 2, 130000.00, 15),
(47, 8, 1, 110000.00, 8),
(47, 3, 2, 110000.00, 12),
(48, 2, 2, 50000.00, 8),
(48, 10, 2, 40000.00, 6),
(49, 9, 1, 300000.00, 20),
(49, 5, 2, 70000.00, 10),
(50, 6, 1, 150000.00, 12),
(50, 4, 1, 200000.00, 15);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `chucvu`
--

CREATE TABLE `chucvu` (
  `MaChucVu` int(11) NOT NULL,
  `TenChucVu` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `chucvu`
--

INSERT INTO `chucvu` (`MaChucVu`, `TenChucVu`) VALUES
(1, 'Quản lý'),
(2, 'Nhân viên bán hàng'),
(3, 'Thu ngân'),
(4, 'Bếp trưởng'),
(5, 'Phụ bếp'),
(6, 'Nhân viên kho'),
(7, 'Giao hàng'),
(8, 'Marketing'),
(9, 'Kế toán'),
(10, 'Bảo vệ');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `chuongtrinhkhuyenmai`
--

CREATE TABLE `chuongtrinhkhuyenmai` (
  `MaKM` int(11) NOT NULL,
  `TenCTKM` varchar(255) DEFAULT NULL,
  `NgayBatDau` datetime DEFAULT current_timestamp(),
  `NgayKetThuc` datetime DEFAULT current_timestamp(),
  `GhiChu` varchar(255) DEFAULT NULL,
  `LoaiKhuyenMai` varchar(64) DEFAULT 'Giảm phần trăm',
  `PhanTramGiam` int(11) DEFAULT 0,
  `DieuKienToiThieu` decimal(15,2) DEFAULT 0.00,
  `SoLuongMua` int(11) DEFAULT 0,
  `SoLuongTang` int(11) DEFAULT 0,
  `MaBanhMua` int(11) DEFAULT NULL,
  `MaBanhTang` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `chuongtrinhkhuyenmai`
--

INSERT INTO `chuongtrinhkhuyenmai` (`MaKM`, `TenCTKM`, `NgayBatDau`, `NgayKetThuc`, `GhiChu`, `LoaiKhuyenMai`, `PhanTramGiam`, `DieuKienToiThieu`, `SoLuongMua`, `SoLuongTang`, `MaBanhMua`, `MaBanhTang`) VALUES
(1, 'Sale 10%', '2026-03-29 23:21:59', '2026-03-29 23:21:59', NULL, 'Giảm phần trăm', 10, 100.00, 0, 0, NULL, NULL),
(2, 'Sale 20%', '2026-03-29 23:21:59', '2026-03-29 23:21:59', NULL, 'Giảm phần trăm', 20, 200.00, 0, 0, NULL, NULL),
(3, 'Sale 30%', '2026-03-29 23:21:59', '2026-03-29 23:21:59', NULL, 'Giảm phần trăm', 30, 300.00, 0, 0, NULL, NULL),
(4, 'Sale 5%', '2026-03-29 23:21:59', '2026-03-29 23:21:59', NULL, 'Giảm phần trăm', 5, 50.00, 0, 0, NULL, NULL),
(5, 'Sale 15%', '2026-03-29 23:21:59', '2026-03-29 23:21:59', NULL, 'Giảm phần trăm', 15, 150.00, 0, 0, NULL, NULL),
(6, 'Combo 1', '2026-03-29 23:21:59', '2026-03-29 23:21:59', NULL, 'Giảm phần trăm', 0, 0.00, 0, 0, NULL, NULL),
(7, 'Combo 2', '2026-03-29 23:21:59', '2026-03-29 23:21:59', NULL, 'Giảm phần trăm', 0, 0.00, 0, 0, NULL, NULL),
(8, 'Tặng bánh', '2026-03-29 23:21:59', '2026-03-29 23:21:59', NULL, 'Giảm phần trăm', 0, 0.00, 0, 0, NULL, NULL),
(9, 'Flash sale', '2026-03-29 23:21:59', '2026-03-29 23:21:59', NULL, 'Giảm phần trăm', 25, 0.00, 0, 0, NULL, NULL),
(10, 'Weekend', '2026-03-29 23:21:59', '2026-03-29 23:21:59', NULL, 'Giảm phần trăm', 12, 0.00, 0, 0, NULL, NULL);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `congthuc`
--

CREATE TABLE `congthuc` (
  `MaBanh` int(11) DEFAULT NULL,
  `MaDVT` int(11) DEFAULT NULL,
  `CachLam` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `congthuc`
--

INSERT INTO `congthuc` (`MaBanh`, `MaDVT`, `CachLam`) VALUES
(1, 1, 'Trộn bột và nướng'),
(2, 1, 'Nhào bột'),
(3, 1, 'Nướng chocolate'),
(4, 1, 'Trang trí'),
(5, 1, 'Nướng giòn'),
(6, 1, 'Làm lạnh'),
(7, 1, 'Thêm trái cây'),
(8, 1, 'Truyền thống'),
(9, 1, 'Cao cấp'),
(10, 1, 'Mini');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `ct_chuongtrinhkhuyenmai`
--

CREATE TABLE `ct_chuongtrinhkhuyenmai` (
  `MaKM` int(11) DEFAULT NULL,
  `MaBanh` int(11) DEFAULT NULL,
  `PhanTramGiam` int(11) DEFAULT NULL CHECK (`PhanTramGiam` > 0 and `PhanTramGiam` <= 100)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `ct_chuongtrinhkhuyenmai`
--

INSERT INTO `ct_chuongtrinhkhuyenmai` (`MaKM`, `MaBanh`, `PhanTramGiam`) VALUES
(1, 1, 10),
(2, 2, 20),
(3, 3, 30),
(4, 4, 5),
(5, 5, 15),
(6, 6, 10),
(7, 7, 20),
(8, 8, 25),
(9, 9, 30),
(10, 10, 12);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `ct_phieunhaphang`
--

CREATE TABLE `ct_phieunhaphang` (
  `MaPhieuNhap` int(11) DEFAULT NULL,
  `MaBanh` int(11) DEFAULT NULL,
  `MaNVL` int(11) DEFAULT NULL,
  `SoLuong` int(11) NOT NULL CHECK (`SoLuong` >= 0),
  `DonGia` decimal(15,2) NOT NULL CHECK (`DonGia` >= 0),
  `ThanhTien` decimal(15,2) GENERATED ALWAYS AS (`SoLuong` * `DonGia`) STORED,
  `TinhTrang` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `ct_phieunhaphang`
--

INSERT INTO `ct_phieunhaphang` (`MaPhieuNhap`, `MaBanh`, `MaNVL`, `SoLuong`, `DonGia`, `TinhTrang`) VALUES
(1, 1, 1, 10, 20000.00, 'OK'),
(1, 2, 2, 5, 15000.00, 'OK'),
(1, 3, 3, 7, 30000.00, 'OK'),
(2, 2, 4, 20, 3000.00, 'OK'),
(2, 3, 5, 10, 50000.00, 'OK'),
(2, 4, 6, 8, 70000.00, 'OK'),
(3, 1, 7, 6, 60000.00, 'OK'),
(3, 5, 8, 3, 80000.00, 'OK'),
(3, 6, 9, 9, 5000.00, 'OK'),
(4, 7, 10, 5, 90000.00, 'OK'),
(4, 8, 1, 6, 20000.00, 'OK'),
(4, 9, 2, 7, 15000.00, 'OK'),
(5, 3, 3, 10, 30000.00, 'OK'),
(5, 4, 4, 20, 3000.00, 'OK'),
(5, 5, 5, 8, 50000.00, 'OK'),
(6, 6, 6, 7, 70000.00, 'OK'),
(6, 7, 7, 5, 60000.00, 'OK'),
(6, 8, 8, 3, 80000.00, 'OK'),
(7, 9, 9, 9, 5000.00, 'OK'),
(7, 10, 10, 4, 90000.00, 'OK'),
(7, 1, 1, 6, 20000.00, 'OK'),
(8, 2, 2, 7, 15000.00, 'OK'),
(8, 3, 3, 8, 30000.00, 'OK'),
(8, 4, 4, 9, 3000.00, 'OK'),
(9, 5, 5, 6, 50000.00, 'OK'),
(9, 6, 6, 7, 70000.00, 'OK'),
(9, 7, 7, 8, 60000.00, 'OK'),
(10, 8, 8, 5, 80000.00, 'OK'),
(10, 9, 9, 9, 5000.00, 'OK'),
(10, 10, 10, 6, 90000.00, 'OK');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `donvitinh`
--

CREATE TABLE `donvitinh` (
  `MaDVT` int(11) NOT NULL,
  `Ten` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `donvitinh`
--

INSERT INTO `donvitinh` (`MaDVT`, `Ten`) VALUES
(1, 'Cái'),
(2, 'Hộp'),
(3, 'Kg'),
(4, 'Gram'),
(5, 'Lít'),
(6, 'Chai'),
(7, 'Gói'),
(8, 'Thùng'),
(9, 'Miếng'),
(10, 'Set');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `hangsanxuat`
--

CREATE TABLE `hangsanxuat` (
  `MaHang` int(11) NOT NULL,
  `TenHang` varchar(255) NOT NULL,
  `DiaChi` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `hangsanxuat`
--

INSERT INTO `hangsanxuat` (`MaHang`, `TenHang`, `DiaChi`) VALUES
(1, 'ABC Bakery', 'HCM'),
(2, 'Sweet Home', 'Hà Nội'),
(3, 'Cake House', 'Đà Nẵng'),
(4, 'Paris Bakery', 'Pháp'),
(5, 'Tokyo Cake', 'Nhật'),
(6, 'Korea Sweet', 'Hàn Quốc'),
(7, 'USA Bakery', 'Mỹ'),
(8, 'German Cake', 'Đức'),
(9, 'Italy Sweet', 'Ý'),
(10, 'Local Brand', 'Việt Nam');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `hoadon`
--

CREATE TABLE `hoadon` (
  `MaHD` int(11) NOT NULL,
  `NgayLapHD` datetime NOT NULL,
  `MaNV` int(11) DEFAULT NULL,
  `MaKH` int(11) DEFAULT NULL,
  `ThanhTien` decimal(15,2) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `hoadon`
--

INSERT INTO `hoadon` (`MaHD`, `NgayLapHD`, `MaNV`, `MaKH`, `ThanhTien`) VALUES
(1, '2026-02-01 08:15:00', 1, 1, 250000.00),
(2, '2026-02-01 09:30:00', 2, 5, 180000.00),
(3, '2026-02-01 10:45:00', 3, 10, 320000.00),
(4, '2026-02-01 13:10:00', 4, 3, 150000.00),
(5, '2026-02-01 15:25:00', 5, 8, 410000.00),
(6, '2026-02-02 08:20:00', 6, 12, 220000.00),
(7, '2026-02-02 09:50:00', 7, 15, 360000.00),
(8, '2026-02-02 11:00:00', 8, 20, 275000.00),
(9, '2026-02-02 14:35:00', 9, 25, 500000.00),
(10, '2026-02-02 16:10:00', 10, 30, 190000.00),
(11, '2026-02-03 08:05:00', 11, 2, 210000.00),
(12, '2026-02-03 09:40:00', 12, 6, 330000.00),
(13, '2026-02-03 10:55:00', 13, 9, 280000.00),
(14, '2026-02-03 13:15:00', 14, 14, 450000.00),
(15, '2026-02-03 15:50:00', 15, 18, 370000.00),
(16, '2026-02-04 08:25:00', 16, 21, 260000.00),
(17, '2026-02-04 09:45:00', 17, 24, 390000.00),
(18, '2026-02-04 11:30:00', 18, 28, 310000.00),
(19, '2026-02-04 14:20:00', 19, 32, 470000.00),
(20, '2026-02-04 16:40:00', 20, 35, 200000.00),
(21, '2026-02-05 08:10:00', 21, 4, 180000.00),
(22, '2026-02-05 09:35:00', 22, 7, 290000.00),
(23, '2026-02-05 10:50:00', 23, 11, 340000.00),
(24, '2026-02-05 13:25:00', 24, 16, 410000.00),
(25, '2026-02-05 15:55:00', 25, 19, 380000.00),
(26, '2026-02-06 08:00:00', 26, 22, 230000.00),
(27, '2026-02-06 09:20:00', 27, 26, 360000.00),
(28, '2026-02-06 10:40:00', 28, 29, 300000.00),
(29, '2026-02-06 13:05:00', 29, 33, 420000.00),
(30, '2026-02-06 15:30:00', 30, 37, 510000.00),
(31, '2026-02-07 08:15:00', 1, 40, 275000.00),
(32, '2026-02-07 09:45:00', 2, 42, 190000.00),
(33, '2026-02-07 11:10:00', 3, 45, 350000.00),
(34, '2026-02-07 13:35:00', 4, 48, 460000.00),
(35, '2026-02-07 15:50:00', 5, 50, 220000.00),
(36, '2026-02-08 08:05:00', 6, 13, 300000.00),
(37, '2026-02-08 09:25:00', 7, 17, 410000.00),
(38, '2026-02-08 10:55:00', 8, 23, 370000.00),
(39, '2026-02-08 13:15:00', 9, 27, 480000.00),
(40, '2026-02-08 16:00:00', 10, 31, 260000.00),
(41, '2026-02-09 08:10:00', 11, 34, 310000.00),
(42, '2026-02-09 09:40:00', 12, 36, 420000.00),
(43, '2026-02-09 11:05:00', 13, 38, 390000.00),
(44, '2026-02-09 13:30:00', 14, 41, 450000.00),
(45, '2026-02-09 15:45:00', 15, 43, 280000.00),
(46, '2026-02-10 08:20:00', 16, 44, 360000.00),
(47, '2026-02-10 09:50:00', 17, 46, 470000.00),
(48, '2026-02-10 11:15:00', 18, 47, 330000.00),
(49, '2026-02-10 13:40:00', 19, 49, 410000.00),
(50, '2026-02-10 16:10:00', 20, 20, 290000.00);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `khachhang`
--

CREATE TABLE `khachhang` (
  `MaKH` int(11) NOT NULL,
  `Ho` varchar(255) NOT NULL,
  `Ten` varchar(255) NOT NULL,
  `DiaChi` varchar(255) DEFAULT NULL,
  `SDT` varchar(15) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `khachhang`
--

INSERT INTO `khachhang` (`MaKH`, `Ho`, `Ten`, `DiaChi`, `SDT`) VALUES
(1, 'Nguyễn', 'Văn An', 'HCM', '0901000001'),
(2, 'Trần', 'Thị Bích', 'HCM', '0901000002'),
(3, 'Lê', 'Hoàng Nam', 'HCM', '0901000003'),
(4, 'Phạm', 'Quang Huy', 'HCM', '0901000004'),
(5, 'Hoàng', 'Minh Tuấn', 'HCM', '0901000005'),
(6, 'Võ', 'Ngọc Lan', 'HCM', '0901000006'),
(7, 'Đặng', 'Thành Công', 'HCM', '0901000007'),
(8, 'Bùi', 'Gia Hân', 'HCM', '0901000008'),
(9, 'Đỗ', 'Anh Thư', 'HCM', '0901000009'),
(10, 'Ngô', 'Trung Kiên', 'HCM', '0901000010'),
(11, 'Nguyễn', 'Thùy Linh', 'HCM', '0901000011'),
(12, 'Trần', 'Đức Minh', 'HCM', '0901000012'),
(13, 'Lê', 'Thanh Tâm', 'HCM', '0901000013'),
(14, 'Phạm', 'Khánh Vy', 'HCM', '0901000014'),
(15, 'Hoàng', 'Quốc Bảo', 'HCM', '0901000015'),
(16, 'Võ', 'Mỹ Duyên', 'HCM', '0901000016'),
(17, 'Đặng', 'Gia Bảo', 'HCM', '0901000017'),
(18, 'Bùi', 'Thu Trang', 'HCM', '0901000018'),
(19, 'Đỗ', 'Hữu Phúc', 'HCM', '0901000019'),
(20, 'Ngô', 'Phương Anh', 'HCM', '0901000020'),
(21, 'Nguyễn', 'Công Thành', 'HCM', '0901000021'),
(22, 'Trần', 'Ngọc Hân', 'HCM', '0901000022'),
(23, 'Lê', 'Minh Khoa', 'HCM', '0901000023'),
(24, 'Phạm', 'Tuấn Kiệt', 'HCM', '0901000024'),
(25, 'Hoàng', 'Thảo My', 'HCM', '0901000025'),
(26, 'Võ', 'Đức Tài', 'HCM', '0901000026'),
(27, 'Đặng', 'Thanh Sơn', 'HCM', '0901000027'),
(28, 'Bùi', 'Kim Ngân', 'HCM', '0901000028'),
(29, 'Đỗ', 'Hải Đăng', 'HCM', '0901000029'),
(30, 'Ngô', 'Ngọc Trâm', 'HCM', '0901000030'),
(31, 'Nguyễn', 'Văn Phúc', 'HCM', '0901000031'),
(32, 'Trần', 'Bảo Trân', 'HCM', '0901000032'),
(33, 'Lê', 'Quốc Anh', 'HCM', '0901000033'),
(34, 'Phạm', 'Ngọc Mai', 'HCM', '0901000034'),
(35, 'Hoàng', 'Minh Nhật', 'HCM', '0901000035'),
(36, 'Võ', 'Thanh Hằng', 'HCM', '0901000036'),
(37, 'Đặng', 'Quang Vinh', 'HCM', '0901000037'),
(38, 'Bùi', 'Lan Chi', 'HCM', '0901000038'),
(39, 'Đỗ', 'Tuấn Anh', 'HCM', '0901000039'),
(40, 'Ngô', 'Bích Ngọc', 'HCM', '0901000040'),
(41, 'Nguyễn', 'Đức Anh', 'HCM', '0901000041'),
(42, 'Trần', 'Phương Linh', 'HCM', '0901000042'),
(43, 'Lê', 'Trọng Nghĩa', 'HCM', '0901000043'),
(44, 'Phạm', 'Thanh Huyền', 'HCM', '0901000044'),
(45, 'Hoàng', 'Anh Khoa', 'HCM', '0901000045'),
(46, 'Võ', 'Hồng Nhung', 'HCM', '0901000046'),
(47, 'Đặng', 'Gia Huy', 'HCM', '0901000047'),
(48, 'Bùi', 'Thảo Vy', 'HCM', '0901000048'),
(49, 'Đỗ', 'Minh Quân', 'HCM', '0901000049'),
(50, 'Ngô', 'Thu Hà', 'HCM', '0901000050');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `loaibanh`
--

CREATE TABLE `loaibanh` (
  `MaLoai` int(11) NOT NULL,
  `TenLoai` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `loaibanh`
--

INSERT INTO `loaibanh` (`MaLoai`, `TenLoai`) VALUES
(1, 'Bánh kem'),
(2, 'Bánh mì'),
(3, 'Bánh ngọt'),
(4, 'Bánh sinh nhật'),
(5, 'Bánh quy'),
(6, 'Bánh lạnh'),
(7, 'Bánh chocolate'),
(8, 'Bánh trái cây'),
(9, 'Bánh truyền thống'),
(10, 'Bánh cao cấp');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `nguyenlieu`
--

CREATE TABLE `nguyenlieu` (
  `MaNL` int(11) NOT NULL,
  `Ten` varchar(255) NOT NULL,
  `SoLuong` int(11) DEFAULT NULL,
  `DonGia` decimal(15,2) DEFAULT NULL,
  `MaDVT` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `nguyenlieu`
--

INSERT INTO `nguyenlieu` (`MaNL`, `Ten`, `SoLuong`, `DonGia`, `MaDVT`) VALUES
(1, 'Bột mì', 100, 20000.00, 3),
(2, 'Đường', 80, 15000.00, 3),
(3, 'Sữa', 50, 30000.00, 5),
(4, 'Trứng', 200, 3000.00, 1),
(5, 'Bơ', 40, 50000.00, 3),
(6, 'Chocolate', 30, 70000.00, 3),
(7, 'Kem tươi', 20, 60000.00, 5),
(8, 'Vanilla', 10, 80000.00, 7),
(9, 'Muối', 60, 5000.00, 3),
(10, 'Phô mai', 25, 90000.00, 3);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `nhacungcap`
--

CREATE TABLE `nhacungcap` (
  `MaNCC` int(11) NOT NULL,
  `TenNCC` varchar(255) NOT NULL,
  `MaSoThue` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `nhacungcap`
--

INSERT INTO `nhacungcap` (`MaNCC`, `TenNCC`, `MaSoThue`) VALUES
(1, 'NCC A', 'MST001'),
(2, 'NCC B', 'MST002'),
(3, 'NCC C', 'MST003'),
(4, 'NCC D', 'MST004'),
(5, 'NCC E', 'MST005'),
(6, 'NCC F', 'MST006'),
(7, 'NCC G', 'MST007'),
(8, 'NCC H', 'MST008'),
(9, 'NCC I', 'MST009'),
(10, 'NCC K', 'MST010'),
(11, 'NCC L', 'MST0010');

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `nhanvien`
--

CREATE TABLE `nhanvien` (
  `MaNV` int(11) NOT NULL,
  `Ho` varchar(100) NOT NULL,
  `Ten` varchar(100) NOT NULL,
  `NgaySinh` date DEFAULT NULL,
  `LuongCoBan` decimal(15,2) DEFAULT NULL,
  `ChucVu` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `nhanvien`
--

INSERT INTO `nhanvien` (`MaNV`, `Ho`, `Ten`, `NgaySinh`, `LuongCoBan`, `ChucVu`) VALUES
(1, 'Nguyễn', 'Văn Hùng', '1998-01-10', 8000000.00, 1),
(2, 'Trần', 'Thị Mai', '1999-02-15', 7000000.00, 2),
(3, 'Lê', 'Quang Huy', '2000-03-20', 6500000.00, 3),
(4, 'Phạm', 'Minh Tuấn', '1997-04-12', 9000000.00, 4),
(5, 'Hoàng', 'Ngọc Anh', '2001-05-05', 6000000.00, 5),
(6, 'Võ', 'Thanh Bình', '1999-06-18', 7000000.00, 6),
(7, 'Đặng', 'Hải Nam', '1996-07-22', 8500000.00, 7),
(8, 'Bùi', 'Thu Trang', '2000-08-30', 7500000.00, 8),
(9, 'Đỗ', 'Hoàng Long', '1998-09-14', 7800000.00, 9),
(10, 'Ngô', 'Phương Thảo', '2002-10-09', 5000000.00, 10),
(11, 'Nguyễn', 'Đức Anh', '1999-11-11', 7200000.00, 2),
(12, 'Trần', 'Khánh Linh', '2001-12-01', 6800000.00, 3),
(13, 'Lê', 'Thanh Sơn', '1997-01-25', 8200000.00, 4),
(14, 'Phạm', 'Gia Bảo', '1998-02-17', 7700000.00, 5),
(15, 'Hoàng', 'Minh Khoa', '2000-03-03', 6500000.00, 6),
(16, 'Võ', 'Thùy Dương', '2001-04-21', 6900000.00, 7),
(17, 'Đặng', 'Quốc Việt', '1996-05-30', 8800000.00, 8),
(18, 'Bùi', 'Lan Anh', '1999-06-06', 7400000.00, 9),
(19, 'Đỗ', 'Tuấn Kiệt', '2002-07-19', 6000000.00, 10),
(20, 'Ngô', 'Hồng Nhung', '2001-08-08', 7100000.00, 1),
(21, 'Nguyễn', 'Trọng Nghĩa', '1997-09-09', 8300000.00, 2),
(22, 'Trần', 'Bích Ngọc', '2000-10-10', 6700000.00, 3),
(23, 'Lê', 'Anh Quân', '1998-11-23', 7900000.00, 4),
(24, 'Phạm', 'Thanh Huyền', '2001-12-12', 6200000.00, 5),
(25, 'Hoàng', 'Gia Huy', '1999-01-15', 7600000.00, 6),
(26, 'Võ', 'Minh Tâm', '2000-02-27', 6800000.00, 7),
(27, 'Đặng', 'Ngọc Trâm', '2002-03-14', 5900000.00, 8),
(28, 'Bùi', 'Quốc Bảo', '1998-04-04', 8100000.00, 9),
(29, 'Đỗ', 'Thảo Vy', '2001-05-25', 6300000.00, 10),
(30, 'Ngô', 'Đức Minh', '1997-06-30', 8400000.00, 1);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `phieunhaphang`
--

CREATE TABLE `phieunhaphang` (
  `MaPhieuNhap` int(11) NOT NULL,
  `Ngay` datetime DEFAULT current_timestamp(),
  `MaNV` int(11) DEFAULT NULL,
  `MaNCC` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `phieunhaphang`
--

INSERT INTO `phieunhaphang` (`MaPhieuNhap`, `Ngay`, `MaNV`, `MaNCC`) VALUES
(1, '2026-03-29 23:21:59', 1, 1),
(2, '2026-03-29 23:21:59', 2, 2),
(3, '2026-03-29 23:21:59', 3, 3),
(4, '2026-03-29 23:21:59', 4, 4),
(5, '2026-03-29 23:21:59', 5, 5),
(6, '2026-03-29 23:21:59', 6, 6),
(7, '2026-03-29 23:21:59', 7, 7),
(8, '2026-03-29 23:21:59', 8, 8),
(9, '2026-03-29 23:21:59', 9, 9),
(10, '2026-03-29 23:21:59', 10, 10);

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `tichdiem`
--

CREATE TABLE `tichdiem` (
  `MaKH` int(11) DEFAULT NULL,
  `TICHDIEM` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `tichdiem`
--

INSERT INTO `tichdiem` (`MaKH`, `TICHDIEM`) VALUES
(1, 100),
(2, 200),
(3, 150),
(4, 300),
(5, 120),
(6, 80),
(7, 220),
(8, 90),
(9, 160),
(10, 50);

--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `banh`
--
ALTER TABLE `banh`
  ADD PRIMARY KEY (`MaBanh`),
  ADD KEY `MaDVT` (`MaDVT`),
  ADD KEY `MaLoai` (`MaLoai`),
  ADD KEY `MaHang` (`MaHang`);

--
-- Chỉ mục cho bảng `chitiethoadon`
--
ALTER TABLE `chitiethoadon`
  ADD KEY `MaHD` (`MaHD`),
  ADD KEY `MaBanh` (`MaBanh`);

--
-- Chỉ mục cho bảng `chucvu`
--
ALTER TABLE `chucvu`
  ADD PRIMARY KEY (`MaChucVu`);

--
-- Chỉ mục cho bảng `chuongtrinhkhuyenmai`
--
ALTER TABLE `chuongtrinhkhuyenmai`
  ADD PRIMARY KEY (`MaKM`);

--
-- Chỉ mục cho bảng `congthuc`
--
ALTER TABLE `congthuc`
  ADD KEY `MaBanh` (`MaBanh`),
  ADD KEY `MaDVT` (`MaDVT`);

--
-- Chỉ mục cho bảng `ct_chuongtrinhkhuyenmai`
--
ALTER TABLE `ct_chuongtrinhkhuyenmai`
  ADD KEY `MaKM` (`MaKM`),
  ADD KEY `MaBanh` (`MaBanh`);

--
-- Chỉ mục cho bảng `ct_phieunhaphang`
--
ALTER TABLE `ct_phieunhaphang`
  ADD KEY `MaPhieuNhap` (`MaPhieuNhap`),
  ADD KEY `MaBanh` (`MaBanh`),
  ADD KEY `MaNVL` (`MaNVL`);

--
-- Chỉ mục cho bảng `donvitinh`
--
ALTER TABLE `donvitinh`
  ADD PRIMARY KEY (`MaDVT`);

--
-- Chỉ mục cho bảng `hangsanxuat`
--
ALTER TABLE `hangsanxuat`
  ADD PRIMARY KEY (`MaHang`);

--
-- Chỉ mục cho bảng `hoadon`
--
ALTER TABLE `hoadon`
  ADD PRIMARY KEY (`MaHD`),
  ADD KEY `MaNV` (`MaNV`),
  ADD KEY `MaKH` (`MaKH`);

--
-- Chỉ mục cho bảng `khachhang`
--
ALTER TABLE `khachhang`
  ADD PRIMARY KEY (`MaKH`);

--
-- Chỉ mục cho bảng `loaibanh`
--
ALTER TABLE `loaibanh`
  ADD PRIMARY KEY (`MaLoai`);

--
-- Chỉ mục cho bảng `nguyenlieu`
--
ALTER TABLE `nguyenlieu`
  ADD PRIMARY KEY (`MaNL`),
  ADD KEY `MaDVT` (`MaDVT`);

--
-- Chỉ mục cho bảng `nhacungcap`
--
ALTER TABLE `nhacungcap`
  ADD PRIMARY KEY (`MaNCC`);

--
-- Chỉ mục cho bảng `nhanvien`
--
ALTER TABLE `nhanvien`
  ADD PRIMARY KEY (`MaNV`),
  ADD KEY `ChucVu` (`ChucVu`);

--
-- Chỉ mục cho bảng `phieunhaphang`
--
ALTER TABLE `phieunhaphang`
  ADD PRIMARY KEY (`MaPhieuNhap`),
  ADD KEY `MaNV` (`MaNV`),
  ADD KEY `MaNCC` (`MaNCC`);

--
-- Chỉ mục cho bảng `tichdiem`
--
ALTER TABLE `tichdiem`
  ADD KEY `MaKH` (`MaKH`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `banh`
--
ALTER TABLE `banh`
  MODIFY `MaBanh` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;

--
-- AUTO_INCREMENT cho bảng `chucvu`
--
ALTER TABLE `chucvu`
  MODIFY `MaChucVu` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT cho bảng `chuongtrinhkhuyenmai`
--
ALTER TABLE `chuongtrinhkhuyenmai`
  MODIFY `MaKM` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT cho bảng `donvitinh`
--
ALTER TABLE `donvitinh`
  MODIFY `MaDVT` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT cho bảng `hangsanxuat`
--
ALTER TABLE `hangsanxuat`
  MODIFY `MaHang` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT cho bảng `hoadon`
--
ALTER TABLE `hoadon`
  MODIFY `MaHD` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=51;

--
-- AUTO_INCREMENT cho bảng `khachhang`
--
ALTER TABLE `khachhang`
  MODIFY `MaKH` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=51;

--
-- AUTO_INCREMENT cho bảng `loaibanh`
--
ALTER TABLE `loaibanh`
  MODIFY `MaLoai` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT cho bảng `nguyenlieu`
--
ALTER TABLE `nguyenlieu`
  MODIFY `MaNL` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- AUTO_INCREMENT cho bảng `nhacungcap`
--
ALTER TABLE `nhacungcap`
  MODIFY `MaNCC` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=12;

--
-- AUTO_INCREMENT cho bảng `nhanvien`
--
ALTER TABLE `nhanvien`
  MODIFY `MaNV` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;

--
-- AUTO_INCREMENT cho bảng `phieunhaphang`
--
ALTER TABLE `phieunhaphang`
  MODIFY `MaPhieuNhap` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;

--
-- Các ràng buộc cho các bảng đã đổ
--

--
-- Các ràng buộc cho bảng `banh`
--
ALTER TABLE `banh`
  ADD CONSTRAINT `banh_ibfk_1` FOREIGN KEY (`MaDVT`) REFERENCES `donvitinh` (`MaDVT`),
  ADD CONSTRAINT `banh_ibfk_2` FOREIGN KEY (`MaLoai`) REFERENCES `loaibanh` (`MaLoai`),
  ADD CONSTRAINT `banh_ibfk_3` FOREIGN KEY (`MaHang`) REFERENCES `hangsanxuat` (`MaHang`);

--
-- Các ràng buộc cho bảng `chitiethoadon`
--
ALTER TABLE `chitiethoadon`
  ADD CONSTRAINT `chitiethoadon_ibfk_1` FOREIGN KEY (`MaHD`) REFERENCES `hoadon` (`MaHD`),
  ADD CONSTRAINT `chitiethoadon_ibfk_2` FOREIGN KEY (`MaBanh`) REFERENCES `banh` (`MaBanh`);

--
-- Các ràng buộc cho bảng `congthuc`
--
ALTER TABLE `congthuc`
  ADD CONSTRAINT `congthuc_ibfk_1` FOREIGN KEY (`MaBanh`) REFERENCES `banh` (`MaBanh`),
  ADD CONSTRAINT `congthuc_ibfk_2` FOREIGN KEY (`MaDVT`) REFERENCES `donvitinh` (`MaDVT`);

--
-- Các ràng buộc cho bảng `ct_chuongtrinhkhuyenmai`
--
ALTER TABLE `ct_chuongtrinhkhuyenmai`
  ADD CONSTRAINT `ct_chuongtrinhkhuyenmai_ibfk_1` FOREIGN KEY (`MaKM`) REFERENCES `chuongtrinhkhuyenmai` (`MaKM`),
  ADD CONSTRAINT `ct_chuongtrinhkhuyenmai_ibfk_2` FOREIGN KEY (`MaBanh`) REFERENCES `banh` (`MaBanh`);

--
-- Các ràng buộc cho bảng `ct_phieunhaphang`
--
ALTER TABLE `ct_phieunhaphang`
  ADD CONSTRAINT `ct_phieunhaphang_ibfk_1` FOREIGN KEY (`MaPhieuNhap`) REFERENCES `phieunhaphang` (`MaPhieuNhap`),
  ADD CONSTRAINT `ct_phieunhaphang_ibfk_2` FOREIGN KEY (`MaBanh`) REFERENCES `banh` (`MaBanh`),
  ADD CONSTRAINT `ct_phieunhaphang_ibfk_3` FOREIGN KEY (`MaNVL`) REFERENCES `nguyenlieu` (`MaNL`);

--
-- Các ràng buộc cho bảng `hoadon`
--
ALTER TABLE `hoadon`
  ADD CONSTRAINT `hoadon_ibfk_1` FOREIGN KEY (`MaNV`) REFERENCES `nhanvien` (`MaNV`),
  ADD CONSTRAINT `hoadon_ibfk_2` FOREIGN KEY (`MaKH`) REFERENCES `khachhang` (`MaKH`);

--
-- Các ràng buộc cho bảng `nguyenlieu`
--
ALTER TABLE `nguyenlieu`
  ADD CONSTRAINT `nguyenlieu_ibfk_1` FOREIGN KEY (`MaDVT`) REFERENCES `donvitinh` (`MaDVT`);

--
-- Các ràng buộc cho bảng `nhanvien`
--
ALTER TABLE `nhanvien`
  ADD CONSTRAINT `nhanvien_ibfk_1` FOREIGN KEY (`ChucVu`) REFERENCES `chucvu` (`MaChucVu`);

--
-- Các ràng buộc cho bảng `phieunhaphang`
--
ALTER TABLE `phieunhaphang`
  ADD CONSTRAINT `phieunhaphang_ibfk_1` FOREIGN KEY (`MaNV`) REFERENCES `nhanvien` (`MaNV`),
  ADD CONSTRAINT `phieunhaphang_ibfk_2` FOREIGN KEY (`MaNCC`) REFERENCES `nhacungcap` (`MaNCC`);

--
-- Các ràng buộc cho bảng `tichdiem`
--
ALTER TABLE `tichdiem`
  ADD CONSTRAINT `tichdiem_ibfk_1` FOREIGN KEY (`MaKH`) REFERENCES `khachhang` (`MaKH`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
