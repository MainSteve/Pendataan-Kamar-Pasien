public class Riwayat {
    private int id;
    private String nama;
    private String nik;
    private String tanggalKeluar;

    public Riwayat(int id, String nama, String nik, String tanggalKeluar) {
        this.id = id;
        this.nama = nama;
        this.nik = nik;
        this.tanggalKeluar = tanggalKeluar;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getNik() {
        return nik;
    }

    public void setNik(String nik) {
        this.nik = nik;
    }

    public String getTanggalKeluar() {
        return tanggalKeluar;
    }

    public void setTanggalKeluar(String tanggalKeluar) {
        this.tanggalKeluar = tanggalKeluar;
    }
}