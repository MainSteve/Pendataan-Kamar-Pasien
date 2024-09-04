import java.io.IOException;
import java.sql.Connection;
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
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;

public class PasienRawatInapSceneController {
    @FXML
    private AnchorPane slider;

    @FXML
    private Label welcomeLabel;

    @FXML
    private TableView<Pasien> tableView;
    @FXML
    private TableColumn<Pasien, String> namaColumn;
    @FXML
    private TableColumn<Pasien, String> nikColumn;
    @FXML
    private TableColumn<Pasien, String> ruanganColumn;
    @FXML
    private TableColumn<Pasien, Void> actionColumn;

    @FXML
    public void initialize() {
        
        namaColumn.setCellValueFactory(new PropertyValueFactory<>("nama"));
        nikColumn.setCellValueFactory(new PropertyValueFactory<>("nik"));
        ruanganColumn.setCellValueFactory(new PropertyValueFactory<>("ruangan"));

        
        ObservableList<Pasien> data = loadDataFromDatabase();
        tableView.setItems(data);

        
        addButtonToTable();

        String caretakerName = CaretakerSession.getInstance().getCaretakerName();
        welcomeLabel.setText("Selamat bekerja, " + caretakerName);
    }

    private ObservableList<Pasien> loadDataFromDatabase() {
        ObservableList<Pasien> data = FXCollections.observableArrayList();
        try {
            Connection conn = DBConnection.getConnection();
            String query = "SELECT nama, nik, nomor_ruangan FROM pasien WHERE nomor_ruangan IS NOT NULL";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                data.add(new Pasien(rs.getString("nama"), rs.getString("nik"), rs.getString("nomor_ruangan")));
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return data;
    }

    private void addButtonToTable() {
        Callback<TableColumn<Pasien, Void>, TableCell<Pasien, Void>> cellFactory = new Callback<TableColumn<Pasien, Void>, TableCell<Pasien, Void>>() {
            @Override
            public TableCell<Pasien, Void> call(final TableColumn<Pasien, Void> param) {
                final TableCell<Pasien, Void> cell = new TableCell<Pasien, Void>() {

                    private final Button btn = new Button("View");

                    {
                        btn.setOnAction((ActionEvent event) -> {
                            Pasien pasien = getTableView().getItems().get(getIndex());
                            try {
                                FXMLLoader loader = new FXMLLoader(
                                        getClass().getResource("DetailPasienRawatInapScene.fxml"));
                                Parent root = loader.load();

                                
                                DetailPasienRawatInapSceneController controller = loader.getController();
                                controller.loadPasienData(pasien.getNik());

                                
                                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                                Scene scene = new Scene(root);
                                stage.setScene(scene);
                                stage.show();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
                return cell;
            }
        };

        actionColumn.setCellFactory(cellFactory);
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
