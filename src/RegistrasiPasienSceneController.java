import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class RegistrasiPasienSceneController {

    @FXML
    private AnchorPane slider;

    @FXML
    private Label welcomeLabel;

    @FXML
    private Button btnLogout;

    @FXML
    private TextField namaPasienDaftar;

    @FXML
    private DatePicker tglPasienDaftar;

    @FXML
    private Button btnYes;

    @FXML
    private Label labelJudul;

    @FXML
    private Button btnInMenu;

    @FXML
    private Label labelDetail;

    @FXML
    private TextField nikPasienDaftar;

    @FXML
    private Label errorLabel;

    @FXML
    public void initialize() {
        String caretakerName = CaretakerSession.getInstance().getCaretakerName();
        welcomeLabel.setText("Selamat bekerja, " + caretakerName);
    }

    @FXML
    void btnDaftarkanPasienOnClicked(ActionEvent event) throws IOException {
        String nama = namaPasienDaftar.getText();
        String nik = nikPasienDaftar.getText();
        String tanggalLahir = (tglPasienDaftar.getValue() != null) ? tglPasienDaftar.getValue().toString() : null; 
                                                                                                                   
                                                                                                                   
                                                                                                                   

        
        if (nama == null || nama.isEmpty() || nik == null || nik.isEmpty() || tanggalLahir == null
                || tanggalLahir.isEmpty()) {
            errorLabel.setText("Pastikan Anda sudah memasukkan Nama, NIK, dan Tanggal Lahir Pasien.");
            return; 
        }

        try {
            
            Connection conn = DBConnection.getConnection();

            
            String query = "INSERT INTO pasien (nama, nik, tanggal_lahir, riwayat_penyakit, nomor_ruangan) VALUES (?, ?, ?, NULL, NULL)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, nama);
            stmt.setString(2, nik);
            stmt.setString(3, tanggalLahir);

            
            stmt.executeUpdate();

            
            stmt.close();
            conn.close();

            
            changeScene(event, "PasienRawatInapScene.fxml");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    void btnPasienRawatInapOnClicked(ActionEvent event) {
        changeScene(event, "PasienRawatInapScene.fxml");
    }

    @FXML
    void btnRegistrasiPasienOnClicked(ActionEvent event) {
        changeScene(event, "RegistrasiPasienScene.fxml");
    }

    @FXML
    void btnRiwayatInapOnClicked(ActionEvent event) {
        changeScene(event, "RiwayatInapScene.fxml");
    }

    @FXML
    void btnDaftarRawatInapOnClicked(ActionEvent event) {
        changeScene(event, "DaftarRawatInapScene.fxml");
    }

    @FXML
    void btnLogoutOnClicked(ActionEvent event) {
        changeScene(event, "LoginScene.fxml");
    }

    public void updateWelcomeLabel(String text) {
        welcomeLabel.setText(text);
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