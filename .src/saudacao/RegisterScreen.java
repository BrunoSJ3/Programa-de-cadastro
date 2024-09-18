import java.util.Scanner;

public class RegisterScreen extends UserOptions {

    public static void main(String[] args) 
    
    {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Bem-vindo ao sistema de cadastro de pessoas!");

        while (true) {
            System.out.println("Menu de opções:");
            System.out.println("a. Cadastrar");
            System.out.println("b. Listar");
            System.out.println("c. Pesquisar");
            System.out.println("d. Alterar");
            System.out.println("e. Excluir");
            System.out.println("f. Gravar");
            System.out.println("g. Sair");

            String opcao = scanner.nextLine();
            carregarPessoas();

            switch (opcao) {
                case "a":
                    cadastrarPessoas(scanner);
                    break;
                case "b":
                    listarPessoas();
                    break;
                case "c":
                    pesquisarPessoas(scanner);
                    break;
                case "d":
                    alterarPessoa(scanner);
                    break;
                case "e":
                    excluirPessoa(scanner);
                    break;
                case "f":
                    gravarDados();
                    break;
                case "g":
                    System.out.println("Saindo do sistema...");
                    return;
                default:
                    System.out.println("Voltando a tela de opções");
            }
        }
    }

}
