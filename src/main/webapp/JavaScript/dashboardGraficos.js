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


    // ======================================================
    // GRÁFICO: FINANZAS
    // ======================================================

    new Chart(document.getElementById("graficoFinanzas"), {
        type: "line",
        data: {
            labels: ["Ene","Feb","Mar","Abr","May","Jun"],
            datasets: [{
                label: "Ingresos",
                data: [4000, 5000, 4500, 7000, 6200, 8000],
                borderColor: "#2ecc71",
                tension: .4
            },{
                label: "Gastos",
                data: [3000, 3200, 2900, 4100, 3800, 4200],
                borderColor: "#e74c3c",
                tension: .4
            }]
        }
    });


    // ======================================================
    // GRÁFICO: PROYECTOS
    // ======================================================

    new Chart(document.getElementById("graficoProyectos"), {
        type: "doughnut",
        data: {
            labels: ["Activos","En pausa","Finalizados"],
            datasets: [{
                backgroundColor: ["#3498db","#f1c40f","#2ecc71"],
                data: [10,3,7]
            }]
        }
    });

}); // FIN DEL DOMContentLoaded
