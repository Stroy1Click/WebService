document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('confirmationForm');
    if (!form) return;

    const codeInput = document.getElementById('code');
    const newPassInput = document.getElementById('newPassword');
    const confirmPassInput = document.getElementById('confirmPassword');
    const errorDiv = document.getElementById('error-message');
    const csrfToken = document.getElementById('csrfToken')?.value;

    const savedEmail = localStorage.getItem('user_email');

    if (!savedEmail) {
        showError("Email не найден. Пожалуйста, начните процедуру восстановления заново.");
        form.querySelector('button').disabled = true;
        return;
    }

    form.addEventListener('submit', async function (e) {
        e.preventDefault();
        errorDiv.style.display = 'none';
        errorDiv.innerText = '';

        if (newPassInput.value !== confirmPassInput.value) {
            showError("Пароли не совпадают");
            return;
        }

        const payload = {
            newPassword: newPassInput.value,
            confirmPassword: confirmPassInput.value,
            codeVerificationRequest: {
                email: savedEmail,
                code: parseInt(codeInput.value)
            }
        };

        try {
            const response = await fetch('/api/v1/confirmation-codes/password-reset', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'X-CSRF-TOKEN': csrfToken
                },
                body: JSON.stringify(payload)
            });

            const responseText = await response.text();
            let data = {};
            try {
                data = JSON.parse(responseText);
            } catch (e) {
                data = { message: responseText };
            }

            if (response.ok) {
                localStorage.removeItem('user_email');
                alert("Пароль успешно изменен! Теперь вы можете войти.");
                window.location.href = '/account/login';
            } else {
                handleApiErrors(response.status, data);
            }
        } catch (error) {
            console.error(error);
            showError("Ошибка сети. Попробуйте позже.");
        }
    });

    function handleApiErrors(status, data) {
        const fieldErrors = data.errors || (data.properties && data.properties.errors);

        if (fieldErrors && typeof fieldErrors === 'object') {
            const messages = Object.values(fieldErrors).flat();
            showError(messages[0]);
        } else {
            showError(data.detail || data.message || `Ошибка сервера: ${status}`);
        }
    }

    function showError(msg) {
        errorDiv.innerText = msg;
        errorDiv.style.display = 'block';
    }
});