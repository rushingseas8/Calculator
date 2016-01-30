import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

public class NumberAction extends AbstractAction {
    private int number;
    private Class c = getClass();

    public NumberAction(int i) {
        super("" + i);
        this.number = i;
    }

    public void actionPerformed(ActionEvent a) {
        Calculator.append(""+number);
    }
}