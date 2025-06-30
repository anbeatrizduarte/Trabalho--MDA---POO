import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class GerenciadorResultados {

    public void calcularNotasAlunos(List<Aluno> listaAlunos, String gabarito) {
        if (gabarito == null || gabarito.length() != 10) {
            System.err.println("Gabarito inválido. As notas não podem ser calculadas.");
            return;
        }

        for (Aluno aluno : listaAlunos) {
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

    public void ordenarEsalvarResultados(String nomeDisciplina, List<Aluno> listaAlunos) {
        // Ordenar por nome e salvar
        List<Aluno> alunoOrdenadoNome = new ArrayList<>(listaAlunos);
        alunoOrdenadoNome.sort(Comparator.comparing(a -> a.getNomeAluno().toLowerCase()));

        String dirOrdemAlfabetica = "RespostasAlunos/Ordem Alfábetica";
        File diretorioAlfabetico = new File(dirOrdemAlfabetica);
        if (!diretorioAlfabetico.exists()) {
            diretorioAlfabetico.mkdirs();
        }
        String nomeArquivoAlfabetico = dirOrdemAlfabetica + "/" + nomeDisciplina + "_OrdemAlfabetica.txt";
        salvarArquivoNotas(nomeArquivoAlfabetico, alunoOrdenadoNome, false);

        // Ordenar por nota e salvar
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
            for (Aluno aluno : lista) { // Usando enhanced for loop
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
}