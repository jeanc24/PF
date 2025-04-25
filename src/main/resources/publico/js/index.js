function mostrarAcortador() {
    localStorage.setItem('bienvenidaMostrada', 'true');
    document.getElementById('contenedor-bienvenida').classList.add('hidden');
    document.getElementById('contenedor-acortador').classList.remove('hidden');
}

function redirectToLogin() {
    window.location.href = '/login';
}

function redirectToSignUp() {
    window.location.href = '/signup';
}
// Si no existe el token, lo pedimos y lo guardamos
async function verificarYObtenerToken() {
    const token = localStorage.getItem("jwt");

    if (!token || isTokenExpired(token)) {
        try {
            const res = await fetch("/api/token");
            const data = await res.json();
            if (data.jwt) {
                localStorage.setItem("jwt", data.jwt);
                console.log("✅ Token anónimo generado y guardado.");
            }
        } catch (err) {
            console.error("❌ Error al obtener token anónimo:", err);
        }
    } else {
        console.log("🔒 Token existente es válido. No se genera uno nuevo.");
    }
}


document.addEventListener('DOMContentLoaded', async () => {
    await verificarYObtenerToken();
    console.log('Valor de bienvenidaMostrada:', localStorage.getItem('bienvenidaMostrada'));
    console.log('Contenedor acortador:', document.getElementById('contenedor-acortador').classList);
    const bienvenidaMostrada = localStorage.getItem('bienvenidaMostrada') === 'true';
     document.getElementById('contenedor-bienvenida').classList.add('hidden');
     document.getElementById('contenedor-acortador').classList.remove('hidden');


    const form = document.getElementById('urlForm');
    const input = document.getElementById('urlInput');
    const previewContainer = document.getElementById('preview');

    input.addEventListener('keypress', function (event) {
        if (event.key === 'Enter') {
            form.submit();
        }
    });

    // Preview automático al escribir
    let previewTimeout;

    input.addEventListener('input', () => {
        clearTimeout(previewTimeout);

        previewTimeout = setTimeout(() => {
            const url = input.value.trim();

            if (!url || !isValidUrl(url)) {
                previewContainer.innerHTML = '';
                return;
            }

            previewContainer.innerHTML = '';

            const linkElement = document.createElement('a');
            linkElement.href = url.startsWith("http") ? url : "https://" + url;
            linkElement.className = 'link-previews';
            linkElement.dataset.size = 'normal';
            linkElement.dataset.media = '["image"]';

            previewContainer.appendChild(linkElement);

            microlink('.link-previews', {
                size: 'normal',
                media: 'image',
                fetchData: true
            });

        }, 500);



    });

    function isValidUrl(string) {
        try {
            new URL(string.startsWith("http") ? string : "https://" + string);
            return true;
        } catch (_) {
            return false;
        }
    }

    // Envío del formulario
    form.addEventListener('submit', async (e) => {
        e.preventDefault();
        const url = input.value;
        if (input.checkValidity()) {
            try {
                const response = await fetch('/api/urls/', {

                    method: 'POST',
                    headers: {
                        "Content-Type": "application/json",
                        "Authorization": `Bearer ${localStorage.getItem("jwt")}`

                    },
                    body: JSON.stringify({urlOriginal: url})
                });

                if (!response.ok) throw new Error("Error al acortar la URL");

                const data = await response.json();
                alert("URL acortada: " + data.urlAcortada);
            } catch (error) {
                console.error(error);
                alert("Hubo un problema al acortar la URL.");
            }
        } else {
            alert("Please enter a valid URL.");
        }
    });

    // Mostrar el botón de logout si hay usuario autenticado (no anónimo)
    const logoutBtn = document.getElementById("logout");
    const token = localStorage.getItem("jwt");
    const payload = token ? getTokenPayload(token) : null;

    if (logoutBtn) {
        if (token && !isTokenExpired(token) && payload && payload.username) {
            logoutBtn.classList.remove("hidden");
            logoutBtn.addEventListener("click", () => {
                // Limpiar JWT y localStorage
                localStorage.removeItem("jwt");
                localStorage.removeItem("bienvenidaMostrada");

                // Desregistrar el Service Worker y limpiar caché
                if ('serviceWorker' in navigator) {
                    navigator.serviceWorker.getRegistration().then(registration => {
                        if (registration) {
                            registration.unregister().then(success => {
                                console.log("[SW] ✅ Desregistrado:", success);
                                caches.keys().then(keys => {
                                    keys.forEach(key => {
                                        caches.delete(key).then(() => {
                                            console.log(`[SW] 🧹 Cache '${key}' eliminado`);
                                        });
                                    });
                                });
                            });
                        }
                    });
                }

                // Redirigir al logout real
                window.location.href = "/logout";
            });
        } else {
            logoutBtn.classList.add("hidden");
        }
    }

// Registro del Service Worker
    if ('serviceWorker' in navigator) {
        window.addEventListener('load', () => {
            navigator.serviceWorker.register('/service-worker.js')
                .then(reg => {
                    console.log('[SW] Service Worker registrado correctamente:', reg.scope);
                })
                .catch(err => {
                    console.error('[SW] Error al registrar el Service Worker:', err);
                });
        });
    }



});

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


function isTokenExpired(token) {
    try {
        const payload = JSON.parse(atob(token.split('.')[1]));
        const now = Math.floor(Date.now() / 1000);
        return payload.exp && payload.exp < now;
    } catch {
        return true;
    }
}

const token = localStorage.getItem("jwt");
if (!token || isTokenExpired(token)) {
    localStorage.removeItem("jwt");
}