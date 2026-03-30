import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/*
 Servidor bancario RMI.
 */
public class BankServer extends UnicastRemoteObject implements BankService {

    private static final long serialVersionUID = 1L;

    // Puerto RMI Registry (estándar: 1099)
    private static final int PUERTO_RMI = 1099;

    // Nombre con el que el servidor se registra en el RMI Registry
    private static final String NOMBRE_SERVICIO = "BankService";

    private final Map<String, BankAccount> cuentas = new HashMap<>();

    // Contador atómico para generar números de cuenta únicos
    private final AtomicInteger contadorCuentas = new AtomicInteger(1000);

    protected BankServer() throws RemoteException {
        super();
    }

    @Override
    public synchronized BankAccount crearCuenta(String nombrePropietario, double saldoInicial)
            throws RemoteException {

        String numeroCuenta = "ACC-" + contadorCuentas.getAndIncrement();

        BankAccount cuenta = new BankAccount(numeroCuenta, nombrePropietario, saldoInicial);
        cuentas.put(numeroCuenta, cuenta);

        System.out.printf("[SERVIDOR] Cuenta creada -> %s%n", cuenta);
        return cuenta;
    }

    @Override
    public synchronized BankAccount depositar(String numeroCuenta, double monto)
            throws RemoteException, BankException {

        if (monto <= 0) {
            throw new BankException("El monto del depósito debe ser mayor a cero.");
        }

        BankAccount cuenta = buscarCuenta(numeroCuenta);

        double saldoAnterior = cuenta.getSaldo();
        cuenta.depositar(monto);

        System.out.printf("[SERVIDOR] Depósito en %s | Antes: $%.2f | Después: $%.2f%n",
                numeroCuenta, saldoAnterior, cuenta.getSaldo());

        return cuenta;
    }

    @Override
    public synchronized BankAccount retirar(String numeroCuenta, double monto)
            throws RemoteException, BankException {

        if (monto <= 0) {
            throw new BankException("El monto del retiro debe ser mayor a cero.");
        }

        BankAccount cuenta = buscarCuenta(numeroCuenta);

        if (cuenta.getSaldo() < monto) {
            throw new BankException(String.format(
                    "Saldo insuficiente. Saldo actual: $%.2f | Monto solicitado: $%.2f",
                    cuenta.getSaldo(), monto));
        }

        double saldoAnterior = cuenta.getSaldo();
        cuenta.retirar(monto);

        System.out.printf("[SERVIDOR] Retiro en %s | Antes: $%.2f | Después: $%.2f%n",
                numeroCuenta, saldoAnterior, cuenta.getSaldo());

        return cuenta;
    }

    @Override
    public synchronized BankAccount obtenerCuenta(String numeroCuenta)
            throws RemoteException, BankException {
        return buscarCuenta(numeroCuenta);
    }

    private BankAccount buscarCuenta(String numeroCuenta) throws BankException {
        BankAccount cuenta = cuentas.get(numeroCuenta);
        if (cuenta == null) {
            throw new BankException("Cuenta no encontrada: " + numeroCuenta);
        }
        return cuenta;
    }

    public static void main(String[] args) {
        System.out.println("+==========================================+");
        System.out.println("|        SERVIDOR BANCARIO RMI             |");
        System.out.println("+==========================================+");

        try {
            BankServer servidor = new BankServer();

            Registry registro = LocateRegistry.createRegistry(PUERTO_RMI);

            registro.rebind(NOMBRE_SERVICIO, servidor);

            System.out.printf("| Servicio '%s' registrado en puerto %d%n",
                    NOMBRE_SERVICIO, PUERTO_RMI);
            System.out.println("| Esperando conexiones de clientes...");
            System.out.println("+==========================================+");

        } catch (RemoteException e) {
            System.err.println("[ERROR] No se pudo iniciar el servidor RMI: " + e.getMessage());
            e.printStackTrace();
        }
    }
}