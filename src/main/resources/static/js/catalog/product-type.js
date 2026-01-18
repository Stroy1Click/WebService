document.addEventListener('DOMContentLoaded', () => {
    const pathSegments = window.location.pathname.split('/');
    const subcategoryId = pathSegments[2];

    const gridContainer = document.querySelector('.grid');

    if (gridContainer && subcategoryId && !isNaN(subcategoryId)) {
        fetchProductTypes(subcategoryId, gridContainer);
    }
});

async function fetchProductTypes(subcategoryId, container) {
    const API_URL = `/api/v1/subcategories/${subcategoryId}/product-types`;
    const STORAGE_URL = '/api/v1/storage';

    try {
        const response = await fetch(API_URL);
        container.innerHTML = '';

        if (!response.ok) throw new Error('Ошибка сети');

        const productTypes = await response.json();

        // --- ДИАГНОСТИКА: Посмотри в консоль браузера (F12) ---
        console.log("Полученные данные:", productTypes);

        if (!productTypes || productTypes.length === 0) {
            container.innerHTML = '<p class="loading-text">Типы товаров не найдены.</p>';
            return;
        }

        productTypes.forEach(type => {
            const cardLink = document.createElement('a');
            cardLink.className = 'card';
            cardLink.href = `/product-types/${type.id}/products`;

            // Выводим в консоль имя картинки для каждой карточки
            console.log(`Тип: ${type.title}, Имя картинки из БД:`, type.image);

            // ПРОВЕРКА: Если type.image — это null или "", запрос к storage не пойдет.
            // Мы принудительно формируем путь, если имя файла существует.
            let finalImageUrl = 'https://via.placeholder.com/400x300?text=No+Image';

            if (type.image && type.image.trim() !== "") {
                finalImageUrl = `${STORAGE_URL}/${type.image}`;
            }

            cardLink.innerHTML = `
                <img src="${finalImageUrl}" alt="${type.title}">
                <div class="card-info">
                    <div class="card-title">${type.title}</div>
                </div>
            `;
            container.appendChild(cardLink);
        });

    } catch (error) {
        console.error('Ошибка:', error);
        container.innerHTML = '<p class="error-text">Ошибка загрузки.</p>';
    }
}