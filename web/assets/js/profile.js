/**
 * Profile Page JavaScript
 * Xử lý các tương tác trong trang profile người dùng
 */

$(document).ready(function() {
    initializeProfile();
});

function initializeProfile() {
    // Initialize form validation
    initFormValidation();
    
    // Initialize avatar upload
    initAvatarUpload();
    
    // Initialize password strength meter
    initPasswordStrength();
    
    // Initialize profile statistics
    loadProfileStats();
    
    // Initialize smooth animations
    initAnimations();
}

/**
 * Form Validation
 */
function initFormValidation() {
    // Email validation
    $('#email').on('blur', function() {
        validateEmail(this);
    });
    
    // Phone validation
    $('#phone').on('blur', function() {
        validatePhone(this);
    });
    
    // Password validation
    $('#newPassword').on('input', function() {
        validatePassword(this);
        checkPasswordMatch();
    });
    
    $('#confirmPassword').on('input', function() {
        checkPasswordMatch();
    });
    
    // Form submission
    $('#profileForm').on('submit', function(e) {
        if (!validateForm()) {
            e.preventDefault();
            showAlert('Vui lòng kiểm tra lại thông tin!', 'error');
        }
    });
    
    $('#passwordForm').on('submit', function(e) {
        if (!validatePasswordForm()) {
            e.preventDefault();
            showAlert('Vui lòng kiểm tra lại mật khẩu!', 'error');
        }
    });
}

function validateEmail(input) {
    const email = $(input).val();
    const emailPattern = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    
    if (!email) {
        showFieldError(input, 'Email không được để trống');
        return false;
    } else if (!emailPattern.test(email)) {
        showFieldError(input, 'Định dạng email không hợp lệ');
        return false;
    } else {
        clearFieldError(input);
        return true;
    }
}

function validatePhone(input) {
    const phone = $(input).val();
    const phonePattern = /^[0-9]{10,11}$/;
    
    if (!phone) {
        showFieldError(input, 'Số điện thoại không được để trống');
        return false;
    } else if (!phonePattern.test(phone)) {
        showFieldError(input, 'Số điện thoại phải có 10-11 chữ số');
        return false;
    } else {
        clearFieldError(input);
        return true;
    }
}

function validatePassword(input) {
    const password = $(input).val();
    const minLength = 6;
    
    if (!password) {
        showFieldError(input, 'Mật khẩu không được để trống');
        return false;
    } else if (password.length < minLength) {
        showFieldError(input, `Mật khẩu phải có ít nhất ${minLength} ký tự`);
        return false;
    } else {
        clearFieldError(input);
        return true;
    }
}

function checkPasswordMatch() {
    const newPassword = $('#newPassword').val();
    const confirmPassword = $('#confirmPassword').val();
    
    if (confirmPassword && newPassword !== confirmPassword) {
        showFieldError('#confirmPassword', 'Mật khẩu xác nhận không khớp');
        return false;
    } else {
        clearFieldError('#confirmPassword');
        return true;
    }
}

function validateForm() {
    let isValid = true;
    
    // Validate required fields
    $('#profileForm input[required]').each(function() {
        if (!$(this).val()) {
            showFieldError(this, 'Trường này không được để trống');
            isValid = false;
        }
    });
    
    // Validate email
    if (!validateEmail('#email')) {
        isValid = false;
    }
    
    // Validate phone
    if (!validatePhone('#phone')) {
        isValid = false;
    }
    
    return isValid;
}

function validatePasswordForm() {
    let isValid = true;
    
    // Validate current password
    if (!$('#currentPassword').val()) {
        showFieldError('#currentPassword', 'Vui lòng nhập mật khẩu hiện tại');
        isValid = false;
    }
    
    // Validate new password
    if (!validatePassword('#newPassword')) {
        isValid = false;
    }
    
    // Check password match
    if (!checkPasswordMatch()) {
        isValid = false;
    }
    
    return isValid;
}

function showFieldError(input, message) {
    const $input = $(input);
    $input.addClass('is-invalid');
    
    // Remove existing error message
    $input.next('.invalid-feedback').remove();
    
    // Add new error message
    $input.after(`<div class="invalid-feedback">${message}</div>`);
}

function clearFieldError(input) {
    const $input = $(input);
    $input.removeClass('is-invalid');
    $input.next('.invalid-feedback').remove();
}

/**
 * Avatar Upload
 */
function initAvatarUpload() {
    $('#avatarFile').on('change', function(e) {
        const file = e.target.files[0];
        if (file) {
            validateAvatarFile(file);
        }
    });
    
    // Drag and drop functionality
    const dropZone = $('.file-upload-label');
    
    dropZone.on('dragover', function(e) {
        e.preventDefault();
        $(this).addClass('drag-over');
    });
    
    dropZone.on('dragleave', function(e) {
        e.preventDefault();
        $(this).removeClass('drag-over');
    });
    
    dropZone.on('drop', function(e) {
        e.preventDefault();
        $(this).removeClass('drag-over');
        
        const files = e.originalEvent.dataTransfer.files;
        if (files.length > 0) {
            $('#avatarFile')[0].files = files;
            validateAvatarFile(files[0]);
        }
    });
}

function validateAvatarFile(file) {
    const maxSize = 5 * 1024 * 1024; // 5MB
    const allowedTypes = ['image/jpeg', 'image/jpg', 'image/png', 'image/gif'];
    
    if (!allowedTypes.includes(file.type)) {
        showAlert('Chỉ chấp nhận file ảnh (JPG, PNG, GIF)!', 'error');
        $('#avatarFile').val('');
        return false;
    }
    
    if (file.size > maxSize) {
        showAlert('Kích thước file không được vượt quá 5MB!', 'error');
        $('#avatarFile').val('');
        return false;
    }
    
    // Preview image
    previewAvatar(file);
    return true;
}

function previewAvatar(file) {
    const reader = new FileReader();
    reader.onload = function(e) {
        $('.current-avatar').attr('src', e.target.result);
        $('.file-upload-text').text('File đã chọn: ' + file.name);
    };
    reader.readAsDataURL(file);
}

/**
 * Password Strength Meter
 */
function initPasswordStrength() {
    $('#newPassword').on('input', function() {
        const password = $(this).val();
        showPasswordStrength(password);
    });
}

function showPasswordStrength(password) {
    const strength = calculatePasswordStrength(password);
    const $meter = $('.password-strength-meter');
    
    if (!$meter.length && password) {
        $('#newPassword').after(`
            <div class="password-strength-meter">
                <div class="strength-bar">
                    <div class="strength-fill"></div>
                </div>
                <div class="strength-text"></div>
            </div>
        `);
    }
    
    if (password) {
        const $fill = $('.strength-fill');
        const $text = $('.strength-text');
        
        $fill.css('width', strength.percentage + '%');
        $fill.removeClass('weak medium strong').addClass(strength.class);
        $text.text(strength.text);
    } else {
        $('.password-strength-meter').remove();
    }
}

function calculatePasswordStrength(password) {
    let score = 0;
    
    if (password.length >= 8) score += 20;
    if (password.length >= 12) score += 20;
    if (/[a-z]/.test(password)) score += 20;
    if (/[A-Z]/.test(password)) score += 20;
    if (/[0-9]/.test(password)) score += 20;
    
    let strength = {
        percentage: score,
        class: 'weak',
        text: 'Yếu'
    };
    
    if (score >= 60) {
        strength.class = 'medium';
        strength.text = 'Trung bình';
    }
    
    if (score >= 80) {
        strength.class = 'strong';
        strength.text = 'Mạnh';
    }
    
    return strength;
}

/**
 * Profile Statistics
 */
function loadProfileStats() {
    // Load wishlist count
    $.ajax({
        url: contextPath + '/wishlist?action=count',
        method: 'GET',
        success: function(response) {
            if (response.success) {
                updateStatCard('wishlist', response.count);
            }
        },
        error: function() {
            console.log('Failed to load wishlist count');
        }
    });
    
    // Load order count (if applicable)
    // $.ajax({
    //     url: contextPath + '/orders?action=count',
    //     method: 'GET',
    //     success: function(response) {
    //         if (response.success) {
    //             updateStatCard('orders', response.count);
    //         }
    //     }
    // });
}

function updateStatCard(type, count) {
    $(`.stat-card[data-type="${type}"] .stat-number`).text(count);
}

/**
 * Animations
 */
function initAnimations() {
    // Fade in elements
    $('.profile-container').addClass('fade-in');
    
    // Smooth scroll for anchor links
    $('a[href^="#"]').on('click', function(e) {
        e.preventDefault();
        const target = $(this.getAttribute('href'));
        if (target.length) {
            $('html, body').animate({
                scrollTop: target.offset().top - 100
            }, 800);
        }
    });
    
    // Animate counters
    animateCounters();
}

function animateCounters() {
    $('.stat-number').each(function() {
        const $this = $(this);
        const target = parseInt($this.text());
        
        $this.prop('Counter', 0).animate({
            Counter: target
        }, {
            duration: 1000,
            easing: 'swing',
            step: function(now) {
                $this.text(Math.ceil(now));
            }
        });
    });
}

/**
 * Utility Functions
 */
function showAlert(message, type = 'info') {
    const alertClass = type === 'error' ? 'alert-error' : 
                      type === 'success' ? 'alert-success' : 'alert-info';
    
    const alertHtml = `
        <div class="alert ${alertClass} alert-dismissible">
            ${message}
            <button type="button" class="btn-close" onclick="$(this).parent().fadeOut()">×</button>
        </div>
    `;
    
    // Remove existing alerts
    $('.alert-dismissible').fadeOut();
    
    // Add new alert
    $('.profile-container').prepend(alertHtml);
    
    // Auto dismiss after 5 seconds
    setTimeout(function() {
        $('.alert-dismissible').fadeOut();
    }, 5000);
}

function showLoading(element) {
    $(element).addClass('loading');
}

function hideLoading(element) {
    $(element).removeClass('loading');
}

/**
 * Profile Form Submission
 */
function submitProfileForm() {
    const $form = $('#profileForm');
    const formData = new FormData($form[0]);
    
    showLoading($form);
    
    $.ajax({
        url: $form.attr('action'),
        method: 'POST',
        data: formData,
        processData: false,
        contentType: false,
        success: function(response) {
            hideLoading($form);
            
            if (response.success) {
                showAlert('Cập nhật thông tin thành công!', 'success');
                
                // Update display information
                if (response.user) {
                    updateUserInfo(response.user);
                }
            } else {
                showAlert(response.message || 'Có lỗi xảy ra!', 'error');
            }
        },
        error: function() {
            hideLoading($form);
            showAlert('Có lỗi xảy ra khi cập nhật thông tin!', 'error');
        }
    });
}

function updateUserInfo(user) {
    // Update avatar
    if (user.avatar) {
        $('.profile-avatar, .current-avatar').attr('src', contextPath + '/uploads/avatars/' + user.avatar);
    }
    
    // Update name
    $('.profile-name').text(user.fullName);
    
    // Update header greeting
    $('.user-greeting strong').text(user.fullName);
}

// Password strength meter CSS
const passwordStrengthCSS = `
<style>
.password-strength-meter {
    margin-top: 10px;
}

.strength-bar {
    height: 6px;
    background: #e0e0e0;
    border-radius: 3px;
    overflow: hidden;
    margin-bottom: 5px;
}

.strength-fill {
    height: 100%;
    transition: width 0.3s ease, background-color 0.3s ease;
}

.strength-fill.weak {
    background: #dc3545;
}

.strength-fill.medium {
    background: #ffc107;
}

.strength-fill.strong {
    background: #28a745;
}

.strength-text {
    font-size: 12px;
    color: #666;
    font-weight: 500;
}

.file-upload-label.drag-over {
    background: #e9ecef;
    border-color: #5a6b3a;
    transform: scale(1.02);
}

.btn-close {
    background: none;
    border: none;
    font-size: 20px;
    font-weight: bold;
    line-height: 1;
    color: #000;
    text-shadow: 0 1px 0 #fff;
    opacity: 0.5;
    cursor: pointer;
    position: absolute;
    top: 10px;
    right: 15px;
}

.btn-close:hover {
    opacity: 0.75;
}

.alert-dismissible {
    position: relative;
    padding-right: 50px;
}
</style>
`;

// Inject CSS
$('head').append(passwordStrengthCSS);

// Global variables
const contextPath = $('meta[name="contextPath"]').attr('content') || '';

// Export functions for global use
window.showAlert = showAlert;
window.submitProfileForm = submitProfileForm;
