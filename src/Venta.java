import java.time.LocalDate;
import java.util.Objects;

public record Venta(LocalDate fecha, String producto, int cantidad, float precioUnitario) {

    public Venta {
        validarFecha(fecha);
        validarProducto(producto);
        validarCantidad(cantidad);
        validarPrecioUnitario(precioUnitario);
    }

    private void validarCantidad(int cantidad) {
        if (cantidad < 0) {
            throw new IllegalArgumentException("La cantidad no puede ser negativa.");
        }
    }

    private void validarPrecioUnitario(float precioUnitario) {
        if (precioUnitario <= 0) {
            throw new IllegalArgumentException("El precio unitario no puede ser inferior o igual a 0.");
        }
    }

    private void validarFecha(LocalDate fecha) {
        Objects.requireNonNull(fecha, "La fecha no puede ser nula.");
        if (fecha.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("La fecha no puede ser futura.");
        }
    }

    private void validarProducto(String producto) {
        Objects.requireNonNull(producto, "El producto no puede ser nulo.");
        if (producto.isBlank()) {
            throw new IllegalArgumentException("El producto no puede estar en blanco.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Venta venta)) return false;
        return Objects.equals(fecha, venta.fecha) && Objects.equals(producto, venta.producto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fecha, producto);
    }

    @Override
    public String toString() {
        return String.format("Venta[fecha=%s, producto=%s, cantidad=%s, precioUnitario=%s]", this.fecha, this.producto, this.cantidad, this.precioUnitario);
    }
}
