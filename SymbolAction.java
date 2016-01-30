import javax.swing.event.*;
import java.awt.event.*;
import java.awt.*;
import javax.swing.*;

public class SymbolAction extends AbstractAction {
    private String symbol;

    public SymbolAction(String name, String symbol) {
        super(name);
        this.symbol = symbol;
    }

    public void actionPerformed(ActionEvent a) {
        if(!Calculator.getDisplay().contains(symbol))
            Calculator.append(symbol);
    }
}