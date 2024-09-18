import java.time.LocalDate;

class User

{
    private int id;
    private String nome;
    private String sobrenome;
    private char genero;
    private LocalDate dataNascimento;
    private int idade;

    public User(int id, String nome, String sobrenome, char genero, LocalDate dataNascimento, int idade) {
        this.id = id;
        this.nome = nome;
        this.sobrenome = sobrenome;
        this.genero = genero;
        this.dataNascimento = dataNascimento;
        this.idade = idade;
    }

    public int getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public char getGenero() {
        return genero;
    }

    public LocalDate getDataNascimento() {
        return dataNascimento;
    }

    public int getIdade() {
        return idade;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public void setGenero(char genero) {
        this.genero = genero;
    }

    public void setDataNascimento(LocalDate dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public void setIdade(int idade) {
        this.idade = idade;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Nome: " + nome + ", Sobrenome: " + sobrenome + ", Gênero: " + genero
                + ", Data de nascimento: " + dataNascimento + ", Idade: " + idade;
    }
}