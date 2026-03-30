# Banco RMI - Guía de uso

## Requisitos
- JDK 8 o superior (`java -version` para verificar)

## Compilación
```bash
javac -source 8 -target 8 *.java
```

## Ejecución

### Terminal 1 – Servidor
```bash
java BankServer
```

### Terminal 2, 3, … – Clientes (uno por terminal)
```bash
java BankClient
```
