document.addEventListener('DOMContentLoaded', () => {
    // 1. Получаем ID товара из URL (например, /products/105 -> 105)
    const match = window.location.pathname.match(/\/products\/(\d+)/);
    const productId = match ? match[1] : null;

    if (productId) {
        loadProductInfo(productId);
        loadProductImages(productId);
    } else {
        console.error("Не удалось определить ID товара из URL");
    }
});

// === ЗАГРУЗКА ИНФОРМАЦИИ О ТОВАРЕ ===
async function loadProductInfo(id) {
    try {
        const response = await fetch(`/api/v1/products/${id}`);
        if (!response.ok) throw new Error('Ошибка загрузки данных');

        const data = await response.json(); // ProductDto

        // Заполняем текстовые поля
        document.getElementById('productId').textContent = data.id;
        document.getElementById('productTitle').textContent = data.title;
        document.getElementById('productDescription').textContent = data.description;
        document.getElementById('productPrice').textContent = `${data.price.toLocaleString()} ₽`;

        // Логика "В наличии"
        const stockEl = document.getElementById('productStock');
        if (data.inStock) {
            stockEl.textContent = 'В наличии';
            stockEl.className = 'stock in-stock';
        } else {
            stockEl.textContent = 'Нет в наличии';
            stockEl.className = 'stock out-of-stock';
        }
    } catch (e) {
        console.error(e);
        document.getElementById('productTitle').textContent = 'Ошибка загрузки товара';
    }
}

// === ЛОГИКА ГАЛЕРЕИ ===
let currentImageIndex = 0;
let productImages = [];

async function loadProductImages(id) {
    try {
        const response = await fetch(`/api/v1/products/${id}/images`);
        const data = await response.json(); // List<ProductImageDto>

        // Преобразуем список DTO в список ссылок
        if (data && data.length > 0) {
            productImages = data.map(img => `/api/v1/storage/${img.link}`);
        } else {
            // Заглушка, если картинок нет
            productImages = ['/static/images/placeholder.png'];
        }

        renderGallery();
    } catch (e) {
        console.error(e);
        productImages = ['/static/images/placeholder.png'];
        renderGallery();
    }
}

function renderGallery() {
    const mainImgContainer = document.getElementById('mainImageContainer');
    const thumbnailsContainer = document.getElementById('thumbnailsContainer');

    // 1. Отрисовка главной картинки и стрелок
    const hasMultiple = productImages.length > 1;

    mainImgContainer.innerHTML = `
        <img src="${productImages[currentImageIndex]}" alt="Товар" class="fade-in">
        ${hasMultiple ? `
            <button class="gallery-arrow prev" onclick="changeSlide(-1)">❮</button>
            <button class="gallery-arrow next" onclick="changeSlide(1)">❯</button>
        ` : ''}
    `;

    // 2. Отрисовка миниатюр
    if (hasMultiple) {
        thumbnailsContainer.innerHTML = productImages.map((src, idx) => `
            <img src="${src}" 
                 class="${idx === currentImageIndex ? 'active-thumb' : ''}" 
                 onclick="setSlide(${idx})" 
                 alt="thumb">
        `).join('');
    } else {
        thumbnailsContainer.innerHTML = ''; // Скрываем миниатюры, если картинка одна
    }
}

// Переключение стрелками
window.changeSlide = function(step) {
    currentImageIndex += step;
    if (currentImageIndex >= productImages.length) currentImageIndex = 0;
    if (currentImageIndex < 0) currentImageIndex = productImages.length - 1;
    renderGallery();
};

// Переключение кликом по миниатюре
window.setSlide = function(index) {
    currentImageIndex = index;
    renderGallery();
};

// Находим кнопку
const addToCartBtn = document.querySelector('.add-to-cart-btn');

if (addToCartBtn) {
    addToCartBtn.onclick = () => {
        // Собираем данные о товаре из текущей страницы
        // (Предполагается, что переменная productId уже есть в этом файле из URL)
        // Если нет, парсим из DOM
        const id = document.getElementById('productId').innerText;
        const title = document.getElementById('productTitle').innerText;
        // Цену нужно очистить от " ₽" и пробелов
        const priceRaw = document.getElementById('productPrice').innerText;
        const price = parseFloat(priceRaw.replace(/[^\d.]/g, ''));

        // Берем картинку (первую из галереи или mainImage)
        const imgEl = document.querySelector('#mainImageContainer img');
        const imageSrc = imgEl ? imgEl.src : '';

        const product = {
            id: parseInt(id),
            title: title,
            price: price,
            image: imageSrc
        };

        // Вызываем сервис корзины
        CartService.addToCart(product, 1);
    };
}