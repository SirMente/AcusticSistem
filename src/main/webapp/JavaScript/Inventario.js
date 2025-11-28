// JavaScript/Inventario.js

document.addEventListener('DOMContentLoaded', function () {
    // ----------------------------------------------------
    // 1. Obtener elementos del DOM (IDs del JSP de Inventario)
    // ----------------------------------------------------
    const modal = document.getElementById('add-item-modal');
    const form = document.getElementById('add-item-form');
    const tituloModal = document.getElementById('modal-title');
    const btnSubmit = document.getElementById('submit-btn');

    const btnOpenModal = document.getElementById('open-modal-btn');
    const btnCloseModal = document.getElementById('close-modal-btn');

    // CAMPOS DE DATOS DEL PRODUCTO
    const inputId = document.getElementById('producto-id');
    const inputNombre = document.getElementById('articulo-nombre');
    const inputDescripcion = document.getElementById('articulo-descripcion');
    const inputMarca = document.getElementById('articulo-marca');
    const inputModelo = document.getElementById('articulo-modelo');
    const inputCantidad = document.getElementById('articulo-cantidad');
    const inputStockMinimo = document.getElementById('articulo-stock-minimo');
    const inputPrecio = document.getElementById('articulo-precio');
    const inputRucProveedor = document.getElementById('ruc-proveedor');
    const inputImagenUrl = document.getElementById('imagen-url');

    // Campo de acción
    const inputAccion = document.getElementById('action-type');

    // Función para ABRIR el modal (usa la clase 'active' del CSS)
    function openModal() {
        modal.classList.add('active'); // <-- CLAVE: Añadir la clase 'active'
    }

    // Función para CERRAR el modal (remueve la clase 'active' del CSS)
    function closeModal() {
        modal.classList.remove('active'); // <-- CLAVE: Remover la clase 'active'
    }

    // ----------------------------------------------------
    // 2. Lógica para botón AGREGAR (Nuevo Producto)
    // ----------------------------------------------------
    btnOpenModal.addEventListener('click', function () {
        // Resetear el formulario para AGREGAR
        inputId.value = '0';
        inputAccion.value = 'agregar';
        tituloModal.textContent = 'Agregar Nuevo Artículo';
        btnSubmit.textContent = 'Guardar Artículo';
        form.reset();

        openModal(); // <-- Usar la nueva función
    });

    // ----------------------------------------------------
    // 3. Lógica para botones EDITAR (Pre-llenar el formulario)
    // ----------------------------------------------------
    document.querySelectorAll('.edit-btn').forEach(button => {
        button.addEventListener('click', function (e) {
            e.preventDefault();

            // 🔑 Llenar el formulario con los datos del data-attribute (usando los nuevos campos)
            // Se usa 'decodeURIComponent' para manejar los valores codificados del JSP
            inputId.value = this.getAttribute('data-id');
            inputNombre.value = decodeURIComponent(this.getAttribute('data-nombre'));
            inputDescripcion.value = decodeURIComponent(this.getAttribute('data-descripcion'));
            inputMarca.value = decodeURIComponent(this.getAttribute('data-marca'));
            inputModelo.value = decodeURIComponent(this.getAttribute('data-modelo'));
            inputCantidad.value = this.getAttribute('data-cantidad');
            inputStockMinimo.value = this.getAttribute('data-stock-minimo');
            inputPrecio.value = this.getAttribute('data-precio');
            inputImagenUrl.value = decodeURIComponent(this.getAttribute('data-imagen-url'));

            // Seleccionar el RUC en el <select>
            const rucProveedorValue = this.getAttribute('data-ruc-proveedor');
            if (inputRucProveedor) {
                inputRucProveedor.value = rucProveedorValue;
            }

            // Cambiar la UI para EDITAR y la acción del Controller
            inputAccion.value = 'actualizar';
            tituloModal.textContent = 'Editar Artículo';
            btnSubmit.textContent = 'Guardar Cambios';

            openModal(); // <-- Usar la nueva función
        });
    });

    // ----------------------------------------------------
    // 4. Lógica para cerrar el modal
    // ----------------------------------------------------
    btnCloseModal.addEventListener('click', function () {
        closeModal(); // <-- Usar la nueva función
    });

    // Cerrar si se clickea fuera del modal
    window.addEventListener('click', function (event) {
        if (event.target === modal) {
            closeModal(); // <-- Usar la nueva función
        }
    });

    // ----------------------------------------------------
    // (Opcional) Implementación de búsqueda en la tabla (si la necesitas)
    // ----------------------------------------------------
});