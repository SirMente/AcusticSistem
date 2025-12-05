// Finanzas.js

document.addEventListener('DOMContentLoaded', function() {

    // Selección de elementos
    const modal = document.getElementById('modal');
    const btnNuevo = document.getElementById('btnNuevo');
    const btnCerrar = document.getElementById('cerrarModal');
    const formRegistro = document.getElementById('formRegistro');

    // Mostrar modal al hacer clic en "Nuevo registro"
    btnNuevo.addEventListener('click', () => {
        modal.style.display = 'flex';
    });

    // Cerrar modal
    btnCerrar.addEventListener('click', () => {
        modal.style.display = 'none';
        formRegistro.reset();
    });

    // Cerrar modal si se hace clic fuera del contenido
    window.addEventListener('click', (e) => {
        if (e.target === modal) {
            modal.style.display = 'none';
            formRegistro.reset();
        }
    });

    // Validación simple antes de enviar
    formRegistro.addEventListener('submit', (e) => {
        const fecha = document.getElementById('fecha').value;
        const tipo = document.getElementById('tipo').value;
        const categoria = document.getElementById('categoria').value;
        const descripcion = document.getElementById('descripcion').value;
        const monto = document.getElementById('monto').value;

        if (!fecha || !tipo || !categoria || !descripcion || !monto || monto <= 0) {
            alert('Por favor completa todos los campos correctamente.');
            e.preventDefault();
        }
        // Si todo está correcto, el formulario se envía al Servlet FinanzaServlet
    });

});
