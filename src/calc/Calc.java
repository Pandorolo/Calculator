package calc;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Area;
import java.awt.geom.RoundRectangle2D;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.AbstractBorder;
import javax.swing.border.Border;

public class Calc implements ActionListener {
  // ATTRIBUTES
  
  // Main frame
  private final JFrame frame;
  
  // Panel used for the lower part of the calculator
  private final JPanel panel;
  
  // Various buttons and display
  private final JButton[] numButtons = new JButton[10];
  private final JButton[] funcButtons = new JButton[8];
  private final JButton addButton;
  private final JButton subButton;
  private final JButton mulButton;
  private final JButton divButton;
  private final JButton decButton;
  private final JButton equButton;
  private final JButton delButton;
  private final JButton clrButton;
  private final JTextField display;

  // Fonts
  private final Font displayFont = new Font("Segoe", Font.BOLD, 50);
  private final Font font = new Font("Segoe", Font.BOLD, 20);
  private final Font delFont = new Font("Segoe", Font.BOLD, 35);
  
  // Colors and borders
  private final Color cBackground   = new Color(32, 32, 32);
  private final Color cFunctions    = new Color(50, 50, 50);
  private final Color cEqual        = new Color(83,218, 169);
  private final Color cNumbers      = new Color(59, 59, 59);
  private final Color cForeground   = new Color(255,255,255);
  private final Border empty = BorderFactory.createEmptyBorder();
  AbstractBorder rounded = new TextBubbleBorder(cNumbers,0,8,0);
  
  // Variables used for the back-end of the calculator
  private double num1 = 0, num2 = 0, result = 0;
  private boolean dec = false;
  private char operator = ' ';
  
  // CONSTRUCT
  public Calc() {
    // Basic inital setup of the main window
    frame = new JFrame("Calcolatrice");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setSize(320, 470);
    frame.setLayout(null);
    frame.setResizable(false);
    
    // Initalize the display
    display = new JTextField("");
    display.setBounds(0, 0, 320, 100);
    display.setEditable(false);
    display.setFont(displayFont);
    display.setHorizontalAlignment(JTextField.RIGHT);
    display.setBackground(cBackground);
    display.setForeground(cForeground);
    display.setBorder(empty);
    
    // Configure the text of each function button
    addButton = new JButton("+");
    subButton = new JButton("-");
    mulButton = new JButton("x");
    divButton = new JButton("/");
    decButton = new JButton(".");
    equButton = new JButton("=");
    delButton = new JButton("⇐");
    clrButton = new JButton("CE");
    
    // Insert the buttons in the array
    funcButtons[0] = addButton;
    funcButtons[1] = subButton;
    funcButtons[2] = mulButton;
    funcButtons[3] = divButton;
    funcButtons[4] = decButton;
    funcButtons[5] = equButton;
    funcButtons[6] = delButton;    
    funcButtons[7] = clrButton;
    
    // Configure each function button
    for (int i = 0; i < 8; i++) {
      funcButtons[i].addActionListener(this);
      funcButtons[i].setFont(font);
      funcButtons[i].setFocusable(false);
      funcButtons[i].setBackground(cFunctions);
      funcButtons[i].setForeground(cForeground);
      funcButtons[i].setBorder(rounded);
    }
    
    // Create and configure every number button
    for (int i = 0; i < 10; i++) {
      numButtons[i] = new JButton(String.valueOf(i));
      numButtons[i].addActionListener(this);
      numButtons[i].setFont(font);
      numButtons[i].setFocusable(false);
      numButtons[i].setBackground(cNumbers);
      numButtons[i].setForeground(cForeground);
      numButtons[i].setBorder(rounded);
    }
    
    // Set the size of clear and delete buttons
    clrButton.setBounds(0, 100, 160, 50);
    delButton.setBounds(160, 100, 160, 50);
    
    // Set their borders to empty - they'd be rounded if not
    clrButton.setBorder(empty);
    delButton.setBorder(empty);
    delButton.setFont(delFont);
    
    // Set the equal's button color
    equButton.setBackground(cEqual);
    
    // Create the panel to hold number and function buttons
    panel = new JPanel();
    panel.setBounds(0, 150, 322, 285);
    
    // Set the layout to a grid of 4 columns and 4 rows,
    // with 2 pixels of hgap and vgap
    panel.setLayout(new GridLayout(4, 4, 2, 2));
    panel.setBackground(cBackground);
    
    // Add the buttons to the panel in a preferred order
    panel.add(numButtons[1]);
    panel.add(numButtons[2]);
    panel.add(numButtons[3]);
    panel.add(addButton);
    panel.add(numButtons[4]);
    panel.add(numButtons[5]);
    panel.add(numButtons[6]);
    panel.add(subButton);
    panel.add(numButtons[7]);
    panel.add(numButtons[8]);
    panel.add(numButtons[9]);
    panel.add(mulButton);
    panel.add(decButton);
    panel.add(numButtons[0]);
    panel.add(equButton);
    panel.add(divButton);

    // Add to the main window the display, clear, del buttons and the panel
    frame.add(display);
    frame.add(clrButton);
    frame.add(delButton);
    frame.add(panel);
    frame.setVisible(true);
  }
  
  public static void main(String[] args) {
    Calc calc = new Calc();
  }

  // Event listener
  @Override
  public void actionPerformed(ActionEvent e) {
    // Check if the event is sent by a number button
    for (int i = 0; i < 10; i++) {
      // Check if you can add the number (not bigger than 10 digits and is not a first 0)
      boolean clickable = display.getText().length() < 8 && !(display.getText().isEmpty() && i == 0);
      
      if (e.getSource() == numButtons[i] && clickable) {
        // Concatenate the digit to the display's number
        display.setText(display.getText().concat(String.valueOf(i)));
      }
    }
    
    // Decimal button - if it's not already decimal, add a dot
    if (e.getSource() == decButton) {
      if (!dec) {
        display.setText(display.getText().concat("."));
        dec = true;
      }
    }
    
    // Sum button
    if (e.getSource() == addButton) {
      num1 += Double.parseDouble(display.getText());
      operator = '+';
      dec = false;
      display.setText("");
    }
    
    // Sub button
    if (e.getSource() == subButton) {
      num1 = Double.parseDouble(display.getText());
      operator = '-';
      dec = false;
      display.setText("");
    }
    
    // Moltiplication button
    if (e.getSource() == mulButton) {
      num1 = Double.parseDouble(display.getText());
      operator = '*';
      dec = false;
      display.setText("");
    }
    
    // Division button
    if (e.getSource() == divButton) {
      num1 = Double.parseDouble(display.getText());
      operator = '/';
      dec = false;
      display.setText("");
    }
    
    
    if (e.getSource() == equButton) {
      // When the equal button is clicked, save the display's text in num2
      num2 = Double.parseDouble(display.getText());

      switch (operator) {
        case '+':
          result = num1 + num2;
          break;
        case '-':
          result = num1 - num2;
          break;
        case '*':
          result = num1 * num2;
          break;
        case '/':
          result = num1 / num2;
          break;
        case ' ':
          break;
      }

      // Calculate the result and display it on the screen
      display.setText(String.valueOf(Math.round(result * 10000.0) / 10000.0));
      
      // Set decimal to false (new number) and num1 to 0
      dec = false;
      num1 = 0;
      operator = ' ';
    }
    
    // Clear button
    if (e.getSource() == clrButton) {
      display.setText("");
      num1 = 0;
      result = 0;
      dec = false;
    }
    
    // Delete button
    if (e.getSource() == delButton) {
      String string = display.getText();
      display.setText("");
      
      for (int i = 0; i < string.length() - 1; i++) {
        display.setText(display.getText() + string.charAt(i));
      }
    }
  }
}

// Class used for rounded borders
// Author: Andrew Thompson (https://stackoverflow.com/users/418556/andrew-thompson)
class TextBubbleBorder extends AbstractBorder {

    private Color color;
    private int thickness = 4;
    private int radii = 8;
    private int pointerSize = 7;
    private Insets insets = null;
    private BasicStroke stroke = null;
    private int strokePad;
    private int pointerPad = 4;
    private boolean left = true;
    RenderingHints hints;

    TextBubbleBorder(
            Color color) {
        this(color, 4, 8, 7);
    }

    TextBubbleBorder(
            Color color, int thickness, int radii, int pointerSize) {
        this.thickness = thickness;
        this.radii = radii;
        this.pointerSize = pointerSize;
        this.color = color;

        stroke = new BasicStroke(thickness);
        strokePad = thickness / 2;

        hints = new RenderingHints(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        int pad = radii + strokePad;
        int bottomPad = pad + pointerSize + strokePad;
        insets = new Insets(pad, pad, bottomPad, pad);
    }

    TextBubbleBorder(
            Color color, int thickness, int radii, int pointerSize, boolean left) {
        this(color, thickness, radii, pointerSize);
        this.left = left;
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return insets;
    }

    @Override
    public Insets getBorderInsets(Component c, Insets insets) {
        return getBorderInsets(c);
    }

    @Override
    public void paintBorder(
            Component c,
            Graphics g,
            int x, int y,
            int width, int height) {

        Graphics2D g2 = (Graphics2D) g;

        int bottomLineY = height - thickness - pointerSize;

        RoundRectangle2D.Double bubble = new RoundRectangle2D.Double(
                0 + strokePad,
                0 + strokePad,
                width - thickness,
                bottomLineY,
                radii,
                radii);

        Polygon pointer = new Polygon();

        if (left) {
            // left point
            pointer.addPoint(
                    strokePad + radii + pointerPad,
                    bottomLineY);
            // right point
            pointer.addPoint(
                    strokePad + radii + pointerPad + pointerSize,
                    bottomLineY);
            // bottom point
            pointer.addPoint(
                    strokePad + radii + pointerPad + (pointerSize / 2),
                    height - strokePad);
        } else {
            // left point
            pointer.addPoint(
                    width - (strokePad + radii + pointerPad),
                    bottomLineY);
            // right point
            pointer.addPoint(
                    width - (strokePad + radii + pointerPad + pointerSize),
                    bottomLineY);
            // bottom point
            pointer.addPoint(
                    width - (strokePad + radii + pointerPad + (pointerSize / 2)),
                    height - strokePad);
        }

        Area area = new Area(bubble);
        area.add(new Area(pointer));

        g2.setRenderingHints(hints);

        // Paint the BG color of the parent, everywhere outside the clip
        // of the text bubble.
        Component parent  = c.getParent();
        if (parent!=null) {
            Color bg = parent.getBackground();
            Rectangle rect = new Rectangle(0,0,width, height);
            Area borderRegion = new Area(rect);
            borderRegion.subtract(area);
            g2.setClip(borderRegion);
            g2.setColor(bg);
            g2.fillRect(0, 0, width, height);
            g2.setClip(null);
        }

        g2.setColor(color);
        g2.setStroke(stroke);
        g2.draw(area);
    }
}
