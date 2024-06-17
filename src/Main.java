//простой калькулятор работающий с целыми числами от 1 до 10, с арабскими и римскими
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    //массив ниже нужен для соответствия римских чисел с арабскими
    static String[] romanNumbers = new String[] {"I", "II", "III", "IV", "V", "VI", "VII", "VIII", "IX", "X"};
    static boolean romanExpression = false;           //определяет выражение римское или арабское
    static Scanner in = new Scanner(System.in);
    static String input = "0";
    static String result;

    public static void main(String[] args) {
        System.out.println("Чтобы остановить работу программы отправьте пустую строку");

        while (!input.isEmpty()) {       //основной цикл программы, чтобы остановить нужно отправить пустую строку
            System.out.println("Введите пример");
            input = in.nextLine();
            romanExpression = false;

            if (input.isEmpty()) break;

            try {
                result = Main.calc(input);//основной метод производит проверку и вычисление возвращает строковый ответ
            }
            catch (Exception ex) {
                System.out.println(ex.getMessage());
                break;
            }

            System.out.println(result);
        }
    }

    public static String calc(String input) throws Exception {
        String[] args = new String[3];

        args = Main.separation(input);                      //выделяет из строки два слагаемых и знак
        Main.checking(args[0], args[2]);                    //проверяет два слагаемых на корректность
        result = Integer.toString(Main.calcResult(args));   //вычисляет результат и преобразует в строку
        result = Main.checkResult(result);              //проверяет результат на случай если выражение было римское

        return result;
    }

    public static String[] separation(String input) throws Exception {    //метод возвращает два числа и знак строками
        String oneVar, twoVar, sign;
        int index;

        input = input.replaceAll(" ", "");
        Pattern pattern = Pattern.compile("[*/+-]");
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) index = matcher.start();
        else throw new Exception("арифметический знак отсутствует");

        if (index == 0 || index == input.length() - 1) throw new Exception("отсутствует одно из чисел или оба");

        oneVar = input.substring(0, index);
        sign = input.substring(index, index + 1);
        twoVar = input.substring(index + 1);

        return new String[] {oneVar, sign, twoVar};
    }

    public static void checking(String oneVar, String twoVar) throws Exception {     //проверяет числа на корректность
        boolean oneVarRoman = false;
        boolean twoVarRoman = false;

        if (!checkingForRomanNumber(oneVar) && !checkingForArabicNumber(oneVar)) {
            throw new Exception("Первое слагаемое должно быть целым числом от 1 до 10");
        }

        if (!checkingForRomanNumber(twoVar) && !checkingForArabicNumber(twoVar)) {
            throw new Exception("Второе слагаемое должно быть целым числом от 1 до 10");
        }

        if (checkingForRomanNumber(oneVar) && checkingForArabicNumber(twoVar)) {
            throw new Exception("оба слагаемые должны быть либо римскими либо арабскими");
        }

        if (!checkingForRomanNumber(oneVar) && !checkingForArabicNumber(twoVar)) {
            throw new Exception("оба слагаемые должны быть либо римскими либо арабскими");
        }

        if (checkingForRomanNumber(oneVar) && checkingForRomanNumber(twoVar)) romanExpression = true;
    }

    public static boolean checkingForRomanNumber(String var) {   //проверяет является ли число римским целым от 1 до 10
        for (int i = 0; i <= 9; i++) {
            if (var.equals(romanNumbers[i])) return true;
        }

        return false;
    }

    public static boolean checkingForArabicNumber(String var) {  //проверяет является ли число арабским целым от 1 до 10
        for (int i = 0; i <= 9; i++) {
            if (var.equals(Integer.toString(i + 1))) return true;
        }

        return false;
    }

    public static int calcResult(String[] args) throws Exception {     //вычисляет результат и возвращает целое число
        int oneVar, twoVar;
        String sign;

        sign = args[1];
        oneVar = Main.convertToNumber(args[0]);
        twoVar = Main.convertToNumber(args[2]);

        switch (sign) {
            case "*":
                return oneVar * twoVar;
            case "/":
                return oneVar / twoVar;
            case "+":
                return oneVar + twoVar;
            case "-":
                return oneVar - twoVar;
            default:
                throw new Exception("не удалось выполнить вычисление");
        }
    }

    public static int convertToNumber(String var) throws Exception {      //преобразует строки в числовой формат
        for (int i = 0; i <= 9; i++) {
            if (var.equals(Integer.toString(i + 1)) || var.equals(romanNumbers[i])) return i + 1;
        }

        throw new Exception("не удалось преобразовать к числу");
    }

    public static String checkResult(String result) throws Exception {   //проверяет результат на римский ответ
        int res = Integer.parseInt(result);

        if (romanExpression && (res <= 0)) {
            throw new Exception("не удалось преобразовать ответ в римский формат");
        }

        if (romanExpression) {
            String[] num = new String[] {"", "X", "XX", "XXX", "LX", "L", "LX", "LXX", "LXXX", "CX", "C"};
            String dres = num[res / 10];
            String cres;

            if(res - res / 10 * 10 == 0) cres = "";
            else cres = romanNumbers[res - res / 10 * 10 - 1];

            return dres + cres;
        }

        return result;
    }
}