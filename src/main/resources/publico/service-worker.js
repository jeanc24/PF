const CACHE_VERSION = 2; // <-- Incrementa este número para forzar reinstalación
const CACHE_NAME = `url-shortener-cache-v${CACHE_VERSION}`;
const urlsToCache = [
    '/',
    '/listar',
    '/css/output.css',
    '/js/index.js',
    '/js/listarUrl.js'
];

// Instalación del SW y caché inicial
self.addEventListener('install', event => {
    console.log('[SW] Instalación en proceso...');
    console.log('[SW] 🛠️ Ejecutando install...');
    event.waitUntil(
        caches.open(CACHE_NAME).then(cache => {
            console.log('[SW] Cache abierto, agregando URLs...');
            return cache.addAll(urlsToCache);
        }).catch(err => {
            console.error('[SW] Error al abrir cache:', err);
        })
    );
});

// Activación del SW y limpieza de cachés antiguas
self.addEventListener('activate', event => {
    console.log('[Service Worker] Activado');
    event.waitUntil(
        caches.keys().then(keys => {
            return Promise.all(
                keys.filter(key => key !== CACHE_NAME).map(key => caches.delete(key))
            );
        })
    );
});


// Interceptar peticiones
self.addEventListener('fetch', event => {
    const requestUrl = new URL(event.request.url);

    // No interceptes API calls que requieren auth
    if (requestUrl.pathname.startsWith('/api/')) {
        return; // deja que el navegador lo maneje directamente
    }

    event.respondWith(
        caches.match(event.request).then(response => {
            return response || fetch(event.request);
        })
    );
});
