import javax.swing.*;
import java.awt.event.*;

public class OperationAction extends AbstractAction {
    private int operationID;

    public OperationAction(String symbol, int ID) {
        super(symbol);
        this.operationID = ID;
    }

    public void actionPerformed(ActionEvent a) {
        //Special cases are unary, so execute now
        if(operationID == 0) {
            Calculator.doOperation(); return;
        }

        if((operationID >= 5 && operationID < 8) || (operationID > 8 && operationID < 13) || operationID > 13) {
            int temp = Calculator.getOperation();
            Calculator.setOperation(operationID);
            Calculator.doOperation();
            Calculator.setOperation(temp);
            return;
        }
        
        //AppMain.append(""+number);
        Calculator.shouldErase(true);
        Calculator.save();
        Calculator.setOperation(operationID);
    }
}