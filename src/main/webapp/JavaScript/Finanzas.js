    document.addEventListener("DOMContentLoaded", () => {
    const tabla = document.querySelector("#tablaFinanzas tbody");
    const modal = document.querySelector("#modal");
    const btnNuevo = document.querySelector("#btnNuevo");
    const cerrarModal = document.querySelector("#cerrarModal");
    const form = document.querySelector("#formRegistro");

    // Cargar datos iniciales
    fetch("../HTML/finanzas.json")
        .then(res => res.json())
        .then(data => mostrarDatos(data));

    function mostrarDatos(data) {
        tabla.innerHTML = "";
        let ingresosTotales = 0;
        let gastosTotales = 0;

        data.forEach(item => {
        const tr = document.createElement("tr");
        tr.innerHTML = `
            <td>${item.fecha}</td>
            <td>${item.categoria}</td>
            <td>${item.ingresos ? "$" + item.ingresos.toLocaleString() : "$0"}</td>
            <td>${item.gastos ? "$" + item.gastos.toLocaleString() : "$0"}</td>
        `;
        tabla.appendChild(tr);

        ingresosTotales += item.ingresos || 0;
        gastosTotales += item.gastos || 0;
        });

        document.getElementById("totalBalance").textContent =
        "$" + (ingresosTotales - gastosTotales).toLocaleString();

        const margen =
        ingresosTotales > 0
            ? Math.round(((ingresosTotales - gastosTotales) / ingresosTotales) * 100)
            : 0;

        document.getElementById("margenGanancia").textContent = margen + "%";
        document.getElementById("porcentaje").textContent = margen + "%";
    }

    // Modal
    btnNuevo.addEventListener("click", () => (modal.style.display = "flex"));
    cerrarModal.addEventListener("click", () => (modal.style.display = "none"));

    // Guardar nuevo registro
    form.addEventListener("submit", e => {
        e.preventDefault();
        const nuevo = {
        fecha: document.getElementById("fecha").value,
        categoria: document.getElementById("categoria").value,
        ingresos:
            document.getElementById("tipo").value === "ingreso"
            ? parseFloat(document.getElementById("monto").value)
            : 0,
        gastos:
            document.getElementById("tipo").value === "gasto"
            ? parseFloat(document.getElementById("monto").value)
            : 0,
        };

        let datos = JSON.parse(localStorage.getItem("finanzas")) || [];
        datos.push(nuevo);
        localStorage.setItem("finanzas", JSON.stringify(datos));
        mostrarDatos(datos);
        modal.style.display = "none";
        form.reset();
    });
    });
