import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.*;
import java.awt.event.*;
import java.text.*;

/**
 * List of useful keyboard shortcuts:
 * ESC - clear
 * 
 */
public class Calculator {
    private static JFrame frame;
    private static JPanel panel;

    private static Font font;
    static {
        font = new Font("Monaco", Font.PLAIN, 48);
    }

    //private static DecimalFormat df = new DecimalFormat("#.#################");

    private static GridBagConstraints c;

    private static JTextField result;
    private static JButton clear, clearEntry, second, raddeg, posneg;
    private static JButton inverse, square, cube, npow, epow;
    private static JButton factorial, sqrt, cbrt, nroot, ln;
    private static JButton sin, cos, tan;
    private static JButton one, two, three, four, five, six, seven, eight, nine, zero;
    private static JButton add, subtract, multiply, divide, equals, decimal;

    //True if we should erase the input instead of appending
    private static boolean shouldErase = true;

    //If true, we are in radian mode
    private static boolean radianMode = true;

    private static double lastNumber = 0;
    private static double currentNumber = 0;
    private static int operation = -1;

    public Calculator() {
        frame = new JFrame("Calculator");
        frame.setSize(500, 300);

        panel = new JPanel();
        panel.setSize(500, 300);
        panel.setLayout(new GridBagLayout());

        //Create the top result view
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.NORTH;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 9;
        c.gridheight = 2;
        c.weightx = 1;
        c.weighty = 1;
        result = new JTextField("0");
        result.setHorizontalAlignment(SwingConstants.RIGHT);
        result.setEditable(false);
        result.setFont(font);
        panel.add(result, c);

        //Prepare to add all the buttons
        c = new GridBagConstraints();
        c.fill = GridBagConstraints.HORIZONTAL;
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        c.weightx = 1;
        c.weighty = 1;

        //Row 1
        clear = new JButton(new ClearAction("C", false));
        bind(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), clear);
        panel.add(clear, c);

        c.gridx = 1;
        clearEntry = new JButton(new ClearAction("CE", true));
        bind(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, KeyEvent.SHIFT_DOWN_MASK), clearEntry);
        //bind(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0), clearEntry);
        panel.add(clearEntry, c);

        c.gridx = 2;
        second = new JButton("2nd");
        panel.add(second, c);

        c.gridx = 3;
        raddeg = new JButton(new AbstractAction("Rad") {
                public void actionPerformed(ActionEvent a) {
                    putValue(Action.NAME, (radianMode=!radianMode)?"Rad":"Deg");
                }
            });
        panel.add(raddeg, c);

        c.gridx = 4;
        posneg = new JButton(new AbstractAction("±") {
                public void actionPerformed(ActionEvent a) {
                    currentNumber = -currentNumber; display();
                }
            });
        bind(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, KeyEvent.ALT_DOWN_MASK), posneg);
        panel.add(posneg, c);

        c.gridx = 5;
        seven = new JButton(new NumberAction(7));
        bind(KeyStroke.getKeyStroke("7"), seven);
        panel.add(seven, c);

        c.gridx = 6;
        eight = new JButton(new NumberAction(8));
        bind(KeyStroke.getKeyStroke("8"), eight);
        panel.add(eight, c);

        c.gridx = 7;
        nine = new JButton(new NumberAction(9));
        bind(KeyStroke.getKeyStroke("9"), nine);
        panel.add(nine, c);

        c.gridx = 8;
        divide = new JButton(new OperationAction("÷", 4));
        bind(KeyStroke.getKeyStroke(KeyEvent.VK_SLASH, 0), divide);
        panel.add(divide, c);

        //Row 2
        c.gridy = 3;
        c.gridx = 0;
        inverse = new JButton(new OperationAction("1/x", 5));
        bind(KeyStroke.getKeyStroke(KeyEvent.VK_SLASH, KeyEvent.ALT_DOWN_MASK), inverse);
        panel.add(inverse, c);

        c.gridx = 1;
        square = new JButton(new OperationAction("x^2", 6));
        bind(KeyStroke.getKeyStroke(KeyEvent.VK_2, KeyEvent.SHIFT_DOWN_MASK), square);
        panel.add(square, c);

        c.gridx = 2;
        cube = new JButton(new OperationAction("x^3", 7));
        bind(KeyStroke.getKeyStroke(KeyEvent.VK_3, KeyEvent.SHIFT_DOWN_MASK), cube);
        panel.add(cube, c);

        c.gridx = 3;
        npow = new JButton(new OperationAction("x^y", 8));
        bind(KeyStroke.getKeyStroke(KeyEvent.VK_6, KeyEvent.SHIFT_DOWN_MASK), npow);
        panel.add(npow, c);

        c.gridx = 4;
        epow = new JButton(new OperationAction("e^x", 9));
        panel.add(epow, c);

        c.gridx = 5;
        four = new JButton(new NumberAction(4));
        bind(KeyStroke.getKeyStroke("4"), four);
        panel.add(four, c);

        c.gridx = 6;
        five = new JButton(new NumberAction(5));
        bind(KeyStroke.getKeyStroke("5"), five);
        panel.add(five, c);

        c.gridx = 7;
        six = new JButton(new NumberAction(6));
        bind(KeyStroke.getKeyStroke("6"), six);
        panel.add(six, c);

        c.gridx = 8;
        multiply = new JButton(new OperationAction("x", 3));
        bind(KeyStroke.getKeyStroke(KeyEvent.VK_8, KeyEvent.SHIFT_DOWN_MASK), multiply);
        panel.add(multiply, c);

        //Row 3
        c.gridy = 4;
        c.gridx = 0;
        factorial = new JButton(new OperationAction("x!", 10));
        bind(KeyStroke.getKeyStroke(KeyEvent.VK_1, KeyEvent.SHIFT_DOWN_MASK), factorial);
        panel.add(factorial, c);

        c.gridx = 1;
        sqrt = new JButton(new OperationAction("√x", 11));
        bind(KeyStroke.getKeyStroke(KeyEvent.VK_2, KeyEvent.ALT_DOWN_MASK), sqrt);
        panel.add(sqrt, c);

        c.gridx = 2;
        cbrt = new JButton(new OperationAction("3√x)", 12));
        bind(KeyStroke.getKeyStroke(KeyEvent.VK_3, KeyEvent.ALT_DOWN_MASK), cbrt);
        panel.add(cbrt, c);

        c.gridx = 3;
        nroot = new JButton(new OperationAction("y√x", 13));
        bind(KeyStroke.getKeyStroke(KeyEvent.VK_6, KeyEvent.ALT_DOWN_MASK), nroot);
        panel.add(nroot, c);

        c.gridx = 4;
        ln = new JButton(new OperationAction("ln", 14));
        panel.add(ln, c);

        c.gridx = 5;
        one = new JButton(new NumberAction(1));
        bind(KeyStroke.getKeyStroke("1"), one);
        panel.add(one, c);

        c.gridx = 6;
        two = new JButton(new NumberAction(2));
        bind(KeyStroke.getKeyStroke("2"), two);
        panel.add(two, c);

        c.gridx = 7;
        three = new JButton(new NumberAction(3));
        bind(KeyStroke.getKeyStroke("3"), three);
        panel.add(three, c);

        c.gridx = 8;
        add = new JButton(new OperationAction("+", 2));
        bind(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, KeyEvent.SHIFT_DOWN_MASK), add);
        panel.add(add, c);

        //Row 4
        c.gridy = 5;
        c.gridx = 0;
        sin = new JButton(new OperationAction("sin", 15));
        bind(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0), sin);
        panel.add(sin, c);

        c.gridx = 1;
        cos = new JButton(new OperationAction("cos", 16));
        bind(KeyStroke.getKeyStroke(KeyEvent.VK_C, 0), cos);
        panel.add(cos, c);

        c.gridx = 2;
        tan = new JButton(new OperationAction("tan", 17));
        bind(KeyStroke.getKeyStroke(KeyEvent.VK_T, 0), tan);
        panel.add(tan, c);

        c.gridx = 3;
        sin = new JButton("?");
        panel.add(sin, c);

        c.gridx = 4;
        sin = new JButton("?");
        panel.add(sin, c);

        c.gridx = 5;
        decimal = new JButton(new SymbolAction(".", "."));
        bind(KeyStroke.getKeyStroke(KeyEvent.VK_PERIOD, 0), decimal);
        panel.add(decimal, c);

        c.gridx = 6;
        zero = new JButton(new NumberAction(0));
        bind(KeyStroke.getKeyStroke("0"), zero);
        panel.add(zero, c);

        c.gridx = 7;
        equals = new JButton(new OperationAction("=", 0));
        bind(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), equals);
        panel.add(equals, c);

        c.gridx = 8;
        subtract = new JButton(new OperationAction("-", 1));
        bind(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, 0), subtract);
        panel.add(subtract, c);

        //A few things that don't have buttons but still have keyboard values.
        bind(KeyStroke.getKeyStroke(KeyEvent.VK_P, 0), new JButton(new ConstantAction("π", 3.14159265358979))); //Pi
        bind(KeyStroke.getKeyStroke(KeyEvent.VK_E, 0), new JButton(new ConstantAction("e", 2.71828182845905))); //e
        bind(KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.SHIFT_DOWN_MASK), new JButton(new SymbolAction("E", "E"))); //E, ie, power of ten
        bind(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SLASH, 0), new JButton(new UndoAction("<-")));

        //Copy and paste support!
        bind(KeyStroke.getKeyStroke(KeyEvent.VK_1, KeyEvent.META_DOWN_MASK),
            new AbstractAction() {
                public void actionPerformed(ActionEvent a){
                    //System.out.println("Copy!");
                    copyToClipboard();
                }
            });

        bind(KeyStroke.getKeyStroke(KeyEvent.VK_2, KeyEvent.META_DOWN_MASK),
            new AbstractAction(){
                public void actionPerformed(ActionEvent a){
                    //System.out.println("Paste!");
                    String s = loadFromClipboard();
                    if(s == null)  return; //Return if failed to get clipboard data

                    //Return if failed to parse a double from clipboard
                    double d = 0;
                    try { d = Double.parseDouble(s); } catch (Exception e) { return; }
                    currentNumber = d;

                    //Update display if found a valid display
                    display();
                }});

        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.pack(); //We want a fixed size
        frame.setVisible(true);
    }

    private static void bind(KeyStroke k, JButton b) {
        frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(k, b.hashCode());
        frame.getRootPane().getActionMap().put(b.hashCode(), b.getAction());
    }

    private static void bind(KeyStroke k, AbstractAction a) {
        frame.getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(k, a.toString());
        frame.getRootPane().getActionMap().put(a.toString(), a);
    }

    private static void copyToClipboard() {
        //System.out.println("Copying to clipboard!");
        StringSelection stringSelection = new StringSelection(result.getText());
        Clipboard cp = Toolkit.getDefaultToolkit().getSystemClipboard();
        cp.setContents(stringSelection, null);
    }

    private static String loadFromClipboard() {
        //System.out.println("Loading from clipboard!");
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Clipboard clipboard = toolkit.getSystemClipboard();
        try {
            String s = (String)clipboard.getData(DataFlavor.stringFlavor);
            //System.out.println("Clipboard:" + s);
            return s;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Appends the String s to the end of "result".
     */
    public static void append(String s) {
        //System.out.println("Appending: " + s);
        if(shouldErase) {
            result.setText("");
            shouldErase = false;
        }

        result.setText(result.getText() + s);
        resize();
        try {
            currentNumber = Double.parseDouble(result.getText());
        } catch (Exception e) {
            currentNumber = 0;
        }
    }

    /**
     * Removes the last character at the end of "result".
     */
    public static void unappend() {
        if(result.getText().length() == 0) return;
        result.setText(result.getText().substring(0, result.getText().length() - 1));
        resize();
        try {
            currentNumber = Double.parseDouble(result.getText());
        } catch (Exception e) {
            currentNumber = 0;
        }
    }

    /**
     * Toggles the flag for whether or not we should
     * erase the results window on next number input.
     */
    public static void shouldErase(boolean to) {
        //System.out.println("Setting shouldErase to: " + to);
        shouldErase = to;
    }

    /**
     * Saves the current results window as a double value.
     */
    public static void save() {
        try {
            lastNumber = Double.parseDouble(result.getText());
        } catch (Exception e) {
            lastNumber = 0;
        }
        //result.setText("0");

        //System.out.println("Saving: " + lastNumber);
    }

    public static void clearCurrent() {
        //System.out.println("Clearing current memory");
        currentNumber = 0;
        result.setText("0");
        resize();
    }

    public static void clearLast() {
        //System.out.println("Clearing past memory");
        lastNumber = 0;
    }

    public static int getOperation() {
        return operation;
    }

    public static void setOperation(int operationID) {
        //System.out.println("Setting operation to " + operationID);
        operation = operationID;
    }

    public static void doOperation() {
        //System.out.println("Doing operation: " + operation);
        switch(operation) {
            case 0:  display(); break;
            case 1:  currentNumber = lastNumber - currentNumber;                display(); break; //Subtract
            case 2:  currentNumber = lastNumber + currentNumber;                display(); break; //Add
            case 3:  currentNumber = lastNumber * currentNumber;                display(); break; //Multiply
            case 4:  currentNumber = lastNumber / currentNumber;                display(); break; //Divide
            case 5:  currentNumber = 1 / currentNumber;                         display(); break; //Inverse
            case 6:  currentNumber = Math.pow(currentNumber,2);                 display(); break; //Square
            case 7:  currentNumber = Math.pow(currentNumber,3);                 display(); break; //Cube
            case 8:  currentNumber = Math.pow(lastNumber, currentNumber);       display(); break; //n pow
            case 9:  currentNumber = Math.pow(Math.E, currentNumber);           display(); break; //e pow
            case 10: currentNumber = factorial(currentNumber);                  display(); break; //factorial
            case 11: currentNumber = Math.sqrt(currentNumber);                  display(); break; //sqrt
            case 12: currentNumber = Math.cbrt(currentNumber);                  display(); break; //cbrt
            case 13: currentNumber = Math.pow(lastNumber, 1.0 / currentNumber); display(); break; //n root
            case 14: currentNumber = Math.log(currentNumber);                   display(); break; //factorial
            case 15: currentNumber = Math.sin(radianMode?currentNumber:Math.toRadians(currentNumber)); display(); break; 
            case 16: currentNumber = Math.cos(radianMode?currentNumber:Math.toRadians(currentNumber)); display(); break; 
            case 17: currentNumber = Math.tan(radianMode?currentNumber:Math.toRadians(currentNumber)); display(); break; 
            default: display();
        }
    }

    private static double factorial(double d) {
        //In the event of an integer, defer to a simpler algorithm.
        if(d == (int)d) 
            return intFactorial(d);

        //Else, we use Stirling's approximation to a few terms.
        return Math.sqrt(2*Math.PI*d) * Math.pow(d/Math.E, d) * (1 + (1/(12*d)) + (1/(288*d*d)) - (139/(51840*d*d*d)) - (531/(2488320*d*d*d*d)));
    }

    /**
     * Returns the basic integer factorial of a number.
     * This method uses doubles so that it can hold huge numbers.
     */
    private static double intFactorial(double n) {
        return n <= 1 ? 1 : n * intFactorial(n - 1);
    }

    private static void display() {
        //System.out.println("Updating display to " + currentNumber);

        try { Double.parseDouble(""+currentNumber); }
        catch (Exception e) { currentNumber = 0; }

        result.setText("" + currentNumber);
        resize();
    }

    public static String getDisplay() {
        return result.getText();
    }

    //Update the font size to fill the window
    private static void resize() {
        double ratio = (double)result.getWidth() / result.getFontMetrics(new Font("Monaco", Font.PLAIN, 48)).stringWidth(result.getText());
        font = new Font("Monaco", Font.PLAIN, Math.min((int)(ratio * 48) - 2, 48));
        result.setFont(font);
    }

    public static void main(String[] args) {
        new Calculator();
    }
}