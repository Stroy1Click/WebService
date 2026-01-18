document.addEventListener('DOMContentLoaded', function () {
    const form = document.getElementById('confirmationForm');

    if (!form) return;

    const emailInput = document.getElementById('email');
    const errorDiv = document.getElementById('error-message');
    const csrfToken = document.getElementById('csrfToken')?.value;

    form.addEventListener('submit', async function (e) {
        e.preventDefault();

        errorDiv.style.display = 'none';
        errorDiv.innerText = '';

        const payload = {
            email: emailInput.value.trim(),
            confirmationCodeType: "EMAIL"
        };

        try {
            const response = await fetch('/api/v1/confirmation-codes', {
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
                data = { detail: responseText };
            }

            if (response.ok) {
                localStorage.setItem('user_email', emailInput.value.trim());

                window.location.href = '/account/email/verify';
            }else {
                handleApiErrors(response.status, data);
            }
        } catch (error) {
            console.error('Network error:', error);
            showError('Ошибка соединения с сервером. Попробуйте позже.');
        }
    });

    function handleApiErrors(status, data) {
        const fieldErrors = data.errors || (data.properties && data.properties.errors);

        if (fieldErrors && typeof fieldErrors === 'object') {
            const firstMsg = Object.values(fieldErrors).flat()[0];
            showError(firstMsg);
        } else {
            showError(data.detail || data.title || `Ошибка сервера: ${status}`);
        }
    }

    function showError(message) {
        errorDiv.innerText = message;
        errorDiv.style.display = 'block';
    }
});