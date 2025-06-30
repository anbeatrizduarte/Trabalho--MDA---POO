import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        CadastroRespostas cadastro = new CadastroRespostas();
        Scanner sc = new Scanner(System.in);
        int opcao = -1;

        while (opcao != 0) {
            System.out.println("\n=== Menu Principal ===");
            System.out.println("1. Criar Arquivo da Disciplina");
            System.out.println("2. Gerar Resultado da Disciplina");
            System.out.println("3. Visualizar Respostas dos Alunos");
            System.out.println("0. Sair");
            System.out.print("Digite a opcao: ");

            opcao = sc.nextInt();
            sc.nextLine();

            switch (opcao) {
                case 1:
                    cadastro.criarArquivoDisciplina(sc);
                    break;
                case 2:
                    cadastro.gerarResultado(sc);
                    break;
                case 3:
                    System.out.print("Digite o nome da disciplina para visualizar (ex: Matematica.txt): ");
                    String nomeArqVisualizar = sc.nextLine();
                    cadastro.visualizarArquivo("RespostasAlunos/" + nomeArqVisualizar);
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
        sc.close();
    }
}