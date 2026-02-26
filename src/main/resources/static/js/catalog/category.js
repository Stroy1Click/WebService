document.addEventListener('DOMContentLoaded', () => {
    fetchCategories();
});

async function fetchCategories() {
    const gridContainer = document.querySelector('.grid');

    const API_URL = '/api/v1/categories';
    const STORAGE_URL = '/api/v1/storage/catalog';

    try {
        const response = await fetch(API_URL);

        gridContainer.innerHTML = '';

        if (!response.ok) {
            throw new Error(`Ошибка сервера: ${response.status}`);
        }

        const categories = await response.json();

        if (!categories || categories.length === 0) {
            gridContainer.innerHTML = '<p class="loading-text">Категории пока не добавлены.</p>';
            return;
        }

        const fragment = document.createDocumentFragment();

        categories.forEach(category => {
            const card = createCategoryCard(category, STORAGE_URL);
            fragment.appendChild(card);
        });

        gridContainer.appendChild(fragment);

    } catch (error) {
        console.error('Ошибка загрузки:', error);
        gridContainer.innerHTML = '<p class="error-text">Не удалось загрузить данные. Проверьте соединение с сервером.</p>';
    }
}

function createCategoryCard(category, storagePath) {
    const cardLink = document.createElement('a');
    cardLink.className = 'card';

    cardLink.href = `/categories/${category.id}/subcategories`;

    const imageUrl = category.image
        ? `${storagePath}/${category.image}`
        : 'https://via.placeholder.com/400x300?text=Нет+фото';

    cardLink.innerHTML = `
        <img src="${imageUrl}" alt="${category.title}" loading="lazy">
        <div class="card-info">
            <div class="card-title">${category.title}</div>
            </div>
    `;

    return cardLink;
}