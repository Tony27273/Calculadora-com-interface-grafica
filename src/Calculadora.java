import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class Calculadora {
    private final Color CINZA_CLARO = new Color(84, 84, 84);
    private final Color CINZA_ESCURO = new Color(53, 53, 53, 255);
    private final Color PRETO = new Color(23, 23, 23, 255);
    private final Color LARANJA = new Color(81, 18, 118);

    private final String[] VALORES_BOTOES = {
            "AC", "+/-", "%", "÷",
            "7", "8", "9", "×",
            "4", "5", "6", "-",
            "1", "2", "3", "+",
            "0", ".", "√", "="
    };

    private final Set<String> OPERADORES = new HashSet<>(Arrays.asList("÷", "×", "-", "+", "="));
    private final Set<String> SIMBOLOS_TOPO = new HashSet<>(Arrays.asList("AC", "+/-", "%"));
    private final Set<String> DIGITOS = new HashSet<>(Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9"));

    private final JFrame janela = new JFrame("Calculadora");
    private final JLabel rotuloDisplay = new JLabel();
    private final JPanel painelDisplay = new JPanel();
    private final JPanel painelBotoes = new JPanel();

    private String primeiroOperando = "0";
    private String operadorAtual = null;
    private String segundoOperando = null;
    private boolean novoNumeroEsperado = false;

    public Calculadora() {
        configurarJanela();
        configurarDisplay();
        configurarBotoes();
        janela.setVisible(true);
    }

    private void configurarJanela() {
        janela.setMinimumSize(new Dimension(300, 450));
        janela.setLocationRelativeTo(null);
        janela.setResizable(true);
        janela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        janela.setLayout(new BorderLayout());
    }

    private void configurarDisplay() {
        rotuloDisplay.setBackground(PRETO);
        rotuloDisplay.setForeground(Color.WHITE);
        rotuloDisplay.setFont(new Font("Arial", Font.PLAIN, 80));
        rotuloDisplay.setHorizontalAlignment(JLabel.RIGHT);
        rotuloDisplay.setText("0");
        rotuloDisplay.setOpaque(true);

        painelDisplay.setLayout(new BorderLayout());
        painelDisplay.add(rotuloDisplay);
        janela.add(painelDisplay, BorderLayout.NORTH);
    }

    private void configurarBotoes() {
        painelBotoes.setLayout(new GridLayout(5, 4, 2, 2));
        painelBotoes.setBackground(PRETO);
        janela.add(painelBotoes);

        for (String valorBotao : VALORES_BOTOES) {
            JButton botao = criarBotao(valorBotao);
            painelBotoes.add(botao);

            botao.addActionListener(e -> tratarCliqueBotao(botao));
        }
    }

    private JButton criarBotao(String texto) {
        JButton botao = new JButton(texto);
        botao.setFont(new Font("Arial", Font.PLAIN, 30));
        botao.setFocusable(false);
        botao.setBorder(new LineBorder(PRETO));

        if (SIMBOLOS_TOPO.contains(texto)) {
            botao.setBackground(CINZA_CLARO);
            botao.setForeground(PRETO);
        } else if (OPERADORES.contains(texto)) {
            botao.setBackground(LARANJA);
            botao.setForeground(Color.WHITE);
        } else {
            botao.setBackground(CINZA_ESCURO);
            botao.setForeground(Color.WHITE);
        }

        if ("0".equals(texto)) {
            botao.setFont(new Font("Arial", Font.PLAIN, 28));
        }

        return botao;
    }

    private void tratarCliqueBotao(JButton botao) {
        String valorBotao = botao.getText();

        if (OPERADORES.contains(valorBotao)) {
            tratarOperador(valorBotao);
        } else if (SIMBOLOS_TOPO.contains(valorBotao)) {
            tratarSimboloTopo(valorBotao);
        } else {
            tratarDigitoOuPonto(valorBotao);
        }
    }

    private void tratarOperador(String operador) {
        if ("=".equals(operador)) {
            calcularResultado();
        } else if (OPERADORES.contains(operador)) {
            if (operadorAtual == null) {
                primeiroOperando = rotuloDisplay.getText();
            } else {
                calcularResultado();
                primeiroOperando = rotuloDisplay.getText();
            }
            operadorAtual = operador;
            novoNumeroEsperado = true;
        }
    }

    private void calcularResultado() {
        if (operadorAtual != null) {
            segundoOperando = rotuloDisplay.getText();
            double numA = Double.parseDouble(primeiroOperando);
            double numB = Double.parseDouble(segundoOperando);
            double resultado = 0;

            switch (operadorAtual) {
                case "+":
                    resultado = numA + numB;
                    break;
                case "-":
                    resultado = numA - numB;
                    break;
                case "×":
                    resultado = numA * numB;
                    break;
                case "÷":
                    if (numB != 0) {
                        resultado = numA / numB;
                    } else {
                        rotuloDisplay.setText("Erro");
                        limparTudo();
                        return;
                    }
                    break;
            }

            rotuloDisplay.setText(removerZeroDecimal(resultado));
            operadorAtual = null;
        }
    }

    private void tratarSimboloTopo(String simbolo) {
        switch (simbolo) {
            case "AC":
                limparTudo();
                rotuloDisplay.setText("0");
                break;
            case "+/-":
                double numDisplay = Double.parseDouble(rotuloDisplay.getText());
                numDisplay *= -1;
                rotuloDisplay.setText(removerZeroDecimal(numDisplay));
                break;
            case "%":
                numDisplay = Double.parseDouble(rotuloDisplay.getText());
                numDisplay /= 100;
                rotuloDisplay.setText(removerZeroDecimal(numDisplay));
                break;
        }
    }

    private void tratarDigitoOuPonto(String valor) {
        if (novoNumeroEsperado) {
            rotuloDisplay.setText("0");
            novoNumeroEsperado = false;
        }

        String textoDisplay = rotuloDisplay.getText();

        if (".".equals(valor)) {
            if (!textoDisplay.contains(".")) {
                rotuloDisplay.setText(textoDisplay + ".");
            }
        } else if (DIGITOS.contains(valor)) {
            if ("0".equals(textoDisplay)) {
                rotuloDisplay.setText(valor);
            } else {
                rotuloDisplay.setText(textoDisplay + valor);
            }
        }
    }

    private void limparTudo() {
        primeiroOperando = "0";
        operadorAtual = null;
        segundoOperando = null;
        novoNumeroEsperado = false;
    }

    private String removerZeroDecimal(double numero) {
        if (numero % 1 == 0) {
            return Integer.toString((int) numero);
        }
        return Double.toString(numero);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Calculadora::new);
    }
}