package engraver;

import java.util.Locale;
import java.util.ResourceBundle;

public class Test {
    public static void main(String[] args) {
        ResourceBundle bundle = ResourceBundle.getBundle("engraver.ui");
        System.out.println(bundle.getString("project"));
    }
}
