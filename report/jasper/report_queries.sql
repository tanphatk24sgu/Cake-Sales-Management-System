-- ================================================
-- JasperReports SQL Queries - Cake Management
-- Parameters trong Jasper: $P{...}
-- ================================================

-- 1) HOA DON - HEADER (1 dong)
SELECT h.MaHD,
       h.NgayLapHD,
       h.ThanhTien,
       nv.MaNV,
       CONCAT(nv.Ho, ' ', nv.Ten) AS TenNhanVien,
       kh.MaKH,
       CONCAT(kh.Ho, ' ', kh.Ten) AS TenKhachHang,
       kh.SDT,
       kh.DiaChi
FROM hoadon h
LEFT JOIN nhanvien nv ON h.MaNV = nv.MaNV
LEFT JOIN khachhang kh ON h.MaKH = kh.MaKH
WHERE h.MaHD = $P{MaHD};

-- 2) HOA DON - DETAIL (nhieu dong)
SELECT c.MaHD,
       c.MaBanh,
       b.TenBanh,
       c.SoLuong,
       c.DonGia,
       c.ThanhTien,
       c.Diem
FROM chitiethoadon c
LEFT JOIN banh b ON c.MaBanh = b.MaBanh
WHERE c.MaHD = $P{MaHD}
ORDER BY c.MaBanh;

-- 3) PHIEU NHAP HANG - HEADER (1 dong)
SELECT p.MaPhieuNhap,
       p.Ngay,
       nv.MaNV,
       CONCAT(nv.Ho, ' ', nv.Ten) AS TenNhanVien,
       ncc.MaNCC,
       ncc.TenNCC,
       ncc.MaSoThue
FROM phieunhaphang p
LEFT JOIN nhanvien nv ON p.MaNV = nv.MaNV
LEFT JOIN nhacungcap ncc ON p.MaNCC = ncc.MaNCC
WHERE p.MaPhieuNhap = $P{MaPhieuNhap};

-- 4) PHIEU NHAP HANG - DETAIL (nhieu dong)
SELECT c.MaPhieuNhap,
       c.MaBanh,
       b.TenBanh,
       c.MaNVL,
       nl.Ten AS TenNguyenLieu,
       c.SoLuong,
       c.DonGia,
       c.ThanhTien,
       c.TinhTrang
FROM ct_phieunhaphang c
LEFT JOIN banh b ON c.MaBanh = b.MaBanh
LEFT JOIN nguyenlieu nl ON c.MaNVL = nl.MaNL
WHERE c.MaPhieuNhap = $P{MaPhieuNhap}
ORDER BY c.MaBanh, c.MaNVL;

-- 5) PHIEU XUAT HANG (THUC TE LA HOA DON XUAT) - DETAIL
SELECT h.MaHD AS MaPhieuXuat,
       h.NgayLapHD AS NgayXuat,
       c.MaBanh,
       b.TenBanh,
       c.SoLuong,
       c.DonGia,
       c.ThanhTien,
       CONCAT(IFNULL(nv.Ho, ''), ' ', IFNULL(nv.Ten, '')) AS NhanVienXuat
FROM hoadon h
JOIN chitiethoadon c ON h.MaHD = c.MaHD
LEFT JOIN banh b ON c.MaBanh = b.MaBanh
LEFT JOIN nhanvien nv ON h.MaNV = nv.MaNV
WHERE h.MaHD = $P{MaHD}
ORDER BY c.MaBanh;

-- 6) DANH SACH HOA DON THEO KHOANG NGAY
SELECT h.MaHD,
       h.NgayLapHD,
       CONCAT(IFNULL(kh.Ho, ''), ' ', IFNULL(kh.Ten, '')) AS TenKhachHang,
       CONCAT(IFNULL(nv.Ho, ''), ' ', IFNULL(nv.Ten, '')) AS TenNhanVien,
       h.ThanhTien
FROM hoadon h
LEFT JOIN khachhang kh ON h.MaKH = kh.MaKH
LEFT JOIN nhanvien nv ON h.MaNV = nv.MaNV
WHERE h.NgayLapHD BETWEEN $P{TuNgay} AND $P{DenNgay}
ORDER BY h.NgayLapHD, h.MaHD;

-- 7) DANH SACH PHIEU NHAP THEO KHOANG NGAY
SELECT p.MaPhieuNhap,
       p.Ngay,
       CONCAT(IFNULL(nv.Ho, ''), ' ', IFNULL(nv.Ten, '')) AS TenNhanVien,
       ncc.TenNCC,
       SUM(c.ThanhTien) AS TongTienNhap
FROM phieunhaphang p
LEFT JOIN nhanvien nv ON p.MaNV = nv.MaNV
LEFT JOIN nhacungcap ncc ON p.MaNCC = ncc.MaNCC
LEFT JOIN ct_phieunhaphang c ON p.MaPhieuNhap = c.MaPhieuNhap
WHERE p.Ngay BETWEEN $P{TuNgay} AND $P{DenNgay}
GROUP BY p.MaPhieuNhap, p.Ngay, nv.Ho, nv.Ten, ncc.TenNCC
ORDER BY p.Ngay, p.MaPhieuNhap;
