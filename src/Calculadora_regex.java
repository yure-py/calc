import java.util.regex.*;

public class Calculadora_regex {

    // TODO: Refazer com shuting yard

    // TODO: Validação de expressão colocada
    // TODO: Parsing para alteradores de precedencia ()
    // TODO: Números negativos

    // DIVSION ZERO ERROR BUG Acho que deu certo

    private static void mensagem(String mensagem, int lenght){
        System.out.println( "=".repeat(lenght) );
        System.out.print(mensagem.indent((lenght - mensagem.length())/2 ) );
        System.out.println( "=".repeat(lenght) );
    }

    private static boolean checarZeroDivision(String expressao){

        Matcher m = Pattern.compile("(\\d+|\\d)/0(?!\\.)").matcher(expressao);
        return m.find();

    }


    static public double calcular(String n1, String n2, String op){

        double a = Double.parseDouble(n1);
        double b = Double.parseDouble(n2);

        return switch (op) {

            case "*" -> a * b;
            case "+" -> a + b;
            case "-" -> a - b;
            case "/" -> a / b;
            default -> 0;

        };
    }

    static String parser_expression(String expressao){

        // Retirando espaços em branco
        expressao = expressao.replaceAll("\\s+", "");

        // regexes
        Pattern firstPrecedence = Pattern.compile
            ("(-?(?:\\d+\\.)?\\d+)([/*])(-?(?:\\d+\\.)?\\d+)");

        Pattern SecondPrecedence = Pattern.compile
                ("(-?(?:\\d+\\.)?\\d+)([-+])(-?(?:\\d+\\.)?\\d+)");


        while (true){

            Matcher m = firstPrecedence.matcher(expressao);
            if (!m.find()) break;

            expressao = expressao.replace(m.group(), Double.toString(calcular(m.group(1), m.group(3), m.group(2)) ));
        }

        while (true){
            Matcher m = SecondPrecedence.matcher(expressao);

            if (!m.find()) break;

            expressao = expressao.replace(m.group(), Double.toString(calcular(m.group(1), m.group(3), m.group(2)) ));

        }
        return expressao;
    }


    static public void iniciar(String expressao){

        if(checarZeroDivision(expressao)){
            System.out.println("ZERO DIVISION!\n");
            return;
        }

        // Parser de parênteses

        Pattern pattern = Pattern.compile("\\((-?(?:\\d+\\.)?\\d+)([+-])(-?(?:\\d+\\.)?\\d+)\\)");

        while (true){

            Matcher m = pattern.matcher(expressao);

            if (!m.find()) break;

            expressao = expressao.replace(
                            m.group(),
                            Double.toString(Calculadora_regex.calcular(m.group(1), m.group(3), m.group(2)))
                        );

        }

        if (expressao.contains("(")){

            int idx_parentese_de_fechaento = expressao.lastIndexOf(")");

            String expressao_com_parenteses = expressao.substring(
                    expressao.indexOf("("),             /* Inclusive ( */
                    idx_parentese_de_fechaento+1        /* Exclusive ) */
            );

            String expressao_resolvida = Calculadora_regex.parser_expression(expressao_com_parenteses);

            expressao = expressao.replace(
                    expressao_com_parenteses, expressao_resolvida.substring(1, expressao_resolvida.length() -1)
            );
        }

        // End: Parser de parênteses

        System.out.print(expressao + ": ");
        System.out.print(parser_expression(expressao));
        System.out.println("\n");

    }
}
