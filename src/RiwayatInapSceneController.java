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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class RiwayatInapSceneController {

    @FXML
    private TableColumn<Riwayat, String> namaColumn;

    @FXML
    private TableColumn<Riwayat, String> actionColumn;

    @FXML
    private AnchorPane slider;

    @FXML
    private Label welcomeLabel;

    @FXML
    private TableColumn<Riwayat, String> tanggalKeluarColumn;

    @FXML
    private Button btnLogout;

    @FXML
    private Label labelBawah;

    @FXML
    private TableColumn<Riwayat, String> nikColumn;

    @FXML
    private TableView<Riwayat> tableView;

    @FXML
    private TableColumn<Riwayat, Integer> idColumn;

    @FXML
    private Label labelJudul;

    @FXML
    private Button btnInMenu;

    @FXML
    public void initialize() {
        loadRiwayat();

        String caretakerName = CaretakerSession.getInstance().getCaretakerName();
        welcomeLabel.setText("Selamat bekerja, " + caretakerName);
    }

    private void loadRiwayat() {
        ObservableList<Riwayat> riwayatList = FXCollections.observableArrayList();
        try {
            Connection conn = DBConnection.getConnection();
            String query = "SELECT id, nama, nik, tanggal_keluar FROM riwayat";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                riwayatList.add(new Riwayat(rs.getInt("id"), rs.getString("nama"), rs.getString("nik"),
                        rs.getString("tanggal_keluar")));
            }

            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        namaColumn.setCellValueFactory(new PropertyValueFactory<>("nama"));
        nikColumn.setCellValueFactory(new PropertyValueFactory<>("nik"));
        tanggalKeluarColumn.setCellValueFactory(new PropertyValueFactory<>("tanggalKeluar"));

        actionColumn.setCellFactory(createButtonCellFactory());

        tableView.setItems(riwayatList);
    }

    private Callback<TableColumn<Riwayat, String>, TableCell<Riwayat, String>> createButtonCellFactory() {
        return new Callback<>() {
            @Override
            public TableCell<Riwayat, String> call(TableColumn<Riwayat, String> param) {
                return new TableCell<>() {
                    final Button btn = new Button("Detail");

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            btn.setOnAction(event -> {
                                Riwayat riwayat = getTableView().getItems().get(getIndex());
                                try {
                                    FXMLLoader loader = new FXMLLoader(
                                            getClass().getResource("DetailRiwayatPasienRawatInapScene.fxml"));
                                    Parent root = loader.load();

                                    DetailRiwayatPasienRawatInapSceneController controller = loader.getController();
                                    controller.loadRiwayatData(riwayat.getId());

                                    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                                    stage.setScene(new Scene(root));
                                    stage.show();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });
                            setGraphic(btn);
                            setText(null);
                        }
                    }
                };
            }
        };
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
