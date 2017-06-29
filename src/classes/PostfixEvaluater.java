package classes;
import java.util.*;


public class PostfixEvaluater{
  
  public static double evaluate(String expression, double value){
    expression = InfixToPostfix.infixToPostfix(expression); //convert infix to postfix
    
    Operations operator = new Operations(); //operations reference to distinguish binary and unary operators
    Stack<Double> op = new Stack<Double>(); //operations stack used to organize information
    StringTokenizer st = new StringTokenizer(expression); //tokenize input into tokens
    
    while(st.hasMoreTokens()){
      String input = st.nextToken();
      if (input == null)
        break;
      
      if (input.equals("X")) //append the value of x if the variable is mentioned
        input = value + "";
      
      if (operator.isSingleOperator(input)){ 
        double a = op.pop();
        op.push(operator.compute(a, input));
      }
      else if (operator.isOperator(input)){
        double b = op.pop();
        double a = op.pop();
        op.push(operator.compute(a, b, input));
      }
      else{
        op.push(Double.parseDouble(input)); 
      }
    }
    return op.pop();
    
  }
}