// JavaScript/Inventario.js

document.addEventListener('DOMContentLoaded', function () {

    // ----------------------------------------------------
    // 1. Obtener elementos del DOM
    // ----------------------------------------------------
    const modal = document.getElementById('add-item-modal');
    const form = document.getElementById('add-item-form');
    const tituloModal = document.getElementById('modal-title');
    const btnSubmit = document.getElementById('submit-btn');

    const btnOpenModal = document.getElementById('open-modal-btn');
    const btnCloseModal = document.getElementById('close-modal-btn');

    // CAMPOS DE FORMULARIO
    const inputId = document.getElementById('producto-id');
    const inputNombre = document.getElementById('articulo-nombre');
    const inputDescripcion = document.getElementById('articulo-descripcion');
    const inputMarca = document.getElementById('articulo-marca');
    const inputModelo = document.getElementById('articulo-modelo');
    const inputCantidad = document.getElementById('articulo-cantidad');
    const inputStockMinimo = document.getElementById('articulo-stock-minimo');
    const inputPrecio = document.getElementById('articulo-precio');
    const inputRucProveedor = document.getElementById('ruc-proveedor');
    const inputImagenUrl = document.getElementById('imagen-url'); // hidden
    const inputImagen = document.getElementById('imagen');        // file input
    const previewImg = document.getElementById('preview-img');    // preview

    const inputAccion = document.getElementById('action-type');


    // ----------------------------------------------------
    // FUNCIONES PARA ABRIR Y CERRAR MODAL
    // ----------------------------------------------------
    function openModal() {
        modal.classList.add('active');
    }

    function closeModal() {
        modal.classList.remove('active');
    }


    // ----------------------------------------------------
    // 2. Lógica para NUEVO PRODUCTO
    // ----------------------------------------------------
    btnOpenModal.addEventListener('click', function () {

        inputId.value = '0';
        inputAccion.value = 'agregar';
        tituloModal.textContent = 'Agregar Nuevo Artículo';
        btnSubmit.textContent = 'Guardar Artículo';

        form.reset();
        inputImagenUrl.value = "";  // quitar referencia previa

        // quitar previsualización
        previewImg.style.display = 'none';
        previewImg.src = "";

        openModal();
    });


    // ----------------------------------------------------
    // 3. Botones EDITAR PRODUCTO
    // ----------------------------------------------------
    document.querySelectorAll('.edit-btn').forEach(button => {
        button.addEventListener('click', function (e) {
            e.preventDefault();

            inputId.value = this.getAttribute('data-id');
            inputNombre.value = decodeURIComponent(this.getAttribute('data-nombre'));
            inputDescripcion.value = decodeURIComponent(this.getAttribute('data-descripcion'));
            inputMarca.value = decodeURIComponent(this.getAttribute('data-marca'));
            inputModelo.value = decodeURIComponent(this.getAttribute('data-modelo'));
            inputCantidad.value = this.getAttribute('data-cantidad');
            inputStockMinimo.value = this.getAttribute('data-stock-minimo');
            inputPrecio.value = this.getAttribute('data-precio');

            // proveedor
            const rucProveedorValue = this.getAttribute('data-ruc-proveedor');
            inputRucProveedor.value = rucProveedorValue;

            // --- IMAGEN ACTUAL ---
            let imgUrl = decodeURIComponent(this.getAttribute('data-imagen-url'));

            if (imgUrl && imgUrl !== "null" && imgUrl !== "") {
                inputImagenUrl.value = imgUrl; // mantener imagen existente

                // contextPath debe existir en JSP:
                // <script>const contextPath = "<%= request.getContextPath() %>";</script>
                previewImg.src = contextPath + "/" + imgUrl;
                previewImg.style.display = "block";
            } else {
                previewImg.src = "";
                previewImg.style.display = "none";
                inputImagenUrl.value = "";
            }

            inputAccion.value = 'actualizar';
            tituloModal.textContent = 'Editar Artículo';
            btnSubmit.textContent = 'Guardar Cambios';

            openModal();
        });
    });


    // ----------------------------------------------------
    // 4. Cerrar modal
    // ----------------------------------------------------
    btnCloseModal.addEventListener('click', function () {
        closeModal();
    });

    window.addEventListener('click', function (event) {
        if (event.target === modal) {
            closeModal();
        }
    });


    // ----------------------------------------------------
    // 5. PREVISUALIZAR IMAGEN NUEVA
    // ----------------------------------------------------
    inputImagen.addEventListener("change", function () {
        const file = this.files[0];

        if (file) {
            const reader = new FileReader();

            reader.onload = function (e) {
                previewImg.src = e.target.result;
                previewImg.style.display = "block";
            }

            reader.readAsDataURL(file);
        }
    });

});
