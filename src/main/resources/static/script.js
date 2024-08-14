// API base URL
const apiUrl = 'http://localhost:8080/api/products';

// DOM elements
const productTable = document.getElementById('productTable');
const searchBtn = document.getElementById('searchBtn');
const searchName = document.getElementById('searchName');
const minPrice = document.getElementById('minPrice');
const maxPrice = document.getElementById('maxPrice');
const addProductBtn = document.getElementById('addProductBtn');
const productModal = new bootstrap.Modal(document.getElementById('productModal'));
const productForm = document.getElementById('productForm');
let currentProductId = null; // For tracking update operations

// Fetch and display products
function fetchProducts(queryParams = '') {
    fetch(`${apiUrl}?${queryParams}`)
        .then(response => response.json())
        .then(products => {
            productTable.innerHTML = '';
            products.forEach(product => {
                productTable.innerHTML += `
                    <tr>
                        <td>${product.id}</td>
                        <td>${product.name}</td>
                        <td>${product.price}</td>
                        <td>${product.discountPrice}</td>
                        <td>${product.category}</td>
                        <td>${product.status}</td>
                        <td>
                            <button class="btn btn-primary btn-sm" onclick="viewProduct(${product.id})">View</button>
                            <button class="btn btn-warning btn-sm" onclick="editProduct(${product.id})">Edit</button>
                            <button class="btn btn-danger btn-sm" onclick="deleteProduct(${product.id})">Delete</button>
                        </td>
                    </tr>`;
            });
        })
        .catch(error => console.error('Error fetching products:', error));
}

// Search products
searchBtn.addEventListener('click', () => {
    const name = searchName.value;
    const min = minPrice.value;
    const max = maxPrice.value;
    let queryParams = '';
    
    if (name) queryParams += `name=${name}&`;
    if (min) queryParams += `minPrice=${min}&`;
    if (max) queryParams += `maxPrice=${max}`;
    
    fetchProducts(queryParams);
});

// Add/Edit product
productForm.addEventListener('submit', (e) => {
    e.preventDefault();
    
    const productData = {
        name: document.getElementById('productName').value,
        price: parseFloat(document.getElementById('productPrice').value),
        discountPrice: parseFloat(document.getElementById('discountPrice').value),
        category: document.getElementById('productCategory').value,
        status: document.getElementById('productStatus').value,
        description: document.getElementById('productDescription').value,
        imageUrl: document.getElementById('productImage').value
    };
    
    if (currentProductId) {
        // Update existing product
        fetch(`${apiUrl}/${currentProductId}`, {
            method: 'PUT',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(productData)
        })
        .then(response => response.json())
        .then(() => {
            fetchProducts();
            productModal.hide();
        })
        .catch(error => console.error('Error updating product:', error));
    } else {
        // Add new product
        fetch(apiUrl, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(productData)
        })
        .then(response => response.json())
        .then(() => {
            fetchProducts();
            productModal.hide();
        })
        .catch(error => console.error('Error adding product:', error));
    }
});

// View product details (for updating)
function viewProduct(id) {
    fetch(`${apiUrl}/${id}`)
        .then(response => response.json())
        .then(product => {
            currentProductId = product.id;
            document.getElementById('productName').value = product.name;
            document.getElementById('productPrice').value = product.price;
            document.getElementById('discountPrice').value = product.discountPrice;
            document.getElementById('productCategory').value = product.category;
            document.getElementById('productStatus').value = product.status;
            document.getElementById('productDescription').value = product.description;
            document.getElementById('productImage').value = product.imageUrl;
            
            productModal.show();
        })
        .catch(error => console.error('Error fetching product:', error));
}

// Edit product (same as view but opens in edit mode)
function editProduct(id) {
    viewProduct(id);
}

// Delete product
function deleteProduct(id) {
    if (confirm('Are you sure you want to delete this product?')) {
        fetch(`${apiUrl}/${id}`, {
            method: 'DELETE'
        })
        .then(() => {
            fetchProducts();
        })
        .catch(error => console.error('Error deleting product:', error));
    }
}

// Handle add product button click
addProductBtn.addEventListener('click', () => {
    currentProductId = null;
    productForm.reset();
    productModal.show();
});

// Initial fetch of products
fetchProducts();
