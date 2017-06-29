package classes;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import java.util.*;

@SuppressWarnings("serial")
public class SlopeCalculator extends JFrame implements ActionListener, MouseListener{
  private JPanel buttonPanel;
  private static final int UPPER_BUFFER = 100;
  private Image backgroundImg;
  private Image display;
  private Image highlight;
  private Image displayInitialize;
  private Image displayOff;
  private Image maxChar;
  private boolean drawHighlight = false;
  private int highlightX = 0;
  private int highlightY = 0;
  private boolean drawZero = false;
  private boolean turnedOn = false;
  private boolean turnedOff = true;
  private boolean off = true;
  private static String command = "";
  private static String expression = "";
  private double guess;
  private KeyMapping keyMapping;
  
 
  public SlopeCalculator(){
    super("Slope Calculator");
    fetchImages();
    keyMapping = new KeyMapping();
    setSize(465,351);
    setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    setResizable(false);
    
    buttonPanel = new JPanel();
    add(buttonPanel);
    addMenuBar();
    
    addMouseListener(this);
    setVisible(true);
  }
  
  private void fetchImages(){
    try{
      backgroundImg = ImageIO.read((SlopeCalculator.class.getClassLoader().getResource("images/CalculatorBackground.jpg")));
      display = ImageIO.read((SlopeCalculator.class.getClassLoader().getResource("images/editedmonitor.png")));
      highlight = ImageIO.read((SlopeCalculator.class.getClassLoader().getResource("images/highlight.png")));
      displayInitialize = ImageIO.read((SlopeCalculator.class.getClassLoader().getResource("images/editedoff.png")));
      displayOff = ImageIO.read((SlopeCalculator.class.getClassLoader().getResource("images/editedoff.png")));
      maxChar = ImageIO.read((SlopeCalculator.class.getClassLoader().getResource("images/editedmaxcharmonitor.png")));
    }
    catch(IOException e){
      JOptionPane.showMessageDialog(this, "Error: Could not find required image files!");
    }
  }
  
  private void addMenuBar(){
    JMenuBar menu = new JMenuBar();
    
    JMenu file = new JMenu("File");
    
    JMenuItem quit = new JMenuItem("Quit");
    quit.addActionListener(this);
    
    
    file.add(quit);
    
    menu.add(file);
    
    setJMenuBar(menu);
  }
  
  private void drawInterface(Graphics g){
    Graphics2D g2d = (Graphics2D) g;
    for(int i = 0; i < 6; i++)
      g2d.drawLine(0,UPPER_BUFFER + i * 50, 464, UPPER_BUFFER + i * 50);
    
    for(int i = 0; i < 9; i++)
    	if(i == 6 || i == 5){
    		g2d.drawLine(i * 58, UPPER_BUFFER, i * 58, 350-50);
    	}
    	else{
    		g2d.drawLine(i * 58, UPPER_BUFFER, i * 58, 350);
    	}
  }
  

  
  private void updateScreen(Graphics g){
    g.setFont(new Font("Helvetica", Font.PLAIN, 30));
    g.setColor(Color.white);
    g.drawString(command, 113, 85);
  }
  
  public void paint(Graphics g){
    super.paint(g);
    
    
    g.drawImage(backgroundImg, 0, 100, null);
    if (!turnedOff && !turnedOn && !off){
      if (command.length() <= 18)
        g.drawImage(display,  0,  44,  490,  57,  null);
      else
        g.drawImage(maxChar, 0, 44, 490, 57, null);
    }
    else if (turnedOn){
      off = false;
      command = "";
      expression = "";
      g.drawImage(displayInitialize, 0, 44, 490, 57, null);
      try{
        Thread.sleep(100);
      }
      catch(Exception e){}
      g.drawImage(display,  0,  44,  490,  57,  null);
    }
    else{
      off = true;
      g.drawImage(displayOff, 0, 44, 490, 57, null);
      command = "";
      expression = "";
    }
    
    drawInterface(g);
    updateScreen(g);
  }
  
  @Override
  public void actionPerformed(ActionEvent arg0) {
    String cmd = arg0.getActionCommand();
    if (cmd.equals("Quit"))
      System.exit(0);
  }
  
  private void outputNotSupported(){
    JOptionPane.showMessageDialog(this,"Notice: This operation is not supported in this version!","Notice: Unsupported Operation",JOptionPane.ERROR_MESSAGE);
  }
  
  private void updateCommand(int x, int y){
    String cmdBefore = command;
    
    if (keyMapping.getCommandAppending.get(x + "|" + y) != null){
      command += keyMapping.getCommandAppending.get(x + "|" + y);
    }
    else if (x == 0 && (y > 0)){
      if (!InputVerification.isValidExponent(command)){
        JOptionPane.showMessageDialog(this, "Notice: You cannot apply an exponential function without an expression enclosed in brackets.","Notice: Invalid Operation", JOptionPane.ERROR_MESSAGE);
        command = command.substring(0, command.length()-3);
        return;
      }
      if (!InputVerification.hasRepeatedInvalidOperators(command)){
        JOptionPane.showMessageDialog(this,"Fatal Error: Invalid Input! You cannot have repeated / invalid operators.","Fatal Error: Invalid Input", JOptionPane.ERROR_MESSAGE);
        command = command.substring(0, command.length() -3);
        return;
      }
    }
    else if ((x == 1 && y == 3) || (x == 1 && y == 4) || (x == 1 && y == 0)){
      outputNotSupported();
      return;
    }
    else if (x == 3 && y == 4){
      JOptionPane.showMessageDialog(this,"Notice: Decimals are not supported in this version!","Notice: Unsupported Operation", JOptionPane.ERROR_MESSAGE);
      return;
    }
    
    verifyCommandValidity(false);
  }
  
  private void removeLastOperation(){
    command = command.substring(0, command.length()-1);
  }
  
  
  private boolean verifyCommandValidity(boolean isFinal){
    if (!InputVerification.hasBalancedBrackets(command, isFinal)){
      JOptionPane.showMessageDialog(this,"Fatal Error: You have invalid bracket proportions!","Fatal Error: Invalid Input",JOptionPane.ERROR_MESSAGE);
      removeLastOperation();
      return false;
    }
    if (!InputVerification.hasRepeatedInvalidOperators(command)){
      JOptionPane.showMessageDialog(this,"Fatal Error: Invalid Input! You cannot have repeated / invalid operators.","Fatal Error: Invalid Input",JOptionPane.ERROR_MESSAGE);
      removeLastOperation();
      return false;
    }
    return true;
  }
  
  
  public static String getCommand(){
    return new NumericalTokenizer().convertToSpacedNumericalFormat(command); //expression;
  }
  

  public static String getExpression(){
    return command; 
  }
  
  private void calculate(){
    if (!verifyCommandValidity(true))
      return;
    if (command.equals("")){
      JOptionPane.showMessageDialog(this,"Fatal Error: You cannot calculate without inputting an equation!","Fatal Error: Invalid Input",JOptionPane.ERROR_MESSAGE);
      return;
    }
    String x = JOptionPane.showInputDialog("At which x-value do you want the slope?");
    if(x == null){
    	return;
    }
    Operation.setOperation(SlopeCalculator.getCommand());
    double num = Operation.derivative(Double.parseDouble(x));
    JOptionPane.showMessageDialog(this, "The slope is " + num);
  }
  
  
  @Override
  public void mousePressed(MouseEvent arg0) {
    int x = (arg0.getX()) / 58;
    int y = (arg0.getY() - 100) / 50;
    
    if (x < 0 || x > 7 || y < 0 || y > 4)
      return;
    
    turnedOn = ((x == 4 && y == 0) ? (true) : (false));
    turnedOff = ((x == 5 &&
                  y == 0) ? (true) : (false));
    drawHighlight = true;
    
    if (x >=4 && x <= 6 && y == 4)
      drawZero = true;
    else
      drawZero = false;
    
    highlightX = x;
    highlightY = y;
    
    if (off)
      return;
    
    if (x == 7 && y == 4){ //calculate button
      calculate();
      return; 
    }
    
    if (x == 6 && y == 0){
      command = "";
      expression = "";
    }
    
    if (command.length() > 18)
      return;
    
    updateCommand(x, y);
  }
  

  public void mouseReleased(MouseEvent arg0) {
    drawHighlight = false;
    repaint();
  }
  
  
  public static void main(String[] args) {
    new SlopeCalculator();
  }
  

  public void mouseClicked(MouseEvent arg0) {}
  

  public void mouseEntered(MouseEvent arg0) {}
  

  public void mouseExited(MouseEvent arg0) {}
}
