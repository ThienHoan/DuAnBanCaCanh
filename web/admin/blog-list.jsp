<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý Blog - Admin</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.7.2/font/bootstrap-icons.css" rel="stylesheet">    <link href="https://cdn.jsdelivr.net/npm/datatables.net-bs5@1.11.5/css/dataTables.bootstrap5.min.css" rel="stylesheet">
    <style>
        .status-badge {
            font-size: 0.875rem;
        }
        .action-buttons .btn {
            padding: 0.25rem 0.5rem;
            font-size: 0.875rem;
        }
        .content-preview {
            max-width: 300px;
            overflow: hidden;
            text-overflow: ellipsis;
            white-space: nowrap;
        }
        .card {
            border: none;
            border-radius: 15px;
            box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
        }
        
        /* Table improvements */
        .table-responsive {
            overflow-x: auto;
            -webkit-overflow-scrolling: touch;
        }
        
        #postsTable {
            font-size: 0.9rem;
        }
        
        #postsTable th {
            border-bottom: 2px solid #dee2e6;
            font-weight: 600;
            white-space: nowrap;
        }
        
        #postsTable td {
            padding: 0.75rem 0.5rem;
            border-bottom: 1px solid #dee2e6;
        }
        
        .action-buttons {
            white-space: nowrap;
        }
        
        .action-buttons .btn {
            margin: 0 2px 2px 0;
        }
        
        /* Responsive adjustments */
        @media (max-width: 768px) {
            .action-buttons {
                min-width: 150px;
            }
            
            .action-buttons .btn {
                font-size: 0.8rem;
                padding: 0.2rem 0.4rem;
            }
        }
    </style>
</head>
<body>
    <div class="container-fluid">        <div class="row">
            <!-- Sidebar -->
            <jsp:include page="includes/sidebar.jsp">
                <jsp:param name="page" value="blog" />
                <jsp:param name="subpage" value="list" />
            </jsp:include>

            <!-- Main content -->
            <main class="col-md-9 ms-sm-auto col-lg-10 px-md-4">
                <div class="d-flex justify-content-between flex-wrap flex-md-nowrap align-items-center pt-3 pb-2 mb-3 border-bottom">
                    <h1 class="h2">Quản lý Blog</h1>
                    <div class="btn-toolbar mb-2 mb-md-0">
                        <div class="btn-group me-2">
                            <a href="${pageContext.request.contextPath}/admin-blog-form?action=create" class="btn btn-primary">
                                <i class="bi bi-plus-circle"></i> Tạo bài viết mới
                            </a>
                            <a href="${pageContext.request.contextPath}/admin-categories" class="btn btn-outline-secondary">
                                <i class="bi bi-tags"></i> Quản lý danh mục
                            </a>
                        </div>
                    </div>
                </div>

                <!-- Messages -->
                <c:if test="${not empty sessionScope.successMessage}">
                    <div class="alert alert-success alert-dismissible fade show" role="alert">
                        ${sessionScope.successMessage}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                    <c:remove var="successMessage" scope="session" />
                </c:if>

                <c:if test="${not empty sessionScope.errorMessage}">
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        ${sessionScope.errorMessage}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                    <c:remove var="errorMessage" scope="session" />
                </c:if>

                <c:if test="${not empty errorMessage}">
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        ${errorMessage}
                        <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
                    </div>
                </c:if>

                <!-- Posts Table -->
                <div class="card">
                    <div class="card-header">
                        <h5 class="card-title mb-0">Danh sách bài viết</h5>
                    </div>
                    <div class="card-body">
                        <c:choose>
                            <c:when test="${empty posts}">
                                <div class="text-center py-5">
                                    <i class="bi bi-journal-x fs-1 text-muted"></i>
                                    <p class="text-muted mt-3">Chưa có bài viết nào.</p>
                                    <a href="${pageContext.request.contextPath}/admin-blog-form?action=create" class="btn btn-primary">
                                        Tạo bài viết đầu tiên
                                    </a>
                                </div>
                            </c:when>
                            <c:otherwise>                                <div class="table-responsive">
                                    <table class="table table-striped table-hover" id="postsTable" style="min-width: 1200px;">
                                        <thead class="table-dark">
                                            <tr>
                                                <th style="width: 60px;">ID</th>
                                                <th style="width: 300px;">Tiêu đề</th>
                                                <th style="width: 120px;">Danh mục</th>
                                                <th style="width: 100px;">Tác giả</th>
                                                <th style="width: 120px;">Trạng thái</th>
                                                <th style="width: 100px;">Lượt xem</th>
                                                <th style="width: 140px;">Ngày tạo</th>
                                                <th style="width: 200px;">Hành động</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                            <c:forEach var="post" items="${posts}">                                                <tr>
                                                    <td style="vertical-align: middle;">${post.postId}</td>
                                                    <td style="max-width: 300px;">
                                                        <div>
                                                            <strong style="display: block; word-wrap: break-word;">
                                                                <c:choose>
                                                                    <c:when test="${fn:length(post.title) > 60}">
                                                                        <span title="${fn:escapeXml(post.title)}">
                                                                            ${fn:substring(post.title, 0, 60)}...
                                                                        </span>
                                                                    </c:when>
                                                                    <c:otherwise>
                                                                        ${fn:escapeXml(post.title)}
                                                                    </c:otherwise>
                                                                </c:choose>
                                                            </strong>
                                                            <c:if test="${not empty post.summary}">
                                                                <small class="text-muted d-block mt-1" style="font-size: 0.8em; line-height: 1.2;">
                                                                    <c:choose>
                                                                        <c:when test="${fn:length(post.summary) > 80}">
                                                                            ${fn:substring(post.summary, 0, 80)}...
                                                                        </c:when>
                                                                        <c:otherwise>
                                                                            ${fn:escapeXml(post.summary)}
                                                                        </c:otherwise>
                                                                    </c:choose>
                                                                </small>
                                                            </c:if>
                                                        </div>
                                                    </td>                                                    <td style="vertical-align: middle;">
                                                        <c:choose>
                                                            <c:when test="${post.categoryObject != null}">
                                                                <span class="badge bg-info">${fn:escapeXml(post.categoryObject.categoryName)}</span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="badge bg-secondary">Chưa phân loại</span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td style="vertical-align: middle;">
                                                        <small>${fn:escapeXml(post.authorName)}</small>
                                                    </td>                                                    <td style="vertical-align: middle;">
                                                        <c:choose>
                                                            <c:when test="${post.status == 'published'}">
                                                                <span class="badge bg-success status-badge">Đã xuất bản</span>
                                                            </c:when>
                                                            <c:when test="${post.status == 'draft'}">
                                                                <span class="badge bg-warning status-badge">Bản nháp</span>
                                                            </c:when>
                                                            <c:otherwise>
                                                                <span class="badge bg-secondary status-badge">${fn:escapeXml(post.status)}</span>
                                                            </c:otherwise>
                                                        </c:choose>
                                                    </td>
                                                    <td style="vertical-align: middle; text-align: center;">
                                                        <small><i class="bi bi-eye"></i> ${post.viewCount}</small>
                                                    </td>
                                                    <td style="vertical-align: middle;">
                                                        <small>
                                                            <fmt:formatDate value="${post.createdAt}" pattern="dd/MM/yyyy"/>
                                                            <br>
                                                            <fmt:formatDate value="${post.createdAt}" pattern="HH:mm"/>
                                                        </small>
                                                    </td>
                                                    <td style="vertical-align: middle;">
                                                        <div class="action-buttons d-flex flex-wrap gap-1"
                                                             style="min-width: 180px;">
                                                            <!-- View button -->
                                                            <c:if test="${post.status == 'published'}">
                                                                <a href="${pageContext.request.contextPath}/blog-detail?id=${post.postId}" 
                                                                   class="btn btn-outline-info btn-sm" target="_blank" title="Xem bài viết">
                                                                    <i class="bi bi-eye"></i>
                                                                </a>
                                                            </c:if>
                                                            
                                                            <!-- Edit button -->
                                                            <a href="${pageContext.request.contextPath}/admin-blog-form?action=edit&id=${post.postId}" 
                                                               class="btn btn-outline-primary btn-sm" title="Chỉnh sửa">
                                                                <i class="bi bi-pencil"></i>
                                                            </a>
                                                            
                                                            <!-- Publish/Unpublish button -->
                                                            <c:choose>
                                                                <c:when test="${post.status == 'published'}">
                                                                    <button class="btn btn-outline-warning btn-sm status-btn" title="Ẩn bài viết"
                                                                            data-post-id="${post.postId}" data-action="unpublish">
                                                                        <i class="bi bi-eye-slash"></i>
                                                                    </button>
                                                                </c:when>
                                                                <c:otherwise>
                                                                    <button class="btn btn-outline-success btn-sm status-btn" title="Xuất bản"
                                                                            data-post-id="${post.postId}" data-action="publish">
                                                                        <i class="bi bi-upload"></i>
                                                                    </button>
                                                                </c:otherwise>
                                                            </c:choose>
                                                            
                                                            <!-- Delete button -->
                                                            <button class="btn btn-outline-danger btn-sm delete-btn" title="Xóa bài viết"
                                                                    data-post-id="${post.postId}" data-post-title="${fn:escapeXml(post.title)}">
                                                                <i class="bi bi-trash"></i>
                                                            </button>
                                                        </div>
                                                    </td>
                                                </tr>
                                            </c:forEach>
                                        </tbody>
                                    </table>
                                </div>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </main>
        </div>
    </div>

    <!-- Delete Confirmation Modal -->
    <div class="modal fade" id="deleteModal" tabindex="-1">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title">Xác nhận xóa</h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                </div>
                <div class="modal-body">
                    <p>Bạn có chắc muốn xóa bài viết <strong id="postTitle"></strong> không?</p>
                    <p class="text-danger">Hành động này không thể hoàn tác!</p>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">Hủy</button>
                    <button type="button" class="btn btn-danger" id="confirmDelete">Xóa</button>
                </div>
            </div>
        </div>
    </div>

    <!-- Status Toggle Form (Hidden) -->
    <form id="statusForm" method="post" action="${pageContext.request.contextPath}/admin-blog-action" style="display: none;">
        <input type="hidden" name="action" id="statusAction">
        <input type="hidden" name="id" id="statusPostId">
    </form>

    <!-- Delete Form (Hidden) -->
    <form id="deleteForm" method="post" action="${pageContext.request.contextPath}/admin-blog-action" style="display: none;">
        <input type="hidden" name="action" value="delete">
        <input type="hidden" name="id" id="deletePostId">
    </form>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/jquery@3.6.0/dist/jquery.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/datatables.net@1.11.5/js/jquery.dataTables.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/datatables.net-bs5@1.11.5/js/dataTables.bootstrap5.min.js"></script>
      <script>
        $(document).ready(function() {
            // Initialize DataTables
            $('#postsTable').DataTable({
                "language": {
                    "url": "//cdn.datatables.net/plug-ins/1.11.5/i18n/vi.json"
                },
                "order": [[ 0, "desc" ]],
                "pageLength": 25,
                "responsive": true
            });

            // Handle status toggle buttons
            $('.status-btn').click(function() {
                const postId = $(this).data('post-id');
                const action = $(this).data('action');
                togglePostStatus(postId, action);
            });

            // Handle delete buttons
            $('.delete-btn').click(function() {
                const postId = $(this).data('post-id');
                const postTitle = $(this).data('post-title');
                deletePost(postId, postTitle);
            });
        });

        function togglePostStatus(postId, action) {
            document.getElementById('statusAction').value = action;
            document.getElementById('statusPostId').value = postId;
            document.getElementById('statusForm').submit();
        }

        function deletePost(postId, postTitle) {
            document.getElementById('postTitle').textContent = postTitle;
            document.getElementById('deletePostId').value = postId;
            
            const deleteModal = new bootstrap.Modal(document.getElementById('deleteModal'));
            deleteModal.show();
            
            document.getElementById('confirmDelete').onclick = function() {
                document.getElementById('deleteForm').submit();
            };
        }
    </script>
</body>
</html>
