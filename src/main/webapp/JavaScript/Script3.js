// Script1.js

document.addEventListener('DOMContentLoaded', function() {
    // ----------------------------------------------------
    // 1. Obtener elementos del DOM (IDs del JSP)
    // ----------------------------------------------------
    const modal = document.getElementById('modal-agregar-cliente');
    const form = document.getElementById('form-cliente-action');
    const tituloModal = document.getElementById('modal-title-action');
    const btnSubmit = document.getElementById('btn-submit-action');
    
    // CAMPOS DE DATOS: CLAVE para la edición
    const inputId = document.getElementById('cliente-id');
    const inputNombre = document.getElementById('nombre');
    const inputEmpresa = document.getElementById('empresa');
    const inputTelefono = document.getElementById('telefono');
    const inputEmail = document.getElementById('email');

    // ----------------------------------------------------
    // 2. Lógica para botón AGREGAR (Nuevo Cliente)
    // ----------------------------------------------------
    document.getElementById('btn-agregar-cliente').addEventListener('click', function() {
        // Resetear el formulario para AGREGAR: ID en 0 para INSERT
        inputId.value = '0'; 
        tituloModal.textContent = 'Agregar cliente';
        btnSubmit.textContent = 'Agregar';
        form.reset(); // Limpia los campos
        modal.style.display = 'flex';
    });

    // ----------------------------------------------------
    // 3. Lógica para botones EDITAR (Pre-llenar el formulario)
    // ----------------------------------------------------
    document.querySelectorAll('.edit-btn').forEach(button => {
        button.addEventListener('click', function(e) {
            e.preventDefault();
            
            // Llenar el formulario con los datos del data-attribute
            // CLAVE: Establecer el ID para que el Controller sepa que es UPDATE
            inputId.value = this.getAttribute('data-id');
            inputNombre.value = this.getAttribute('data-nombre');
            inputEmpresa.value = this.getAttribute('data-empresa');
            inputTelefono.value = this.getAttribute('data-telefono');
            inputEmail.value = this.getAttribute('data-email');

            // Cambiar la UI para EDITAR
            tituloModal.textContent = 'Editar Cliente';
            btnSubmit.textContent = 'Guardar Cambios';
            
            modal.style.display = 'flex';
        });
    });
    
    // ----------------------------------------------------
    // 4. Lógica para cerrar el modal
    // ----------------------------------------------------
    modal.querySelector('.close-btn').addEventListener('click', function() {
        modal.style.display = 'none';
    });
});