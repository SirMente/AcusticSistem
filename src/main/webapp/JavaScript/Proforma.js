/* -----------------------------------------------------------
 VARIABLES PRINCIPALES
 -----------------------------------------------------------*/
const itemsBody = document.getElementById('items-body');
const subtotalDisplay = document.getElementById('subtotal_display');
const impuestosDisplay = document.getElementById('impuestos_display');
const totalDisplay = document.getElementById('total_display');
const presupuestoHidden = document.getElementById('presupuesto_hidden_input');
const btnSubmit = document.getElementById('btn-submit-proforma');

const servicioSelect = document.getElementById('servicio_select');
const productoSelect = document.getElementById('producto_select');
const cantidadInput = document.getElementById('cantidad_input');
const clienteSelect = document.getElementById("docu_cliente_select");

let detalleItems = [];
const IGV_RATE = 0.18;

/* -----------------------------------------------------------
 FUNCIÓN: RECALCULAR TOTALES
 -----------------------------------------------------------*/
function recalcularTotales() {
    let totalGeneral = 0;
    detalleItems.forEach(item => totalGeneral += item.subtotal);

    const totalRedondeado = parseFloat(totalGeneral.toFixed(2));
    const subtotal = totalRedondeado / (1 + IGV_RATE);
    const impuestos = totalRedondeado - subtotal;

    subtotalDisplay.value = subtotal.toFixed(2);
    impuestosDisplay.value = impuestos.toFixed(2);
    totalDisplay.value = totalRedondeado.toFixed(2);
    presupuestoHidden.value = totalRedondeado.toFixed(2);

    btnSubmit.disabled = detalleItems.length === 0 || clienteSelect.value === "";
}

/* -----------------------------------------------------------
 AGREGAR ÍTEM
 -----------------------------------------------------------*/
function agregarItem() {
    let selectedOption, type, id, description, price;
    const cantidad = parseInt(cantidadInput.value);

    if (servicioSelect.value && productoSelect.value) {
        alert("Debe elegir solo Servicio O Producto.");
        return;
    }

    if (servicioSelect.value) {
        selectedOption = servicioSelect.options[servicioSelect.selectedIndex];
        type = "Servicio";
        id = servicioSelect.value;
        description = selectedOption.text.trim();
        price = parseFloat(selectedOption.dataset.precio);
    } else if (productoSelect.value) {
        selectedOption = productoSelect.options[productoSelect.selectedIndex];
        type = "Producto";
        id = productoSelect.value;
        description = selectedOption.text.trim();
        price = parseFloat(selectedOption.dataset.precio);
    } else {
        alert("Seleccione un Servicio o Producto.");
        return;
    }

    if (cantidad < 1 || isNaN(cantidad)) {
        alert("Cantidad inválida.");
        return;
    }

    const itemSubtotal = price * cantidad;

    detalleItems.push({
        uniqueId: Date.now(),
        tipo: type,
        id: id,
        descripcion: description.split(" - S/")[0],
        precioUnitario: price,
        cantidad: cantidad,
        subtotal: itemSubtotal
    });

    renderizarItems();
    recalcularTotales();

    servicioSelect.value = "";
    productoSelect.value = "";
    cantidadInput.value = 1;
}

/* -----------------------------------------------------------
 ELIMINAR ÍTEM
 -----------------------------------------------------------*/
function eliminarItem(id) {
    detalleItems = detalleItems.filter(item => item.uniqueId !== id);
    renderizarItems();
    recalcularTotales();
}

/* -----------------------------------------------------------
 RENDERIZAR ITEMS EN LA TABLA + INPUTS OCULTOS
 -----------------------------------------------------------*/
function renderizarItems() {
    itemsBody.innerHTML = "";
    const form = document.getElementById("proforma-form");

    document.querySelectorAll('#proforma-form input[name^="detalle_items"]').forEach(input => input.remove());

    if (detalleItems.length === 0) {
        itemsBody.innerHTML = `<tr><td colspan="6" style="text-align:center;padding:10px;color:#999;">No hay ítems</td></tr>`;
        return;
    }

    let index = 0;
    detalleItems.forEach(item => {
        const row = itemsBody.insertRow();
        row.innerHTML = `
            <td>${item.tipo}</td>
            <td>${item.descripcion}</td>
            <td style="text-align:right;">S/ ${item.precioUnitario.toFixed(2)}</td>
            <td style="text-align:center;">${item.cantidad}</td>
            <td style="text-align:right;font-weight:bold;">S/ ${item.subtotal.toFixed(2)}</td>
            <td style="text-align:center;">
                <button type="button" onclick="eliminarItem(${item.uniqueId})">❌</button>
            </td>
        `;

        form.insertAdjacentHTML("beforeend",
                `<input type="hidden" name="detalle_items[${index}].id" value="${item.id}">
             <input type="hidden" name="detalle_items[${index}].tipo" value="${item.tipo === "Servicio" ? 0 : 1}">
             <input type="hidden" name="detalle_items[${index}].cantidad" value="${item.cantidad}">
             <input type="hidden" name="detalle_items[${index}].precioUnitario" value="${item.precioUnitario}">`
                );
        index++;
    });
}

/* -----------------------------------------------------------
 CARGA DE CLIENTE (FETCH)
 -----------------------------------------------------------*/
function cargarDatosCliente() {
    const docu = clienteSelect.value;

    const limpiarCamposCliente = () => {
        document.getElementById("cliente_nombre").value = "";
        document.getElementById("cliente_empresa").value = "";
        document.getElementById("cliente_email").value = "";
        recalcularTotales();
    };

    if (!docu) {
        limpiarCamposCliente();
        return;
    }

    fetch(CONTEXT_PATH + "/ClienteData?docu=" + docu)
            .then(r => r.ok ? r.json() : Promise.reject("Error HTTP " + r.status))
            .then(data => {
                if (data.error || !data.nombre) {
                    limpiarCamposCliente();
                    document.getElementById("cliente_nombre").value = "Error al cargar";
                    return;
                }

                document.getElementById("cliente_nombre").value = data.nombre;
                document.getElementById("cliente_empresa").value = data.direccion || "";
                document.getElementById("cliente_email").value = data.email;

                recalcularTotales();
            })
            .catch(e => {
                console.error("Error en FETCH:", e);
                limpiarCamposCliente();
                document.getElementById("cliente_nombre").value = "Error de conexión";
            });
}

/* -----------------------------------------------------------
 MODAL PRINCIPAL DE PROFORMA
 -----------------------------------------------------------*/
const modal = document.getElementById("proforma-modal");
const newProformaBtn = document.getElementById("new-proforma-btn");

newProformaBtn.onclick = () => {
    modal.style.display = "block";
    document.getElementById("fecha_emision").value = new Date().toISOString().slice(0, 10);
};

function cerrarModal() {
    modal.style.display = "none";
    document.getElementById("proforma-form").reset();
    detalleItems = [];
    renderizarItems();
    recalcularTotales();
}

document.querySelector(".close").onclick = cerrarModal;
document.querySelector(".btn-cancelar").onclick = cerrarModal;

/* -----------------------------------------------------------
 MODAL DE ESTADO (SEGUNDO MODAL)
 -----------------------------------------------------------*/
const modalEstado = document.getElementById("modal-estado-proforma");
const nuevoEstadoSelect = document.getElementById("nuevo_estado");

function abrirModalEstado(idProforma, estadoActual) {
    document.getElementById('estado_id_proforma').value = idProforma;
    document.getElementById('estado_display_id').textContent = idProforma;
    nuevoEstadoSelect.value = estadoActual;

    modalEstado.style.display = "block";
}

function cerrarModalEstado() {
    modalEstado.style.display = "none";
}

document.querySelector(".close-estado").onclick = cerrarModalEstado;


/* -----------------------------------------------------------
 VER PROFORMA (REDIRECCIÓN)
 -----------------------------------------------------------*/
function verProforma(id) {
    window.location.href = `${CONTEXT_PATH}/GestionProformas?accion=ver&id=` + id;
}

/* -----------------------------------------------------------
 CONTROL UNIFICADO DE CLICK FUERA DE LOS MODALES
 -----------------------------------------------------------*/
window.addEventListener("click", e => {
    if (e.target === modal) {
        cerrarModal();
    } else if (e.target === modalEstado) {
        cerrarModalEstado();
    }
});

/* -----------------------------------------------------------
 AUTOCOMPLETAR DOMINIO EMAIL
 -----------------------------------------------------------*/
const emailInput = document.getElementById("email");
const domainOptions = document.querySelectorAll(".domain-option");

domainOptions.forEach(opt => {
    opt.addEventListener("click", () => {
        const domain = opt.textContent;
        let val = emailInput.value;

        if (val.includes("@"))
            val = val.split("@")[0];

        emailInput.value = val + "@" + domain;
        emailInput.focus();
    });
});

/* -----------------------------------------------------------
 CONTROL: SERVICIO VS PRODUCTO
 -----------------------------------------------------------*/
servicioSelect.addEventListener("change", () => {
    if (servicioSelect.value)
        productoSelect.value = "";
});

productoSelect.addEventListener("change", () => {
    if (productoSelect.value)
        servicioSelect.value = "";
});

/* -----------------------------------------------------------
 INICIALIZACIÓN
 -----------------------------------------------------------*/
document.addEventListener("DOMContentLoaded", () => {
    renderizarItems();
    recalcularTotales();
});
