// Obtener elementos del DOM
const newProformaBtn = document.getElementById('new-proforma-btn');
const modal = document.getElementById('proforma-modal');
const closeBtn = document.querySelector('.close');
const cancelBtn = document.querySelector('.btn-cancelar');
const saveBtn = document.querySelector('.btn-guardar');
const proformaForm = document.getElementById('proforma-form');
const emailInput = document.getElementById('email');
const domainOptions = document.querySelectorAll('.domain-option');

// Abrir modal
newProformaBtn.addEventListener('click', function() {
  modal.style.display = 'block';
  // Limpiar formulario al abrir
  proformaForm.reset();
});

// Cerrar modal
closeBtn.addEventListener('click', closeModal);
cancelBtn.addEventListener('click', closeModal);

// Cerrar modal al hacer clic fuera del contenido
window.addEventListener('click', function(event) {
  if (event.target === modal) {
    closeModal();
  }
});

// Función para cerrar modal
function closeModal() {
  modal.style.display = 'none';
}

// Manejar clic en opciones de dominio
domainOptions.forEach(option => {
  option.addEventListener('click', function() {
    const domain = this.textContent;
    const currentEmail = emailInput.value;
    
    // Si ya hay un @ en el email, reemplazar solo la parte después del @
    if (currentEmail.includes('@')) {
      const username = currentEmail.split('@')[0];
      emailInput.value = username + '@' + domain;
    } else {
      // Si no hay @, agregar el dominio completo
      emailInput.value = currentEmail + '@' + domain;
    }
    
    // Enfocar el input de email
    emailInput.focus();
  });
});

// Manejar envío del formulario
saveBtn.addEventListener('click', function() {
  // Validar formulario
  if (validateForm()) {
    // Obtener datos del formulario
    const formData = getFormData();
    
    // Aquí puedes enviar los datos a tu backend o procesarlos
    console.log('Datos de la proforma:', formData);
    
    // Mostrar mensaje de éxito
    alert('Proforma guardada exitosamente');
    
    // Cerrar modal
    closeModal();
    
    // Opcional: agregar la nueva proforma a la tabla
    addProformaToTable(formData);
  }
});

// Validar formulario
function validateForm() {
  const nombres = document.getElementById('nombres').value.trim();
  const producto = document.getElementById('producto').value.trim();
  
  if (!nombres) {
    alert('Por favor, ingresa el nombre del cliente');
    document.getElementById('nombres').focus();
    return false;
  }
  
  if (!producto) {
    alert('Por favor, ingresa el producto o servicio');
    document.getElementById('producto').focus();
    return false;
  }
  
  return true;
}

// Obtener datos del formulario
function getFormData() {
  const nombres = document.getElementById('nombres').value.trim();
  const empresa = document.getElementById('empresa').value.trim();
  const telefono = document.getElementById('telefono').value.trim();
  const email = document.getElementById('email').value.trim();
  const producto = document.getElementById('producto').value.trim();
  const cantidad = document.getElementById('cantidad').value;
  const presupuesto = document.getElementById('presupuesto').value;
  const descripcion = document.getElementById('descripcion').value.trim();
  
  return {
    nombres,
    empresa,
    telefono,
    email,
    producto,
    cantidad: parseInt(cantidad) || 1,
    presupuesto: parseFloat(presupuesto) || 0,
    descripcion,
    fechaEmision: new Date().toLocaleDateString('es-ES'),
    estado: 'Pendiente',
    numeroProforma: generateProformaNumber()
  };
}

// Generar número de proforma
function generateProformaNumber() {
  const now = new Date();
  const year = now.getFullYear();
  const month = String(now.getMonth() + 1).padStart(2, '0');
  const day = String(now.getDate()).padStart(2, '0');
  const random = Math.floor(Math.random() * 1000).toString().padStart(3, '0');
  
  return `PF-${year}${month}${day}-${random}`;
}

// Agregar proforma a la tabla (opcional)
function addProformaToTable(proformaData) {
  const tableBody = document.querySelector('.proformas-table tbody');
  const newRow = document.createElement('tr');
  
  // Determinar clase de estado
  let statusClass = 'status-pendiente';
  let statusText = 'Pendiente';
  
  newRow.innerHTML = `
    <td>${proformaData.numeroProforma}</td>
    <td>${proformaData.nombres}${proformaData.empresa ? ' - ' + proformaData.empresa : ''}</td>
    <td>${proformaData.fechaEmision}</td>
    <td>$${proformaData.presupuesto.toLocaleString('en-US', {minimumFractionDigits: 2})}</td>
    <td>
      <span class="status-tag ${statusClass}">${statusText}</span>
    </td>
  `;
  
  // Agregar la nueva fila al principio de la tabla
  tableBody.insertBefore(newRow, tableBody.firstChild);
}

// Manejar tecla Escape para cerrar modal
document.addEventListener('keydown', function(event) {
  if (event.key === 'Escape' && modal.style.display === 'block') {
    closeModal();
  }
});