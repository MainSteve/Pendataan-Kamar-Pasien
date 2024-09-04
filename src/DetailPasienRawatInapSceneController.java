import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.Date;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class DetailPasienRawatInapSceneController {

    @FXML
    private Button btnLogout;

    @FXML
    private Label labelBawah;

    @FXML
    private Button btnNo;

    @FXML
    private Label labelDetail;

    @FXML
    private TextField nikPasien;

    @FXML
    private TextField usiaPasien;

    @FXML
    private Label welcomeLabel;

    @FXML
    private TextField nomorRuanganPasien;

    @FXML
    private TextField tanggalLahirPasien;

    @FXML
    private Button btnYes;

    @FXML
    private Label labelJudul;

    @FXML
    private TextField namaPasien;

    @FXML
    private TextArea riwayatPenyakitPasien;

    @FXML
    public void initialize() {
        String caretakerName = CaretakerSession.getInstance().getCaretakerName();
        welcomeLabel.setText("Selamat bekerja, " + caretakerName);
    }

    public void loadPasienData(String nik) {
        try {
            Connection conn = DBConnection.getConnection();
            String query = "SELECT nama, nik, tanggal_lahir, nomor_ruangan, riwayat_penyakit FROM pasien WHERE nik = '"
                    + nik + "'";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {
                namaPasien.setText(rs.getString("nama"));
                nikPasien.setText(rs.getString("nik"));
                tanggalLahirPasien.setText(rs.getString("tanggal_lahir"));
                nomorRuanganPasien.setText(rs.getString("nomor_ruangan"));
                riwayatPenyakitPasien.setText(rs.getString("riwayat_penyakit"));

                
                Date tanggalLahirDB = rs.getDate("tanggal_lahir");
                int usia = hitungUsia(tanggalLahirDB);
                usiaPasien.setText(usia + " tahun");
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int hitungUsia(Date tanggalLahir) {
        Calendar calLahir = Calendar.getInstance();
        calLahir.setTime(tanggalLahir);
        Calendar calSekarang = Calendar.getInstance();

        int tahun = calSekarang.get(Calendar.YEAR) - calLahir.get(Calendar.YEAR);
        int bulan = calSekarang.get(Calendar.MONTH) - calLahir.get(Calendar.MONTH);
        int hari = calSekarang.get(Calendar.DAY_OF_MONTH) - calLahir.get(Calendar.DAY_OF_MONTH);

        if (bulan < 0 || (bulan == 0 && hari < 0)) {
            tahun--;
        }

        return tahun;
    }

    @FXML
    void btnKembaliOnClicked(ActionEvent event) {
        changeScene(event, "PasienRawatInapScene.fxml");
    }

    @FXML
    void btnPulangOnClicked(ActionEvent event) {
        String nama = namaPasien.getText();
        String nik = nikPasien.getText();
        String tanggalLahir = tanggalLahirPasien.getText();
        String nomorRuangan = nomorRuanganPasien.getText();
        String lastCaretaker = CaretakerSession.getInstance().getCaretakerName(); 
        String kelasRuangan = getKelasRuangan(nomorRuangan); 
        String tanggalKeluar = java.time.LocalDate.now().toString(); 

        
        String queryRiwayat = "INSERT INTO riwayat (nama, nik, tanggal_lahir, nomor_ruangan, kelas_ruangan, tanggal_keluar, last_caretaker) VALUES (?, ?, ?, ?, ?, ?, ?)";

        
        String queryUpdatePasien = "UPDATE pasien SET nomor_ruangan = NULL WHERE nik = ?";

        try (Connection conn = DBConnection.getConnection();) {

            
            try (PreparedStatement pstmtRiwayat = conn.prepareStatement(queryRiwayat)) {
                pstmtRiwayat.setString(1, nama);
                pstmtRiwayat.setString(2, nik);
                pstmtRiwayat.setString(3, tanggalLahir);
                pstmtRiwayat.setString(4, nomorRuangan);
                pstmtRiwayat.setString(5, kelasRuangan);
                pstmtRiwayat.setString(6, tanggalKeluar);
                pstmtRiwayat.setString(7, lastCaretaker);

                pstmtRiwayat.executeUpdate();
            }

            
            try (PreparedStatement pstmtUpdatePasien = conn.prepareStatement(queryUpdatePasien)) {
                pstmtUpdatePasien.setString(1, nik);
                pstmtUpdatePasien.executeUpdate();
            }

            
            changeScene(event, "RiwayatInapScene.fxml");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnLogoutOnClicked(ActionEvent event) {
        changeScene(event, "LoginScene.fxml");
    }

    private void changeScene(ActionEvent event, String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    
    private String getKelasRuangan(String nomorRuangan) {
        String kelasRuangan = null;
        String query = "SELECT kelas_ruangan FROM ruang WHERE nomor_ruangan = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, nomorRuangan);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                kelasRuangan = rs.getString("kelas_ruangan");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return kelasRuangan;
    }

}
