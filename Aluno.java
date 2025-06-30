public class Aluno {
    private String nomeAluno;
    private String respostas;
    private int nota;

    public Aluno(String nomeAluno, String respostas, int nota) {
        this.nomeAluno = nomeAluno;
        this.respostas = respostas;
        this.nota = nota;
    }

    public String getNomeAluno() {
        return nomeAluno;
    }

    public String getRespostas() {
        return respostas;
    }

    public void setNomeAluno(String nomeAluno) {
        this.nomeAluno = nomeAluno;
    }

    public void setRespostas(String respostas) {
        this.respostas = respostas;
    }

    public int getNota() {
        return nota;
    }

    public void setNota(int nota) {
        this.nota = nota;
    }
}
