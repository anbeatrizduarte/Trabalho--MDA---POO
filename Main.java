import java.util.Scanner;

public class Main { // Ou App, ou o nome que você usa para sua classe principal

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        CadastroRespostas cadastroRespostas = new CadastroRespostas();

        int opcao;
        do {
            System.out.println("\n--- Menu Principal ---");
            System.out.println("1. Criar Arquivo de Respostas da Disciplina");
            System.out.println("2. Gerar Resultados da Disciplina");
            System.out.println("0. Sair");
            System.out.print("Escolha uma opção: ");

            // Validação de entrada para a opção do menu
            while (!scanner.hasNextInt()) {
                System.out.println("Entrada inválida. Por favor, digite um número.");
                scanner.next(); // Descarta a entrada inválida
                System.out.print("Escolha uma opção: ");
            }
            opcao = scanner.nextInt();
            scanner.nextLine(); // Consumir a quebra de linha após nextInt()

            switch (opcao) {
                case 1:
                    cadastroRespostas.criarArquivoDisciplina(scanner);
                    break;
                case 2:
                    cadastroRespostas.gerarResultado(scanner);
                    break;
                case 0:
                    System.out.println("Saindo do programa. Até mais!");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
                    break;
            }
        } while (opcao != 0);

        scanner.close(); // É importante fechar o Scanner ao final
    }
}