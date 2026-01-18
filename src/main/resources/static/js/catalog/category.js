document.addEventListener('DOMContentLoaded', () => {
    fetchCategories();
});

async function fetchCategories() {
    // Выбираем контейнер, где лежит текст "Загрузка..."
    const gridContainer = document.querySelector('.grid');

    // Базовые URL
    const API_URL = '/api/v1/categories';
    const STORAGE_URL = '/api/v1/storage';

    try {
        // Выполняем запрос к вашему API
        const response = await fetch(API_URL);

        // КАК ТОЛЬКО ПОЛУЧЕН ОТВЕТ — СРАЗУ ОЧИЩАЕМ КОНТЕЙНЕР
        // Это убирает надпись "Загрузка..."
        gridContainer.innerHTML = '';

        if (!response.ok) {
            throw new Error(`Ошибка сервера: ${response.status}`);
        }

        const categories = await response.json();

        // Если категорий нет в базе данных
        if (!categories || categories.length === 0) {
            gridContainer.innerHTML = '<p class="loading-text">Категории пока не добавлены.</p>';
            return;
        }

        // Используем фрагмент для быстрой отрисовки
        const fragment = document.createDocumentFragment();

        categories.forEach(category => {
            const card = createCategoryCard(category, STORAGE_URL);
            fragment.appendChild(card);
        });

        // Добавляем все карточки в DOM одним махом
        gridContainer.appendChild(fragment);

    } catch (error) {
        console.error('Ошибка загрузки:', error);
        // В случае ошибки (например, бэкенд выключен) убираем загрузку и пишем ошибку
        gridContainer.innerHTML = '<p class="error-text">Не удалось загрузить данные. Проверьте соединение с сервером.</p>';
    }
}

/**
 * Функция создания карточки категории
 */
function createCategoryCard(category, storagePath) {
    // Создаем ссылку, чтобы вся карточка была кликабельной
    const cardLink = document.createElement('a');
    cardLink.className = 'card';

    // Формируем путь для перехода согласно вашему контроллеру
    cardLink.href = `/categories/${category.id}/subcategories`;

    // Формируем путь к картинке через ваш эндпоинт хранения
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