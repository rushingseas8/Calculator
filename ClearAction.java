import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

public class ClearAction extends AbstractAction {
    private boolean keepMemory; 
    public ClearAction(String name, boolean keepMemory) {
        super(name);
        this.keepMemory = keepMemory;
    }

    public void actionPerformed(ActionEvent a) {
        Calculator.clearCurrent();
        Calculator.shouldErase(true);
        if(!keepMemory) Calculator.clearLast();
    }
}