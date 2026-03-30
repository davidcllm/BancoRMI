import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 Interfaz remota del servicio bancario.
 Esta interfaz es la llave entre cliente y servidor:
 -El servidor la implementa con la lógica real.
 -El cliente solo conoce esta interfaz; nunca ve la implementación.
 */
public interface BankService extends Remote {

    BankAccount crearCuenta(String nombrePropietario, double saldoInicial)
            throws RemoteException;

    BankAccount depositar(String numeroCuenta, double monto)
            throws RemoteException, BankException;

    BankAccount retirar(String numeroCuenta, double monto)
            throws RemoteException, BankException;

    BankAccount obtenerCuenta(String numeroCuenta)
            throws RemoteException, BankException;
}