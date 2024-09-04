import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginSceneController {
    @FXML
    private TextField textFieldId;

    @FXML
    private Button btnYes;

    @FXML
    private Label loginSceneLable;

    @FXML
    private PasswordField TextFieldPassword;

    @FXML
    private Label errorLabel;

    @FXML
    void btnLoginClicked(ActionEvent event) {
        String idString = textFieldId.getText();
        String passwdString = TextFieldPassword.getText();

        LoginController loginController = new LoginController();
        boolean isValid = loginController.validateLogin(idString, passwdString);

        errorLabel.setText("");

        if (isValid) {

            String caretakerName = loginController.getCaretakerName(idString);
            CaretakerSession.getInstance().setCaretakerName(caretakerName);

            changeScene(event, "PasienRawatInapScene.fxml");
        } else {
            errorLabel.setText("ID atau Password yang anda masukkan salah");
        }
    }


    
    private void changeScene(ActionEvent event, String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
