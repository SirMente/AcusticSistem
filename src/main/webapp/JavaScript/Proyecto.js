const lista = document.getElementById('proyectos-lista');
const btnNuevo = document.getElementById('btn-nuevo-proyecto');
const modal = document.getElementById('modal-proyecto');
const form = document.getElementById('form-proyecto');
const btnCerrar = document.querySelector('.btn-cerrar');
const tabs = document.querySelectorAll('.tab');

let proyectos = [];

// === Cargar datos desde JSON ===
fetch('../HTML/Proyectos.json')
  .then(res => res.json())
  .then(data => {
    proyectos = data;
    mostrarProyectos('todos');
  });

// === Mostrar proyectos ===
function mostrarProyectos(filtro) {
  lista.innerHTML = '';
  let filtrados = proyectos;

  if (filtro === 'activos') {
    filtrados = proyectos.filter(p => p.progreso < 100);
  } else if (filtro === 'finalizados') {
    filtrados = proyectos.filter(p => p.progreso === 100);
  }

  filtrados.forEach(p => {
    const card = document.createElement('div');
    card.className = 'card-proyecto';
    card.innerHTML = `
      <h4>${p.nombre}</h4>
      <p><strong>Cliente:</strong> ${p.cliente}</p>
      <p><strong>Técnico:</strong> ${p.tecnico1}</p>
      <p><strong>Progreso:</strong> ${p.progreso}%</p>
      <div class="progress-bar"><div class="progress" style="width:${p.progreso}%;"></div></div>
      <button class="btn-ver">Ver Proyecto</button>
    `;
    lista.appendChild(card);
  });
}

// === Tabs (filtros) ===
tabs.forEach(tab => {
  tab.addEventListener('click', () => {
    tabs.forEach(t => t.classList.remove('active'));
    tab.classList.add('active');
    mostrarProyectos(tab.dataset.tab);
  });
});

// === Modal ===
btnNuevo.addEventListener('click', () => modal.style.display = 'flex');
btnCerrar.addEventListener('click', () => modal.style.display = 'none');

// === Crear proyecto ===
form.addEventListener('submit', e => {
  e.preventDefault();
  const nuevo = {
    nombre: `Proyecto ${form.proforma.value}`,
    cliente: form.cliente.value,
    proforma: form.proforma.value,
    tecnico1: form.tecnico1.value,
    tecnico2: form.tecnico2.value,
    tecnico3: form.tecnico3.value,
    tecnico4: form.tecnico4.value,
    fechaInicio: form.fechaInicio.value,
    fechaFin: form.fechaFin.value,
    progreso: 0
  };
  proyectos.push(nuevo);
  mostrarProyectos('todos');
  modal.style.display = 'none';
  form.reset();
});
