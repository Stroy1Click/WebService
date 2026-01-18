document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('confirmationForm');

    // Проверка на наличие формы, чтобы избежать ошибок на других страницах
    if (!form) return;

    const emailInput = document.getElementById('email');
    const errorDiv = document.getElementById('error-message');
    const csrfToken = document.getElementById('csrfToken')?.value;

    form.addEventListener('submit', async function (e) {
        e.preventDefault();

        // 1. Очистка старых ошибок
        errorDiv.style.display = 'none';
        errorDiv.innerText = '';

        // 2. Сбор данных (используем trim() для чистоты ввода)
        const payload = {
            email: emailInput.value.trim(),
            confirmationCodeType: "EMAIL" // Должно совпадать с твоим Enum Type
        };

        try {
            const response = await fetch('/api/v1/confirmation-codes', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'X-CSRF-TOKEN': csrfToken // Передаем токен для прохождения защиты
                },
                body: JSON.stringify(payload)
            });

            // Пытаемся получить JSON, даже если сервер вернул ошибку
            const responseText = await response.text();
            let data = {};
            try {
                data = JSON.parse(responseText);
            } catch (e) {
                data = { detail: responseText };
            }

            // Внутри email-request.js, там где response.ok:
            if (response.ok) {
                localStorage.setItem('user_email', emailInput.value.trim()); // СОХРАНЯЕМ EMAIL

                window.location.href = '/account/email/verify';
            }else {
                // 4. Обработка ошибок валидации и бизнес-логики
                handleApiErrors(response.status, data);
            }
        } catch (error) {
            console.error('Network error:', error);
            showError('Ошибка соединения с сервером. Попробуйте позже.');
        }
    });

    function handleApiErrors(status, data) {
        // Проверяем формат Problem Detail (Spring Boot)
        const fieldErrors = data.errors || (data.properties && data.properties.errors);

        if (fieldErrors && typeof fieldErrors === 'object') {
            // Если есть ошибки по полям, берем первую (например, по email)
            const firstMsg = Object.values(fieldErrors).flat()[0];
            showError(firstMsg);
        } else {
            // Иначе выводим общее сообщение
            showError(data.detail || data.title || `Ошибка сервера: ${status}`);
        }
    }

    function showError(message) {
        errorDiv.innerText = message;
        errorDiv.style.display = 'block';
    }
});