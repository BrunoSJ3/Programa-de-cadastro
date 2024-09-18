import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

abstract class UserOptions

{
    private static List<User> pessoas = new ArrayList<>();
    private static int idCounter = 1;

    protected static void cadastrarPessoas(Scanner scanner) {
        System.out.println("Cadastro de pessoa:");

        String nome = lerNome(scanner);
        String sobrenome = lerSobrenome(scanner);
        char genero = lerGenero(scanner);
        LocalDate dataNascimento = lerDataNascimento(scanner);
        int idade = calcularIdade(dataNascimento);

        UserConcrete user = new UserConcrete(idCounter++, nome, sobrenome, genero, dataNascimento, idade);
        pessoas.add(user);
        salvarPessoas(pessoas);
        System.out.println("Pessoa cadastrada com sucesso!");
    }

    private static String lerNome(Scanner scanner) {
        System.out.print("Nome: ");
        return scanner.nextLine();
    }

    private static String lerSobrenome(Scanner scanner) {
        System.out.print("Sobrenome: ");
        return scanner.nextLine();
    }

    private static char lerGenero(Scanner scanner) {
        while (true) {
            System.out.print("Gênero (M/F): ");
            String generoStr = scanner.next();
            if (generoStr.equalsIgnoreCase("M") || generoStr.equalsIgnoreCase("F")) {
                scanner.nextLine(); // Consume newline left-over
                return generoStr.charAt(0);
            } else {
                System.out.println("Gênero inválido. Por favor, digite M ou F.");
            }
        }
    }

    private static LocalDate lerDataNascimento(Scanner scanner) {
        while (true) {
            System.out.print("Data de nascimento (dd/MM/yyyy): ");
            String dataNascimentoStr = scanner.nextLine();
            try {
                LocalDate dataNascimento = LocalDate.parse(dataNascimentoStr,
                        DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                if (dataNascimento.isAfter(LocalDate.now())) {
                    System.out.println("Erro: Data de nascimento inválida. Você não pode ter nascido no futuro!");
                } else {
                    return dataNascimento;
                }
            } catch (DateTimeParseException e) {
                System.out.println("Erro: Data de nascimento inválida. Por favor, digite no formato dd/MM/yyyy.");
            }
        }
    }

    protected static void carregarPessoas() {
        List<User> users = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(";");
                if (userData.length >= 6) { // Check if the array has at least 6 elements
                    int id = Integer.parseInt(userData[0]);
                    String nome = userData[1];
                    String sobrenome = userData[2];
                    char genero = userData[3].charAt(0);
                    LocalDate dataNascimento = LocalDate.parse(userData[4], DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                    int idade = calcularIdade(dataNascimento);

                    UserConcrete user = new UserConcrete(id, nome, sobrenome, genero, dataNascimento, idade);
                    users.add(user);
                    if (id > idCounter) {
                        idCounter = id;
                    }
                } else {
                    System.out.println("Erro ao carregar pessoa: linha inválida");
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler arquivo: " + e.getMessage());
        }
        pessoas = users;
        idCounter++; // Increment the idCounter to get the next available ID
    }

    protected static void listarPessoas() {
        try (BufferedReader reader = new BufferedReader(new FileReader("users.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(";");
                if (userData.length >= 6) { // Check if the array has at least 6 elements
                    System.out.println("ID: " + userData[0] + ", Nome: " + userData[1] + ", Sobrenome: " + userData[2]
                            + ", Gênero: " + userData[3] + ", Data de nascimento: " + userData[4] + ", Idade: "
                            + userData[5]);
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
            boolean found = false;
            while ((line = reader.readLine()) != null) {
                String[] userData = line.split(";");
                if (userData[1].contains(searchQuery) || userData[2].contains(searchQuery)) {
                    System.out.println("Aqui está o usuário: ");
                    System.out.println("ID: " + userData[0]);
                    System.out.println("Nome: " + userData[1]);
                    System.out.println("Sobrenome: " + userData[2]);
                    System.out.println("Gênero: " + userData[3]);
                    System.out.println("Data de Nascimento: " + userData[4]);
                    System.out.println("Idade: " + userData[5]);
                    System.out.println();
                    found = true;
                    break; // Sai do loop após encontrar o usuário
                }
            }
            if (!found) {
                System.out.println("Usuário não encontrado ");
            }

        } catch (IOException e) {
            System.out.println("Erro ao ler arquivo: " + e.getMessage());
        }

    }

    protected static void alterarPessoa(Scanner scanner) {
        System.out.print("Digite o ID da pessoa a ser alterada: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline left-over

        int index = -1;
        for (int i = 0; i < pessoas.size(); i++) {
            if (pessoas.get(i).getId() == id) {
                index = i;
                break;
            }
        }

        if (index != -1) {
            User user = pessoas.get(index);
            System.out.print("Novo nome: ");
            String novoNome = scanner.nextLine();
            System.out.print("Novo sobrenome: ");
            String novoSobrenome = scanner.nextLine();
            System.out.print("Novo gênero (M/F): ");
            String generoStr = scanner.next();
            char novoGenero = generoStr.charAt(0);
            scanner.nextLine(); // Consume newline left-over
            System.out.print("Nova data de nascimento (dd/MM/yyyy): ");
            String novaDataNascimentoStr = scanner.nextLine();
            LocalDate novaDataNascimento = LocalDate.parse(novaDataNascimentoStr,
                    DateTimeFormatter.ofPattern("dd/MM/yyyy"));
            int novaIdade = calcularIdade(novaDataNascimento);

            // Update the user data
            user.setNome(novoNome);
            user.setSobrenome(novoSobrenome);
            user.setGenero(novoGenero);
            user.setDataNascimento(novaDataNascimento);
            user.setIdade(novaIdade);

            // Save the updated user data
            try (PrintWriter writer = new PrintWriter("users.txt", "UTF-8")) {
                for (User pessoa : pessoas) {
                    writer.println(pessoa.getId() + ";" + pessoa.getNome() + ";" + pessoa.getSobrenome() + ";"
                            + pessoa.getGenero() + ";"
                            + pessoa.getDataNascimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                            + ";" + pessoa.getIdade());
                }
                System.out.println("Usuário alterado com sucesso!");
            } catch (IOException e) {
                System.out.println("Erro ao salvar usuário: " + e.getMessage());
            }
        } else {
            System.out.println("Usuário não encontrado.");
        }
    }

    protected static void excluirPessoa(Scanner scanner) {
        System.out.print(" Digite o ID da pessoa a ser excluída: ");
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
        try (FileWriter writer = new FileWriter("users.txt")) {
            for (User pessoa : pessoas) {
                writer.write(pessoa.getId() + ";" + pessoa.getNome() + ";" + pessoa.getSobrenome() + ";"
                        + pessoa.getGenero()
                        + ";" + pessoa.getDataNascimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ";"
                        + pessoa.getIdade() + "\n");
            }
            System.out.println("Dados gravados com sucesso!");
        } catch (IOException e) {
            System.out.println("Erro ao gravar dados: " + e.getMessage());
        }
    }

    protected static void carregarDados() {
        System.out.println("Cadastro de pessoa:");
        System.out.print("Nome: ");
        String nome = System.console().readLine();

        System.out.print("Sobrenome: ");
        String sobrenome = System.console().readLine();

        System.out.print("Gênero (M/F): ");
        String generoStr = System.console().readLine();
        char genero;
        while (true) {
            if (generoStr.equalsIgnoreCase("M") || generoStr.equalsIgnoreCase("F")) {
                genero = generoStr.charAt(0);
                break;
            } else {
                System.out.print("Gênero inválido. Por favor, digite M ou F: ");
                generoStr = System.console().readLine();
            }
        }

        System.out.print("Data de nascimento (dd/MM/yyyy): ");
        String dataNascimentoStr = System.console().readLine();

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

    protected static void salvarPessoas(List<User> users) {
        try (PrintWriter writer = new PrintWriter("users.txt")) {
            for (User user : users) {
                writer.println(user.getId() + ";" + user.getNome() + ";" + user.getSobrenome() + ";" + user.getGenero()
                        + ";" + user.getDataNascimento().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + ";"
                        + user.getIdade());
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar pessoa: " + e.getMessage());
        }
    }

    protected static int calcularIdade(LocalDate dataNascimento) {
        try {
            LocalDate dataAtual = LocalDate.now();
            Period periodo = Period.between(dataNascimento, dataAtual);
            return periodo.getYears();
        } catch (DateTimeException e) {
            System.out.println("Erro: Data de nascimento inválida.");
            return -1; // ou outro valor que indique um erro
        }
    }

}
