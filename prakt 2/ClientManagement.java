import java.util.Scanner;

public class ClientManagement {
    public static void main(String[] args) {
        System.out.println("IP Adresse: ");
        Scanner scanner = new Scanner(System.in);
        String ipAddress = scanner.nextLine();
        String[] products = {"Milch", "Salami", "Wasser", "Eier"};
        for(String x : products){
            new Client(x, ipAddress).start();
        }
    }
}
