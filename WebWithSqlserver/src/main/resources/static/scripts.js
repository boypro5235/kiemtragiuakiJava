// Fetch all products from the API and display them
function fetchProducts() {
    fetch('/products/getAll')
        .then(response => response.json())
        .then(products => {
            console.log('Products:', products);
            const productList = document.getElementById('productList');
            productList.innerHTML = '';
            if (products.length === 0) {
                productList.textContent = 'No products available';
            } else {
                products.forEach(product => {
                    const li = document.createElement('li');
                    li.innerHTML = `
                        <strong>${product.name}</strong><br>
                        Price: $${product.price}<br>
                        Discount Price: $${product.discountPrice || 'N/A'}<br>
                        Description: ${product.description}<br>
                        Category: ${product.category}<br>
                        Status: ${product.status || 'N/A'}
                        <div class="product-actions">
                            <button onclick="editProduct(${product.id})">Edit</button>
                            <button onclick="deleteProduct(${product.id})">Delete</button>
                        </div>
                    `;
                    productList.appendChild(li);
                });
            }
        })
        .catch(error => console.error('Error fetching products:', error));
}

// Handle form submission to create a new product
document.getElementById('createProductForm').addEventListener('submit', function(event) {
    event.preventDefault();

    const productData = {
        name: document.getElementById('name').value,
        price: document.getElementById('price').value,
        discountPrice: document.getElementById('discountPrice').value,
        description: document.getElementById('description').value,
        category: document.getElementById('category').value,
    };

    fetch('/products/create', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(productData),
    })
    .then(response => {
        if (response.ok) {
            alert('Product created successfully!');
            document.getElementById('createProductForm').reset();
            fetchProducts(); // Refresh the product list
        } else {
            alert('Failed to create product.');
        }
    })
    .catch(error => console.error('Error creating product:', error));
});

// Handle form submission to search for a product by ID
document.getElementById('searchProductForm').addEventListener('submit', function(event) {
    event.preventDefault();

    const productId = document.getElementById('searchId').value;

    fetch(`/products/getById/${productId}`)
        .then(response => response.json())
        .then(product => {
            const searchResult = document.getElementById('searchResult');
            searchResult.innerHTML = '';
            if (product) {
                searchResult.innerHTML = `
                    <strong>${product.name}</strong><br>
                    Price: $${product.price}<br>
                    Discount Price: $${product.discountPrice || 'N/A'}<br>
                    Description: ${product.description}<br>
                    Category: ${product.category}<br>
                    Status: ${product.status || 'N/A'}
                `;
            } else {
                searchResult.textContent = 'Product not found';
            }
        })
        .catch(error => console.error('Error fetching product by ID:', error));
});

// Edit a product
function editProduct(id) {
    const name = prompt('Enter new name:');
    const price = prompt('Enter new price:');
    const discountPrice = prompt('Enter new discount price:');
    const description = prompt('Enter new description:');
    const category = prompt('Enter new category:');

    const productData = {
        name: name,
        price: price,
        discountPrice: discountPrice,
        description: description,
        category: category,
    };

    fetch(`/products/${id}`, {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(productData),
    })
    .then(response => {
        if (response.ok) {
            alert('Product updated successfully!');
            fetchProducts(); // Refresh the product list
        } else {
            alert('Failed to update product.');
        }
    })
    .catch(error => console.error('Error updating product:', error));
}

// Delete a product
function deleteProduct(id) {
    if (confirm('Are you sure you want to delete this product?')) {
        fetch(`/products/${id}`, {
            method: 'DELETE',
        })
        .then(response => {
            if (response.ok) {
                alert('Product deleted successfully!');
                fetchProducts(); // Refresh the product list
            } else {
                alert('Failed to delete product.');
            }
        })
        .catch(error => console.error('Error deleting product:', error));
    }
}
