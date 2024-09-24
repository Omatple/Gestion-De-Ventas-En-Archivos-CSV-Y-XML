import java.time.LocalDate;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Ventas ventas = new Ventas();
        /*
         * ventas.soloTranscribir();
         */
        ventas.comenzar();
        resumenEstadisticasAnual(ventas, "2024");
        ventas.terminar();
    }

    private static void resumenEstadisticasAnual(Ventas ventas, String anio) {
        System.out.println("-----------------------");
        System.out.printf("RESUMEN DE VENTAS AÑO %s%n", anio);
        System.out.println("-----------------------");
        String[] stringMesesAnio = {"Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"};
        Map<String, String[]> mapaEstadisticas;
        String mesConMasVentas = "";
        int cantidadVendidaMejorMes = 0;
        for (int i = 0; i < stringMesesAnio.length; i++) {
            String productoMasVendido = "";
            int cantidadVendida = 0;
            String productoMasIngresos = "";
            float cantidadIngresos = 0;
            if (i < 9) {
                mapaEstadisticas = ventas.estadisticasVentas(LocalDate.parse(String.format("01/0%s/%s", i + 1, anio), Ventas.FORMATO_FECHA));
            } else {
                mapaEstadisticas = ventas.estadisticasVentas(LocalDate.parse(String.format("01/%s/%s", i + 1, anio), Ventas.FORMATO_FECHA));
            }
            int auxiliarSumaTotalVentas = 0;
            for (Map.Entry<String, String[]> entry : mapaEstadisticas.entrySet()) {
                int indice = 0;
                if (Integer.parseInt(entry.getValue()[indice]) >= cantidadVendida) {
                    cantidadVendida = Integer.parseInt(entry.getValue()[indice++]);
                    productoMasVendido = entry.getKey();
                }
                if (Float.parseFloat(entry.getValue()[indice]) >= cantidadIngresos) {
                    cantidadIngresos = Float.parseFloat(entry.getValue()[indice]);
                    productoMasIngresos = entry.getKey();
                }
                auxiliarSumaTotalVentas += Integer.parseInt(entry.getValue()[0]);
            }
            if (auxiliarSumaTotalVentas >= cantidadVendidaMejorMes) {
                cantidadVendidaMejorMes = auxiliarSumaTotalVentas;
                mesConMasVentas = stringMesesAnio[i];
            }
            if (cantidadVendida == 0) {
                System.out.printf("Mes: %s%n    No se vendió ningún producto este mes.%n%n", stringMesesAnio[i]);
            } else {
                System.out.printf("Mes: %s%n    Producto mas vendido: %s, Cantidad vendida: %s%n    Producto que ha generado mas ingresos: %s, Cantidad de ingresos: %.2f€%n%n", stringMesesAnio[i], productoMasVendido, cantidadVendida, productoMasIngresos, cantidadIngresos);
            }
        }
        if (cantidadVendidaMejorMes == 0) {
            System.out.printf("En el año %s no se ha vendido ningún producto.%n", anio);
        } else {
            System.out.printf("El mes del año %s en el que mas ventas se han realizado ha sido el mes de %s con un total de %s ventas.%n", anio, mesConMasVentas, cantidadVendidaMejorMes);
        }
        System.out.println("--------------------------------------------------------------------------------------------------------------");
    }
}