import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginController {

    public boolean validateLogin(String username, String password) {
        boolean isValid = false;
        String query = "SELECT * FROM caretaker WHERE username = ? AND password = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                isValid = true;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isValid;
    }

    public String getCaretakerName(String username) {
        String name = null;
        String query = "SELECT nama FROM caretaker WHERE username = ?";

        try (Connection conn = DBConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                name = rs.getString("nama");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return name;
    }
}
