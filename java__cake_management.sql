USE CAKE_MANAGEMENT;

CREATE TABLE DONVITINH(
MaDVT INT AUTO_INCREMENT PRIMARY KEY,
Ten VARCHAR(255) NOT NULL
);

CREATE TABLE HANGSANXUAT(
MaHang INT AUTO_INCREMENT PRIMARY KEY,
TenHang VARCHAR(255) NOT NULL,
DiaChi VARCHAR(255)
);

CREATE TABLE NGUYENLIEU(
MaNL INT AUTO_INCREMENT PRIMARY KEY,
Ten VARCHAR(255) NOT NULL,
SoLuong INT,
DonGia DECIMAL(15,2),
MaDVT INT,
FOREIGN KEY (MaDVT) REFERENCES DONVITINH(MaDVT)
);

CREATE TABLE LOAIBANH(
MaLoai INT AUTO_INCREMENT PRIMARY KEY,
TenLoai VARCHAR(255) NOT NULL
);

CREATE TABLE CHUCVU(
MaChucVu INT AUTO_INCREMENT PRIMARY KEY,
TenChucVu VARCHAR(255)
);

CREATE TABLE NHANVIEN(
MaNV INT AUTO_INCREMENT PRIMARY KEY,
Ho VARCHAR(100) NOT NULL,
Ten VARCHAR(100) NOT NULL,
NgaySinh DATE,
LuongCoBan DECIMAL(15,2),
ChucVu INT,
FOREIGN KEY (ChucVu) REFERENCES CHUCVU(MaChucVu)
);

CREATE TABLE NHACUNGCAP(
MaNCC INT AUTO_INCREMENT PRIMARY KEY,
TenNCC VARCHAR(255) NOT NULL,
MaSoThue VARCHAR(255) NOT NULL
);

CREATE TABLE KHACHHANG(
MaKH INT AUTO_INCREMENT PRIMARY KEY,
Ho VARCHAR(255) NOT NULL,
Ten VARCHAR(255) NOT NULL,
DiaChi VARCHAR(255),
SDT VARCHAR(15)
);

CREATE TABLE HOADON(
MaHD INT AUTO_INCREMENT PRIMARY KEY,
NgayLapHD DATETIME NOT NULL,
MaNV INT,
MaKH INT,
ThanhTien DECIMAL(15,2),
FOREIGN KEY (MaNV) REFERENCES NHANVIEN(MaNV),
FOREIGN KEY (MaKH) REFERENCES KHACHHANG(MaKH)
);

CREATE TABLE CHUONGTRINHKHUYENMAI(
MaKM INT AUTO_INCREMENT PRIMARY KEY,
TenCTKM VARCHAR(255),
NgayBatDau DATETIME DEFAULT NOW(),
NgayKetThuc DATETIME DEFAULT NOW(),
GhiChu VARCHAR(255),
LoaiKhuyenMai VARCHAR(64) DEFAULT 'Giảm phần trăm',
PhanTramGiam INT DEFAULT 0,
DieuKienToiThieu DECIMAL(15,2) DEFAULT 0,
SoLuongMua INT DEFAULT 0,
SoLuongTang INT DEFAULT 0,
MaBanhMua INT NULL,
MaBanhTang INT NULL
);

CREATE TABLE PHIEUNHAPHANG(
MaPhieuNhap INT AUTO_INCREMENT PRIMARY KEY,
Ngay DATETIME DEFAULT NOW(),
MaNV INT,
MaNCC INT,
FOREIGN KEY (MaNV) REFERENCES NHANVIEN(MaNV),
FOREIGN KEY (MaNCC) REFERENCES NHACUNGCAP(MaNCC)
);

CREATE TABLE BANH(
MaBanh INT AUTO_INCREMENT PRIMARY KEY,
TenBanh VARCHAR(255) NOT NULL,
SoLuong INT CHECK (SoLuong >= 0),
MaDVT INT,
MaLoai INT,
MaHang INT,
FOREIGN KEY (MaDVT) REFERENCES DONVITINH(MaDVT),
FOREIGN KEY (MaLoai) REFERENCES LOAIBANH(MaLoai),
FOREIGN KEY (MaHang) REFERENCES HANGSANXUAT(MaHang)
);

CREATE TABLE CT_PHIEUNHAPHANG(
MaPhieuNhap INT,
MaBanh INT,
MaNVL INT,
SoLuong INT NOT NULL CHECK (SoLuong >= 0),
DonGia DECIMAL(15,2) NOT NULL CHECK (DonGia >= 0),
ThanhTien DECIMAL(15,2) GENERATED ALWAYS AS (SoLuong * DonGia) STORED,
TinhTrang VARCHAR(255),
FOREIGN KEY (MaPhieuNhap) REFERENCES PHIEUNHAPHANG(MaPhieuNhap),
FOREIGN KEY (MaBanh) REFERENCES BANH(MaBanh),
FOREIGN KEY (MaNVL) REFERENCES NGUYENLIEU(MaNL)
);

CREATE TABLE CHITIETHOADON(
MaHD INT,
MaBanh INT,
SoLuong INT NOT NULL CHECK (SoLuong >= 0),
DonGia DECIMAL(15,2) NOT NULL CHECK (DonGia >= 0),
ThanhTien DECIMAL(15,2) GENERATED ALWAYS AS (SoLuong * DonGia) STORED,
Diem INT,
FOREIGN KEY (MaHD) REFERENCES HOADON(MaHD),
FOREIGN KEY (MaBanh) REFERENCES BANH(MaBanh)
);

CREATE TABLE CT_CHUONGTRINHKHUYENMAI(
MaKM INT,
MaBanh INT,
PhanTramGiam INT CHECK (PhanTramGiam > 0 AND PhanTramGiam <= 100),
FOREIGN KEY (MaKM) REFERENCES CHUONGTRINHKHUYENMAI(MaKM),
FOREIGN KEY (MaBanh) REFERENCES BANH(MaBanh)
);

CREATE TABLE CONGTHUC(
MaBanh INT,
MaDVT INT,
CachLam VARCHAR(255),
FOREIGN KEY (MaBanh) REFERENCES BANH(MaBanh),
FOREIGN KEY (MaDVT) REFERENCES DONVITINH(MaDVT)
);

CREATE TABLE TICHDIEM(
MaKH INT,
TICHDIEM INT NOT NULL,
FOREIGN KEY (MaKH) REFERENCES KHACHHANG(MaKH)
);

INSERT INTO DONVITINH(Ten) VALUES
('Cái'), ('Hộp'), ('Kg'), ('Gram'), ('Lít'),
('Chai'), ('Gói'), ('Thùng'), ('Miếng'), ('Set');

INSERT INTO HANGSANXUAT(TenHang, DiaChi) VALUES
('ABC Bakery', 'HCM'),
('Sweet Home', 'Hà Nội'),
('Cake House', 'Đà Nẵng'),
('Paris Bakery', 'Pháp'),
('Tokyo Cake', 'Nhật'),
('Korea Sweet', 'Hàn Quốc'),
('USA Bakery', 'Mỹ'),
('German Cake', 'Đức'),
('Italy Sweet', 'Ý'),
('Local Brand', 'Việt Nam');

INSERT INTO LOAIBANH(TenLoai) VALUES
('Bánh kem'), ('Bánh mì'), ('Bánh ngọt'), ('Bánh sinh nhật'),
('Bánh quy'), ('Bánh lạnh'), ('Bánh chocolate'),
('Bánh trái cây'), ('Bánh truyền thống'), ('Bánh cao cấp');

INSERT INTO CHUCVU(TenChucVu) VALUES
('Quản lý'), ('Nhân viên bán hàng'), ('Thu ngân'),
('Bếp trưởng'), ('Phụ bếp'),
('Nhân viên kho'), ('Giao hàng'),
('Marketing'), ('Kế toán'), ('Bảo vệ');

INSERT INTO NHANVIEN(Ho, Ten, NgaySinh, LuongCoBan, ChucVu) VALUES
('Nguyễn','Văn Hùng','1998-01-10',8000000,1),
('Trần','Thị Mai','1999-02-15',7000000,2),
('Lê','Quang Huy','2000-03-20',6500000,3),
('Phạm','Minh Tuấn','1997-04-12',9000000,4),
('Hoàng','Ngọc Anh','2001-05-05',6000000,5),
('Võ','Thanh Bình','1999-06-18',7000000,6),
('Đặng','Hải Nam','1996-07-22',8500000,7),
('Bùi','Thu Trang','2000-08-30',7500000,8),
('Đỗ','Hoàng Long','1998-09-14',7800000,9),
('Ngô','Phương Thảo','2002-10-09',5000000,10),

('Nguyễn','Đức Anh','1999-11-11',7200000,2),
('Trần','Khánh Linh','2001-12-01',6800000,3),
('Lê','Thanh Sơn','1997-01-25',8200000,4),
('Phạm','Gia Bảo','1998-02-17',7700000,5),
('Hoàng','Minh Khoa','2000-03-03',6500000,6),
('Võ','Thùy Dương','2001-04-21',6900000,7),
('Đặng','Quốc Việt','1996-05-30',8800000,8),
('Bùi','Lan Anh','1999-06-06',7400000,9),
('Đỗ','Tuấn Kiệt','2002-07-19',6000000,10),
('Ngô','Hồng Nhung','2001-08-08',7100000,1),

('Nguyễn','Trọng Nghĩa','1997-09-09',8300000,2),
('Trần','Bích Ngọc','2000-10-10',6700000,3),
('Lê','Anh Quân','1998-11-23',7900000,4),
('Phạm','Thanh Huyền','2001-12-12',6200000,5),
('Hoàng','Gia Huy','1999-01-15',7600000,6),
('Võ','Minh Tâm','2000-02-27',6800000,7),
('Đặng','Ngọc Trâm','2002-03-14',5900000,8),
('Bùi','Quốc Bảo','1998-04-04',8100000,9),
('Đỗ','Thảo Vy','2001-05-25',6300000,10),
('Ngô','Đức Minh','1997-06-30',8400000,1);

INSERT INTO KHACHHANG(Ho, Ten, DiaChi, SDT) VALUES
('Nguyễn','Văn An','HCM','0901000001'),
('Trần','Thị Bích','HCM','0901000002'),
('Lê','Hoàng Nam','HCM','0901000003'),
('Phạm','Quang Huy','HCM','0901000004'),
('Hoàng','Minh Tuấn','HCM','0901000005'),
('Võ','Ngọc Lan','HCM','0901000006'),
('Đặng','Thành Công','HCM','0901000007'),
('Bùi','Gia Hân','HCM','0901000008'),
('Đỗ','Anh Thư','HCM','0901000009'),
('Ngô','Trung Kiên','HCM','0901000010'),

('Nguyễn','Thùy Linh','HCM','0901000011'),
('Trần','Đức Minh','HCM','0901000012'),
('Lê','Thanh Tâm','HCM','0901000013'),
('Phạm','Khánh Vy','HCM','0901000014'),
('Hoàng','Quốc Bảo','HCM','0901000015'),
('Võ','Mỹ Duyên','HCM','0901000016'),
('Đặng','Gia Bảo','HCM','0901000017'),
('Bùi','Thu Trang','HCM','0901000018'),
('Đỗ','Hữu Phúc','HCM','0901000019'),
('Ngô','Phương Anh','HCM','0901000020'),

('Nguyễn','Công Thành','HCM','0901000021'),
('Trần','Ngọc Hân','HCM','0901000022'),
('Lê','Minh Khoa','HCM','0901000023'),
('Phạm','Tuấn Kiệt','HCM','0901000024'),
('Hoàng','Thảo My','HCM','0901000025'),
('Võ','Đức Tài','HCM','0901000026'),
('Đặng','Thanh Sơn','HCM','0901000027'),
('Bùi','Kim Ngân','HCM','0901000028'),
('Đỗ','Hải Đăng','HCM','0901000029'),
('Ngô','Ngọc Trâm','HCM','0901000030'),

('Nguyễn','Văn Phúc','HCM','0901000031'),
('Trần','Bảo Trân','HCM','0901000032'),
('Lê','Quốc Anh','HCM','0901000033'),
('Phạm','Ngọc Mai','HCM','0901000034'),
('Hoàng','Minh Nhật','HCM','0901000035'),
('Võ','Thanh Hằng','HCM','0901000036'),
('Đặng','Quang Vinh','HCM','0901000037'),
('Bùi','Lan Chi','HCM','0901000038'),
('Đỗ','Tuấn Anh','HCM','0901000039'),
('Ngô','Bích Ngọc','HCM','0901000040'),

('Nguyễn','Đức Anh','HCM','0901000041'),
('Trần','Phương Linh','HCM','0901000042'),
('Lê','Trọng Nghĩa','HCM','0901000043'),
('Phạm','Thanh Huyền','HCM','0901000044'),
('Hoàng','Anh Khoa','HCM','0901000045'),
('Võ','Hồng Nhung','HCM','0901000046'),
('Đặng','Gia Huy','HCM','0901000047'),
('Bùi','Thảo Vy','HCM','0901000048'),
('Đỗ','Minh Quân','HCM','0901000049'),
('Ngô','Thu Hà','HCM','0901000050');

INSERT INTO NHACUNGCAP(TenNCC, MaSoThue) VALUES
('NCC A', 'MST001'),
('NCC B', 'MST002'),
('NCC C', 'MST003'),
('NCC D', 'MST004'),
('NCC E', 'MST005'),
('NCC F', 'MST006'),
('NCC G', 'MST007'),
('NCC H', 'MST008'),
('NCC I', 'MST009'),
('NCC K', 'MST010');

INSERT INTO NGUYENLIEU(Ten, SoLuong, DonGia, MaDVT) VALUES
('Bột mì', 100, 20000, 3),
('Đường', 80, 15000, 3),
('Sữa', 50, 30000, 5),
('Trứng', 200, 3000, 1),
('Bơ', 40, 50000, 3),
('Chocolate', 30, 70000, 3),
('Kem tươi', 20, 60000, 5),
('Vanilla', 10, 80000, 7),
('Muối', 60, 5000, 3),
('Phô mai', 25, 90000, 3);

INSERT INTO BANH(TenBanh, SoLuong, MaDVT, MaLoai, MaHang) VALUES
('Bánh matcha',50,1,3,1),
('Bánh tiramisu',40,1,6,2),
('Bánh mousse xoài',30,1,6,3),
('Bánh flan',60,1,3,4),
('Bánh su kem',80,1,3,5),
('Bánh mì ngọt',100,1,2,6),
('Bánh bông lan',70,1,3,7),
('Bánh cupcake',90,1,3,8),
('Bánh cheesecake',35,1,6,9),
('Bánh donut',120,1,2,10),

('Bánh caramel',55,1,3,1),
('Bánh dừa',65,1,3,2),
('Bánh đậu xanh',75,1,9,3),
('Bánh pía',85,1,9,4),
('Bánh crepe',45,1,3,5),
('Bánh waffle',50,1,3,6),
('Bánh pancake',60,1,3,7),
('Bánh mì sandwich',110,1,2,8),
('Bánh pizza mini',70,1,2,9),
('Bánh khoai mì',80,1,9,10),

('Bánh chuối',90,1,9,1),
('Bánh táo',50,1,8,2),
('Bánh việt quất',40,1,8,3),
('Bánh sầu riêng',30,1,8,4),
('Bánh cam',60,1,8,5),
('Bánh kiwi',45,1,8,6),
('Bánh dứa',55,1,8,7),
('Bánh hạnh nhân',65,1,5,8),
('Bánh mè',75,1,5,9),
('Bánh gừng',85,1,5,10);

INSERT INTO HOADON (NgayLapHD, MaNV, MaKH, ThanhTien) VALUES
('2026-02-01 08:15:00',1,1,250000),
('2026-02-01 09:30:00',2,5,180000),
('2026-02-01 10:45:00',3,10,320000),
('2026-02-01 13:10:00',4,3,150000),
('2026-02-01 15:25:00',5,8,410000),

('2026-02-02 08:20:00',6,12,220000),
('2026-02-02 09:50:00',7,15,360000),
('2026-02-02 11:00:00',8,20,275000),
('2026-02-02 14:35:00',9,25,500000),
('2026-02-02 16:10:00',10,30,190000),

('2026-02-03 08:05:00',11,2,210000),
('2026-02-03 09:40:00',12,6,330000),
('2026-02-03 10:55:00',13,9,280000),
('2026-02-03 13:15:00',14,14,450000),
('2026-02-03 15:50:00',15,18,370000),

('2026-02-04 08:25:00',16,21,260000),
('2026-02-04 09:45:00',17,24,390000),
('2026-02-04 11:30:00',18,28,310000),
('2026-02-04 14:20:00',19,32,470000),
('2026-02-04 16:40:00',20,35,200000),

('2026-02-05 08:10:00',21,4,180000),
('2026-02-05 09:35:00',22,7,290000),
('2026-02-05 10:50:00',23,11,340000),
('2026-02-05 13:25:00',24,16,410000),
('2026-02-05 15:55:00',25,19,380000),

('2026-02-06 08:00:00',26,22,230000),
('2026-02-06 09:20:00',27,26,360000),
('2026-02-06 10:40:00',28,29,300000),
('2026-02-06 13:05:00',29,33,420000),
('2026-02-06 15:30:00',30,37,510000),

('2026-02-07 08:15:00',1,40,275000),
('2026-02-07 09:45:00',2,42,190000),
('2026-02-07 11:10:00',3,45,350000),
('2026-02-07 13:35:00',4,48,460000),
('2026-02-07 15:50:00',5,50,220000),

('2026-02-08 08:05:00',6,13,300000),
('2026-02-08 09:25:00',7,17,410000),
('2026-02-08 10:55:00',8,23,370000),
('2026-02-08 13:15:00',9,27,480000),
('2026-02-08 16:00:00',10,31,260000),

('2026-02-09 08:10:00',11,34,310000),
('2026-02-09 09:40:00',12,36,420000),
('2026-02-09 11:05:00',13,38,390000),
('2026-02-09 13:30:00',14,41,450000),
('2026-02-09 15:45:00',15,43,280000),

('2026-02-10 08:20:00',16,44,360000),
('2026-02-10 09:50:00',17,46,470000),
('2026-02-10 11:15:00',18,47,330000),
('2026-02-10 13:40:00',19,49,410000),
('2026-02-10 16:10:00',20,20,290000);

INSERT INTO CHITIETHOADON (MaHD, MaBanh, SoLuong, DonGia, Diem) VALUES
-- HD 1
(1,1,1,120000,10),(1,2,2,50000,8),(1,5,1,70000,6),
-- HD 2
(2,3,2,110000,10),(2,6,1,150000,12),
-- HD 3
(3,4,1,200000,15),(3,7,1,130000,10),(3,10,2,40000,6),
-- HD 4
(4,2,2,50000,8),(4,8,1,110000,9),
-- HD 5
(5,9,1,300000,20),(5,1,1,120000,10),(5,3,1,110000,9),

-- HD 6
(6,5,2,70000,10),(6,6,1,150000,12),
-- HD 7
(7,7,2,130000,15),(7,8,1,110000,8),(7,2,1,50000,5),
-- HD 8
(8,10,3,40000,9),(8,1,1,120000,10),
-- HD 9
(9,3,2,110000,12),(9,4,1,200000,15),
-- HD 10
(10,6,1,150000,12),(10,5,2,70000,10),

-- HD 11
(11,2,2,50000,8),(11,9,1,300000,20),
-- HD 12
(12,1,1,120000,10),(12,7,2,130000,15),
-- HD 13
(13,8,1,110000,8),(13,3,2,110000,12),
-- HD 14
(14,4,1,200000,15),(14,6,1,150000,12),
-- HD 15
(15,5,2,70000,10),(15,10,2,40000,6),

-- HD 16
(16,1,1,120000,10),(16,2,1,50000,5),(16,3,1,110000,8),
-- HD 17
(17,7,2,130000,15),(17,8,1,110000,8),
-- HD 18
(18,9,1,300000,20),(18,10,1,40000,5),
-- HD 19
(19,6,1,150000,12),(19,5,2,70000,10),
-- HD 20
(20,4,1,200000,15),(20,2,2,50000,8),

-- HD 21
(21,3,2,110000,12),(21,1,1,120000,10),
-- HD 22
(22,8,1,110000,8),(22,7,2,130000,15),
-- HD 23
(23,10,3,40000,9),(23,5,1,70000,6),
-- HD 24
(24,9,1,300000,20),(24,4,1,200000,15),
-- HD 25
(25,2,2,50000,8),(25,6,1,150000,12),

-- HD 26
(26,1,1,120000,10),(26,3,2,110000,12),
-- HD 27
(27,7,2,130000,15),(27,10,1,40000,5),
-- HD 28
(28,8,1,110000,8),(28,5,2,70000,10),
-- HD 29
(29,4,1,200000,15),(29,2,2,50000,8),
-- HD 30
(30,6,1,150000,12),(30,9,1,300000,20),

-- HD 31
(31,3,2,110000,12),(31,1,1,120000,10),
-- HD 32
(32,7,2,130000,15),(32,8,1,110000,8),
-- HD 33
(33,10,3,40000,9),(33,2,1,50000,5),
-- HD 34
(34,9,1,300000,20),(34,6,1,150000,12),
-- HD 35
(35,5,2,70000,10),(35,4,1,200000,15),

-- HD 36
(36,1,1,120000,10),(36,7,2,130000,15),
-- HD 37
(37,8,1,110000,8),(37,3,2,110000,12),
-- HD 38
(38,2,2,50000,8),(38,10,2,40000,6),
-- HD 39
(39,9,1,300000,20),(39,5,2,70000,10),
-- HD 40
(40,6,1,150000,12),(40,4,1,200000,15),

-- HD 41
(41,3,2,110000,12),(41,1,1,120000,10),
-- HD 42
(42,7,2,130000,15),(42,8,1,110000,8),
-- HD 43
(43,10,3,40000,9),(43,2,1,50000,5),
-- HD 44
(44,9,1,300000,20),(44,6,1,150000,12),
-- HD 45
(45,5,2,70000,10),(45,4,1,200000,15),

-- HD 46
(46,1,1,120000,10),(46,7,2,130000,15),
-- HD 47
(47,8,1,110000,8),(47,3,2,110000,12),
-- HD 48
(48,2,2,50000,8),(48,10,2,40000,6),
-- HD 49
(49,9,1,300000,20),(49,5,2,70000,10),
-- HD 50
(50,6,1,150000,12),(50,4,1,200000,15);

INSERT INTO PHIEUNHAPHANG(MaNV, MaNCC) VALUES
(1,1),(2,2),(3,3),(4,4),(5,5),
(6,6),(7,7),(8,8),(9,9),(10,10),
(1,2),(2,3),(3,4),(4,5),(5,6),
(6,7),(7,8),(8,9),(9,10),(10,1),

(11,2),(12,3),(13,4),(14,5),(15,6),
(16,7),(17,8),(18,9),(19,10),(20,1);

INSERT INTO CT_PHIEUNHAPHANG(MaPhieuNhap, MaBanh, MaNVL, SoLuong, DonGia, TinhTrang) VALUES
(1,1,1,10,20000,'OK'),(1,2,2,5,15000,'OK'),(1,3,3,7,30000,'OK'),
(2,2,4,20,3000,'OK'),(2,3,5,10,50000,'OK'),(2,4,6,8,70000,'OK'),
(3,1,7,6,60000,'OK'),(3,5,8,3,80000,'OK'),(3,6,9,9,5000,'OK'),
(4,7,10,5,90000,'OK'),(4,8,1,6,20000,'OK'),(4,9,2,7,15000,'OK'),
(5,3,3,10,30000,'OK'),(5,4,4,20,3000,'OK'),(5,5,5,8,50000,'OK'),
(6,6,6,7,70000,'OK'),(6,7,7,5,60000,'OK'),(6,8,8,3,80000,'OK'),
(7,9,9,9,5000,'OK'),(7,10,10,4,90000,'OK'),(7,1,1,6,20000,'OK'),
(8,2,2,7,15000,'OK'),(8,3,3,8,30000,'OK'),(8,4,4,9,3000,'OK'),
(9,5,5,6,50000,'OK'),(9,6,6,7,70000,'OK'),(9,7,7,8,60000,'OK'),
(10,8,8,5,80000,'OK'),(10,9,9,9,5000,'OK'),(10,10,10,6,90000,'OK');

INSERT INTO CT_PHIEUNHAPHANG (MaPhieuNhap, MaBanh, MaNVL, SoLuong, DonGia, TinhTrang) VALUES

-- Phiếu 11
(11,1,1,10,20000,'OK'),(11,2,2,5,15000,'OK'),(11,3,3,7,30000,'OK'),

-- Phiếu 12
(12,4,4,20,3000,'OK'),(12,5,5,10,50000,'OK'),(12,6,6,8,70000,'OK'),

-- Phiếu 13
(13,7,7,6,60000,'OK'),(13,8,8,3,80000,'OK'),(13,9,9,9,5000,'OK'),

-- Phiếu 14
(14,10,10,5,90000,'OK'),(14,1,1,6,20000,'OK'),(14,2,2,7,15000,'OK'),

-- Phiếu 15
(15,3,3,8,30000,'OK'),(15,4,4,9,3000,'OK'),(15,5,5,6,50000,'OK'),

-- Phiếu 16
(16,6,6,7,70000,'OK'),(16,7,7,8,60000,'OK'),(16,8,8,5,80000,'OK'),

-- Phiếu 17
(17,9,9,9,5000,'OK'),(17,10,10,6,90000,'OK'),(17,1,1,6,20000,'OK'),

-- Phiếu 18
(18,2,2,7,15000,'OK'),(18,3,3,8,30000,'OK'),(18,4,4,9,3000,'OK'),

-- Phiếu 19
(19,5,5,6,50000,'OK'),(19,6,6,7,70000,'OK'),(19,7,7,8,60000,'OK'),

-- Phiếu 20
(20,8,8,5,80000,'OK'),(20,9,9,9,5000,'OK'),(20,10,10,6,90000,'OK'),

-- Phiếu 21
(21,1,2,10,20000,'OK'),(21,3,4,5,15000,'OK'),(21,5,6,7,30000,'OK'),

-- Phiếu 22
(22,2,3,20,3000,'OK'),(22,4,5,10,50000,'OK'),(22,6,7,8,70000,'OK'),

-- Phiếu 23
(23,7,8,6,60000,'OK'),(23,9,10,3,80000,'OK'),(23,1,1,9,5000,'OK'),

-- Phiếu 24
(24,3,2,5,90000,'OK'),(24,5,4,6,20000,'OK'),(24,7,6,7,15000,'OK'),

-- Phiếu 25
(25,8,3,8,30000,'OK'),(25,10,5,9,3000,'OK'),(25,2,7,6,50000,'OK'),

-- Phiếu 26
(26,4,6,7,70000,'OK'),(26,6,8,8,60000,'OK'),(26,8,10,5,80000,'OK'),

-- Phiếu 27
(27,9,1,9,5000,'OK'),(27,1,3,6,90000,'OK'),(27,3,5,6,20000,'OK'),

-- Phiếu 28
(28,2,4,7,15000,'OK'),(28,4,6,8,30000,'OK'),(28,6,8,9,3000,'OK'),

-- Phiếu 29
(29,5,7,6,50000,'OK'),(29,7,9,7,70000,'OK'),(29,9,1,8,60000,'OK'),

-- Phiếu 30
(30,10,2,5,80000,'OK'),(30,1,4,9,5000,'OK'),(30,3,6,6,90000,'OK');

INSERT INTO TICHDIEM VALUES
(1,100),(2,200),(3,150),(4,300),(5,120),
(6,80),(7,220),(8,90),(9,160),(10,50);

INSERT INTO CHUONGTRINHKHUYENMAI(TenCTKM, PhanTramGiam) VALUES
('Sale 10%',10),('Sale 20%',20),('Sale 30%',30),
('Sale 5%',5),('Sale 15%',15),
('Combo 1',0),('Combo 2',0),
('Tặng bánh',0),('Flash sale',25),('Weekend',12);

INSERT INTO CT_CHUONGTRINHKHUYENMAI VALUES
(1,1,10),(2,2,20),(3,3,30),(4,4,5),(5,5,15),
(6,6,10),(7,7,20),(8,8,25),(9,9,30),(10,10,12);

INSERT INTO CONGTHUC (MaBanh, MaDVT, CachLam) VALUES
-- Nhóm bánh hiện đại
(1,1,'Trộn bột matcha, đánh kem, nướng và phủ lớp matcha'),
(2,1,'Xếp lớp bánh và kem mascarpone, phủ cacao, làm lạnh'),
(3,1,'Làm mousse xoài từ xoài xay, gelatin và kem tươi'),
(4,1,'Hấp hỗn hợp trứng sữa caramel'),
(5,1,'Nướng vỏ bánh, bơm nhân kem vào bên trong'),

-- Nhóm bánh mì
(6,1,'Nhào bột, ủ men, nướng bánh mì ngọt'),
(18,1,'Nướng bánh mì sandwich mềm, cắt lát'),
(19,1,'Nướng đế bánh, thêm topping và phô mai'),

-- Nhóm bánh bông lan
(7,1,'Đánh trứng, trộn bột, nướng bông lan'),
(8,1,'Nướng bánh nhỏ, trang trí kem trên mặt'),

-- Nhóm bánh lạnh
(9,1,'Trộn phô mai với kem, làm lạnh tạo cheesecake'),
(2,1,'Làm lạnh nhiều lớp kem và bánh'),

-- Nhóm bánh chiên/nướng
(10,1,'Chiên bột tạo hình donut, phủ đường hoặc socola'),
(16,1,'Nướng waffle bằng khuôn, ăn kèm mật ong'),
(17,1,'Chiên pancake trên chảo, ăn kèm siro'),

-- Nhóm bánh truyền thống
(13,1,'Nấu đậu xanh, làm nhân, nướng bánh'),
(14,1,'Làm nhân đậu, bọc vỏ và nướng bánh pía'),
(20,1,'Nướng bánh từ khoai mì và nước cốt dừa'),

-- Nhóm bánh trái cây
(21,1,'Nướng bánh chuối từ chuối chín nghiền'),
(22,1,'Nướng bánh táo với lát táo tươi'),
(23,1,'Nướng bánh việt quất với sốt berry'),
(24,1,'Làm bánh từ sầu riêng, nướng nhẹ'),
(25,1,'Nướng bánh cam với vỏ cam bào'),
(26,1,'Làm bánh kiwi, phủ lát kiwi tươi'),
(27,1,'Nướng bánh dứa, thêm mứt dứa'),

-- Nhóm bánh khô
(28,1,'Nướng bánh hạnh nhân giòn'),
(29,1,'Nướng bánh mè thơm'),
(30,1,'Nướng bánh gừng với gia vị đặc trưng');