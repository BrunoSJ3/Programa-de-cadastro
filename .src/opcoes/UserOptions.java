import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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
        scanner.nextLine(); // Add this line to consume the newline character
        System.out.print("Data de nascimento (dd/MM/yyyy): ");
        String dataNascimento = scanner.nextLine();

        try {
            LocalDate dataNascimentoLocalDate = LocalDate.parse(dataNascimento,
                    DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            int idade = calcularIdade(dataNascimentoLocalDate);

            UserConcrete user = new UserConcrete(idCounter++, nome, sobrenome, genero, dataNascimentoLocalDate, idade);
            pessoas.add(user);
            salvarPessoas();
            System.out.println("Pessoa cadastrada com sucesso!");
        } catch (DateTimeParseException e) {
            System.out.println("Data de nascimento inválida. Por favor, digite no formato dd/MM/yyyy.");
        }
    }

    protected static void listarPessoas() {
        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(";");
                if (userData.length >= 6) { // Check if the array has at least 6 elements
                    System.out.print("ID: " + userData[0] + " ");
                    System.out.print("/Nome: " + userData[1] + " ");
                    System.out.print("/Sobrenome: " + userData[2] + " ");
                    System.out.print("/Gênero: " + userData[3] + " ");
                    System.out.print("/Data de Nascimento: " + userData[4] + " ");
                    System.out.print("/Idade: " + userData[5] + " ");
                    System.out.println();
                } else {
                    System.out.println("Erro ao carregar pessoa: linha inválida");
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler arquivo: " + e.getMessage());
        }
    }

    protected static void pesquisarPessoas(Scanner scanner) {
        System.out.print("Digite o nome ou sobrenome da pessoa a ser pesquisada: ");
        String searchQuery = scanner.nextLine();

        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(";");
                if (userData[1].contains(searchQuery) || userData[2].contains(searchQuery)) {
                    System.out.println("ID: " + userData[0]);
                    System.out.println("Nome: " + userData[1]);
                    System.out.println("Sobrenome: " + userData[2]);
                    System.out.println("Gênero: " + userData[3]);
                    System.out.println("Data de Nascimento: " + userData[4]);
                    System.out.println("Idade: " + userData[5]);
                    System.out.println();
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler arquivo: " + e.getMessage());
        }
    }

    protected static void alterarPessoa(Scanner scanner) {
        System.out.print("Digite o ID da pessoa a ser alterada: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline left-over

        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            boolean found = false;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(";");
                if (Integer.parseInt(userData[0]) == id) {
                    found = true;
                    System.out.print("Novo nome: ");
                    String novoNome = scanner.nextLine();
                    System.out.print("Novo sobrenome: ");
                    String novoSobrenome = scanner.nextLine();
                    System.out.print("Novo gênero (M/F): ");
                    char novoGenero;
                    while (true) {
                        String generoStr = scanner.next();
                        if (generoStr.equalsIgnoreCase("M") || generoStr.equalsIgnoreCase("F")) {
                            novoGenero = generoStr.charAt(0);
                            break;
                        } else {
                            System.out.print("Gênero inválido. Por favor, digite M ou F: ");
                        }
                    }
                    scanner.nextLine(); // Consume newline left-over
                    System.out.print("Nova data de nascimento (dd/MM/yyyy): ");
                    String novaDataNascimentoStr = scanner.nextLine();
                    LocalDate novaDataNascimento = LocalDate.parse(novaDataNascimentoStr,
                            DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    int novaIdade = calcularIdade(novaDataNascimento);

                    // Update the user data
                    userData[1] = novoNome;
                    userData[2] = novoSobrenome;
                    userData[3] = String.valueOf(novoGenero);
                    userData[4] = novaDataNascimento.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    userData[5] = String.valueOf(novaIdade);

                    // Write the updated user data back to the file
                    try (PrintWriter writer = new PrintWriter("users.txt", "UTF-8")) {
                        writer.println(userData[0] + ";" + userData[1] + ";" + userData[2] + ";" + userData[3] + ";"
                                + userData[4] + ";" + userData[5]);
                        System.out.println("Usuário alterado com sucesso!");
                    } catch (IOException e) {
                        System.out.println("Erro ao gravar usuário: " + e.getMessage());
                    }
                    break;
                }
            }
            if (!found) {
                System.out.println("Usuário não encontrado.");
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler arquivo: " + e.getMessage());
        }
    }

    protected static void excluirPessoa(Scanner scanner) {
        System.out.print("Digite o ID da pessoa a ser excluída: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline left-over

        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            boolean found = false;
            List<String> userDataList = new ArrayList<>();
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(";");
                if (Integer.parseInt(userData[0]) != id) {
                    userDataList.add(line);
                } else {
                    found = true;
                }
            }
            if (found) {
                try (PrintWriter writer = new PrintWriter("users.txt", "UTF-8")) {
                    for (String userDataStr : userDataList) {
                        writer.println(userDataStr);
                    }
                    System.out.println("Usuário excluído com sucesso!");
                } catch (IOException e) {
                    System.out.println("Erro ao gravar usuário: " + e.getMessage());
                }
            } else {
                System.out.println("Usuário não encontrado.");
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler arquivo: " + e.getMessage());
        }
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
        Scanner scanner = new Scanner(System.in);

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
        scanner.nextLine(); // Consume newline left-over

        System.out.print("Data de nascimento (dd/MM/yyyy): ");
        String dataNascimentoStr = scanner.nextLine();

        try {
            LocalDate dataNascimento = LocalDate.parse(dataNascimentoStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            int idade = calcularIdade(dataNascimento);

            // Generate a unique id for the new user
            int id = idCounter++;

            // Create a new User object
            User user = new User(id, nome, sobrenome, genero, dataNascimento, idade);

            // Save the user to a text file
            try (PrintWriter writer = new PrintWriter("users.txt", "UTF-8")) {
                writer.println(id + ";" + user.getNome() + ";" + user.getSobrenome() + ";" + user.getGenero() + ";"
                        + dataNascimento.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ";" + user.getIdade());
                System.out.println("Usuário salvo com sucesso!");
            } catch (IOException e) {
                System.out.println("Erro ao salvar usuário: " + e.getMessage());
            }
        } catch (DateTimeParseException e) {
            System.out.println("Data de nascimento inválida. Por favor, digite no formato dd/MM/yyyy.");
        }
    }

    public static void gravarDados(User user) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("users.txt", true))) {
            writer.println(user.getId() + ";" + user.getNome() + ";" + user.getSobrenome() + ";" + user.getGenero()
                    + ";" + user.getDataNascimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ";"
                    + user.getIdade());
            System.out.println("Usuário salvo com sucesso!");
        } catch (IOException e) {
            System.out.println("Erro ao salvar usuário: " + e.getMessage());
        }
    }

    protected static void salvarPessoas() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("users.txt"))) {
            for (User user : pessoas) {
                writer.write(user.getId() + ";" + user.getNome() + ";" + user.getSobrenome() + ";" + user.getGenero()
                        + ";" + user.getDataNascimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ";"
                        + user.getIdade());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar pessoas: " + e.getMessage());
        }
    }

    protected static int calcularIdade(LocalDate dataNascimento)

    {
        LocalDate dataAtual = LocalDate.now();
        Period periodo = Period.between(dataNascimento, dataAtual);
        return periodo.getYears();
    }

}
