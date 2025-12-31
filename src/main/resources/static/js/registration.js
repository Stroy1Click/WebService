if (window.registrationScriptLoaded) {
    // Если скрипт уже загружен, выходим
} else {
    window.registrationScriptLoaded = true;

    document.addEventListener('DOMContentLoaded', function () {
        // Весь ваш код здесь...
    });
}

document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('registrationForm');
    const registerBtn = document.getElementById('registerBtn');
    const eyeIcon = document.getElementById('eye');
    const passwordInput = form.querySelector('input[name="password"]');

    /**
     * 1. ЛОГИКА ГЛАЗИКА (ПОКАЗАТЬ/СКРЫТЬ ПАРОЛЬ)
     */
    if (eyeIcon && passwordInput) {
        eyeIcon.addEventListener('click', () => {
            const isPassword = passwordInput.type === 'password';
            passwordInput.type = isPassword ? 'text' : 'password';
            eyeIcon.setAttribute('aria-pressed', isPassword);
            eyeIcon.style.opacity = isPassword ? '1' : '0.6';
        });
    }

    /**
     * 2. ОТПРАВКА ФОРМЫ РЕГИСТРАЦИИ
     */
    if (registerBtn) {
        registerBtn.addEventListener('click', async function (e) {
            e.preventDefault();

            // Сбрасываем старые ошибки
            clearAllErrors();

            // Сбор данных
            const userDto = {
                firstName: form.querySelector('input[name="firstName"]').value.trim(),
                lastName: form.querySelector('input[name="lastName"]').value.trim(),
                email: form.querySelector('input[name="email"]').value.trim(),
                password: passwordInput.value,
                emailConfirmed: false,
                role: 'ROLE_USER'
            };

            const csrfToken = document.getElementById('csrfToken')?.value;

            try {
                const response = await fetch('/api/v1/auth/registration', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json',
                        'X-CSRF-TOKEN': csrfToken
                    },
                    body: JSON.stringify(userDto)
                });

                const responseText = await response.text();
                let data = null;
                try {
                    data = JSON.parse(responseText);
                } catch (e) {
                    data = { detail: responseText };
                }

                if (response.ok) {
                    // УСПЕХ
                    alert('Регистрация прошла успешно!');
                    window.location.href = '/account/login';
                } else {
                    // ОБРАБОТКА ОШИБОК (400, 403, 500)
                    handleServerErrors(response.status, data);
                }

            } catch (error) {
                console.error('Network error:', error);
                showGlobalError('Ошибка сети. Проверьте соединение с сервером.');
            }
        });
    }

    /**
     * РАСПРЕДЕЛЕНИЕ ОШИБОК ПО ПОЛЯМ
     */
    function handleServerErrors(status, data) {
        if (status === 400) {
            // Ищем мапу ошибок (errors) в ответе бэкенда
            const fieldErrors = data.errors || (data.properties && data.properties.errors) || data;

            if (typeof fieldErrors === 'object' && !Array.isArray(fieldErrors)) {
                let foundFields = false;
                for (const [field, message] of Object.entries(fieldErrors)) {
                    // Пропускаем технические поля JSON
                    if (['status', 'title', 'detail', 'instance', 'type'].includes(field)) continue;

                    showFieldError(field, message);
                    foundFields = true;
                }
                if (foundFields) return;
            }
        }

        // Если это не ошибка валидации полей или формат не распознан
        const message = data.detail || data.message || data.title || `Ошибка сервера: ${status}`;
        showGlobalError(message);
    }

    /**
     * ОТОБРАЖЕНИЕ ТЕКСТА ОШИБКИ ПОД КОНКРЕТНЫМ ИНПУТОМ
     */
    function showFieldError(fieldName, message) {
        const input = form.querySelector(`input[name="${fieldName}"]`);
        if (input) {
            const group = input.closest('.input-group');
            if (group) {
                group.classList.add('error'); // Для красной рамки в CSS
                const errorSpan = group.querySelector('.error-message');
                if (errorSpan) {
                    // Если бэкенд прислал список ошибок для одного поля, берем первую
                    errorSpan.textContent = Array.isArray(message) ? message[0] : message;
                    errorSpan.style.display = 'block';
                }
            }
        }
    }

    /**
     * ВЫВОД ОБЩЕЙ ОШИБКИ ВВЕРХУ ФОРМЫ
     */
    function showGlobalError(message) {
        let globalErr = document.getElementById('global-error-msg');
        if (!globalErr) {
            globalErr = document.createElement('div');
            globalErr.id = 'global-error-msg';
            globalErr.style.color = '#e74c3c';
            globalErr.style.textAlign = 'center';
            globalErr.style.marginBottom = '15px';
            globalErr.style.fontSize = '14px';
            form.insertBefore(globalErr, form.firstChild);
        }
        globalErr.textContent = message;
        globalErr.style.display = 'block';
    }

    /**
     * ОЧИСТКА ВСЕХ ОШИБОК ПЕРЕД НОВЫМ ЗАПРОСОМ
     */
    function clearAllErrors() {
        form.querySelectorAll('.error-message').forEach(el => {
            el.textContent = '';
            el.style.display = 'none';
        });
        form.querySelectorAll('.input-group').forEach(el => el.classList.remove('error'));
        const globalErr = document.getElementById('global-error-msg');
        if (globalErr) globalErr.style.display = 'none';
    }
});

