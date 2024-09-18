import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

abstract class UserOptions

{
    private static List<User> pessoas = new ArrayList<>();
    private static int idCounter = 1;

    protected static void cadastrarPessoas(Scanner scanner) {
        System.out.println("Cadastro de pessoa:");
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Sobrenome: ");
        String sobrenome = scanner.nextLine();
        System.out.print("Gênero (M/F): ");
        char genero;
        while (true) {
            String generoStr = scanner.next();
            if (generoStr.equalsIgnoreCase("M") || generoStr.equalsIgnoreCase("F")) {
                genero = generoStr.charAt(0);
                break;
            } else {
                System.out.print("Gênero inválido. Por favor, digite M ou F: ");
            }
        }
        scanner.nextLine(); // consome a nova linha
        System.out.print("Data de nascimento (dd/MM/yyyy): ");
        String dataNascimento = scanner.nextLine();

        try {
            LocalDate dataNascimentoLocalDate = LocalDate.parse(dataNascimento,
                    DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            int idade = calcularIdade(dataNascimentoLocalDate);

            User pessoa = new User(idCounter++, nome, sobrenome, genero, dataNascimentoLocalDate, idade);
            pessoas.add(pessoa);
            System.out.println("Pessoa cadastrada com sucesso!");
        } catch (DateTimeParseException e) {
            System.out.println("Data de nascimento inválida. Por favor, digite no formato dd/MM/yyyy.");
        }
    }

    protected static void listarPessoas()

    {
        System.out.println("Lista de pessoas:");
        for (User pessoa : pessoas) {
            System.out.println(pessoa.toString());
        }
    }

    protected static void pesquisarPessoas(Scanner scanner) {
        System.out.print("Digite o nome ou sobrenome da pessoa a pesquisar: ");
        String pesquisa = scanner.nextLine();

        for (User pessoa : pessoas) {
            if (pessoa.getNome().contains(pesquisa) || pessoa.getSobrenome().contains(pesquisa)) {
                System.out.println(pessoa.toString());
            }
        }
    }

    protected static void alterarPessoa(Scanner scanner) {
        System.out.print("Digite o ID da pessoa a alterar: ");
        int id = scanner.nextInt();

        for (User pessoa : pessoas) {
            if (pessoa.getId() == id) {
                System.out.print("Novo nome: ");
                String novoNome = scanner.next();
                System.out.print("Novo sobrenome: ");
                String novoSobrenome = scanner.next();
                System.out.print("Novo gênero (M/F): ");
                char novoGenero = scanner.next().charAt(0);
                System.out.print("Nova data de nascimento (dd/MM/yyyy): ");
                String novaDataNascimento = scanner.next();

                LocalDate novaDataNascimentoLocalDate = LocalDate.parse(novaDataNascimento,
                        DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                int novaIdade = calcularIdade(novaDataNascimentoLocalDate);

                pessoa.setNome(novoNome);
                pessoa.setSobrenome(novoSobrenome);
                pessoa.setGenero(novoGenero);
                pessoa.setDataNascimento(novaDataNascimentoLocalDate);
                pessoa.setIdade(novaIdade);

                System.out.println("Pessoa alterada com sucesso!");
                return;
            }
        }

        System.out.println("Pessoa não encontrada.");
    }

    protected static void excluirPessoa(Scanner scanner) {
        System.out.print("Digite o ID da pessoa a excluir: ");
        int id = scanner.nextInt();

        for (User pessoa : pessoas) {
            if (pessoa.getId() == id) {
                pessoas.remove(pessoa);
                System.out.println("Pessoa excluída com sucesso!");
                return;
            }
        }

        System.out.println("Pessoa não encontrada.");
    }

    protected static void gravarDados() {
        try (FileWriter writer = new FileWriter("dados.txt")) {
            for (User pessoa : pessoas) {
                writer.write(pessoa.toString() + "\n");
            }
            System.out.println("Dados gravados com sucesso!");
        } catch (IOException e) {
            System.out.println("Erro ao gravar dados: " + e.getMessage());
        }
    }

    protected static void carregarDados() {
        try (BufferedReader reader = new BufferedReader(new FileReader("dados.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0].split(": ")[1]);
                String nome = parts[1].split(": ")[1];
                String sobrenome = parts[2].split(": ")[1];
                char genero = parts[3].split(": ")[1].charAt(0);
                LocalDate dataNascimento = LocalDate.parse(parts[4].split(": ")[1],
                        DateTimeFormatter.ofPattern("yyyy-MM-dd")); // Alterado o formato para yyyy-MM-dd
                int idade = Integer.parseInt(parts[5].split(": ")[1]);

                User pessoa = new User(id, nome, sobrenome, genero, dataNascimento, idade);
                pessoas.add(pessoa);
            }
            System.out.println("Dados carregados com sucesso!");
        } catch (IOException e) {
            System.out.println("Erro ao carregar dados: " + e.getMessage());
        }
    }

    protected static int calcularIdade(LocalDate dataNascimento)

    {
        LocalDate dataAtual = LocalDate.now();
        Period periodo = Period.between(dataNascimento, dataAtual);
        return periodo.getYears();
    }
}
