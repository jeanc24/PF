document.addEventListener('DOMContentLoaded', () => {
    if (typeof Chart === 'undefined') {
        console.error('Chart.js no se cargó correctamente');
        return;
    }
    // Obtener el path completo
    const path = window.location.pathname;
    const segments = path.split('/');
    const shortcode = segments[segments.length - 1];
    const urlAcortada = `http://localhost:7001/api/cut/${shortcode}`;

    // Hacer la petición al backend
    fetch(`/api/urls/detalles/${encodeURIComponent(urlAcortada)}`,{method: "GET",
        headers: {
            "Authorization": `Bearer ${localStorage.getItem("jwt")}` // Agrega el token aquí
        }
    })
        .then(res => {
            if (!res.ok) {
                throw new Error("No autorizado");
            }
            return res.json();
        }).then(data => {
            console.log(data);
            const url = data.url;
            const estadisticas = data.estadisticas;

            // Construir el contenido dinámicamente
            const contenedor = document.getElementById('contenedor-detalles');
            contenedor.innerHTML = construirContenido(url, estadisticas);

            // Inicializar gráficos
            inicializarGraficos(estadisticas);
        })
        .catch(error => {
            const contenedor = document.getElementById('contenedor-detalles');
            contenedor.innerHTML = `
                <div class="alert alert-error shadow-lg">
                    <div>
                        <svg xmlns="http://www.w3.org/2000/svg" class="stroke-current flex-shrink-0 h-6 w-6" fill="none" viewBox="0 0 24 24">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M10 14l2-2m0 0l2-2m-2 2l-2-2m2 2l2 2m7-2a9 9 0 11-18 0 9 9 0 0118 0z" />
                        </svg>
                        <span>Error al cargar los detalles: ${error.message}</span>
                    </div>
                </div>
            `;
        });
});

function construirContenido(url, estadisticas) {
    return `
        <!-- Encabezado -->
        <div class="flex flex-col md:flex-row justify-between items-start md:items-center gap-4">
            <div>
                <h1 class="text-3xl font-bold text-primary">Detalles de URL</h1>
                <div class="flex items-center gap-2 mt-2">
                    <span class="badge badge-primary">Visitas totales: ${url.listaClientes?.length || 0}</span>
                    <a href="${url.urlOriginal}" target="_blank" class="link link-secondary text-sm">Ver URL original</a>
                </div>
            </div>
            <div class="flex gap-2">
                <a href="/listar" class="btn btn-outline">
                    <i class="fas fa-arrow-left mr-2"></i> Volver
                </a>
                <button class="btn btn-primary" onclick="descargarReporte()">
                    <i class="fas fa-download mr-2"></i> Descargar Reporte
                </button>
            </div>
        </div>

        <!-- Tarjeta de información básica -->
        <div class="card bg-base-100 shadow-xl">
            <div class="card-body">
                <div class="flex flex-col md:flex-row gap-6">
                    <!-- QR Code -->
                    <div class="flex flex-col items-center">
                        <div id="qr-code" class="p-4 bg-white rounded-lg mb-2"></div>
                        <span class="text-sm text-base-content/70">Escanea para visitar</span>
                    </div>
                    
                    <!-- Información de la URL -->
                    <div class="flex-grow">
                        <h2 class="card-title">URL Acortada</h2>
                        <div class="mt-2">
                            <a href="${url.urlAcortada}" target="_blank" class="link link-primary text-lg">${url.urlAcortada}</a>
                        </div>
                        <div class="divider my-2"></div>
                        <div class="grid grid-cols-1 md:grid-cols-2 gap-4">
                            <div>
                                <h3 class="font-bold">URL Original</h3>
                                <p class="text-sm text-base-content/80 truncate">${url.urlOriginal}</p>
                            </div>
                            <div>
                                <h3 class="font-bold">Fecha de creación</h3>
                                <p class="text-sm text-base-content/80">${new Date(url.fechaCreacion).toLocaleDateString()}</p>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Sección de gráficos -->
        <div class="grid grid-cols-1 lg:grid-cols-2 gap-6">
            <!-- Gráfico de visitas por día -->
            <div class="card bg-base-100 shadow-xl">
                <div class="card-body">
                    <h2 class="card-title">Visitas por día</h2>
                    <canvas id="visitasChart"></canvas>
                </div>
            </div>

            <!-- Gráfico de navegadores -->
            <div class="card bg-base-100 shadow-xl">
                <div class="card-body">
                    <h2 class="card-title">Navegadores más usados</h2>
                    <canvas id="navegadoresChart"></canvas>
                </div>
            </div>

            <!-- Gráfico de sistemas operativos -->
            <div class="card bg-base-100 shadow-xl">
                <div class="card-body">
                    <h2 class="card-title">Sistemas operativos</h2>
                    <canvas id="soChart"></canvas>
                </div>
            </div>

            <!-- Tabla de últimos accesos -->
            <div class="card bg-base-100 shadow-xl lg:col-span-2">
                <div class="card-body">
                    <h2 class="card-title">Últimos accesos</h2>
                    <div class="overflow-x-auto">
                        <table class="table">
                            <thead>
                                <tr>
                                    <th>Fecha</th>
                                    <th>IP</th>
                                    <th>Navegador</th>
                                    <th>Sistema Operativo</th>
                                </tr>
                            </thead>
                            <tbody id="tabla-accesos">
                                ${url.listaClientes?.map(cliente => `
                                    <tr>
                                        <td>${new Date(cliente.fechaAcceso).toLocaleString()}</td>
                                        <td>${cliente.ipCliente}</td>
                                        <td>${cliente.navegador}</td>
                                        <td>${cliente.sistemaOperativo}</td>
                                    </tr>
                                `).join('') || '<tr><td colspan="4" class="text-center">No hay accesos registrados</td></tr>'}
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    `;
}

function inicializarGraficos(estadisticas) {
    // Generar QR Code
    new QRCode(document.getElementById("qr-code"), {
        text: window.location.origin + "/api/cut/" + window.location.pathname.split('/').pop(),
        width: 120,
        height: 120,
        colorDark: "#3B82F6",
        colorLight: "#ffffff",
        correctLevel: QRCode.CorrectLevel.H
    });

    // Gráfico de visitas por día
    const visitasCtx = document.getElementById('visitasChart').getContext('2d');
    new Chart(visitasCtx, {
        type: 'bar',
        data: {
            labels: Object.keys(estadisticas.visitasPorDia),
            datasets: [{
                label: 'Visitas',
                data: Object.values(estadisticas.visitasPorDia),
                backgroundColor: '#3B82F6',
                borderColor: '#2563EB',
                borderWidth: 1
            }]
        },
        options: {
            responsive: true,
            scales: {
                y: {
                    beginAtZero: true
                }
            }
        }
    });

    // Gráfico de navegadores
    const navegadoresCtx = document.getElementById('navegadoresChart').getContext('2d');
    new Chart(navegadoresCtx, {
        type: 'doughnut',
        data: {
            labels: Object.keys(estadisticas.navegadores),
            datasets: [{
                data: Object.values(estadisticas.navegadores),
                backgroundColor: [
                    '#3B82F6', '#10B981', '#F59E0B', '#EF4444', '#8B5CF6'
                ],
                borderWidth: 1
            }]
        },
        options: {
            responsive: true
        }
    });

    // Gráfico de sistemas operativos
    const soCtx = document.getElementById('soChart').getContext('2d');
    new Chart(soCtx, {
        type: 'pie',
        data: {
            labels: Object.keys(estadisticas.sistemasOperativos),
            datasets: [{
                data: Object.values(estadisticas.sistemasOperativos),
                backgroundColor: [
                    '#3B82F6', '#10B981', '#F59E0B', '#EF4444', '#8B5CF6'
                ],
                borderWidth: 1
            }]
        },
        options: {
            responsive: true
        }
    });
}

function descargarReporte() {
    // Implementar lógica para descargar reporte
    alert('Función de descarga de reporte será implementada');
}