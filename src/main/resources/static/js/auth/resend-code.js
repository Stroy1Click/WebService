document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('confirmationForm'); // Исправлено на ID из твоего HTML

    if (!form) {
        console.error("Форма с id='confirmationForm' не найдена!");
        return;
    }

    const emailInput = document.getElementById('email');
    const errorDiv = document.getElementById('error-message');

    const csrfToken = document.getElementById('csrfToken')?.value;

    const urlParams = new URLSearchParams(window.location.search);
    const typeParam = urlParams.get('type') || 'REGISTRATION';

    const savedEmail = localStorage.getItem('user_email');
    if (savedEmail && emailInput) {
        emailInput.value = savedEmail;
    }

    form.addEventListener('submit', async function (e) {
        e.preventDefault();
        e.stopPropagation();

        if (!emailInput.value) {
            showError("Введите email");
            return;
        }

        const payload = {
            email: emailInput.value.trim(),
            confirmationCodeType: typeParam
        };

        try {
            const response = await fetch('/api/v1/confirmation-codes/regeneration', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'X-CSRF-TOKEN': csrfToken
                },
                body: JSON.stringify(payload)
            });

            const responseText = await response.text();
            let data = {};
            try { data = JSON.parse(responseText); } catch (err) { data = { message: responseText }; }

            if (response.ok) {
                localStorage.setItem('user_email', emailInput.value.trim());

                alert("Код успешно переотправлен!");

                if (typeParam === 'PASSWORD') {
                    window.location.href = '/account/forgot-password/reset';
                } else {
                    window.location.href = '/account/email/verify';
                }
            } else {
                handleApiErrors(response.status, data);
            }
        } catch (error) {
            console.error('Fetch error:', error);
            showError("Ошибка соединения с сервером.");
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