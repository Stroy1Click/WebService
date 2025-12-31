document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('confirmationForm');
    if (!form) return;

    // На этой странице инпут с кодом имеет id="code"
    const codeInput = document.getElementById('code');
    const errorDiv = document.getElementById('error-message');

    // Извлекаем email, который мы должны были сохранить на предыдущем шаге
    const savedEmail = localStorage.getItem('user_email');

    form.addEventListener('submit', async function (e) {
        e.preventDefault();

        // Проверка: нашли ли мы элементы, чтобы избежать TypeError
        if (!codeInput) {
            console.error("Поле 'code' не найдено в HTML");
            return;
        }

        if (!savedEmail) {
            showError("Email не найден. Вернитесь на шаг назад и введите почту.");
            return;
        }

        errorDiv.style.display = 'none';

        // Формируем JSON для CodeVerificationRequest
        const payload = {
            email: savedEmail,
            code: parseInt(codeInput.value) // Преобразуем в число для Integer в Java
        };

        try {
            const response = await fetch('/api/v1/confirmation-codes/email/verify', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    // Если используешь CSRF, добавь его сюда из скрытого поля
                },
                body: JSON.stringify(payload)
            });

            // Читаем ответ (текст или JSON)
            const responseText = await response.text();
            let data;
            try {
                data = JSON.parse(responseText);
            } catch(e) {
                data = { message: responseText };
            }

            if (response.ok) {
                localStorage.removeItem('user_email'); // Очищаем память
                alert("Почта подтверждена!");
                window.location.href = '/user/profile';
            } else {
                handleApiErrors(response.status, data);
            }
        } catch (error) {
            showError("Ошибка сети или сервера.");
        }
    });

    function handleApiErrors(status, data) {
        const fieldErrors = data.errors || (data.properties && data.properties.errors);
        if (fieldErrors) {
            const msg = Object.values(fieldErrors).flat()[0];
            showError(msg);
        } else {
            showError(data.detail || data.message || `Ошибка: ${status}`);
        }
    }

    function showError(msg) {
        errorDiv.innerText = msg;
        errorDiv.style.display = 'block';
    }
});