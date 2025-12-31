async function submitLoginForm(event) {
    event.preventDefault();

    const form = document.getElementById('loginForm');
    const errorMessage = document.getElementById('errorMessage');

    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const csrfToken = document.getElementById('csrfToken').value;

    errorMessage.style.display = 'none';

    if (!email || !password) {
        showError('Пожалуйста, заполните все поля');
        return;
    }

    try {
        const submitBtn = form.querySelector('.login-btn');
        const originalText = submitBtn.textContent;
        submitBtn.textContent = 'Вход...';
        submitBtn.disabled = true;

        const response = await fetch('/process_login', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/x-www-form-urlencoded',
                'X-CSRF-TOKEN': csrfToken
            },
            body: new URLSearchParams({
                'username': email,
                'password': password,
                '_csrf': csrfToken
            })
        });

        submitBtn.textContent = originalText;
        submitBtn.disabled = false;

        if (response.ok) {
            window.location.href = '/user/profile';
        } else {
            showError('Неверный email или пароль');
        }

    } catch (error) {
        console.error('Ошибка:', error);
        showError('Произошла ошибка при входе. Попробуйте позже.');

        const submitBtn = form.querySelector('.login-btn');
        submitBtn.textContent = 'Войти';
        submitBtn.disabled = false;
    }
}

function showError(message) {
    const errorElement = document.getElementById('errorMessage');
    errorElement.textContent = message;
    errorElement.style.display = 'block';

    // Автоматически скрываем ошибку через 5 секунд
    setTimeout(() => {
        errorElement.style.display = 'none';
    }, 5000);
}

function forgotPassword() {
    window.location.href = '/account/forgot-password/request';
}

function register() {
    window.location.href = '/account/registration';
}

function verifyEmail() {
    window.location.href = '/account/email/request';
}


document.addEventListener('DOMContentLoaded', function () {
    const emailInput = document.getElementById('email');
    const passwordInput = document.getElementById('password');

    passwordInput.addEventListener('keypress', function (event) {
        if (event.key === 'Enter') {
            event.preventDefault();
            document.getElementById('loginForm').dispatchEvent(new Event('submit'));
        }
    });
});