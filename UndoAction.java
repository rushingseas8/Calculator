import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

public class UndoAction extends AbstractAction {
    public UndoAction(String name) {
        super(name);
    }

    public void actionPerformed(ActionEvent a) {
        Calculator.unappend();
    }
}