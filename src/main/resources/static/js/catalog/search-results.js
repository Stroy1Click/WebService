// search-results.js

document.addEventListener('DOMContentLoaded', () => {
    const resultsContainer = document.getElementById('grid');
    const query = localStorage.getItem('searchQuery') || '';
    const products = JSON.parse(localStorage.getItem('searchResults') || '[]');

    // Показываем поисковый запрос
    const searchQueryElem = document.querySelector('#search-query');
    if (searchQueryElem) searchQueryElem.textContent = query;

    if (!products.length) {
        resultsContainer.innerHTML = `<p>Товары не найдены.</p>`;
        return;
    }

    // Рендерим все товары
    renderGrid(products);
});

/**
 * Рендер карточек товаров
 */
function renderGrid(products) {
    const grid = document.getElementById('grid');
    grid.innerHTML = products.map(p => `
        <div class="card">
            <div class="img-carousel-container" id="carousel-${p.id}" onclick="goToProduct(${p.id})">
                <div class="spinner-small"></div>
            </div>
            <div class="card-body">
                <div class="card-title" onclick="goToProduct(${p.id})">${p.title}</div>
                <div class="price">${p.price.toLocaleString()} ₽</div>
                <button class="btn-add" onclick="event.stopPropagation(); addToCart(${p.id})">В корзину</button>
            </div>
        </div>
    `).join('');

    // Подгружаем картинки для всех товаров
    products.forEach(p => loadProductImages(p.id));
}

/**
 * Переход на страницу товара
 */
function goToProduct(id) {
    window.location.href = `/products/${id}`;
}

/**
 * Загрузка изображений товара и карусель
 */
async function loadProductImages(productId) {
    const container = document.getElementById(`carousel-${productId}`);
    try {
        const res = await fetch(`/api/v1/products/${productId}/images`);
        const images = await res.json();

        if (!images || images.length === 0) {
            container.innerHTML = `<img src="/placeholder.png" class="carousel-image active">`;
            return;
        }

        let html = images.map((img, idx) => `
            <img src="/api/v1/storage/catalog/${img.link}" class="carousel-image ${idx === 0 ? 'active' : ''}">
        `).join('');

        if (images.length > 1) {
            html += `
                <button class="carousel-btn prev-btn" onclick="changeSlide(event, ${productId}, -1)">❮</button>
                <button class="carousel-btn next-btn" onclick="changeSlide(event, ${productId}, 1)">❯</button>
            `;
        }

        container.innerHTML = html;
    } catch (e) {
        console.error(e);
        container.innerHTML = `<img src="/placeholder.png" class="carousel-image active">`;
    }
}

/**
 * Смена слайда карусели
 */
window.changeSlide = function(event, productId, dir) {
    event.stopPropagation(); // чтобы не срабатывал переход на товар
    const container = document.getElementById(`carousel-${productId}`);
    const imgs = container.querySelectorAll('.carousel-image');
    if (!imgs.length) return;

    let idx = Array.from(imgs).findIndex(img => img.classList.contains('active'));
    imgs[idx].classList.remove('active');
    idx = (idx + dir + imgs.length) % imgs.length;
    imgs[idx].classList.add('active');
};

/**
 * Заглушка для добавления в корзину
 */
function addToCart(productId) {
    console.log(`Добавляем продукт ${productId} в корзину`);
    alert(`Продукт ${productId} добавлен в корзину`);
}
