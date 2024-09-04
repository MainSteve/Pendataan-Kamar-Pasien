import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Statement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class DaftarRawatInapSceneController {

    @FXML
    private AnchorPane slider;

    @FXML
    private Label welcomeLabel;

    @FXML
    private Button btnLogout;

    @FXML
    private Label labelBawah;

    @FXML
    private Button btnYes;

    @FXML
    private Label labelJudul;

    @FXML
    private Button btnInMenu;

    @FXML
    private Label labelDetail;

    @FXML
    private Label errorLabel;

    @FXML
    private ComboBox<String> pilihNamaPasien;

    @FXML
    private ComboBox<String> pilihRuangan;

    @FXML
    public void initialize() {
        loadNamaPasien();
        loadRuangan();

        String caretakerName = CaretakerSession.getInstance().getCaretakerName();
        welcomeLabel.setText("Selamat bekerja, " + caretakerName);
    }

    @FXML
    void btnDaftarkanKamarOnClicked(ActionEvent event) {
        String selectedNamaPasien = pilihNamaPasien.getValue(); 
        String selectedRuangan = pilihRuangan.getValue(); 

        if (selectedNamaPasien != null && selectedRuangan != null) {
            try {
                
                Connection conn = DBConnection.getConnection();

                
                String query = "UPDATE pasien SET nomor_ruangan = ? WHERE nama = ?";
                PreparedStatement stmt = conn.prepareStatement(query);
                stmt.setString(1, selectedRuangan);
                stmt.setString(2, selectedNamaPasien);

                
                stmt.executeUpdate();

                
                stmt.close();
                conn.close();

                
                
                

                changeScene(event, "PasienRawatInapScene.fxml");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            
            errorLabel.setText("Pastikan Anda telah memilih nama pasien dan ruangan.");
        }
    }

    private void loadNamaPasien() {
        ObservableList<String> pasienList = FXCollections.observableArrayList();
        try {
            Connection conn = DBConnection.getConnection();
            String query = "SELECT nama FROM pasien WHERE nomor_ruangan IS NULL";
            Statement stmt = (Statement) conn.createStatement();
            ResultSet rs = ((java.sql.Statement) stmt).executeQuery(query);

            while (rs.next()) {
                pasienList.add(rs.getString("nama"));
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        pilihNamaPasien.setItems(pasienList);
    }

    private void loadRuangan() {
        ObservableList<String> ruanganList = FXCollections.observableArrayList();
        try {
            Connection conn = DBConnection.getConnection();
            String query = "SELECT r.nomor_ruangan FROM ruang r LEFT JOIN pasien p ON r.nomor_ruangan = p.nomor_ruangan WHERE p.nomor_ruangan IS NULL";
            Statement stmt = (Statement) conn.createStatement();
            ResultSet rs = ((java.sql.Statement) stmt).executeQuery(query);

            while (rs.next()) {
                ruanganList.add(rs.getString("nomor_ruangan"));
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        pilihRuangan.setItems(ruanganList);
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