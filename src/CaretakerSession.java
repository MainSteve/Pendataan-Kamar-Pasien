public class CaretakerSession {
    private static CaretakerSession instance;
    private String caretakerName;

    private CaretakerSession() {

    }

    public static CaretakerSession getInstance() {
        if (instance == null) {
            instance = new CaretakerSession();
        }
        return instance;
    }

    public String getCaretakerName() {
        return caretakerName;
    }

    public void setCaretakerName(String caretakerName) {
        this.caretakerName = caretakerName;
    }
}