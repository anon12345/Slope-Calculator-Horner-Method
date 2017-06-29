package classes;

public class Operation{
  private static String operation = "";
  static final double ACCURACY = 1e-10;
  
  public static double operate(double value){
    return PostfixEvaluater.evaluate(operation, value);
  }
  
  
  public static double derivative(double value){
    double firstVal = PostfixEvaluater.evaluate(operation, value);
    double secondVal = (PostfixEvaluater.evaluate(operation, value + ACCURACY));
    double dx = ((secondVal - firstVal)/(ACCURACY));
    return dx;
  }
  
  public static void setOperation(String operation){
    Operation.operation = operation.trim();
  }
}