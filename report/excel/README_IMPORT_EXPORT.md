# Import/Export Excel (Muc trung binh)

## Da hoan thanh

- Export Hoa don + Chi tiet Hoa don ra file .xlsx
- Import Hoa don + Chi tiet Hoa don tu file .xlsx
- Export file mau Import cho Hoa don
- Export Phieu nhap + CT Phieu nhap ra file .xlsx
- Import Phieu nhap + CT Phieu nhap tu file .xlsx
- Export file mau Import cho Phieu nhap
- Validate du lieu truoc khi ghi DB (header, kieu du lieu, trung khoa, FK)
- Ghi DB theo transaction (rollback neu loi)

## Nut tren man Hoa don

- "⬇ Excel": Xuat Hoa don + Chi tiet Hoa don
- "⬆ Excel": Import Hoa don + Chi tiet Hoa don
- "📄 Mau": Tao file mau import Hoa don
- "⚠ Xuat loi": Xuat danh sach loi import gan nhat (TXT/XLSX)

## Nut tren man Phieu nhap hang

- "↻ Lam moi": Tai lai danh sach phieu nhap va chi tiet
- "⬇ Excel": Xuat Phieu nhap + CT Phieu nhap
- "⬆ Excel": Import Phieu nhap + CT Phieu nhap
- "📄 Mau": Tao file mau import Phieu nhap
- "⚠ Xuat loi": Xuat danh sach loi import gan nhat (TXT/XLSX)

## Xuat loi import

- Sau khi import that bai, he thong se hoi xuat file loi ngay lap tuc
- Co the xuat lai bang nut "⚠ Xuat loi"
- Dinh dang ho tro:
  - TXT: de xem nhanh
  - XLSX: de loc/sap xep va gui cho team

## Dinh dang sheet bat buoc

### Hoa don

- Sheet: HoaDon
- Cot: MaHD, NgayLapHD, MaNV, MaKH, ThanhTien

### Chi tiet hoa don

- Sheet: ChiTietHoaDon
- Cot: MaHD, MaBanh, SoLuong, DonGia, Diem

### Phieu nhap

- Sheet: PhieuNhapHang
- Cot: MaPhieuNhap, Ngay, MaNV, MaNCC

### CT phieu nhap

- Sheet: CTPhieuNhapHang
- Cot: MaPhieuNhap, MaBanh, MaNVL, SoLuong, DonGia, TinhTrang

## Dinh dang ngay gio

- yyyy-MM-dd HH:mm:ss
- Hoac o dang Date cua Excel

## API core

- BUS.ExcelImportExportBUS
  - exportHoaDonToExcel(File)
  - importHoaDonFromExcel(File)
  - exportHoaDonTemplate(File)
  - exportPhieuNhapToExcel(File)
  - importPhieuNhapFromExcel(File)
  - exportPhieuNhapTemplate(File)
