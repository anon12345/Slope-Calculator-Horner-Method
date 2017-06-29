package classes;
public class InputVerification {
  
  public static int countCharacter (String input, char character){
    int returnVal = 0;
    for(int i = 0; i < input.length(); i++)
      if (input.charAt(i) == character)
      returnVal++;
    return returnVal;
  }
  
  public static boolean isValidExponent(String input){
    if (countCharacter(input, '(') >= countCharacter(input, ')'))
      return true;
    return false;
  }
  
  public static boolean hasBalancedBrackets(String input, boolean isFinal){
    int openCount = 0;
    int closeCount = 0;
    for(int i = 0; i < input.length(); i++){
      if (input.charAt(i) == '(')
        openCount++;
      else{
        if (input.charAt(i) == ')')
          closeCount++;
      }
    }
    return ((closeCount > openCount) ? (false) : (isFinal && closeCount != openCount) ? (false) : (true));
  }
  
  public static boolean hasRepeatedInvalidOperators(String input){
    if (input.length() == 0)
      return true;
    char prev, current;
    char [] invalidChars = {'x', 'X','+','-','*','^', '/'};
    char [] operators = {'+','-','x','^', '/', '*'};
    for(int i = 1; i < input.length(); i++){
      prev = input.charAt(i-1);
      current = input.charAt(i);
      if (prev == '(' && current == ')')
        return false;
      if (prev == current){
        for(int x = 0; x < invalidChars.length; x++){
          if (prev == invalidChars[x])
            return false;
        }
      }
    }
    for(int i = 0; i < operators.length; i++){
      for(int x = 0; x < input.length(); x++){
        if (operators[i] == input.charAt(x)){
          if (x == input.length()-1)
            break;
          for(int y = 0; y < operators.length; y++){
            if (input.charAt(x+1) == operators[y])
              return false;
          }
        }
      }
    }
    return true;
  }
}
