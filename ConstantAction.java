import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

public class ConstantAction extends AbstractAction {
    private double constant;

    public ConstantAction(String symbol, double constant) {
        super(symbol);
        this.constant = constant;
    }

    public void actionPerformed(ActionEvent a) {
        Calculator.save();
        Calculator.shouldErase(true);
        Calculator.append(""+constant);
    }
}