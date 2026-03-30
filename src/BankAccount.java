import java.io.Serializable;

/**
 Clase que representa una cuenta bancaria.
 */
public class BankAccount implements Serializable {

    private static final long serialVersionUID = 1L;

    private final String numeroCuenta; // Número de cuenta (inmutable)
    private final String nombreTitular; // Nombre del titular
    private double saldo; // Saldo actual (mutable)


    public BankAccount(String numeroCuenta, String nombreTitular, double saldoInicial) {
        this.numeroCuenta  = numeroCuenta;
        this.nombreTitular = nombreTitular;
        this.saldo         = saldoInicial;
    }

    public String getNumeroCuenta() { return numeroCuenta; }
    public String getNombreTitular() { return nombreTitular; }
    public double getSaldo() { return saldo; }

    public void depositar(double monto) {
        this.saldo += monto;
    }

    public void retirar(double monto) {
        this.saldo -= monto;
    }

    @Override
    public String toString() {
        return String.format("Cuenta: %s | Titular: %s | Saldo: $%.2f",
                numeroCuenta, nombreTitular, saldo);
    }
}