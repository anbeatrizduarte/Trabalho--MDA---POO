// CadastroRespostas.java
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.io.File;

public class CadastroRespostas extends Base {
    private String nomeDisciplina;

    public CadastroRespostas() {
        super();
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
            listaAlunos.add(new Aluno(nomeAluno, respostasAluno, 0));

            System.out.print("\nDeseja adicionar outro aluno? (S/N): ");
            String opcaoAdicionar = sc.nextLine();
            if (opcaoAdicionar.equalsIgnoreCase("N")) {
                adicionarAluno = false;
            }
        }

        salvarArquivo(nomeArquivo);
        visualizarArquivo(nomeArquivo);
    }

    public void gerarResultado(Scanner sc) {
        System.out.print("\n=== Gerar Resultado da Disciplina ===\n");
        System.out.print("Digite o nome da disciplina para gerar resultados: ");
        nomeDisciplina = sc.nextLine();

        String nomeArquivoDisciplina = "RespostasAlunos/" + nomeDisciplina + ".txt";

        lerArquivo(nomeArquivoDisciplina);

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

        calcularNotasAlunos(listaAlunos, gabarito);
        ordenarArquivos(nomeDisciplina, listaAlunos);
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

    public void ordenarArquivos(String nomeDisciplina, List<Aluno> listaAlunos) {
        List<Aluno> alunoOrdenadoNome = new ArrayList<>(listaAlunos);
        alunoOrdenadoNome.sort(Comparator.comparing(a -> a.getNomeAluno().toLowerCase()));

        String dirOrdemAlfabetica = "RespostasAlunos/Ordem Alfábetica";
        File diretorioAlfabetico = new File(dirOrdemAlfabetica);
        if (!diretorioAlfabetico.exists()) {
            diretorioAlfabetico.mkdirs();
        }
        String nomeArquivoAlfabetico = dirOrdemAlfabetica + "/" + nomeDisciplina + "_OrdemAlfabetica.txt";
        salvarArquivoNotas(nomeArquivoAlfabetico, alunoOrdenadoNome, false);

        List<Aluno> alunoOrdenadoNota = new ArrayList<>(listaAlunos);
        alunoOrdenadoNota.sort(Comparator.comparing(Aluno::getNota).reversed());

        String dirOrdemNota = "RespostasAlunos/Ordem Nota";
        File diretorioNotas = new File(dirOrdemNota);
        if (!diretorioNotas.exists()) {
            diretorioNotas.mkdirs();
        }
        String arquivoNota = dirOrdemNota + "/" + nomeDisciplina + "_OrdemPorNota.txt";
        salvarArquivoNotas(arquivoNota, alunoOrdenadoNota, true);

    }

    private void salvarArquivoNotas(String caminho, List<Aluno> lista, boolean mostrarMedia) {
        try (BufferedWriter arqNotas = new BufferedWriter(new FileWriter(caminho))) {
            int somaNotas = 0;
            for (int i = 0; i < lista.size(); i++) {
                Aluno aluno = lista.get(i);
                arqNotas.write(aluno.getNomeAluno() + ": " + aluno.getNota());
                arqNotas.newLine();
                somaNotas += aluno.getNota();
            }
            if (mostrarMedia && !lista.isEmpty()) {
                double media = (double) somaNotas / lista.size();
                arqNotas.write("Média da turma: " + String.format("%.2f", media));
                arqNotas.newLine();
            }
            System.out.println("Arquivo de notas salvo: " + caminho);
        } catch (IOException e) {
            System.err.println("Erro ao salvar arquivo de notas " + caminho + ": " + e.getMessage());
        }
    }

    public void calcularNotasAlunos(List<Aluno> listaAlunos, String gabarito) {
        if (gabarito == null || gabarito.length() != 10) {
            System.err.println("Gabarito inválido. As notas não podem ser calculadas.");
            return;
        }

        for (int i = 0; i < listaAlunos.size(); i++) {
            Aluno aluno = listaAlunos.get(i);
            String respostasAlunos = aluno.getRespostas();

            if (respostasAlunos == null || respostasAlunos.length() != 10 || !respostasAlunos.matches("[VF]{10}")) {
                System.out.println("Atenção: As respostas do aluno '" + aluno.getNomeAluno() + "' são inválidas ('" + respostasAlunos + "'). Nota atribuída: 0.");
                aluno.setNota(0);
                continue;
            }

            int acertos = 0;
            for (int j = 0; j < respostasAlunos.length(); j++) {
                if (respostasAlunos.charAt(j) == gabarito.charAt(j)) {
                    acertos++;
                }
            }
            aluno.setNota(acertos);
        }
    }
}