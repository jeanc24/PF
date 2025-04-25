console.log('[Script] listarUrl.js cargado');

let db;
const DB_NAME = 'URLShortenerDB';
const STORE_NAME = 'urls';

// === IndexedDB ===
function abrirIndexedDB() {
    return new Promise((resolve, reject) => {
        const request = indexedDB.open(DB_NAME, 2);
        request.onerror = () => reject('Error al abrir IndexedDB');
        request.onsuccess = () => {
            db = request.result;
            resolve(db);
        };
        request.onupgradeneeded = (event) => {
            db = event.target.result;
            if (!db.objectStoreNames.contains(STORE_NAME)) {
                db.createObjectStore(STORE_NAME, { autoIncrement: true });
            }
        };
    });
}

function guardarUrlsEnIndexedDB(urls) {
    const transaction = db.transaction([STORE_NAME], 'readwrite');
    const store = transaction.objectStore(STORE_NAME);

    urls.forEach(url => {
        try {
            if (typeof url.urlAcortada !== 'string' || url.urlAcortada.trim() === '') return;
            url.listaClientes = url.listaClientes ?? [];
            url.imagenBase64 = url.imagenBase64?.trim() || '';

            store.put(url).onsuccess = () => {
                console.log('[IndexedDB] Guardada:', url.urlAcortada);
            };
        } catch (e) {
            console.error('[IndexedDB] Error al guardar:', e);
        }
    });
}

function cargarUrlsDesdeIndexedDB() {
    return new Promise((resolve) => {
        const transaction = db.transaction([STORE_NAME], 'readonly');
        const store = transaction.objectStore(STORE_NAME);
        const request = store.getAll();
        request.onsuccess = () => resolve(request.result);
        request.onerror = () => resolve([]);
    });
}

// === Render URLs ===
function renderizarUrls(urls, esAdmin) {
    const urlList = document.getElementById('url-list');
    urlList.innerHTML = '';

    if (urls.length === 0) {
        document.getElementById('empty-state').classList.remove('hidden');
        urlList.classList.add('hidden');
    } else {
        document.getElementById('empty-state').classList.add('hidden');
        urlList.classList.remove('hidden');
    }

    urls.forEach(url => {
        const row = document.createElement('tr');
        const qrId = `qr-${Math.random().toString(36).substr(2, 9)}`;
        const shortcode = url.urlAcortada.split('/')[5];


        row.innerHTML = `
            <td class="text-center align-middle">
                <input type="checkbox" class="checkbox checkbox-primary" />
            </td>
            <td class="align-middle">
                <div class="flex items-center gap-4">
                    <div class="avatar">
                        <div class="mask mask-squircle w-12 h-12">
                            <img src="${url.imagenBase64 || '/img/url-default.png'}" alt="Miniatura"
                                 class="object-cover" onerror="this.src='/img/url-default.png'" />
                        </div> 
                    </div>
                    <div class="flex flex-col gap-1">
                        <a href="${url.urlOriginal}" target="_blank"
                           class="link link-primary font-medium truncate max-w-[200px]">
                           ${url.urlOriginal}
                        </a>
                        <div class="flex items-center gap-2">
                            <span class="badge badge-ghost">Acortada:</span>
                            <a href="${url.urlAcortada}" target="_blank"
                               class="link link-secondary text-sm">
                               ${url.urlAcortada}
                            </a>
                        </div>
                    </div>
                </div>
            </td>
            <td class="text-center align-middle">
                <div class="tooltip" data-tip="Escanear QR">
                    <div id="${qrId}" class="qr-container p-2 bg-base-100 rounded-lg inline-block"></div>
                </div>
            </td>
            <td class="text-center align-middle">
                <a href="/detalles/${shortcode}" class="btn btn-primary btn-sm mb-1">
                    Detalles
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 ml-1" fill="none"
                         viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                              d="M13 16h-1v-4h-1m1-4h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z"/>
                    </svg>
                </a>
                ${esAdmin ? `
                    <button class="btn btn-error btn-sm" onclick="eliminarUrl('${shortcode}')">
                        Eliminar
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4 ml-1" fill="none"
                             viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2"
                                  d="M6 18L18 6M6 6l12 12"/>
                        </svg>
                    </button>
                ` : ''}
            </td>
        `;

        urlList.appendChild(row);

        new QRCode(document.getElementById(qrId), {
            text: url.urlAcortada,
            width: 80,
            height: 80,
            colorDark: "#3B82F6",
            colorLight: "transparent",
            correctLevel: QRCode.CorrectLevel.H
        });
    });
}

// === Eliminar URL (solo admin) ===
async function eliminarUrl(shortcode) {
    if (!confirm("¿Estás seguro de que deseas eliminar esta URL?")) return;

    try {
        const token = localStorage.getItem("jwt");
        const response = await fetch(`/api/urls/${shortcode}`, {
            method: 'DELETE',
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });

        if (!response.ok) throw new Error("Error al eliminar la URL");

        alert("URL eliminada exitosamente.");
        console.log("Eliminando URL con ID:", shortcode);

        location.reload(); // o puedes volver a renderizar solo
    } catch (error) {
        console.error("❌ Error al eliminar la URL:", error);
        alert("No se pudo eliminar la URL.");
        console.log("Eliminando URL con ID:", shortcode);

    }
}

// === Decodificar Token ===
function getTokenPayload(token) {
    try {
        const base64Url = token.split('.')[1];
        const base64 = base64Url.replace(/-/g, '+').replace(/_/g, '/');
        const jsonPayload = decodeURIComponent(atob(base64).split('').map(function(c) {
            return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
        }).join(''));
        return JSON.parse(jsonPayload);
    } catch (e) {
        console.error("❌ Error al decodificar el token:", e);
        return null;
    }
}

// === Main ===
document.addEventListener('DOMContentLoaded', async () => {
    try {
        await abrirIndexedDB();

        const token = localStorage.getItem("jwt");
        if (!token) throw new Error("No hay token");

        const payload = getTokenPayload(token);
        const esAdmin = payload && payload.role && payload.role.includes('ADMIN');

        const response = await fetch('/api/lista', {
            method: "GET",
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });

        if (!response.ok) throw new Error("No autorizado o token inválido");

        const urls = await response.json();
        guardarUrlsEnIndexedDB(urls);
        renderizarUrls(urls, esAdmin);

    } catch (error) {
        console.warn('[IndexedDB] Error o sin conexión:', error);
        const offlineUrls = await cargarUrlsDesdeIndexedDB();
        renderizarUrls(offlineUrls, false);
    }
});
