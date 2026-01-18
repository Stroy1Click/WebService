document.addEventListener('DOMContentLoaded', () => {
    // 1. Извлекаем ID категории из URL пути: /categories/{id}/subcategories
    const pathSegments = window.location.pathname.split('/');
    // Путь выглядит так: ["", "categories", "5", "subcategories"] -> ID под индексом 2
    const categoryId = pathSegments[2];

    const gridContainer = document.querySelector('.grid');

    if (gridContainer && categoryId && !isNaN(categoryId)) {
        fetchSubcategories(categoryId, gridContainer);
    } else {
        if (gridContainer) gridContainer.innerHTML = ''; // Очищаем "Загрузка...", если ID не найден
        console.error('Критическая ошибка: ID категории не найден в URL или отсутствует .grid');
        if (gridContainer) {
            gridContainer.innerHTML = '<p class="error-text">Ошибка: категория не определена.</p>';
        }
    }
});

async function fetchSubcategories(categoryId, container) {
    const API_URL = `/api/v1/categories/${categoryId}/subcategories`;
    const STORAGE_URL = '/api/v1/storage';

    try {
        // 2. СРАЗУ делаем запрос
        const response = await fetch(API_URL);

        // 3. Как только получили ответ (любой), первым делом очищаем "Загрузка..."
        container.innerHTML = '';

        if (!response.ok) {
            throw new Error(`Ошибка сервера: ${response.status}`);
        }

        const subcategories = await response.json();

        // 4. Проверка на пустой список
        if (!subcategories || subcategories.length === 0) {
            container.innerHTML = '<p class="loading-text">В этой категории пока нет подкатегорий.</p>';
            return;
        }

        // 5. Отрисовка карточек
        const fragment = document.createDocumentFragment();

        subcategories.forEach(sub => {
            const cardLink = document.createElement('a');
            cardLink.className = 'card';

            // Ссылка на следующий уровень: Типы продуктов
            cardLink.href = `/subcategories/${sub.id}/product-types`;

            const imageUrl = sub.image
                ? `${STORAGE_URL}/${sub.image}`
                : 'https://via.placeholder.com/400x300?text=Нет+фото';

            cardLink.innerHTML = `
                <img src="${imageUrl}" alt="${sub.title}" loading="lazy">
                <div class="card-info">
                    <div class="card-title">${sub.title}</div>
                </div>
            `;
            fragment.appendChild(cardLink);
        });

        container.appendChild(fragment);

    } catch (error) {
        console.error('Ошибка в fetchSubcategories:', error);
        // В случае падения скрипта тоже убираем надпись "Загрузка" и пишем ошибку
        container.innerHTML = '<p class="error-text">Не удалось загрузить данные. Попробуйте обновить страницу (Ctrl+F5).</p>';
    }
}