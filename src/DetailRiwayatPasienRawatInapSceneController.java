import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DetailRiwayatPasienRawatInapSceneController {

    @FXML
    private TextField penginputTerakhirPasien;

    @FXML
    private TextField tanggalKeluarPasien;

    @FXML
    private Button btnLogout;

    @FXML
    private Label labelBawah;

    @FXML
    private Label labelDetail;

    @FXML
    private TextField nikPasien;

    @FXML
    private Label welcomeLabel;

    @FXML
    private TextField kelasRuanganPasien;

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
    public void initialize() {
        String caretakerName = CaretakerSession.getInstance().getCaretakerName();
        welcomeLabel.setText("Selamat bekerja, " + caretakerName);
    }

    public void loadRiwayatData(int id) {
        try {
            Connection conn = DBConnection.getConnection();
            String query = "SELECT nama, nik, tanggal_lahir, nomor_ruangan, kelas_ruangan, tanggal_keluar, last_caretaker FROM riwayat WHERE id = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                namaPasien.setText(rs.getString("nama"));
                nikPasien.setText(rs.getString("nik"));
                tanggalLahirPasien.setText(rs.getString("tanggal_lahir"));
                nomorRuanganPasien.setText(rs.getString("nomor_ruangan"));
                kelasRuanganPasien.setText(rs.getString("kelas_ruangan"));
                tanggalKeluarPasien.setText(rs.getString("tanggal_keluar"));
                penginputTerakhirPasien.setText(rs.getString("last_caretaker"));
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnKembaliOnClicked(ActionEvent event) {
        changeScene(event, "RiwayatInapScene.fxml");
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
}
