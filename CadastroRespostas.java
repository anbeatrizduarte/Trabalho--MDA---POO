import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.List; // Certifique-se de que List está importado

public class CadastroRespostas extends Base {
    private String nomeDisciplina;
    private GerenciadorResultados gerenciadorResultados; // Nova instância

    public CadastroRespostas() {
        super();
        this.gerenciadorResultados = new GerenciadorResultados(); // Inicializa a nova classe
    }

    public void criarArquivoDisciplina(Scanner sc) {
        System.out.print("\n=== Criar Arquivo da Disciplina ===\n");
        System.out.print("Digite o nome da disciplina: ");
        nomeDisciplina = sc.nextLine();

        String nomeArquivo = "RespostasAlunos/" + nomeDisciplina + ".txt";

        boolean adicionarAluno = true;

        while (adicionarAluno) {
            System.out.print("\nNome do aluno: ");
            String nomeAluno = sc.nextLine();

            char[] respostas = new char[10];
            System.out.print("\n-- Preencha as respostas do aluno (V/F) --\n");
            for (int i = 0; i < 10; i++) {
                String respostaQ;
                do {
                    System.out.print("Q" + (i + 1) + ": ");
                    respostaQ = sc.nextLine().trim().toUpperCase();
                    if (respostaQ.length() == 1 && (respostaQ.charAt(0) == 'V' || respostaQ.charAt(0) == 'F')) {
                        respostas[i] = respostaQ.charAt(0);
                        break;
                    } else {
                        System.out.println("Entrada inválida. Digite 'V' para Verdadeiro ou 'F' para Falso.");
                    }
                } while (true);
            }

            String respostasAluno = new String(respostas);
            listaAlunos.add(new Aluno(nomeAluno, respostasAluno, 0)); // 'listaAlunos' é herdada de Base

            System.out.print("\nDeseja adicionar outro aluno? (S/N): ");
            String opcaoAdicionar = sc.nextLine();
            if (opcaoAdicionar.equalsIgnoreCase("N")) {
                adicionarAluno = false;
            }
        }

        salvarArquivo(nomeArquivo); // Método herdado de Base
        visualizarArquivo(nomeArquivo); // Método herdado de Base
    }

    public void gerarResultado(Scanner sc) {
        System.out.print("\n=== Gerar Resultado da Disciplina ===\n");
        System.out.print("Digite o nome da disciplina para gerar resultados: ");
        nomeDisciplina = sc.nextLine();

        String nomeArquivoDisciplina = "RespostasAlunos/" + nomeDisciplina + ".txt";

        lerArquivo(nomeArquivoDisciplina); // Método herdado de Base

        if (listaAlunos.isEmpty()) {
            System.out.println("Nenhum aluno encontrado para a disciplina " + nomeDisciplina + ". Verifique o nome do arquivo.");
            return;
        }

        System.out.print("Digite o caminho completo do arquivo com o gabarito oficial: ");
        String caminhoGabarito = sc.nextLine();
        String gabarito = lerGabarito(caminhoGabarito);

        if (gabarito == null || gabarito.isEmpty()) {
            System.err.println("Gabarito não pôde ser lido ou está vazio. Não é possível calcular as notas.");
            return;
        }

        if (gabarito.length() != 10) {
            System.err.println("Gabarito inválido. O gabarito deve conter exatamente 10 caracteres (V ou F).");
            return;
        }

        // Delega o cálculo das notas e a ordenação/salvamento para a nova classe
        gerenciadorResultados.calcularNotasAlunos(listaAlunos, gabarito);
        gerenciadorResultados.ordenarEsalvarResultados(nomeDisciplina, listaAlunos);

        System.out.println("\nResultados gerados com sucesso para a disciplina: " + nomeDisciplina);
    }

    private String lerGabarito(String caminho) {
        try (BufferedReader br = new BufferedReader(new FileReader(caminho))) {
            String linha = br.readLine();
            if (linha != null) {
                String gabaritoProcessado = linha.trim().toUpperCase();
                if (gabaritoProcessado.matches("[VF]{10}")) {
                    return gabaritoProcessado;
                } else {
                    System.err.println("Conteúdo do gabarito inválido: '" + linha + "'. Deve conter apenas 'V' ou 'F' por 10 questões.");
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler gabarito do caminho " + caminho + ": " + e.getMessage());
        }
        return null;
    }
}