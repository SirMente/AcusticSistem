    console.log("✅ Archivo Proveedores.js cargado correctamente");

    const tablaBody = document.querySelector("#tabla-proveedores tbody");
    const searchInput = document.querySelector(".search-box input");
    const modal = document.querySelector("#modalProveedor");
    const openModalBtn = document.querySelector("#openModalBtn");
    const cancelModalBtn = document.querySelector("#cancelModalBtn");
    const proveedorForm = document.querySelector("#proveedorForm");
    const modalTitle = document.querySelector("#modalTitle");

    let proveedores = [];
    let editIndex = -1;

    // Renderizar tabla
    function renderTable(data) {
    tablaBody.innerHTML = "";
    data.forEach((prov, index) => {
        const tr = document.createElement("tr");
        tr.innerHTML = `
        <td>${prov.nombre}</td>
        <td>${prov.contacto}</td>
        <td>${prov.tipo}</td>
        <td>
            <i class='bx bx-edit' onclick="openEditModal(${index})"></i>
            <i class='bx bx-trash' onclick="deleteProveedor(${index})"></i>
        </td>`;
        tablaBody.appendChild(tr);
    });
    }

    // Mostrar modal
    function openModal(editing = false) {
    modal.style.display = "block";
    modalTitle.textContent = editing ? "Editar Proveedor" : "Añadir Proveedor";
    document.body.style.overflow = "hidden";
    }

    // Cerrar modal
    function closeModal() {
    modal.style.display = "none";
    proveedorForm.reset();
    document.body.style.overflow = "auto";
    editIndex = -1;
    }

    // Cargar JSON inicial
    document.addEventListener("DOMContentLoaded", async () => {
    try {
        const response = await fetch("../HTML/proveedores.json");
        proveedores = await response.json();
        renderTable(proveedores);
    } catch (error) {
        console.error("⚠️ Error al cargar proveedores:", error);
    }
    });

    // Buscar proveedor
    searchInput.addEventListener("input", () => {
    const term = searchInput.value.toLowerCase();
    const filtrados = proveedores.filter(p =>
        p.nombre.toLowerCase().includes(term) ||
        p.tipo.toLowerCase().includes(term)
    );
    renderTable(filtrados);
    });

    // Abrir modal para añadir
    openModalBtn.addEventListener("click", () => openModal(false));

    // Cancelar modal
    cancelModalBtn.addEventListener("click", closeModal);

    // Guardar (añadir o editar)
    proveedorForm.addEventListener("submit", e => {
    e.preventDefault();

    const nombre = document.querySelector("#nombreProveedor").value.trim();
    const contacto = document.querySelector("#contactoProveedor").value.trim();
    const tipo = document.querySelector("#tipoProveedor").value.trim();

    if (editIndex === -1) {
        proveedores.push({ nombre, contacto, tipo });
    } else {
        proveedores[editIndex] = { nombre, contacto, tipo };
    }

    renderTable(proveedores);
    closeModal();
    });

    // Editar proveedor
    function openEditModal(index) {
    editIndex = index;
    const p = proveedores[index];
    document.querySelector("#nombreProveedor").value = p.nombre;
    document.querySelector("#contactoProveedor").value = p.contacto;
    document.querySelector("#tipoProveedor").value = p.tipo;
    openModal(true);
    }

    // Eliminar proveedor
    function deleteProveedor(index) {
    if (confirm(`¿Eliminar al proveedor "${proveedores[index].nombre}"?`)) {
        proveedores.splice(index, 1);
        renderTable(proveedores);
    }
    }

    // Cerrar modal al hacer clic fuera
    window.onclick = e => {
    if (e.target === modal) closeModal();
    };
