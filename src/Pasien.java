public class Pasien {
    private String nama;
    private String nik;
    private String ruangan;

    public Pasien(String nama, String nik, String ruangan) {
        this.nama = nama;
        this.nik = nik;
        this.ruangan = ruangan;
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

    public String getRuangan() {
        return ruangan;
    }

    public void setRuangan(String ruangan) {
        this.ruangan = ruangan;
    }
}