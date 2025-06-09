<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Product Manager</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>
    <body>
        <div class="container-fluid mt-5">
            <div class="row">
                <div class="col-md-12">
                    <div class="card">
                        <div class="card-header">
                            <h3 class="card-title">Product List</h3>
                            <a href="addProduct" class="btn btn-primary float-end">Add New Product</a>
                        </div>
                        <div class="card-body">
                            <div class="table-responsive">
                                <table class="table table-striped table-bordered">
                                    <thead>
                                        <tr>
                                            <th>ID</th>
                                            <th>Category ID</th>
                                            <th>Name</th>
                                            <th>Description</th>
                                            <th>Short Description</th>
                                            <th>Price</th>
                                            <th>Sale Price</th>
                                            <th>Quantity</th>
                                            <th>SKU</th>
                                            <th>Status</th>
                                            <th>Featured</th>
                                            <th>Created At</th>
                                            <th>Updated At</th>
                                            <th>Is Deleted</th>
                                            <th>Actions</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach items="${products}" var="p">
                                            <tr>
                                                <td>${p.productId}</td>
                                                <td>${p.categoryId}</td>
                                                <td>${p.name}</td>
                                                <td>${p.description}</td>
                                                <td>${p.shortDescription}</td>
                                                <td>${p.price}</td>
                                                <td>${p.salePrice}</td>
                                                <td>${p.quantity}</td>
                                                <td>${ps.sku}</td>
                                                <td>${p.status}</td>
                                                <td>${p.getFeatured() == 1 ? 'yes' : 'no'}</td>
                                                <td>${p.createdAt}</td>
                                                <td>${p.updatedAt}</td>
                                                <td>${p.isDeleted}</td>
                                                <td>
                                                    <a href="editProduct?id=${p.productId}" class="btn btn-sm btn-warning">Edit</a>
                                                    <c:choose>
        <c:when test="${p.isDeleted == 0}">
            <a href="deleteProduct?id=${p.productId}" class="btn btn-sm btn-danger" 
               onclick="return confirm('Are you sure you want to delete this product?')">Delete</a>
        </c:when>
        <c:otherwise>
            <a href="deleteProduct?id=${p.productId}" class="btn btn-sm btn-success" 
               onclick="return confirm('Are you sure you want to restore this product?')">Restore</a>
        </c:otherwise>
    </c:choose>
                                                </td>
                                            </tr>
                                        </c:forEach>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- Thêm modal vào cuối body, trước script -->
<div class="modal fade" id="addProductModal" tabindex="-1">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Add New Product</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
                <form id="addProductForm">
                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label class="form-label">Category ID</label>
                            <input type="number" class="form-control" name="categoryId" required>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label class="form-label">Name</label>
                            <input type="text" class="form-control" name="name" required>
                        </div>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Description</label>
                        <textarea class="form-control" name="description" rows="3"></textarea>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Short Description</label>
                        <textarea class="form-control" name="shortDescription" rows="2"></textarea>
                    </div>
                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label class="form-label">Price</label>
                            <input type="number" step="0.01" class="form-control" name="price" required>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label class="form-label">Sale Price</label>
                            <input type="number" step="0.01" class="form-control" name="salePrice">
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-4 mb-3">
                            <label class="form-label">Quantity</label>
                            <input type="number" class="form-control" name="quantity" required>
                        </div>
                        <div class="col-md-4 mb-3">
                            <label class="form-label">SKU</label>
                            <input type="text" class="form-control" name="sku">
                        </div>
                        <div class="col-md-4 mb-3">
                            <label class="form-label">Status</label>
                            <select class="form-control" name="status">
                                <option value="active">Active</option>
                                <option value="inactive">Inactive</option>
                            </select>
                        </div>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Featured</label>
                        <select class="form-control" name="featured">
                            <option value="1">Yes</option>
                            <option value="0">No</option>
                        </select>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                <button type="button" class="btn btn-primary" onclick="submitProduct()">Add Product</button>
            </div>
        </div>
    </div>
</div>

<!-- Thêm modal edit vào cuối body, trước script -->
<div class="modal fade" id="editProductModal" tabindex="-1">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title">Edit Product</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
            </div>
            <div class="modal-body">
                <form id="editProductForm">
                    <input type="hidden" name="productId" id="edit_productId">
                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label class="form-label">Category ID</label>
                            <input type="number" class="form-control" name="categoryId" id="edit_categoryId" required>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label class="form-label">Name</label>
                            <input type="text" class="form-control" name="name" id="edit_name" required>
                        </div>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Description</label>
                        <textarea class="form-control" name="description" id="edit_description" rows="3"></textarea>
                    </div>
                    <div class="mb-3">
                        <label class="form-label">Short Description</label>
                        <textarea class="form-control" name="shortDescription" id="edit_shortDescription" rows="2"></textarea>
                    </div>
                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label class="form-label">Price</label>
                            <input type="number" step="0.01" class="form-control" name="price" id="edit_price" required>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label class="form-label">Sale Price</label>
                            <input type="number" step="0.01" class="form-control" name="salePrice" id="edit_salePrice">
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-4 mb-3">
                            <label class="form-label">Quantity</label>
                            <input type="number" class="form-control" name="quantity" id="edit_quantity" required>
                        </div>
                        <div class="col-md-4 mb-3">
                            <label class="form-label">SKU</label>
                            <input type="text" class="form-control" name="sku" id="edit_sku">
                        </div>
                        <div class="col-md-4 mb-3">
                            <label class="form-label">Status</label>
                            <select class="form-control" name="status" id="edit_status">
                                <option value="active">Active</option>
                                <option value="inactive">Inactive</option>
                            </select>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <label class="form-label">Featured</label>
                            <select class="form-control" name="featured" id="edit_featured">
                                <option value="1">Yes</option>
                                <option value="0">No</option>
                            </select>
                        </div>
                        <div class="col-md-6 mb-3">
                            <label class="form-label">Is Deleted</label>
                            <select class="form-control" name="isDeleted" id="edit_isDeleted">
                                <option value="0">No</option>
                                <option value="1">Yes</option>
                            </select>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Cancel</button>
                <button type="button" class="btn btn-primary" onclick="submitEdit()">Save Changes</button>
            </div>
        </div>
    </div>
</div>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.0.2/dist/js/bootstrap.bundle.min.js"></script>
        <script>
    // Sửa nút Add New Product để mở modal
    document.querySelector('a[href="addProduct"]').setAttribute('data-bs-toggle', 'modal');
    document.querySelector('a[href="addProduct"]').setAttribute('data-bs-target', '#addProductModal');
    document.querySelector('a[href="addProduct"]').removeAttribute('href');

    function submitProduct() {
        const form = document.getElementById('addProductForm');
        const formData = new FormData(form);

        fetch('addProduct', {
            method: 'POST',
            body: new URLSearchParams(formData)
        })
        .then(response => {
            if (response.ok) {
                // Reload page to show new product
                window.location.reload();
            } else {
                throw new Error('Failed to add product');
            }
        })
        .catch(error => {
            alert('Error: ' + error.message);
        });
    }

function openEditModal(productId) {
    fetch('editProduct?id=' + productId)
        .then(response => response.json())
        .then(product => {
            // Điền dữ liệu vào form
            document.getElementById('edit_productId').value = product.productId;
            document.getElementById('edit_categoryId').value = product.categoryId;
            document.getElementById('edit_name').value = product.name;
            document.getElementById('edit_description').value = product.description;
            document.getElementById('edit_shortDescription').value = product.shortDescription;
            document.getElementById('edit_price').value = product.price;
            document.getElementById('edit_salePrice').value = product.salePrice;
            document.getElementById('edit_quantity').value = product.quantity;
            document.getElementById('edit_sku').value = product.sku;
            document.getElementById('edit_status').value = product.status;
            document.getElementById('edit_featured').value = product.featured;
            document.getElementById('edit_isDeleted').value = product.isDeleted;
            
            // Mở modal
            new bootstrap.Modal(document.getElementById('editProductModal')).show();
        })
        .catch(error => alert('Error loading product: ' + error));
}

function submitEdit() {
    const form = document.getElementById('editProductForm');
    const formData = new FormData(form);

    fetch('editProduct', {
        method: 'POST',
        body: new URLSearchParams(formData)
    })
    .then(response => response.json())
    .then(data => {
        if (data.success) {
            window.location.reload();
        } else {
            throw new Error('Failed to update product');
        }
    })
    .catch(error => alert('Error: ' + error.message));
}
</script>
    </body>
</html>