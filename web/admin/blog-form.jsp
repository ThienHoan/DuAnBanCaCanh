<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>
        <c:choose>
            <c:when test="${action == 'edit'}">Chỉnh sửa bài viết</c:when>
            <c:otherwise>Tạo bài viết mới</c:otherwise>
        </c:choose>
        - Admin
    </title>    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.2/font/bootstrap-icons.css" rel="stylesheet">
    <!-- TinyMCE Editor -->
    <c:choose>
        <c:when test="${not empty tinyMCEApiKey and tinyMCEApiKey != 'no-api-key'}">
            <script src="https://cdn.tiny.cloud/1/${tinyMCEApiKey}/tinymce/${tinyMCEVersion}/tinymce.min.js" referrerpolicy="origin"></script>
        </c:when>
        <c:otherwise>
            <!-- Fallback to self-hosted TinyMCE or show warning -->
            <script src="https://cdn.tiny.cloud/1/no-api-key/tinymce/6/tinymce.min.js" referrerpolicy="origin"></script>
        </c:otherwise>
    </c:choose>    <style>
        .card {
            border: none;
            border-radius: 15px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        }
        .form-label {
            font-weight: 600;
        }
        .required {
            color: red;
        }
    </style>
</head>
<body>
    <div class="container-fluid">        <div class="row">
            <!-- Sidebar -->
            <jsp:include page="includes/sidebar.jsp">
                <jsp:param name="page" value="blog" />
                <jsp:param name="subpage" value="form" />
            </jsp:include>

            <!-- Main content -->
            <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4">
                <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                    <h1 class="h2">
                        <c:choose>
                            <c:when test="${action == 'edit'}">Chỉnh sửa bài viết</c:when>
                            <c:otherwise>Tạo bài viết mới</c:otherwise>
                        </c:choose>
                    </h1>
                    <div class="btn-toolbar mb-2 mb-md-0">
                        <a href="${pageContext.request.contextPath}/admin-blogs" class="btn btn-outline-secondary">
                            <i class="bi bi-arrow-left"></i> Quay lại danh sách
                        </a>
                    </div>
                </div>

                <!-- Error Messages -->
                <c:if test="${not empty errorMessage}">
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        ${errorMessage}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>

                <!-- Form -->
                <div class="card">
                    <div class="card-body">
                        <form id="blogForm" method="post" action="${pageContext.request.contextPath}/admin-blog-action">
                            <input type="hidden" name="action" value="${action == 'edit' ? 'update' : 'create'}">
                            <c:if test="${action == 'edit' && not empty post}">
                                <input type="hidden" name="id" value="${post.postId}">
                            </c:if>

                            <div class="row">
                                <div class="col-md-8">
                                    <!-- Title -->
                                    <div class="mb-3">
                                        <label for="title" class="form-label">
                                            Tiêu đề <span class="required">*</span>
                                        </label>
                                        <input type="text" class="form-control" id="title" name="title" 
                                               value="${fn:escapeXml(post.title)}" required>
                                    </div>

                                    <!-- Summary -->
                                    <div class="mb-3">
                                        <label for="summary" class="form-label">Tóm tắt</label>
                                        <textarea class="form-control" id="summary" name="summary" rows="3"
                                                  placeholder="Tóm tắt ngắn gọn về nội dung bài viết...">${fn:escapeXml(post.summary)}</textarea>
                                        <div class="form-text">Tóm tắt sẽ hiển thị trong danh sách bài viết</div>
                                    </div>

                                    <!-- Content -->
                                    <div class="mb-3">
                                        <label for="content" class="form-label">
                                            Nội dung <span class="required">*</span>
                                        </label>
                                        <textarea class="form-control" id="content" name="content" rows="20" required>${post.content}</textarea>
                                    </div>
                                </div>

                                <div class="col-md-4">
                                    <!-- Status -->
                                    <div class="mb-3">
                                        <label for="status" class="form-label">Trạng thái</label>
                                        <select class="form-select" id="status" name="status">
                                            <option value="draft" ${post.status == 'draft' ? 'selected' : ''}>Bản nháp</option>
                                            <option value="published" ${post.status == 'published' ? 'selected' : ''}>Xuất bản</option>
                                        </select>
                                    </div>                                    <!-- Category -->
                                    <div class="mb-3">
                                        <label for="categoryId" class="form-label">Danh mục</label>
                                        <select class="form-select" id="categoryId" name="categoryId" required>
                                            <option value="">Chọn danh mục...</option>
                                            <c:forEach var="cat" items="${categories}">
                                                <option value="${cat.categoryId}" 
                                                        ${post.categoryId == cat.categoryId ? 'selected' : ''}>
                                                    ${fn:escapeXml(cat.categoryName)}
                                                </option>
                                            </c:forEach>
                                        </select>
                                        <div class="form-text">
                                            <a href="${pageContext.request.contextPath}/admin-blog-categories" target="_blank">
                                                Quản lý danh mục
                                            </a>
                                        </div>
                                    </div>

                                    <!-- Featured Image -->
                                    <div class="mb-3">
                                        <label for="featuredImage" class="form-label">Ảnh đại diện</label>
                                        <input type="url" class="form-control" id="featuredImage" name="featuredImage" 
                                               value="${fn:escapeXml(post.featuredImage)}"
                                               placeholder="https://example.com/image.jpg">
                                        <div class="form-text">URL của ảnh đại diện cho bài viết</div>
                                        
                                        <!-- Image Preview -->
                                        <c:if test="${not empty post.featuredImage}">
                                            <div class="mt-2">
                                                <img src="${post.featuredImage}" alt="Preview" class="img-thumbnail" 
                                                     style="max-width: 200px; max-height: 150px;" id="imagePreview">
                                            </div>
                                        </c:if>
                                    </div>

                                    <!-- Tags -->
                                    <div class="mb-3">
                                        <label for="tags" class="form-label">Tags</label>
                                        <input type="text" class="form-control" id="tags" name="tags" 
                                               value="${fn:escapeXml(post.tags)}"
                                               placeholder="tag1, tag2, tag3">
                                        <div class="form-text">Các tags cách nhau bằng dấu phẩy</div>
                                    </div>                                    <!-- Action Buttons -->
                                    <div class="d-grid gap-2">
                                        <button type="submit" class="btn btn-primary">
                                            <i class="bi bi-check-circle"></i>
                                            <c:choose>
                                                <c:when test="${action == 'edit'}">Cập nhật bài viết</c:when>
                                                <c:otherwise>Tạo bài viết</c:otherwise>
                                            </c:choose>
                                        </button>
                                        
                                        <c:if test="${action == 'edit' && post.status == 'published'}">
                                            <a href="${pageContext.request.contextPath}/blog-detail?id=${post.postId}" 
                                               class="btn btn-outline-info" target="_blank">
                                                <i class="bi bi-eye"></i> Xem bài viết
                                            </a>
                                        </c:if>
                                        
                                        <button type="button" class="btn btn-outline-secondary" onclick="saveDraft()">
                                            <i class="bi bi-save"></i> Lưu nháp
                                        </button>
                                        
                                        <a href="${pageContext.request.contextPath}/admin-blogs" class="btn btn-outline-danger">
                                            <i class="bi bi-x-circle"></i> Hủy
                                        </a>
                                    </div>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </main>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    
    <script>        // Initialize TinyMCE Editor
        tinymce.init({
            selector: '#content',
            height: 500,
            menubar: false,
            plugins: [
                'advlist', 'autolink', 'lists', 'link', 'image', 'charmap', 'preview',
                'anchor', 'searchreplace', 'visualblocks', 'code', 'fullscreen',
                'insertdatetime', 'media', 'table', 'help', 'wordcount'
            ],
            toolbar: 'undo redo | blocks | ' +
                'bold italic backcolor | alignleft aligncenter ' +
                'alignright alignjustify | bullist numlist outdent indent | ' +
                'removeformat | help',
            content_style: 'body { font-family:Helvetica,Arial,sans-serif; font-size:14px }',
            language: 'vi',
            setup: function(editor) {
                // Sync content to textarea when editor changes
                editor.on('change keyup', function() {
                    editor.save();
                });
            }
        });

        // Image preview functionality
        document.getElementById('featuredImage').addEventListener('input', function() {
            const url = this.value;
            const preview = document.getElementById('imagePreview');
            
            if (url) {
                if (!preview) {
                    const img = document.createElement('img');
                    img.id = 'imagePreview';
                    img.className = 'img-thumbnail mt-2';
                    img.style.maxWidth = '200px';
                    img.style.maxHeight = '150px';
                    this.parentNode.appendChild(img);
                }
                document.getElementById('imagePreview').src = url;
            } else if (preview) {
                preview.remove();
            }
        });        // Save as draft function
        function saveDraft() {
            // Sync TinyMCE content before saving
            try {
                const editor = tinymce.get('content');
                if (editor) {
                    editor.save();
                }
            } catch (error) {
                console.warn('Could not sync TinyMCE content:', error);
            }
            
            document.getElementById('status').value = 'draft';
            document.getElementById('blogForm').submit();
        }// Form validation
        document.getElementById('blogForm').addEventListener('submit', function(e) {
            console.log('Form submit event triggered');
            
            // Sync TinyMCE content to textarea before validation
            try {
                const editor = tinymce.get('content');
                if (editor) {
                    editor.save(); // This syncs content to the textarea
                    console.log('TinyMCE content synced to textarea');
                }
            } catch (error) {
                console.warn('Could not sync TinyMCE content:', error);
            }
            
            const title = document.getElementById('title').value.trim();
            
            if (!title) {
                console.log('Title validation failed');
                e.preventDefault();
                alert('Vui lòng nhập tiêu đề bài viết!');
                document.getElementById('title').focus();
                return false;
            }

            // Simple content validation - just check textarea value
            const content = document.getElementById('content').value.trim();
            console.log('Content length:', content.length);
            
            if (!content) {
                console.log('Content validation failed');
                e.preventDefault();
                alert('Vui lòng nhập nội dung bài viết!');
                document.getElementById('content').focus();
                return false;
            }

            // Check category selection
            const categoryId = document.getElementById('categoryId').value;
            if (!categoryId) {
                console.log('Category validation failed');
                e.preventDefault();
                alert('Vui lòng chọn danh mục!');
                document.getElementById('categoryId').focus();
                return false;
            }

            console.log('Form validation passed, submitting...');
            return true;
        });
    </script>
</body>
</html>
