import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class Principal {
    public static void main(String[] args) {

        String siglaMoneda = "";
        String monedaCambio = "";

        System.out.println("Bienvenido al conversor de monedas");
        System.out.println("1) Dolar a Pesos Argentinos");
        System.out.println("2) Peso Argentinos a Dolar");
        System.out.println("3) Dolar a Real Brasileño");
        System.out.println("4) Real Brasileño a Dolar");
        System.out.println("5) Salir");

        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese una opción: ");
        String opcion = scanner.nextLine();

        if (opcion.equals("1")) {
            siglaMoneda = "USD";
            monedaCambio = "ARS";
        } else if (opcion.equals("2")) {
            siglaMoneda = "ARS";
            monedaCambio = "USD";
        } else if (opcion.equals("3")) {
            siglaMoneda = "USD";
            monedaCambio = "BRL";
        } else if (opcion.equals("4")) {
            siglaMoneda = "BRL";
            monedaCambio = "USD";
        } else if (opcion.equals("5")) {
            System.out.println("Gracias por su visita");
            scanner.close();
            return;
        } else {
            System.out.println("Opción no válida.");
            scanner.close();
            return;
        }

        System.out.println("Ingrese el monto a convertir");
        double monto = Double.parseDouble(scanner.nextLine());

        if (!siglaMoneda.isEmpty()) {
            URI direccion = URI.create("https://v6.exchangerate-api.com/v6/77cbba8054b6263519a5b3f2/latest/" + siglaMoneda);
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(direccion)
                    .build();

            try {
                HttpResponse<String> response = client
                        .send(request, HttpResponse.BodyHandlers.ofString());
                JsonObject jsonResponse = JsonParser.parseString(response.body()).getAsJsonObject();
                JsonObject rates = jsonResponse.getAsJsonObject("conversion_rates");
                double tasaCambio = rates.get(monedaCambio).getAsDouble();

                double resultado = monto * tasaCambio;
                System.out.println("El monto convertido es: " + resultado + " " + monedaCambio);

            } catch (IOException e) {
                throw new RuntimeException("Error de entrada/salida: " + e.getMessage(), e);
            } catch (InterruptedException e) {
                throw new RuntimeException("La solicitud fue interrumpida: " + e.getMessage(), e);
            }
        } else {
            System.out.println("Opción no válida.");
        }

        scanner.close();
    }
}
