import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;

/*
Cliente bancario RMI.
*/
public class BankClient {

    private static final String HOST = "localhost";
    private static final int PUERTO_RMI = 1099;
    private static final String NOMBRE_SERVICIO = "BankService";

    public static void main(String[] args) {

        System.out.println("+==========================================+");
        System.out.println("|        CLIENTE BANCARIO RMI              |");
        System.out.println("+==========================================+");
        System.out.printf("| Conectando a %s:%d ...%n", HOST, PUERTO_RMI);

        BankService banco;
        try {
            Registry registro = LocateRegistry.getRegistry(HOST, PUERTO_RMI);
            banco = (BankService) registro.lookup(NOMBRE_SERVICIO);

            System.out.println("| Conexión establecida con el servidor.    |");
            System.out.println("+==========================================+");

        } catch (RemoteException e) {
            System.err.println("[ERROR] No se pudo conectar al servidor.");
            return;
        } catch (NotBoundException e) {
            System.err.println("[ERROR] El servicio no está registrado.");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        boolean ejecutando = true;

        while (ejecutando) {
            mostrarMenu();
            System.out.print("Selecciona una opción: ");
            String entrada = scanner.nextLine().trim();

            switch (entrada) {
                case "1":
                    crearCuenta(banco, scanner);
                    break;
                case "2":
                    realizarDeposito(banco, scanner);
                    break;
                case "3":
                    realizarRetiro(banco, scanner);
                    break;
                case "4":
                    consultarSaldo(banco, scanner);
                    break;
                case "5":
                    ejecutando = false;
                    System.out.println("\nSesión finalizada. Hasta luego.");
                    break;
                default:
                    System.out.println("[!] Opción no válida.\n");
            }
        }

        scanner.close();
    }

    private static void crearCuenta(BankService banco, Scanner scanner) {
        System.out.println("\n--- CREAR CUENTA ---");

        System.out.print("Nombre del titular: ");
        String nombre = scanner.nextLine().trim();

        double saldoInicial = leerDoublePositivo(scanner, "Saldo inicial ($): ");

        try {
            BankAccount cuenta = banco.crearCuenta(nombre, saldoInicial);

            System.out.println("\n[OK] Cuenta creada:");
            System.out.println("     Número : " + cuenta.getNumeroCuenta());
            System.out.println("     Titular: " + cuenta.getNombreTitular());
            System.out.printf ("     Saldo  : $%.2f%n", cuenta.getSaldo());

        } catch (RemoteException e) {
            System.err.println("[ERROR] Comunicación fallida.");
        }
        System.out.println();
    }

    private static void realizarDeposito(BankService banco, Scanner scanner) {
        System.out.println("\n--- DEPÓSITO ---");

        System.out.print("Número de cuenta: ");
        String numeroCuenta = scanner.nextLine().trim();

        double monto = leerDoublePositivo(scanner, "Monto a depositar ($): ");

        try {
            BankAccount cuenta = banco.depositar(numeroCuenta, monto);

            System.out.println("\n[OK] Depósito realizado:");
            System.out.printf ("     Cuenta  : %s%n", cuenta.getNumeroCuenta());
            System.out.printf ("     Titular : %s%n", cuenta.getNombreTitular());
            System.out.printf ("     Saldo   : $%.2f%n", cuenta.getSaldo());

        } catch (BankException e) {
            System.err.println("[!] " + e.getMessage());
        } catch (RemoteException e) {
            System.err.println("[ERROR] Comunicación fallida.");
        }
        System.out.println();
    }

    private static void realizarRetiro(BankService banco, Scanner scanner) {
        System.out.println("\n--- RETIRO ---");

        System.out.print("Número de cuenta: ");
        String numeroCuenta = scanner.nextLine().trim();

        double monto = leerDoublePositivo(scanner, "Monto a retirar ($): ");

        try {
            BankAccount cuenta = banco.retirar(numeroCuenta, monto);

            System.out.println("\n[OK] Retiro realizado:");
            System.out.printf ("     Cuenta  : %s%n", cuenta.getNumeroCuenta());
            System.out.printf ("     Titular : %s%n", cuenta.getNombreTitular());
            System.out.printf ("     Saldo   : $%.2f%n", cuenta.getSaldo());

        } catch (BankException e) {
            System.err.println("[!] " + e.getMessage());
        } catch (RemoteException e) {
            System.err.println("[ERROR] Comunicación fallida.");
        }
        System.out.println();
    }

    private static void consultarSaldo(BankService banco, Scanner scanner) {
        System.out.println("\n--- CONSULTA DE SALDO ---");

        System.out.print("Número de cuenta: ");
        String numeroCuenta = scanner.nextLine().trim();

        try {
            BankAccount cuenta = banco.obtenerCuenta(numeroCuenta);

            System.out.println("\n[OK] Información de cuenta:");
            System.out.println("     Número  : " + cuenta.getNumeroCuenta());
            System.out.println("     Titular : " + cuenta.getNombreTitular());
            System.out.printf ("     Saldo   : $%.2f%n", cuenta.getSaldo());

        } catch (BankException e) {
            System.err.println("[!] " + e.getMessage());
        } catch (RemoteException e) {
            System.err.println("[ERROR] Comunicación fallida.");
        }
        System.out.println();
    }

    private static void mostrarMenu() {
        System.out.println("==========================================");
        System.out.println("  MENÚ PRINCIPAL");
        System.out.println("  1. Crear cuenta");
        System.out.println("  2. Depositar");
        System.out.println("  3. Retirar");
        System.out.println("  4. Consultar saldo");
        System.out.println("  5. Salir");
        System.out.println("==========================================");
    }

    private static double leerDoublePositivo(Scanner scanner, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                double valor = Double.parseDouble(scanner.nextLine().trim());
                if (valor > 0) return valor;
                System.out.println("[!] El monto debe ser mayor a cero.");
            } catch (NumberFormatException e) {
                System.out.println("[!] Ingresa un número válido.");
            }
        }
    }
}