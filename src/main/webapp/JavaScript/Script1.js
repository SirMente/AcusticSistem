// Código existente para el menú lateral (asumiendo que funciona)
const sidebar = document.getElementById('sidebar');
const menuBtn = document.getElementById('menu-btn');

if (menuBtn && sidebar) {
    menuBtn.addEventListener('click', () => {
        sidebar.classList.toggle('minimize');
    });
}


// INICIO DE LA CORRECCIÓN CLAVE: 
// Esto asegura que el código del modal solo se ejecute cuando 
// todos los elementos (el botón, el modal, etc.) ya existen en la página.
document.addEventListener('DOMContentLoaded', () => {

    // 1. Obtener referencias a los elementos
    // (Ahora se hace después de que el HTML está cargado, lo que es correcto)
    const btnAbrirModal = document.querySelector('.btn-agregar-cliente');
    const modal = document.getElementById('modal-agregar-cliente');

    // IMPORTANTE: Si el botón o el modal no se encuentran, mostramos un error en consola y detenemos.
    if (!btnAbrirModal || !modal) {
        console.error("Error: El botón o el modal para 'Agregar Cliente' no se encontraron en el DOM. Revisa las clases y IDs.");
        return; 
    }
    
    // Ahora que sabemos que el modal existe, podemos buscar sus elementos internos.
    const btnCerrarModal = modal.querySelector('.close-btn');

    // 2. Función para abrir el modal
    function abrirModal() {
        modal.classList.add('active');
        document.body.style.overflow = 'hidden'; 
    }

    // 3. Función para cerrar el modal
    function cerrarModal() {
        modal.classList.remove('active');
        document.body.style.overflow = 'auto';
    }

    // 4. Asignar eventos
    // Abrir modal al hacer clic en "Agregar Cliente"
    btnAbrirModal.addEventListener('click', abrirModal);

    // Cerrar modal al hacer clic en la 'x'
    btnCerrarModal.addEventListener('click', cerrarModal);

    // Cerrar modal al hacer clic fuera del contenido del modal
    window.addEventListener('click', function(event) {
        if (event.target === modal) {
            cerrarModal();
        }
    });

    // Opcional: Cerrar modal con la tecla 'Escape'
    document.addEventListener('keydown', function(event) {
        if (event.key === 'Escape' && modal.classList.contains('active')) {
            cerrarModal();
        }
    });
    
}); // FIN de la corrección clave (cierre del DOMContentLoaded)


document.addEventListener('DOMContentLoaded', () => {
    // ... Tu código existente para el modal de Agregar Cliente ...

    // --- CÓDIGO NUEVO PARA EL MODAL DE PROFORMA RÁPIDA ---

    // 1. Obtener referencias
    const btnAbrirProforma = document.querySelector('.btn-proforma-rapida'); // Tu botón flotante
    const modalProforma = document.getElementById('modal-proforma-rapida');
    
    // Asegurarse de que el modal existe antes de buscar el botón de cierre
    if (!btnAbrirProforma || !modalProforma) {
        console.warn("Advertencia: El botón o el modal para 'Proforma Rápida' no se encontraron.");
        return; 
    }
    
    const btnCerrarProforma = modalProforma.querySelector('.close-btn');

    // 2. Función para abrir el modal de Proforma
    function abrirProforma() {
        modalProforma.classList.add('active');
        document.body.style.overflow = 'hidden';
    }

    // 3. Función para cerrar el modal de Proforma
    function cerrarProforma() {
        modalProforma.classList.remove('active');
        // Solo restaurar el scroll si el otro modal no está activo
        const otherModal = document.getElementById('modal-agregar-cliente');
        if (!otherModal || !otherModal.classList.contains('active')) {
             document.body.style.overflow = 'auto';
        }
    }

    // 4. Asignar eventos
    btnAbrirProforma.addEventListener('click', abrirProforma);
    btnCerrarProforma.addEventListener('click', cerrarProforma);

    // Cerrar modal Proforma al hacer clic fuera del contenido
    window.addEventListener('click', function(event) {
        if (event.target === modalProforma) {
            cerrarProforma();
        }
    });

    // Opcional: Cerrar modal Proforma con la tecla 'Escape'
    document.addEventListener('keydown', function(event) {
        if (event.key === 'Escape' && modalProforma.classList.contains('active')) {
            cerrarProforma();
        }
    });

}); // Cierre del addEventListener('DOMContentLoaded')