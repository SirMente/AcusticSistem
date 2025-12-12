// =========================
// MENÚ LATERAL
// =========================
const sidebar = document.getElementById('sidebar');
const menuBtn = document.getElementById('menu-btn');

if (menuBtn && sidebar) {
    menuBtn.addEventListener('click', () => {
        sidebar.classList.toggle('minimize');
    });
}



// =========================
// TODO EL RESTO DEL CÓDIGO
// =========================
document.addEventListener('DOMContentLoaded', () => {

    // ======================================================
    // MODAL 1: AGREGAR CLIENTE
    // ======================================================

    const btnAbrirModal = document.querySelector('.btn-agregar-cliente');
    const modal = document.getElementById('modal-agregar-cliente');

    if (!btnAbrirModal || !modal) {
        console.error("Error: El botón o el modal para 'Agregar Cliente' no se encontraron en el DOM.");
    } else {

        const btnCerrarModal = modal.querySelector('.close-btn');

        function abrirModal() {
            modal.classList.add('active');
            document.body.style.overflow = 'hidden';
        }

        function cerrarModal() {
            modal.classList.remove('active');
            document.body.style.overflow = 'auto';
        }

        btnAbrirModal.addEventListener('click', abrirModal);
        btnCerrarModal.addEventListener('click', cerrarModal);

        window.addEventListener('click', (event) => {
            if (event.target === modal) cerrarModal();
        });

        document.addEventListener('keydown', (event) => {
            if (event.key === 'Escape' && modal.classList.contains('active')) {
                cerrarModal();
            }
        });
    }


    // ======================================================
    // MODAL 2: PROFORMA RÁPIDA
    // ======================================================

    const btnAbrirProforma = document.querySelector('.btn-proforma-rapida');
    const modalProforma = document.getElementById('modal-proforma-rapida');

    if (!btnAbrirProforma || !modalProforma) {
        console.warn("Advertencia: El botón o modal de 'Proforma Rápida' no se encontraron.");
    } else {

        const btnCerrarProforma = modalProforma.querySelector('.close-btn');

        function abrirProforma() {
            modalProforma.classList.add('active');
            document.body.style.overflow = 'hidden';
        }

        function cerrarProforma() {
            modalProforma.classList.remove('active');

            // Solo habilitar scroll si el otro modal NO está activo
            const modalCliente = document.getElementById('modal-agregar-cliente');
            if (!modalCliente || !modalCliente.classList.contains('active')) {
                document.body.style.overflow = 'auto';
            }
        }

        btnAbrirProforma.addEventListener('click', abrirProforma);
        btnCerrarProforma.addEventListener('click', cerrarProforma);

        window.addEventListener('click', (event) => {
            if (event.target === modalProforma) cerrarProforma();
        });

        document.addEventListener('keydown', (event) => {
            if (event.key === 'Escape' && modalProforma.classList.contains('active')) {
                cerrarProforma();
            }
        });
    }


    // ============ GRÁFICO FINANZAS ============
const ingresosMes = JSON.parse(document.getElementById("graficoFinanzas").dataset.ingresos);
const gastosMes   = JSON.parse(document.getElementById("graficoFinanzas").dataset.gastos);

new Chart(document.getElementById("graficoFinanzas"), {
    type: 'line',
    data: {
        labels: Object.keys(ingresosMes),
        datasets: [
            {
                label: "Ingresos",
                data: Object.values(ingresosMes),
                borderWidth: 2,
                borderColor: "green"
            },
            {
                label: "Gastos",
                data: Object.values(gastosMes),
                borderWidth: 2,
                borderColor: "red"
            }
        ]
    }
});

// ============ GRÁFICO PROYECTOS ============
// Gráfico Proyectos por Estado
const proyectosCanvas = document.getElementById("graficoProyectos");
const proyectosEstado = JSON.parse(proyectosCanvas.dataset.estados);

new Chart(proyectosCanvas, {
    type: 'pie',
    data: {
        labels: Object.keys(proyectosEstado),
        datasets: [{
            data: Object.values(proyectosEstado),
            backgroundColor: ["#1e88e5", "#43a047", "#fb8c00", "#e53935"]
        }]
    },
    options: {
        responsive: true,
        plugins: {
            legend: { position: 'bottom' }
        }
    }
});
}); // FIN DEL DOMContentLoaded
