<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0-beta3/css/all.min.css">
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title><c:choose><c:when test="${product != null}">Sửa Sản Phẩm</c:when><c:otherwise>Thêm Sản Phẩm Mới</c:otherwise></c:choose></title>
                <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
                <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css">
                <link href="https://fonts.googleapis.com/css2?family=Inter:wght@300;400;500;600;700&display=swap" rel="stylesheet">
                <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
                <style>
                    
                    :root {
                        --primary-color: #4f46e5;
                        --primary-light: #6366f1;
                        --secondary-color: #64748b;
                        --success-color: #10b981;
                        --warning-color: #f59e0b;
                        --danger-color: #ef4444;
                        --background-color: #f8fafc;
                        --card-background: #ffffff;
                        --border-color: #e2e8f0;
                        --text-primary: #1e293b;
                        --text-secondary: #64748b;
                        --shadow-sm: 0 1px 2px 0 rgba(0, 0, 0, 0.05);
                        --shadow-md: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
                        --shadow-lg: 0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05);
                        --border-radius: 12px;
                        --border-radius-sm: 8px;
                    }

                    * {
                        box-sizing: border-box;
                    }

                    body {
                        font-family: 'Inter', -apple-system, BlinkMacSystemFont, sans-serif;
                        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                        min-height: 100vh;
                        color: var(--text-primary);
                        font-size: 14px;
                        line-height: 1.6;
                    }

                    .main-container {
                        padding: 2rem 1rem;
                        min-height: 100vh;
                    }

                    .form-wrapper {
                        max-width: 1200px;
                        margin: 0 auto;
                    }

                    .header-section {
                        background: var(--card-background);
                        border-radius: var(--border-radius);
                        box-shadow: var(--shadow-md);
                        padding: 2rem;
                        margin-bottom: 2rem;
                        border: 1px solid var(--border-color);
                    }

                    .header-title {
                        display: flex;
                        align-items: center;
                        gap: 1rem;
                        margin: 0;
                        color: var(--text-primary);
                        font-weight: 700;
                        font-size: 1.75rem;
                    }

                    .header-icon {
                        width: 48px;
                        height: 48px;
                        background: linear-gradient(135deg, var(--primary-color), var(--primary-light));
                        border-radius: var(--border-radius-sm);
                        display: flex;
                        align-items: center;
                        justify-content: center;
                        color: white;
                        font-size: 1.25rem;
                    }

                    .back-btn {
                        background: var(--background-color);
                        border: 1px solid var(--border-color);
                        color: var(--text-secondary);
                        padding: 0.75rem 1.5rem;
                        border-radius: var(--border-radius-sm);
                        text-decoration: none;
                        font-weight: 500;
                        transition: all 0.2s ease;
                        display: inline-flex;
                        align-items: center;
                        gap: 0.5rem;
                    }

                    .back-btn:hover {
                        background: var(--border-color);
                        color: var(--text-primary);
                        text-decoration: none;
                        transform: translateY(-1px);
                    }

                    .form-card {
                        background: var(--card-background);
                        border-radius: var(--border-radius);
                        box-shadow: var(--shadow-md);
                        border: 1px solid var(--border-color);
                        overflow: hidden;
                        margin-bottom: 2rem;
                    }

                    .form-card-header {
                        background: linear-gradient(135deg, var(--primary-color), var(--primary-light));
                        color: white;
                        padding: 1.5rem 2rem;
                        border-bottom: none;
                        display: flex;
                        align-items: center;
                        gap: 1rem;
                    }

                    .form-card-header h5 {
                        margin: 0;
                        font-weight: 600;
                        font-size: 1.1rem;
                    }

                    .form-card-body {
                        padding: 2rem;
                    }

                    .form-group {
                        margin-bottom: 1.5rem;
                    }

                    .form-label {
                        font-weight: 600;
                        color: var(--text-primary);
                        margin-bottom: 0.5rem;
                        display: block;
                        font-size: 0.875rem;
                    }

                    .required {
                        color: var(--danger-color);
                        margin-left: 0.25rem;
                    }

                    .form-control {
                        border: 2px solid var(--border-color);
                        border-radius: var(--border-radius-sm);
                        padding: 0.75rem 1rem;
                        font-size: 0.875rem;
                        transition: all 0.2s ease;
                        background: var(--card-background);
                        color: var(--text-primary);
                        height: 50px;
                    }

                    .form-control:focus {
                        border-color: var(--primary-color);
                        box-shadow: 0 0 0 3px rgba(79, 70, 229, 0.1);
                        outline: none;
                    }

                    .form-control.is-invalid {
                        border-color: var(--danger-color);
                        box-shadow: 0 0 0 3px rgba(239, 68, 68, 0.1);
                    }

                    .form-control.is-valid {
                        border-color: var(--success-color);
                        box-shadow: 0 0 0 3px rgba(16, 185, 129, 0.1);
                    }

                    .input-group {
                        position: relative;
                    }

                    .input-icon {
                        position: absolute;
                        left: 1rem;
                        top: 50%;
                        transform: translateY(-50%);
                        color: var(--text-secondary);
                        font-size: 0.875rem;
                        z-index: 5;
                    }

                    .form-control.has-icon {
                        padding-left: 2.75rem;
                    }

                    .form-text {
                        font-size: 0.75rem;
                        color: var(--text-secondary);
                        margin-top: 0.5rem;
                    }

                    .custom-checkbox {
                        position: relative;
                        padding-left: 2rem;
                    }

                    .custom-checkbox input {
                        position: absolute;
                        opacity: 0;
                        cursor: pointer;
                    }

                    .checkmark {
                        position: absolute;
                        top: 0.125rem;
                        left: 0;
                        height: 1.25rem;
                        width: 1.25rem;
                        background: var(--card-background);
                        border: 2px solid var(--border-color);
                        border-radius: 4px;
                        transition: all 0.2s ease;
                    }

                    .custom-checkbox input:checked ~ .checkmark {
                        background: var(--primary-color);
                        border-color: var(--primary-color);
                    }

                    .checkmark:after {
                        content: "";
                        position: absolute;
                        display: none;
                        left: 0.25rem;
                        top: 0.125rem;
                        width: 0.25rem;
                        height: 0.5rem;
                        border: solid white;
                        border-width: 0 2px 2px 0;
                        transform: rotate(45deg);
                    }

                    .custom-checkbox input:checked ~ .checkmark:after {
                        display: block;
                    }

                    .alert {
                        border: none;
                        border-radius: var(--border-radius-sm);
                        padding: 1rem 1.5rem;
                        margin-bottom: 1.5rem;
                        border-left: 4px solid;
                    }

                    .alert-danger {
                        background: rgba(239, 68, 68, 0.1);
                        border-left-color: var(--danger-color);
                        color: #dc2626;
                    }

                    .alert-success {
                        background: rgba(16, 185, 129, 0.1);
                        border-left-color: var(--success-color);
                        color: #059669;
                    }

                    .btn {
                        padding: 0.75rem 1.5rem;
                        border-radius: var(--border-radius-sm);
                        font-weight: 600;
                        font-size: 0.875rem;
                        border: 2px solid;
                        transition: all 0.2s ease;
                        display: inline-flex;
                        align-items: center;
                        gap: 0.5rem;
                        text-decoration: none;
                        cursor: pointer;
                    }

                    .btn-primary {
                        background: linear-gradient(135deg, var(--primary-color), var(--primary-light));
                        border-color: var(--primary-color);
                        color: white;
                    }

                    .btn-primary:hover {
                        background: linear-gradient(135deg, var(--primary-light), var(--primary-color));
                        border-color: var(--primary-light);
                        color: white;
                        transform: translateY(-2px);
                        box-shadow: var(--shadow-lg);
                    }

                    .btn-secondary {
                        background: var(--background-color);
                        border-color: var(--border-color);
                        color: var(--text-secondary);
                    }

                    .btn-secondary:hover {
                        background: var(--border-color);
                        border-color: var(--text-secondary);
                        color: var(--text-primary);
                        text-decoration: none;
                    }

                    .btn-danger {
                        background: var(--danger-color);
                        border-color: var(--danger-color);
                        color: white;
                    }

                    .btn-danger:hover {
                        background: #dc2626;
                        border-color: #dc2626;
                        color: white;
                    }

                    .image-upload-area {
                        border: 2px dashed var(--border-color);
                        border-radius: var(--border-radius-sm);
                        padding: 2rem;
                        text-align: center;
                        transition: all 0.2s ease;
                        background: var(--background-color);
                    }

                    .image-upload-area:hover {
                        border-color: var(--primary-color);
                        background: rgba(79, 70, 229, 0.05);
                    }

                    .image-upload-area.dragover {
                        border-color: var(--primary-color);
                        background: rgba(79, 70, 229, 0.1);
                    }

                    .image-preview {
                        position: relative;
                        border-radius: 8px;
                        overflow: hidden;
                        box-shadow: 0 2px 4px rgba(0,0,0,0.1);
                        transition: all 0.2s ease;
                        width: 100%;
                        background: #f8f9fa;
                    }

                    .image-preview:hover {
                        transform: translateY(-2px);
                        box-shadow: 0 4px 8px rgba(0,0,0,0.15);
                    }

                    .image-preview img {
                        width: 100%;
                        height: auto;
                        max-height: 400px;
                        object-fit: contain;
                        display: block;
                    }

                    .image-remove {
                        position: absolute;
                        top: 10px;
                        right: 10px;
                        background: rgba(255,255,255,0.9);
                        border: none;
                        border-radius: 50%;
                        width: 30px;
                        height: 30px;
                        display: flex;
                        align-items: center;
                        justify-content: center;
                        cursor: pointer;
                        color: #dc3545;
                    }

                    .image-remove:hover {
                        background: #dc3545;
                        color: white;
                    }

                    .main-image-badge {
                        position: absolute;
                        top: 10px;
                        left: 10px;
                        background: rgba(255,255,255,0.9);
                        padding: 5px 10px;
                        border-radius: 4px;
                        font-size: 0.9em;
                        color: #ffc107;
                    }

                    .form-actions {
                        background: var(--background-color);
                        padding: 1.5rem 2rem;
                        border-top: 1px solid var(--border-color);
                        display: flex;
                        justify-content: flex-end;
                        gap: 1rem;
                    }

                    .status-badge {
                        padding: 0.25rem 0.75rem;
                        border-radius: 1rem;
                        font-size: 0.75rem;
                        font-weight: 600;
                        text-transform: uppercase;
                        letter-spacing: 0.025em;
                    }

                    .status-active {
                        background: rgba(16, 185, 129, 0.1);
                        color: var(--success-color);
                    }

                    .status-inactive {
                        background: rgba(107, 114, 128, 0.1);
                        color: #6b7280;
                    }

                    .status-out-of-stock {
                        background: rgba(239, 68, 68, 0.1);
                        color: var(--danger-color);
                    }

                    .info-grid {
                        display: grid;
                        grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
                        gap: 1.5rem;
                    }

                    .fade-in {
                        animation: fadeIn 0.5s ease-in-out;
                    }

                    @keyframes fadeIn {
                        from {
                            opacity: 0;
                            transform: translateY(1rem);
                        }
                        to {
                            opacity: 1;
                            transform: translateY(0);
                        }
                    }

                    .loading-spinner {
                        display: inline-block;
                        width: 1rem;
                        height: 1rem;
                        border: 2px solid transparent;
                        border-top: 2px solid currentColor;
                        border-radius: 50%;
                        animation: spin 1s linear infinite;
                    }

                    @keyframes spin {
                        to {
                            transform: rotate(360deg);
                        }
                    }

                    @media (max-width: 768px) {
                        .main-container {
                            padding: 1rem;
                        }

                        .header-section {
                            padding: 1.5rem;
                        }

                        .form-card-body {
                            padding: 1.5rem;
                        }

                        .header-title {
                            font-size: 1.5rem;
                        }

                        .info-grid {
                            grid-template-columns: 1fr;
                        }

                        .form-actions {
                            flex-direction: column;
                            gap: 0.75rem;
                        }

                        .btn {
                            justify-content: center;
                        }
                    }

                    .tooltip {
                        position: relative;
                        display: inline-block;
                        cursor: help;
                    }

                    .tooltip .tooltiptext {
                        visibility: hidden;
                        width: 200px;
                        background-color: rgba(0, 0, 0, 0.9);
                        color: #fff;
                        text-align: center;
                        border-radius: 6px;
                        padding: 5px 10px;
                        position: absolute;
                        z-index: 1;
                        bottom: 125%;
                        left: 50%;
                        margin-left: -100px;
                        opacity: 0;
                        transition: opacity 0.3s;
                        font-size: 12px;
                    }

                    .tooltip:hover .tooltiptext {
                        visibility: visible;
                        opacity: 1;
                    }

                    select optgroup {
                        font-weight: bold;
                        color: #666;
                    }

                    select option {
                        font-weight: normal;
                        padding-left: 15px;
                    }

                    /* Style cho option được chọn */
                    select option:checked {
                        background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                        color: white;
                    }
                    .select-wrapper {
                        position: relative;
                    }

                    .select-icon {
                        position: absolute;
                        left: 1rem;
                        top: 50%;
                        transform: translateY(-50%);
                        color: var(--text-secondary);
                        font-size: 0.875rem;
                        z-index: 5;
                        pointer-events: none;
                    }

                    .select-wrapper {
                        position: relative;
                    }

                    .select-icon {
                        position: absolute;
                        left: 1rem;
                        top: 50%;
                        transform: translateY(-50%);
                        color: var(--text-secondary);
                        font-size: 0.875rem;
                        z-index: 5;
                        pointer-events: none;
                    }


.attributes-table {
    background: var(--card-background);
    border-radius: var(--border-radius-sm);
    overflow: hidden;
}

.attribute-row {
    display: grid;
    grid-template-columns: 250px 1fr 120px;
    gap: 1rem;
    padding: 1rem;
    align-items: center;
    border-bottom: 1px solid var(--border-color);
    transition: all 0.2s ease;
}

.attribute-row:last-child {
    border-bottom: none;
}

.attribute-row:hover {
    background: rgba(0,0,0,0.02);
}

.attribute-label {
    padding-right: 1rem;
}

.attribute-label .form-label {
    margin: 0;
    font-weight: 600;
    color: var(--text-primary);
}

.attribute-input .form-control {
    border: 1px solid var(--border-color);
    border-radius: var(--border-radius-sm);
    padding: 0.5rem 1rem;
    transition: all 0.2s ease;
}

.attribute-input .form-control:focus {
    border-color: var(--primary-color);
    box-shadow: 0 0 0 3px rgba(79, 70, 229, 0.1);
}

.attribute-status {
    text-align: right;
}

.status-badge {
    display: inline-flex;
    align-items: center;
    gap: 0.5rem;
    padding: 0.25rem 0.75rem;
    border-radius: 1rem;
    font-size: 0.75rem;
    font-weight: 500;
}

.status-saved {
    background: rgba(16, 185, 129, 0.1);
    color: var(--success-color);
}

.status-new {
    background: rgba(107, 114, 128, 0.1);
    color: var(--text-secondary);
}

@media (max-width: 768px) {
    .attribute-row {
        grid-template-columns: 1fr;
        gap: 0.5rem;
    }
    
    .attribute-label {
        padding-right: 0;
    }
    
    .attribute-status {
        text-align: left;
    }
}

                </style>
<!--                imge-->
                <!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Quản lý hình ảnh - Fixed</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css">
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            padding: 20px;
        }

        .container {
            max-width: 1200px;
            margin: 0 auto;
        }

        .form-card {
            background: rgba(255, 255, 255, 0.95);
            backdrop-filter: blur(20px);
            border-radius: 20px;
            box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
            overflow: hidden;
            animation: slideUp 0.8s ease-out;
        }

        @keyframes slideUp {
            from {
                opacity: 0;
                transform: translateY(30px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        .form-card-header {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            padding: 25px 30px;
            display: flex;
            align-items: center;
            gap: 15px;
        }

        .form-card-header i {
            font-size: 24px;
            padding: 10px;
            background: rgba(255, 255, 255, 0.2);
            border-radius: 10px;
        }

        .form-card-header h5 {
            font-size: 20px;
            font-weight: 600;
            margin: 0;
        }

        .form-card-body {
            padding: 30px;
        }

        .section-title {
            display: flex;
            align-items: center;
            gap: 12px;
            margin-bottom: 20px;
            color: #333;
            font-weight: 600;
            font-size: 16px;
        }

        .section-title i {
            color: #667eea;
            font-size: 18px;
        }

        .current-images {
            margin-bottom: 40px;
        }

        .main-image-section {
            background: linear-gradient(135deg, #f8f9ff 0%, #e8f0ff 100%);
            border-radius: 15px;
            padding: 25px;
            margin-bottom: 30px;
            border: 2px solid #e1e8ff;
        }

        .additional-images-section {
            background: #f8f9fa;
            border-radius: 15px;
            padding: 25px;
            border: 1px solid #e9ecef;
        }

        .image-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
            gap: 20px;
        }

        .image-preview {
            position: relative;
            background: white;
            border-radius: 15px;
            padding: 15px;
            box-shadow: 0 8px 25px rgba(0, 0, 0, 0.08);
            transition: all 0.3s ease;
        }

        .image-preview:hover {
            transform: translateY(-5px);
            box-shadow: 0 15px 35px rgba(0, 0, 0, 0.15);
        }

        /* ===== FIX CHÍNH: Thay đổi cách hiển thị ảnh ===== */
        .image-preview img {
            width: 100%;
            max-height: 150px;
            object-fit: contain; /* Thay đổi từ cover thành contain */
            border-radius: 10px;
            margin-bottom: 15px;
            background: #f8f9fa; /* Thêm background cho phần trống */
        }

        .main-image-preview {
            grid-column: span 2;
        }

        .main-image-preview img {
            max-height: 200px; /* Thay đổi từ height thành max-height */
        }
.main-image-preview .image-container {
    display: flex;
    justify-content: center;
    align-items: center;
    margin-left: auto;
    margin-right: auto;
    max-width: 100%;
}

        /* ===== THÊM: Styles cho container ảnh tối ưu - FIT CONTENT ===== */
        .image-container {
            display: inline-flex; /* Thay đổi từ flex thành inline-flex */
            justify-content: center;
            align-items: center;
            background: #f8f9fa;
            border-radius: 10px;
            margin-bottom: 15px;
            overflow: hidden;
            position: relative;
            width: fit-content; /* Container tự co theo ảnh */
            max-width: 100%; /* Không vượt quá parent */
            margin-left: auto;
            margin-right: auto;
        }

        /* Container cho ảnh phụ - linh hoạt theo ảnh */
        .additional-images-section .image-container {
            min-height: auto; /* Bỏ min-height cố định */
            max-height: 150px; /* Tăng max-height một chút */
        }

        /* Container cho ảnh chính - linh hoạt hơn */
        .main-image-preview .image-container {
            min-height: auto; /* Bỏ min-height cố định */
            aspect-ratio: unset; /* Bỏ tỷ lệ cố định */
            max-height: 300px; /* Tăng max-height */
            max-width: 100%;
        }

        .image-container img {
            max-width: 100%;
            max-height: 100%;
            width: auto;
            height: auto;
            object-fit: contain;
            border-radius: 8px;
            margin: 0;
            background: transparent;
            display: block; /* Đảm bảo ảnh hiển thị đúng */
        }

        /* Đặc biệt cho ảnh chính - tự động theo kích thước ảnh */
        .main-image-preview .image-container img {
            width: auto;
            height: auto;
            max-width: 100%;
            max-height: 300px;
            object-fit: contain;
        }

        .image-controls {
            display: flex;
            flex-direction: column;
            gap: 10px;
        }

        .main-badge {
            background: linear-gradient(135deg, #ff6b6b, #ee5a6f);
            color: white;
            padding: 4px 12px;
            border-radius: 20px;
            font-size: 12px;
            font-weight: 600;
            align-self: flex-start;
            margin-bottom: 10px;
        }

        .form-control {
            border: 2px solid #e9ecef;
            border-radius: 8px;
            padding: 8px 12px;
            font-size: 14px;
            transition: all 0.3s ease;
        }

        .form-control:focus {
            border-color: #667eea;
            box-shadow: 0 0 0 3px rgba(102, 126, 234, 0.1);
            outline: none;
        }

        .form-check {
            display: flex;
            align-items: center;
            gap: 8px;
            margin-top: 10px;
        }

        .form-check-input {
            width: 18px;
            height: 18px;
            accent-color: #dc3545;
        }

        .radio-group {
            display: flex;
            align-items: center;
            gap: 8px;
            margin-bottom: 10px;
        }

        .radio-group input[type="radio"] {
            width: 18px;
            height: 18px;
            accent-color: #667eea;
        }

        .upload-section {
            background: linear-gradient(135deg, #f1f8ff 0%, #e8f4fd 100%);
            border-radius: 15px;
            padding: 30px;
            border: 2px dashed #b3d9ff;
        }

        .upload-grid {
            display: grid;
            grid-template-columns: 1fr 1fr;
            gap: 30px;
        }

        .image-upload-area {
            background: white;
            border: 2px dashed #d1ecf1;
            border-radius: 15px;
            padding: 40px 20px;
            text-align: center;
            transition: all 0.3s ease;
            cursor: pointer;
        }

        .image-upload-area:hover {
            border-color: #667eea;
            background: #f8f9ff;
            transform: translateY(-2px);
        }

        .image-upload-area i {
            color: #667eea;
            margin-bottom: 15px;
        }

        .image-upload-area p {
            color: #6c757d;
            margin-bottom: 20px;
            font-weight: 500;
        }

        .btn {
            padding: 12px 24px;
            border-radius: 10px;
            font-weight: 600;
            text-decoration: none;
            display: inline-flex;
            align-items: center;
            gap: 8px;
            transition: all 0.3s ease;
            border: none;
            cursor: pointer;
            font-size: 14px;
        }

        .btn-primary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }

        .btn-primary:hover {
            transform: translateY(-2px);
            box-shadow: 0 8px 25px rgba(102, 126, 234, 0.3);
        }

        .btn-secondary {
            background: #6c757d;
            color: white;
        }

        .btn-secondary:hover {
            background: #5a6268;
            transform: translateY(-2px);
        }

        .btn-sm {
            padding: 8px 16px;
            font-size: 13px;
        }

        .form-actions {
            display: flex;
            justify-content: space-between;
            align-items: center;
            margin-top: 40px;
            padding: 25px 30px;
            background: #f8f9fa;
            border-radius: 15px;
        }

        .form-text {
            font-size: 12px;
            color: #6c757d;
            margin-top: 8px;
        }

        .label {
            font-weight: 600;
            color: #333;
            margin-bottom: 8px;
            display: block;
        }

        .order-input {
            width: 80px;
        }

        .delete-section {
            background: #fff5f5;
            border: 1px solid #fed7d7;
            border-radius: 8px;
            padding: 10px;
            margin-top: 10px;
        }

        .text-danger {
            color: #dc3545 !important;
        }

        @media (max-width: 768px) {
            .upload-grid {
                grid-template-columns: 1fr;
                gap: 20px;
            }
            
            .image-grid {
                grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
            }
            
            .main-image-preview {
                grid-column: span 1;
            }
            
            .form-actions {
                flex-direction: column;
                gap: 15px;
            }
        }
        .is-invalid {
    border-color: red !important;
    background-color: #fff5f5;
}
    </style>
            </head>
            <body>
                <div class="main-container">
                    <div class="form-wrapper">
                        <!-- Header Section -->
                        <div class="header-section fade-in">
                            <div class="d-flex justify-content-between align-items-center">
                                <h1 class="header-title">
                                    <div class="header-icon">
                                        <i class="fas fa-fish"></i>
                                    </div>
                            <c:choose>
                                <c:when test="${product != null}">
                                    Chỉnh sửa sản phẩm
                                </c:when>
                                <c:otherwise>
                                    Thêm sản phẩm mới
                                </c:otherwise>
                            </c:choose>
                        </h1>
                        <a href="products" class="back-btn">
                            <i class="fas fa-arrow-left"></i>
                            Quay lại
                        </a>
                    </div>
                </div>
 
                <form action="products" method="post" onsubmit="return validateForm()"  enctype="multipart/form-data" >
                    <c:if test="${product != null}">
                        <input type="hidden" name="action" value="update">
                        <input type="hidden" name="id" value="${product.productId}">
                        
                    </c:if>
                    <c:if test="${product == null}">
                        <input type="hidden" name="action" value="insert">
                    </c:if>

                    <!-- Alert Messages -->
                    <c:if test="${not empty errorMessage}">
                        <div class="alert alert-danger fade-in">
                            <i class="fas fa-exclamation-circle me-2"></i>
                            ${errorMessage}
                        </div>
                    </c:if>

                    <!-- Basic Information Card -->
                    <div class="form-card fade-in">
                        <div class="form-card-header">
                            <i class="fas fa-info-circle"></i>
                            <h5>Thông tin cơ bản</h5>
                        </div>
                        <div class="form-card-body">
                            <div class="info-grid">
                                <div>
                                    <!-- Product Name -->
                                    <div class="form-group">
                                        <label class="form-label" for="name">
                                            Tên sản phẩm <span class="required">*</span>
                                        </label>
                                        <div class="input-group">
                                            <i class="input-icon fas fa-tag"></i>
                                            <input type="text" class="form-control has-icon" id="name" name="name" 
                                                   value="${product.name}" required maxlength="255"
                                                   placeholder="Nhập tên sản phẩm">
                                        </div>
                                    </div>

                                   <div class="form-group">
    <label class="form-label" for="categoryId">
        Danh mục <span class="required">*</span>
    </label>
    <div class="select-wrapper">
        <i class="select-icon fas fa-layer-group"></i>
        <select class="form-control has-icon" id="categoryId" name="categoryId" required>
            <c:forEach var="category" items="${listCategory}">
                <c:choose>
                    <c:when test="${category.parentId == category.categoryId}">
                        <!-- Parent Category -->
                        <option value="${category.categoryId}" 
                                class="parent-category"
                                ${product.categoryId == category.categoryId ? 'selected' : ''}>
                            🐠  ${category.name}
                        </option>
                    </c:when>
                    <c:otherwise>
                        <!-- Child Category with Parent Name -->
                        <c:forEach var="parentCategory" items="${listCategory}">
                            <c:if test="${parentCategory.categoryId == category.parentId}">
                                <option value="${category.categoryId}" 
                                        class="child-category"
                                        ${product.categoryId == category.categoryId ? 'selected' : ''}>
                                        🐟 ${category.name} - Thuộc: ${parentCategory.name}
                                </option>
                            </c:if>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
            </c:forEach>
        </select>
    </div>
    <small class="form-text">Chọn danh mục phù hợp cho sản phẩm</small>
</div>



                                    <!-- SKU -->
                                    <div class="form-group">
                                        <label class="form-label" for="sku">
                                            SKU <span class="required">*</span>
                                            <span class="tooltip">
                                                <i class="fas fa-question-circle text-muted"></i>
                                                <span class="tooltiptext">Mã sản phẩm duy nhất để quản lý kho</span>
                                            </span>
                                        </label>
                                        <div class="input-group">
                                            <i class="input-icon fas fa-barcode"></i>
                                            <input type="text" class="form-control has-icon" id="sku" name="sku" 
                                                   value="${product.sku}" required maxlength="50"
                                                   placeholder="VD: BETTA_001">
                                        </div>
                                        <small class="form-text">Mã sản phẩm duy nhất</small>
                                    </div>
                                </div>

                                <div>
                                    <!-- Price -->
                                    <div class="form-group">
                                        <label class="form-label" for="price">
                                            Giá gốc (₫) <span class="required">*</span>
                                        </label>
                                        <div class="input-group">
                                            <i class="input-icon fas fa-dollar-sign"></i>
                                            <input type="number" class="form-control has-icon" id="price" name="price" 
                                                   value="${product.price}" required min="0" step="1000"
                                                   placeholder="0">
                                        </div>
                                    </div>

                                    <!-- Sale Price -->
                                    <div class="form-group">
                                        <label class="form-label" for="salePrice">
                                            Giá khuyến mãi (₫)
                                        </label>
                                        <div class="input-group">
                                            <i class="input-icon fas fa-percentage"></i>
                                            <input type="number" class="form-control has-icon" id="salePrice" name="salePrice" 
                                                   value="${product.salePrice}" min="0" step="1000"
                                                   placeholder="0 (để trống nếu không có khuyến mãi)">
                                        </div>
                                    </div>

                                    <!-- Quantity -->
                                    <div class="form-group">
                                        <label class="form-label" for="quantity">
                                            Số lượng <span class="required">*</span>
                                        </label>
                                        <div class="input-group">
                                            <i class="input-icon fas fa-boxes"></i>
                                            <input type="number" class="form-control has-icon" id="quantity" name="quantity" 
                                                   value="${product.quantity}" required min="0"
                                                   placeholder="0">
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-6">
                                    <!-- Status -->
                                    <div class="form-group">
                                        <label class="form-label" for="status">
                                            Trạng thái <span class="required">*</span>
                                        </label>
                                        <select class="form-control" id="status" name="status" required>
                                            <option value="active" ${product.status == 'active' ? 'selected' : ''}>
                                                🟢 Hoạt động
                                            </option>
                                            <option value="inactive" ${product.status == 'inactive' ? 'selected' : ''}>
                                                ⚫ Không hoạt động
                                            </option>
                                            <option value="out_of_stock" ${product.status == 'out_of_stock' ? 'selected' : ''}>
                                                🔴 Hết hàng
                                            </option>
                                        </select>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <!-- Featured -->
                                    <div class="form-group">
                                        <label class="form-label">Tùy chọn</label>
                                        <div class="custom-checkbox">
                                            <input type="checkbox" id="featured" name="featured" 
                                                   value="true" ${product.featured ? 'checked' : ''}>
                                            <span class="checkmark"></span>
                                            <label for="featured" class="form-label mb-0">
                                                <i class="fas fa-star text-warning"></i> Sản phẩm nổi bật
                                            </label>
                                        </div>
                                        <small class="form-text">Sản phẩm nổi bật sẽ hiển thị ưu tiên</small>
                                    </div>
                                </div>
                            </div>

                            <!-- Descriptions -->
                            <div class="row">
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label class="form-label" for="shortDescription">Mô tả ngắn</label>
                                        <textarea class="form-control" id="shortDescription" name="shortDescription" 
                                                  rows="3" maxlength="255" placeholder="Mô tả ngắn gọn về sản phẩm">${product.shortDescription}</textarea>
                                        <small class="form-text">Tối đa 255 ký tự</small>
                                    </div>
                                </div>
                                <div class="col-md-6">
                                    <div class="form-group">
                                        <label class="form-label" for="description">Mô tả chi tiết</label>
                                        <textarea class="form-control" id="description" name="description" 
                                                  rows="3" placeholder="Mô tả chi tiết về sản phẩm, cách chăm sóc, đặc điểm...">${product.description}</textarea>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- Fish Details Card -->
                    <div class="form-card fade-in">
    <div class="form-card-header">
        <i class="fas fa-fish"></i>
        <h5>Thông tin chi tiết cá cảnh</h5>
    </div>
    <div class="form-card-body">
        <c:set var="hasProductDetail" value="${productDetail != null}" />
        
        <div class="info-grid">
            <div>
                <div class="form-group">
                    <label class="form-label" for="origin">Xuất xứ</label>
                    <div class="input-group">
                        <i class="input-icon fas fa-globe"></i>
                        <input type="text" class="form-control has-icon" id="origin" name="origin" 
                               value="${hasProductDetail ? productDetail.origin : ''}"
                               placeholder="VD: Việt Nam, Thái Lan">
                    </div>
                </div>

                <div class="form-group">
                    <label class="form-label" for="size">Kích thước</label>
                    <div class="input-group">
                        <i class="input-icon fas fa-ruler"></i>
                        <input type="text" class="form-control has-icon" id="size" name="size" 
                               value="${hasProductDetail ? productDetail.size : ''}"
                               placeholder="VD: 5-7cm">
                    </div>
                </div>

                <div class="form-group">
                    <label class="form-label" for="waterTemperature">Nhiệt độ nước</label>
                    <div class="input-group">
                        <i class="input-icon fas fa-thermometer-half"></i>
                        <input type="text" class="form-control has-icon" id="waterTemperature" name="waterTemperature" 
                               value="${hasProductDetail ? productDetail.waterTemperature : ''}"
                               placeholder="VD: 24-28°C">
                    </div>
                </div>

                <div class="form-group">
                    <label class="form-label" for="waterPh">Độ pH nước</label>
                    <div class="input-group">
                        <i class="input-icon fas fa-flask"></i>
                        <input type="text" class="form-control has-icon" id="waterPh" name="waterPh" 
                               value="${hasProductDetail ? productDetail.waterPh : ''}"
                               placeholder="VD: 6.5-7.5">
                    </div>
                </div>

                <div class="form-group">
                    <label class="form-label" for="lifespan">Tuổi thọ</label>
                    <div class="input-group">
                        <i class="input-icon fas fa-clock"></i>
                        <input type="text" class="form-control has-icon" id="lifespan" name="lifespan" 
                               value="${hasProductDetail ? productDetail.lifespan : ''}"
                               placeholder="VD: 2-3 năm">
                    </div>
                </div>
            </div>

            <div>
                <div class="form-group">
                    <label class="form-label" for="scientificName">Tên khoa học</label>
                    <div class="input-group">
                        <i class="input-icon fas fa-microscope"></i>
                        <input type="text" class="form-control has-icon" id="scientificName" name="scientificName" 
                               value="${hasProductDetail ? productDetail.scientificName : ''}"
                               placeholder="VD: Betta splendens">
                    </div>
                </div>

                <div class="form-group">
                    <label class="form-label" for="commonName">Tên thông thường</label>
                    <div class="input-group">
                        <i class="input-icon fas fa-fish"></i>
                        <input type="text" class="form-control has-icon" id="commonName" name="commonName" 
                               value="${hasProductDetail ? productDetail.commonName : ''}"
                               placeholder="VD: Cá betta">
                    </div>
                </div>

                <div class="form-group">
                    <label class="form-label" for="waterType">Loại nước</label>
                    <select class="form-control" id="waterType" name="waterType">
                        <option value="" ${hasProductDetail && productDetail.waterType == null ? 'selected' : ''}>-- Chọn loại nước --</option>
                        <option value="freshwater" ${hasProductDetail && productDetail.waterType == 'freshwater' ? 'selected' : ''}>🌊 Nước ngọt</option>
                        <option value="saltwater" ${hasProductDetail && productDetail.waterType == 'saltwater' ? 'selected' : ''}>🌊 Nước mặn</option>
                        <option value="brackish" ${hasProductDetail && productDetail.waterType == 'brackish' ? 'selected' : ''}>🌊 Nước lợ</option>
                    </select>
                </div>

                <div class="form-group">
                    <label class="form-label" for="careLevel">Mức độ chăm sóc</label>
                    <select class="form-control" id="careLevel" name="careLevel">
                        <option value="" ${hasProductDetail && productDetail.careLevel == null ? 'selected' : ''}>-- Chọn mức độ chăm sóc --</option>
                        <option value="easy" ${hasProductDetail && productDetail.careLevel == 'easy' ? 'selected' : ''}>🟢 Dễ</option>
                        <option value="moderate" ${hasProductDetail && productDetail.careLevel == 'moderate' ? 'selected' : ''}>🟡 Trung bình</option>
                        <option value="difficult" ${hasProductDetail && productDetail.careLevel == 'difficult' ? 'selected' : ''}>🔴 Khó</option>
                    </select>
                </div>

                <div class="form-group">
                    <label class="form-label" for="breedingDifficulty">Độ khó nhân giống</label>
                    <select class="form-control" id="breedingDifficulty" name="breedingDifficulty">
                        <option value="" ${hasProductDetail && productDetail.breedingDifficulty == null ? 'selected' : ''}>-- Chọn độ khó nhân giống --</option>
                        <option value="easy" ${hasProductDetail && productDetail.breedingDifficulty == 'easy' ? 'selected' : ''}>🟢 Dễ</option>
                        <option value="moderate" ${hasProductDetail && productDetail.breedingDifficulty == 'moderate' ? 'selected' : ''}>🟡 Trung bình</option>
                        <option value="difficult" ${hasProductDetail && productDetail.breedingDifficulty == 'difficult' ? 'selected' : ''}>🔴 Khó</option>
                    </select>
                </div>
            </div>
        </div>

        <div class="row">
            <div class="col-md-6">
                <div class="form-group">
                    <label class="form-label" for="diet">Chế độ ăn</label>
                    <textarea class="form-control" id="diet" name="diet" 
                              rows="3" placeholder="Mô tả chế độ ăn, loại thức ăn phù hợp...">${hasProductDetail ? productDetail.diet : ''}</textarea>
                </div>
            </div>
            <div class="col-md-6">
                <div class="form-group">
                    <label class="form-label" for="compatibility">Đặc tính/Tương thích</label>
                    <textarea class="form-control" id="compatibility" name="compatibility" 
                              rows="3" placeholder="Mô tả đặc tính, khả năng tương thích với các loài khác...">${hasProductDetail ? productDetail.compatibility : ''}</textarea>
                </div>
            </div>
        </div>
    </div>
</div>
         
<!-- Product Attributes Card -->
<div class="form-card fade-in">
    <div class="form-card-header">
        <i class="fas fa-tags"></i>
        <h5>Thuộc tính sản phẩm</h5>
    </div>
    <div class="form-card-body">
        <div class="attributes-table">
            <c:forEach var="attr" items="${listProductAttribute}">
                <c:set var="matchedValue" value="" />
                <c:set var="matchedValueId" value="" />
                <c:forEach var="value" items="${listProductAttributeValueByPID}">
                    <c:if test="${value.attributeId == attr.attributeId}">
                        <c:set var="matchedValue" value="${value.value}" />
                        <c:set var="matchedValueId" value="${value.valueId}" />
                    </c:if>
                </c:forEach>

                <div class="attribute-row" data-attribute-value-id="${matchedValueId}">
                    <div class="attribute-label">
                        <label for="attr_${attr.attributeId}" class="form-label">
                            ${attr.name}
                        </label>
                    </div>
                    <div class="attribute-input">
                        <div class="input-group">
                            <input type="hidden" name="attributeIds[]" value="${attr.attributeId}">
                            <input type="text"
                                   class="form-control"
                                   id="attr_${attr.attributeId}"
                                   name="attributeValues[${attr.attributeId}]"
                                   placeholder="Nhập ${attr.name}"
                                   value="${matchedValue}">
                        </div>
                    </div>
                    <div class="attribute-status">
                        <c:if test="${not empty matchedValueId}">
                            <span class="status-badge status-saved">
                                <i class="fas fa-check-circle"></i> Đã lưu
                            </span>
                        </c:if>
                        <c:if test="${empty matchedValueId}">
                            <span class="status-badge status-new">cần thêm</span>
                        </c:if>
                    </div>
                </div>
            </c:forEach>
        </div>
    </div>
</div>


                    <!-- Image Management Card -->
                    <div class="form-card fade-in">
    <div class="form-card-header">
        <i class="fas fa-images"></i>
        <h5>Quản lý hình ảnh</h5>
    </div>
    <div class="form-card-body">
        <!-- Hiển thị ảnh hiện tại -->
        <c:if test="${not empty productImages}">
            <div class="section-title">
                <i class="fas fa-image"></i> Hình ảnh hiện tại
            </div>
            <div class="image-grid">
                <c:forEach var="image" items="${productImages}">
                    <div class="image-preview ${image.isMain == 1 ? 'main-image-preview' : ''}">
                        <div class="image-container">
                            <img src="${pageContext.request.contextPath}/${image.imageUrl}" alt="Ảnh sản phẩm">
                        </div>
                        <input type="hidden" name="existingImageIds" value="${image.imageId}" />
                        <c:if test="${image.isMain == 1}">
                            <div class="main-badge">Ảnh chính</div>
                            <input type="radio" name="mainImageId" value="${image.imageId}" checked />
                        </c:if>
                        <label>Thứ tự hiển thị:</label>
                        <input type="number" name="displayOrders[${image.imageId}]" value="${image.displayOrder}" class="form-control form-control-sm order-input" />
                        <div class="form-check mt-2">
                            <input class="form-check-input" type="checkbox" name="deleteImageIds[]" value="${image.imageId}" id="delete_${image.imageId}">
                            <label class="form-check-label text-danger" for="delete_${image.imageId}">Xóa ảnh này</label>
                        </div>
                    </div>
                </c:forEach>
            </div>
        </c:if>

        <!-- Tải ảnh mới -->
        <div class="upload-section mt-4">
    <div class="section-title">
        <i class="fas fa-upload"></i> Thêm hình ảnh mới
    </div>

    <div class="upload-grid row">
        <!-- Ảnh chính -->
        <div class="col-md-6">
            <label class="form-label">Ảnh chính</label>
            <ul class="nav nav-tabs" id="mainImageTab" role="tablist">
                <li class="nav-item">
                    <a class="nav-link active" data-bs-toggle="tab" href="#uploadMain" role="tab">Upload</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" data-bs-toggle="tab" href="#linkMain" role="tab">Link ảnh</a>
                </li>
            </ul>
            <div class="tab-content border rounded p-3 mt-2">
                <div class="tab-pane fade show active" id="uploadMain" role="tabpanel">
                    <div class="image-upload-area text-center" onclick="document.getElementById('subImages').click()">
                                                            <i class="fas fa-cloud-upload-alt fa-2x mb-2"></i>
                                                            <p class="text-muted">Kéo thả hoặc click để chọn ảnh chính</p>
                                                            <input type="file" class="d-none" id="subImages" name="subImages" accept="image/*" multiple onchange="showSubFilesName()">
                                                            <div id="subImagesName" style="margin-top:8px;font-size:15px;color:#007bff;"></div>
                                                        </div>
                </div>
                <div class="tab-pane fade" id="linkMain" role="tabpanel">
                    <label>URL ảnh chính</label>
                    <input type="text" name="mainImageUrl" class="form-control" placeholder="https://example.com/image.jpg" />
                </div>
            </div>
        </div>

        <!-- Ảnh phụ -->
        <div class="col-md-6">
            <label class="form-label">Ảnh phụ</label>
            <ul class="nav nav-tabs" id="additionalImageTab" role="tablist">
                <li class="nav-item">
                    <a class="nav-link active" data-bs-toggle="tab" href="#uploadAdditional" role="tab">Upload</a>
                </li>
                <li class="nav-item">
                    <a class="nav-link" data-bs-toggle="tab" href="#linkAdditional" role="tab">Link ảnh</a>
                </li>
            </ul>
            <div class="tab-content border rounded p-3 mt-2">
                <div class="tab-pane fade show active" id="uploadAdditional" role="tabpanel">
                    <div class="image-upload-area text-center" onclick="document.getElementById('additionalImages').click()">
                        <i class="fas fa-images fa-2x mb-2"></i>
                        <p class="text-muted">Chọn nhiều ảnh phụ</p>
                        <input type="file" class="d-none" id="additionalImages" name="additionalImages" multiple accept="image/*">
                    </div>
                </div>
                <div class="tab-pane fade" id="linkAdditional" role="tabpanel">
                    <div class="image-upload-area text-center" onclick="document.getElementById('subImages').click()">
                                                            <i class="fas fa-cloud-upload-alt fa-2x mb-2"></i>
                                                            <p class="text-muted">Kéo thả hoặc click để chọn ảnh phụ</p>
                                                            <input type="file" class="d-none" id="subImages" name="subImages" accept="image/*" multiple onchange="showSubFilesName()">
                                                            <div id="subImagesName" style="margin-top:8px;font-size:15px;color:#007bff;"></div>
                                                        </div>
                    
                    <textarea name="additionalImageUrls" class="form-control" rows="4" placeholder="https://img1.jpg\nhttps://img2.jpg"></textarea>
                </div>
            </div>
        </div>
    </div>
</div>

    </div>
</div>


                    <!-- Form Actions -->
                    <div class="form-card fade-in">
                        <div class="form-actions">
                            <a href="products" class="btn btn-secondary">
                                <i class="fas fa-times"></i>
                                Hủy bỏ
                            </a>
                            <button type="submit" class="btn btn-primary" id="submitBtn">
                                <i class="fas fa-save"></i>
                                <c:choose>
                                    <c:when test="${product != null}">
                                        Cập nhật sản phẩm
                                    </c:when>
                                    <c:otherwise>
                                        Thêm sản phẩm
                                    </c:otherwise>
                                </c:choose>
                            </button>
                        </div>
                    </div>
            
        
                                    </form>


        <!-- Scripts -->
        <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
        <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"></script>
<!--        imge-->
<script>
document.addEventListener("DOMContentLoaded", function () {
    const form = document.querySelector("form");
    form.addEventListener("submit", function (e) {
        const orderInputs = document.querySelectorAll('input[name^="displayOrders["]');
        const seen = new Set();
        let duplicated = false;

        orderInputs.forEach(input => {
            const val = input.value.trim();
            if (val !== "") {
                if (seen.has(val)) {
                    duplicated = true;
                    input.classList.add("is-invalid");
                } else {
                    seen.add(val);
                    input.classList.remove("is-invalid");
                }
            }
        });

        if (duplicated) {
            e.preventDefault();
            alert("Thứ tự hiển thị (Display Order) không được trùng nhau.");
        }
    });
});

</script>
        <script>
                                                

                                                // Real-time validation
                                                document.getElementById('salePrice').addEventListener('blur', function () {
                                                    const price = parseFloat(document.getElementById('price').value);
                                                    const salePrice = parseFloat(this.value);

                                                    if (salePrice && price && salePrice >= price) {
                                                        this.classList.add('is-invalid');
                                                        showNotification('Giá khuyến mãi phải nhỏ hơn giá gốc!', 'warning');
                                                    } else {
                                                        this.classList.remove('is-invalid');
                                                        if (this.value)
                                                            this.classList.add('is-valid');
                                                    }
                                                });

                                                // Image upload handling
                                                function setupImageUpload() {
                                                    const mainImageInput = document.getElementById('mainImage');
                                                    const additionalImagesInput = document.getElementById('additionalImages');

                                                    mainImageInput.addEventListener('change', function () {
                                                        previewImages(this, 'main');
                                                    });

                                                    additionalImagesInput.addEventListener('change', function () {
                                                        previewImages(this, 'additional');
                                                    });

                                                    // Drag and drop functionality
                                                    ['mainImageArea', 'additionalImagesArea'].forEach(areaId => {
                                                        const area = document.getElementById(areaId);

                                                        area.addEventListener('dragover', function (e) {
                                                            e.preventDefault();
                                                            this.classList.add('dragover');
                                                        });

                                                        area.addEventListener('dragleave', function (e) {
                                                            e.preventDefault();
                                                            this.classList.remove('dragover');
                                                        });

                                                        area.addEventListener('drop', function (e) {
                                                            e.preventDefault();
                                                            this.classList.remove('dragover');

                                                            const files = e.dataTransfer.files;
                                                            const input = areaId === 'mainImageArea' ? mainImageInput : additionalImagesInput;
                                                            input.files = files;

                                                            previewImages(input, areaId === 'mainImageArea' ? 'main' : 'additional');
                                                        });
                                                    });
                                                }

                                                function previewImages(input, type) {
                                                    const previewContainer = document.getElementById('previewContainer');

                                                    if (input.files && input.files.length > 0) {
                                                        Array.from(input.files).forEach((file, index) => {
                                                            const reader = new FileReader();
                                                            reader.onload = function (e) {
                                                                const isMain = type === 'main';
                                                                const preview = document.createElement('div');
                                                                preview.className = 'col-md-3 col-6 mb-3';
                                                                preview.innerHTML = `
                                <div class="image-preview">
                                    <img src="${e.target.result}" alt="Preview">
            ${isMain ? '<div class="main-image-badge"><i class="fas fa-star"></i> Ảnh chính</div>' : ''}
                                    <button type="button" class="image-remove preview-remove">
                                        <i class="fas fa-times"></i>
                                    </button>
                                </div>
                            `;
                                                                previewContainer.appendChild(preview);
                                                            };
                                                            reader.readAsDataURL(file);
                                                        });
                                                    }
                                                }

                                                // Remove preview images
                                                document.addEventListener('click', function (e) {
                                                    if (e.target.closest('.preview-remove')) {
                                                        e.target.closest('.col-md-3').remove();
                                                    }
                                                });

                                                // Remove existing images
                                                function removeImage(imageId) {
                                                    if (confirm('Bạn có chắc chắn muốn xóa ảnh này?')) {
                                                        // Show loading state
                                                        const button = event.target.closest('button');
                                                        button.innerHTML = '<span class="loading-spinner"></span>';

                                                        // Simulate API call - replace with actual implementation
                                                        setTimeout(() => {
                                                            button.closest('.col-md-3, .col-md-6').remove();
                                                            showNotification('Đã xóa ảnh thành công!', 'success');
                                                        }, 1000);

                                                        // Actual implementation would be:
                                                        /*
                                                         fetch('products?action=deleteImage', {
                                                         method: 'POST',
                                                         headers: {
                                                         'Content-Type': 'application/json',
                                                         },
                                                         body: JSON.stringify({ imageId: imageId })
                                                         })
                                                         .then(response => response.json())
                                                         .then(data => {
                                                         if (data.success) {
                                                         button.closest('.col-md-3, .col-md-6').remove();
                                                         showNotification('Đã xóa ảnh thành công!', 'success');
                                                         } else {
                                                         showNotification('Có lỗi xảy ra khi xóa ảnh!', 'danger');
                                                         }
                                                         })
                                                         .catch(error => {
                                                         showNotification('Có lỗi xảy ra!', 'danger');
                                                         });
                                                         */
                                                    }
                                                }

                                                // Notification system
                                                function showNotification(message, type = 'info') {
    // Nếu message không hợp lệ, hiển thị nội dung mặc định
    if (!message || (typeof message === 'string' && message.trim() === '')) {
        message = 'Lỗi không phân định';
    }

    const notification = document.createElement('div');
    notification.className = `alert alert-${type} alert-dismissible fade show`;
    notification.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        z-index: 1050;
        min-width: 300px;
        animation: slideInRight 0.3s ease;
    `;

    let icon = 'info-circle';
    if (type === 'success') {
        icon = 'check-circle';
    } else if (type === 'danger') {
        icon = 'exclamation-circle';
    } else if (type === 'warning') {
        icon = 'exclamation-triangle';
    }

    notification.innerHTML = `
        <i class="fas fa-${icon}"></i>
        ${message}
        <button type="button" class="close" data-dismiss="alert">&times;</button>
    `;

    document.body.appendChild(notification);

    setTimeout(() => {
        if (notification.parentNode) {
            notification.remove();
        }
    }, 5000);
}


                                                // Initialize everything when DOM is ready
                                                document.addEventListener('DOMContentLoaded', function () {
                                                    setupImageUpload();

                                                    // Add smooth scrolling for form sections
                                                    document.querySelectorAll('.form-card').forEach((card, index) => {
                                                        card.style.animationDelay = `${index * 0.1}s`;
                                                    });
                                                });

                                                // Add CSS for animations
                                                const style = document.createElement('style');
                                                style.textContent = `
                @keyframes slideInRight {
                    from {
                        transform: translateX(100%);
                        opacity: 0;
                    }
                    to {
                        transform: translateX(0);
                        opacity: 1;
                    }
                }
            `;
                                                document.head.appendChild(style);
        </script>
    </body>
</html>