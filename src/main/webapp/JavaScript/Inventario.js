// Inventario.js (CORREGIDO)

document.addEventListener('DOMContentLoaded', function () {
    // --- 1. VARIABLES DEL MODAL Y ELEMENTOS DEL FORMULARIO ---

    const openModalBtn = document.getElementById('open-modal-btn'); 
    const modal = document.getElementById('add-item-modal');      
    const closeModalBtn = document.getElementById('close-modal-btn'); 
    const form = document.getElementById('add-item-form');        
    
    // Elementos internos del formulario
    const modalTitle = document.getElementById('modal-title');
    const actionType = document.getElementById('action-type');
    const submitBtn = document.getElementById('submit-btn');
    const productoId = document.getElementById('producto-id');

    // Referencias a los campos de entrada
    const inputNombre = document.getElementById('articulo-nombre');
    const inputDescripcion = document.getElementById('articulo-descripcion');
    const inputCantidad = document.getElementById('articulo-cantidad');
    const inputPrecio = document.getElementById('articulo-precio'); 
    const inputProveedor = document.getElementById('articulo-proveedor'); 

    // Nodos de los botones de edición
    const editButtons = document.querySelectorAll('.edit-btn');


    // --- 2. FUNCIONES DE MANEJO DEL MODAL (ABRIR / CERRAR) ---

    // Función para ABRIR el modal: AÑADE la clase 'active'
    function openAddModal() {
        form.reset();
        productoId.value = '0';
        actionType.value = 'agregar';
        modalTitle.textContent = 'Agregar Nuevo Artículo';
        submitBtn.textContent = 'Guardar Artículo';
        
        // 🔑 CORRECCIÓN: Usar classList.add('active')
        modal.classList.add('active');
    }

    // Función para cerrar el modal: REMUEVE la clase 'active'
    function closeModal() {
        // 🔑 CORRECCIÓN: Usar classList.remove('active')
        modal.classList.remove('active');
    }


    // --- 3. LISTENERS GLOBALES ---

    // Evento para ABRIR el modal (en su botón "Agregar Artículo")
    if (openModalBtn) {
        openModalBtn.addEventListener("click", openAddModal);
    }

    // Evento para CERRAR el modal desde el botón X
    if (closeModalBtn) {
        closeModalBtn.addEventListener('click', closeModal);
    }

    // Evento para CERRAR el modal al hacer click fuera de él
    if (modal) {
        modal.addEventListener('click', (e) => {
            if (e.target === modal) {
                closeModal();
            }
        });
    }

    // --- 4. LÓGICA DE EDICIÓN (Cargar datos y abrir modal) ---

    editButtons.forEach(button => {
        button.addEventListener('click', function (e) {
            e.preventDefault();

            // 1. Configurar el modo a 'editar'
            actionType.value = 'editar';
            modalTitle.textContent = 'Editar Artículo Existente';
            submitBtn.textContent = 'Guardar Cambios';

            // 2. Llenar los campos con los datos
            productoId.value = this.getAttribute('data-id');
            inputNombre.value = this.getAttribute('data-nombre');
            inputDescripcion.value = this.getAttribute('data-descripcion');
            inputCantidad.value = this.getAttribute('data-cantidad');
            inputPrecio.value = this.getAttribute('data-precio');
            
            const proveedorActual = this.getAttribute('data-proveedor');
            inputProveedor.value = proveedorActual;

            // 3. Abrir el modal
            // 🔑 CORRECCIÓN: Usar classList.add('active')
            modal.classList.add('active');
        });
    });
}); // Fin de DOMContentLoaded