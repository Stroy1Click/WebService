document.addEventListener('DOMContentLoaded', () => {
    const pathSegments = window.location.pathname.split('/');
    const categoryId = pathSegments[2];

    const gridContainer = document.querySelector('.grid');

    if (gridContainer && categoryId && !isNaN(categoryId)) {
        fetchSubcategories(categoryId, gridContainer);
    } else {
        if (gridContainer) gridContainer.innerHTML = '';
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
        const response = await fetch(API_URL);

        container.innerHTML = '';

        if (!response.ok) {
            throw new Error(`Ошибка сервера: ${response.status}`);
        }

        const subcategories = await response.json();

        if (!subcategories || subcategories.length === 0) {
            container.innerHTML = '<p class="loading-text">В этой категории пока нет подкатегорий.</p>';
            return;
        }

        const fragment = document.createDocumentFragment();

        subcategories.forEach(sub => {
            const cardLink = document.createElement('a');
            cardLink.className = 'card';

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
        container.innerHTML = '<p class="error-text">Не удалось загрузить данные. Попробуйте обновить страницу (Ctrl+F5).</p>';
    }
}