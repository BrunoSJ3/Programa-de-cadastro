import java.time.LocalDate;

public class UserConcrete extends User {
    public UserConcrete(int id, String nome, String sobrenome, char genero, LocalDate dataNascimento, int idade) {
        super(id, nome, sobrenome, genero, dataNascimento, idade);
    }

}
