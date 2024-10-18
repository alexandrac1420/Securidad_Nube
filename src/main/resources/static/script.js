const apiUrl = 'https://serverspring.duckdns.org';

let currentPage = 0;  
const pageSize = 5;   
let totalPages = 0;   

// Redireccionar a la página de login si no hay sesión iniciada
window.addEventListener('load', function () {
    const currentPage = window.location.pathname;

    if (!localStorage.getItem('loggedIn') && 
        !currentPage.includes('login.html') && 
        !currentPage.includes('register.html')) {
        window.location.href = 'login.html';
    }
});

// **Manejo del formulario de inicio de sesión**
document.addEventListener('DOMContentLoaded', function () {
    console.log('DOM cargado');

    // Manejo del formulario de inicio de sesión solo si estamos en la página de login
    if (document.getElementById('loginForm')) {
        const loginForm = document.getElementById('loginForm');
        loginForm.addEventListener('submit', function (event) {
            event.preventDefault();
            const username = document.getElementById('loginUsername').value;
            const password = document.getElementById('loginPassword').value;

            fetch(`${apiUrl}/users/authenticate`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ username, password }),
                credentials: 'include' 
            })
            .then(response => {
                if (response.ok) {
                    localStorage.setItem('loggedIn', 'true');
                    alert('Inicio de sesión exitoso');
                    window.location.href = 'index.html';
                } else {
                    alert('Credenciales inválidas');
                }
            })
            .catch(error => console.error('Error:', error));
        });
    }

    // Manejo del formulario de registro solo si estamos en la página de registro
    if (document.getElementById('registerForm')) {
        const registerForm = document.getElementById('registerForm');
        registerForm.addEventListener('submit', function (event) {
            event.preventDefault();
            const username = document.getElementById('registerUsername').value;
            const password = document.getElementById('registerPassword').value;

            fetch(`${apiUrl}/users/register`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ username, password }),
                credentials: 'include' 
            })
            .then(response => {
                if (response.ok) {
                    alert('Usuario registrado exitosamente');
                    window.location.href = 'login.html';
                } else {
                    alert('Error al registrar el usuario');
                }
            })
            .catch(error => console.error('Error:', error));
        });
    }
});

// **Cerrar sesión**
document.getElementById('logoutButton')?.addEventListener('click', function () {
    localStorage.removeItem('loggedIn'); 
    alert('Sesión cerrada');
    window.location.href = 'login.html';
});

// **Buscar propiedades**
document.getElementById('searchQuery')?.addEventListener('input', function () {
    const query = document.getElementById('searchQuery').value;
    fetch(`${apiUrl}/properties/search?search=${query}&page=${currentPage}&size=${pageSize}`)
        .then(response => response.json())
        .then(data => {
            updatePropertyTable(data.content);
            updatePagination(data);
        })
        .catch(error => console.error('Error:', error));
});

// **Cargar todas las propiedades con paginación**
function loadProperties() {
    fetch(`${apiUrl}/properties?page=${currentPage}&size=${pageSize}`, {
        method: 'GET',
        credentials: 'include' 
    })
    .then(response => {
        if (!response.ok) {
            throw new Error(`Error en la solicitud: ${response.status}`);
        }
        return response.json();  
    })
    .then(data => {
        updatePropertyTable(data.content);  
        updatePagination(data);             
    })
    .catch(error => {
        console.error('Error:', error);
        alert(`Hubo un error al cargar las propiedades: ${error.message}`);
    });
}

// **Actualizar la tabla de propiedades**
function updatePropertyTable(properties) {
    const propertyTableBody = document.querySelector('#propertyTable tbody');
    propertyTableBody.innerHTML = ''; 

    properties.forEach(property => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${property.id}</td>
            <td>${property.address}</td>
            <td>$${property.price}</td>
            <td>${property.size} m²</td>
            <td>${property.description}</td>
            <td>
                <button onclick="editProperty(${property.id})">Editar</button>
                <button onclick="deleteProperty(${property.id})">Eliminar</button>
            </td>
        `;
        propertyTableBody.appendChild(row);
    });
}

// **Actualizar la paginación**
function updatePagination(data) {
    totalPages = data.totalPages;
    document.getElementById('pageInfo').textContent = `Página ${currentPage + 1} de ${totalPages}`;
}

// **Siguiente página**
function nextPage() {
    if (currentPage < totalPages - 1) {
        currentPage++;
        loadProperties();
    }
}

// **Página anterior**
function prevPage() {
    if (currentPage > 0) {
        currentPage--;
        loadProperties();
    }
}

// **Cargar propiedades al cargar la página en index.html**
if (window.location.pathname.includes('index.html')) {
    loadProperties();
}

// **Crear una nueva propiedad**
function createProperty() {
    const property = {
        address: document.getElementById('address').value,
        price: parseFloat(document.getElementById('price').value),
        size: parseFloat(document.getElementById('size').value),
        description: document.getElementById('description').value
    };

    fetch(`${apiUrl}/properties`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(property),
        credentials: 'include'
    })
    .then(response => response.json())
    .then(data => {
        console.log('Propiedad creada:', data);
        loadProperties();
        clearForm();
    })
    .catch(error => console.error('Error:', error));
}

// **Actualizar una propiedad existente**
function updateProperty(id) {
    const property = {
        address: document.getElementById('address').value,
        price: parseFloat(document.getElementById('price').value),
        size: parseFloat(document.getElementById('size').value),
        description: document.getElementById('description').value
    };

    fetch(`${apiUrl}/properties/${id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(property),
        credentials: 'include'
    })
    .then(response => response.json())
    .then(data => {
        console.log('Propiedad actualizada:', data);
        loadProperties();
        clearForm();
    })
    .catch(error => console.error('Error:', error));
}

// **Eliminar una propiedad**
function deleteProperty(id) {
    if (confirm('¿Estás seguro de que deseas eliminar esta propiedad?')) {
        fetch(`${apiUrl}/properties/${id}`, {
            method: 'DELETE',
            credentials: 'include'
        })
        .then(() => loadProperties())
        .catch(error => console.error('Error:', error));
    }
}

// **Limpiar el formulario**
function clearForm() {
    document.getElementById('propertyForm').reset();
    document.getElementById('addButton').style.display = 'block';
    document.getElementById('updateButton').style.display = 'none';
}

// **Editar propiedad**
function editProperty(id) {
    fetch(`${apiUrl}/properties/${id}`, {
        method: 'GET',
        credentials: 'include'
    })
    .then(response => response.json())
    .then(property => {
        document.getElementById('address').value = property.address;
        document.getElementById('price').value = property.price;
        document.getElementById('size').value = property.size;
        document.getElementById('description').value = property.description;

        selectedPropertyId = id;

        document.getElementById('addButton').style.display = 'none';
        document.getElementById('updateButton').style.display = 'block';
    })
    .catch(error => console.error('Error:', error));
}
