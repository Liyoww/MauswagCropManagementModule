const CACHE_NAME = 'cropsync-v2'; // I-update ang version (v1 to v2)
const ASSETS = [
  '/',
  '/login.html',
  '/index.html',
  '/planner.html',
  '/manifest.json'
];

// Install: Save assets
self.addEventListener('install', event => {
  event.waitUntil(
    caches.open(CACHE_NAME).then(cache => cache.addAll(ASSETS))
  );
  self.skipWaiting();
});

// Activate: Clean old caches
self.addEventListener('activate', event => {
  event.waitUntil(
    caches.keys().then(keys => {
      return Promise.all(
        keys.filter(key => key !== CACHE_NAME).map(key => caches.delete(key))
      );
    })
  );
  self.clients.claim(); // Force immediately control the page
});

// Fetch: NETWORK FIRST strategy
self.addEventListener('fetch', event => {
  const url = new URL(event.request.url);

  // 1. SKIP API CALLS - Laging sa server dapat ito
  if (url.pathname.startsWith('/api/') || url.href.includes('ngrok')) {
    return; 
  }

  // 2. NETWORK FIRST strategy para sa HTML/Assets
  // Subukan muna sa internet, kung fail (offline), kunin sa cache.
  event.respondWith(
    fetch(event.request)
      .catch(() => {
        return caches.match(event.request);
      })
  );
});