# JasperReports - Truy van chi tiet cho module In an

Tai lieu nay tap trung vao phan truy van chi tiet de dung cho report:

- Hoa don ban hang
- Phieu nhap hang
- Phieu xuat hang (mapping tu hoa don)
- Danh sach theo khoang ngay

## 1) File truy van

Su dung file: report/jasper/report_queries.sql

## 2) Tham so Jasper nen dat

- MaHD: java.lang.Integer
- MaPhieuNhap: java.lang.Integer
- TuNgay: java.sql.Timestamp
- DenNgay: java.sql.Timestamp

## 3) Cach tich hop vao report

1. Tao report Header va report Detail rieng (hoac Subreport)
2. Query Header: lay thong tin chung (so phieu, ngay, nhan vien, khach/NCC)
3. Query Detail: lay danh sach dong hang
4. Truyen parameter MaHD hoac MaPhieuNhap tu man hinh

## 4) Goi y bo cuc report

- Header: logo, ten cua hang, ma phieu, ngay, nguoi lap
- Body: bang chi tiet (ten hang, so luong, don gia, thanh tien)
- Footer: tong cong, chu ky, ghi chu

## 5) Lop truy van Java da san sang

Da bo sung DAO/ReportQueryDAO.java voi cac ham:

- getHoaDonHeader(int maHD)
- getHoaDonDetails(int maHD)
- getPhieuNhapHeader(int maPhieuNhap)
- getPhieuNhapDetails(int maPhieuNhap)
- getHoaDonListByDateRange(Timestamp tuNgay, Timestamp denNgay)

Ban co the dung truc tiep de test data truoc khi noi vao Jasper.

## 6) Ghi chu ve phieu xuat

Schema hien tai chua co bang PHIEUXUATHANG rieng.
De thong nhat nghiep vu va de chay nhanh module report, phieu xuat duoc map tu HOADON + CHITIETHOADON.
