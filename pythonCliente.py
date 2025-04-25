import requests

# URL base de la API
base_url = "http://localhost:7001/api/urls"
token_url = "http://localhost:7001/api/token"  # URL para obtener el token

# Función para imprimir urls
def print_urls(url):
    print("🧾 ============================")
    print(f"🌐 Original: {url.get('urlOriginal', 'No disponible')}")
    print(f"🪄 Acortada: {url.get('urlAcortada', 'No disponible')}")
    print(f"📈 Accesos: {url.get('cantidadAccesos', 'No disponible')}")
    print(f"🕒 Creación: {url.get('fechaCreacion', 'No disponible')}")
    imagen_base64 = url.get('imagenBase64', '')
    print(f"🖼️ Imagen (base64): {'(vacía)' if not imagen_base64 else 'sí'}")
    print("================================")

# Función para obtener el token JWT
def get_token():
    # Realizamos la solicitud GET para obtener el token
    response = requests.get(token_url)
    
    if response.status_code == 200:
        token = response.json().get("jwt")  # El token se encuentra en "jwt"
        if token:
            return token
        else:
            print("No se recibió un token válido.")
            return None
    else:
        print(f"Error {response.status_code}: {response.text}")
        return None

# Obtener la lista de URLs
def get_urls(token):
    headers = {"Authorization": f"Bearer {token}"}
    response = requests.get(base_url, headers=headers)
    if response.status_code == 200:
        urls = response.json()  # Asumiendo que la respuesta es una lista de URLs
        for url in urls:
            print_urls(url)
            
    else:
        print(f"Error {response.status_code}: {response.text}")

# Crear una nueva URL
def post_url(token):
    url_original = input("Ingresa la URL completa: ")
    data = {
        "urlOriginal": url_original,  # Solo necesitamos urlOriginal
    }
    headers = {"Authorization": f"Bearer {token}"}
    response = requests.post(base_url, json=data, headers=headers)
    if response.status_code == 201:
        print_urls(response.json())  # Imprimir la URL creada
    else:
        print(f"Error {response.status_code}: {response.text}")

# Actualizar una URL existente
def put_url(token):
    url_id = input("ID de la URL a actualizar: ")
    url_original = input("Nueva URL original: ")  # Solo se requiere la nueva URL original
    data = {
        "urlOriginal": url_original,  # Solo actualizar la URL original
    }
    headers = {"Authorization": f"Bearer {token}"}
    response = requests.put(f"{base_url}/{url_id}", json=data, headers=headers)
    if response.status_code == 201:
        print_urls(response.json())  # Imprimir la URL creada

    else:
        print(f"Error {response.status_code}: {response.text}")

# Eliminar una URL
def delete_url(token):
    url_id = input("ID de la URL a eliminar: ")
    headers = {"Authorization": f"Bearer {token}"}
    response = requests.delete(f"{base_url}/{url_id}", headers=headers)
    if response.status_code == 204:
        print("URL eliminada correctamente.")
    else:
        print(f"Error {response.status_code}: {response.text}")

# Menú
print("Selecciona una operación:")
print("1. GET (listar URLs)")
print("2. POST (crear URL)")
print("3. PUT (actualizar URL)")
print("4. DELETE (eliminar URL)")

opcion = input("Opción: ")

# Solicitar el token JWT antes de realizar operaciones
token = get_token()
if token:
    if opcion == "1":
        get_urls(token)
    elif opcion == "2":
        post_url(token)
    elif opcion == "3":
        put_url(token)
    elif opcion == "4":
        delete_url(token)
    else:
        print("Opción no válida.")
else:
    print("No se pudo obtener el token. Intenta de nuevo.")
