document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('confirmationForm');
    if (!form) return;

    const codeInput = document.getElementById('code');
    const errorDiv = document.getElementById('error-message');

    const savedEmail = localStorage.getItem('user_email');

    form.addEventListener('submit', async function (e) {
        e.preventDefault();

        if (!codeInput) {
            console.error("Поле 'code' не найдено в HTML");
            return;
        }

        if (!savedEmail) {
            showError("Email не найден. Вернитесь на шаг назад и введите почту.");
            return;
        }

        errorDiv.style.display = 'none';

        const payload = {
            email: savedEmail,
            code: parseInt(codeInput.value)
        };

        try {
            const response = await fetch('/api/v1/confirmation-codes/email/verify', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify(payload)
            });

            const responseText = await response.text();
            let data;
            try {
                data = JSON.parse(responseText);
            } catch(e) {
                data = { message: responseText };
            }

            if (response.ok) {
                localStorage.removeItem('user_email');
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