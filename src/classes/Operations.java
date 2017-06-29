package classes;
import java.util.*;

public class Operations{
  private HashSet<String> operator = new HashSet<String>();
  private HashSet<String> singleOperator = new HashSet<String>();
  
  public Operations(){
    singleOperator.add("sin");
    singleOperator.add("cos");
    singleOperator.add("tan");
    singleOperator.add("snh");
    singleOperator.add("csh");
    singleOperator.add("log");
    
    operator.add("*");
    operator.add("+");
    operator.add("-");
    operator.add("/");
    operator.add("%");
    operator.add("^");
  }
  
  public boolean isOperator(String s){
    return operator.contains(s) || singleOperator.contains(s); 
  }
  
  public boolean isSingleOperator(String s){
    return singleOperator.contains(s); 
  }
  
  public double compute(double a, double b, String operator){
    if (operator.equals("*"))
      return a * b;
    if (operator.equals("+"))
      return a + b;
    if (operator.equals("-"))
      return a - b;
    if (operator.equals("/"))
      return a / b;
    if (operator.equals("^"))
      return Math.pow(a, b);
    if (operator.equals("%"))
      return a % b;
    return -1;
  }
  
  public double compute(double a, String operator){
    if (operator.equals("sin"))
      return Math.sin(a);
    if (operator.equals("cos"))
      return Math.cos(a);
    if (operator.equals("tan"))
      return Math.tan(a);
    if (operator.equals("csh"))
      return Math.cosh(a);
    if (operator.equals("snh"))
      return Math.sinh(a);
    if (operator.equals("log"))
      return Math.log(a);
    return -1;
  }
  
  public boolean hasHigherPrecendece(String a, String b){
    HashMap<String, Integer> order = new HashMap<String, Integer>();
    order.put("+", 1);
    order.put("-", 2);
    order.put("*", 3); 
    order.put("/", 4);
    order.put("^", 5);
    
    order.put("sin", 6);
    order.put("cos", 6);
    order.put("tan", 6);
    order.put("log", 6);
    
    order.put("snh", 7);
    order.put("cnh", 7);
    
    if (!order.containsKey(b))
      return false;
    return (order.get(b) <= order.get(a));
  }
  
}