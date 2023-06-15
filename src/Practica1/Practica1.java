/* Rodríguez Contreras Raul Arturo
Mat 1261510
Crea un analizador léxico básico, que distinga entre los siguientes tokens: if, identificadores, enteros y flotantes.
 */
import java.util.Scanner;
 import java.util.regex.Matcher;
 import java.util.regex.Pattern;
 
 
 public class Practica1 {
     private String tokType;
     private String token;

     public Practica1() {
        run();
     }
     private void run() {
         try (Scanner sc = new Scanner(System.in)) {             
             System.out.println(" Ingrese una expresión:");
             String cad = sc.nextLine();
             String[] words = cad.split(" ");
             System.out.printf("\n%10s | %s\n", "Tipo", "Token");
                for (String word : words) {
                    response(word);
                    System.out.printf("%10s   %s\n", tokType, token);
                }
         }
         
     }

     
     public static void main(String[] args) {
         new Practica1();
         
     }
    private int response(String word){
        token = word;
            if(isInvalid(word)){
                tokType = "Error";
                token = "No puede comenzar con _ o numero";
                return 0;
            }
            else if(isKeyword(word)){
                tokType = "IF";
                return 1;
            }else if(isInteger(word)){
                tokType = "Integer";
                return 2;
            }else if(isFloat(word)){
                tokType = "Float";
                return 3;
            }else if(isIdentifier(word)){
                tokType = "Identifier";
                return 4;
            }else{
                tokType = "Error";
                token = word;
                return 6;
            }
     }
     Boolean isKeyword(String word){
         String[] keywords = {"if"};
         for (String keyword : keywords) {
             if (keyword.equals(word)) {
                 return true;
             }
         }
         return false;
     }
     Boolean isInteger(String word){
         Pattern pat = Pattern.compile("[+-]?[0-9]+"); //numero entero
         Matcher mat = pat.matcher(word);
         return mat.matches();
     }
     Boolean isFloat(String word){
         Pattern pat = Pattern.compile("[+-]?([0-9]+([.][0-9]*)?|[.][0-9]+)"); //numero flotante
         Matcher mat = pat.matcher(word);
         return mat.matches();
     }
     Boolean isIdentifier(String word){
         Pattern pat = Pattern.compile("[a-zA-Z][a-zA-Z0-9]*"); //Letras entre la a-z o A-Z 
         Matcher mat = pat.matcher(word);
         return mat.matches();
     }  
     Boolean isInvalid(String word){
            Pattern pat = Pattern.compile("^_[a-zA-Z0-9_]+"); //guion bajo y letra o numero o _
            Matcher mat = pat.matcher(word);
            Pattern pat2 = Pattern.compile("[0-9]+[a-zA-Z]+"); //numero y letra o numero o _
            Matcher mat2 = pat2.matcher(word);
            return mat.matches() || mat2.matches();
     }                             
    
}

 