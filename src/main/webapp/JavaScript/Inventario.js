// Inventario.js

// --- VARIABLES DEL MODAL ---
// 🚨 CORRECCIÓN AQUÍ: Definir addButton usando el ID del botón en el HTML.
const addButton = document.getElementById('open-modal-btn'); 

const modal = document.getElementById('add-item-modal'); // Referencia al DIV principal
const closeModalBtn = document.getElementById('close-modal-btn'); // Botón X para cerrar
const modalForm = document.getElementById('add-item-form'); // Formulario dentro del modal

// Referencias a los campos de entrada del formulario del modal (usados en la lógica de submit)
const inputNombre = document.getElementById('articulo-nombre');
const inputCantidad = document.getElementById('articulo-cantidad');
const inputCosto = document.getElementById('articulo-costo');
const inputProveedor = document.getElementById('articulo-proveedor');


// --- 1. ABRIR Y CERRAR EL MODAL ---

// Función para abrir el modal (añade la clase 'active')
function openModal() {
    modalForm.reset(); 
    modal.classList.add('active');
}

// Función para cerrar el modal (quita la clase 'active')
function closeModal() {
    modal.classList.remove('active');
}

// Evento para ABRIR el modal (en su botón "Agregar Artículo")
addButton.addEventListener("click", openModal); // Esto ahora funcionará

// Evento para CERRAR el modal desde el botón X
closeModalBtn.addEventListener('click', closeModal);

// Evento para CERRAR el modal al hacer click fuera de él
modal.addEventListener('click', (e) => {
    // Si el clic es directamente en el overlay (el fondo oscuro)
    if (e.target === modal) {
        closeModal();
    }
});


// --- 2. AGREGAR NUEVO ARTÍCULO DESDE EL MODAL ---
modalForm.addEventListener('submit', (e) => {
// ... (resto de la lógica del formulario)
});