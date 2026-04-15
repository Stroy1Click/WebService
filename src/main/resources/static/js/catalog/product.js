document.addEventListener('DOMContentLoaded', () => {
    const match = window.location.pathname.match(/\/products\/(\d+)/);
    const productId = match ? match[1] : null;

    if (productId) {
        loadProductInfo(productId);
        loadProductImages(productId);
    } else {
        console.error("Не удалось определить ID товара из URL");
    }
});

let currentProductUnit = null;

const unitLabels = {
    PIECE: 'шт.',
    KG: 'кг',
    GRAM: 'г',
    LITER: 'л',
    ML: 'мл',
    METER: 'м',
    PACK: 'упак.'
};
async function loadProductInfo(id) {
    try {

        const response = await fetch(`/api/v1/products/${id}`);
        if (!response.ok) throw new Error('Ошибка загрузки данных');

        const data = await response.json(); // ProductDto

        document.getElementById('productId').textContent = data.id;
        document.getElementById('productTitle').textContent = data.title;
        document.getElementById('productDescription').textContent = data.description;

        // Получаем единицу измерения
        const unit = unitLabels[data.unit] || 'шт.';
        // Выводим цену вместе с единицей измерения
        document.getElementById('productPrice').textContent = `${data.price.toLocaleString()} ₽ / ${unit}`;
        currentProductUnit = data.unit;

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

let currentImageIndex = 0;
let productImages = [];

async function loadProductImages(id) {
    try {
        const response = await fetch(`/api/v1/products/${id}/images`);
        const data = await response.json();

        if (data && data.length > 0) {
            productImages = data.map(img => `/api/v1/storage/catalog/${img.link}`);
        } else {
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

    const hasMultiple = productImages.length > 1;

    mainImgContainer.innerHTML = `
        <img src="${productImages[currentImageIndex]}" alt="Товар" class="fade-in">
        ${hasMultiple ? `
            <button class="gallery-arrow prev" onclick="changeSlide(-1)">❮</button>
            <button class="gallery-arrow next" onclick="changeSlide(1)">❯</button>
        ` : ''}
    `;

    if (hasMultiple) {
        thumbnailsContainer.innerHTML = productImages.map((src, idx) => `
            <img src="${src}" 
                 class="${idx === currentImageIndex ? 'active-thumb' : ''}" 
                 onclick="setSlide(${idx})" 
                 alt="thumb">
        `).join('');
    } else {
        thumbnailsContainer.innerHTML = '';
    }
}

window.changeSlide = function(step) {
    currentImageIndex += step;
    if (currentImageIndex >= productImages.length) currentImageIndex = 0;
    if (currentImageIndex < 0) currentImageIndex = productImages.length - 1;
    renderGallery();
};

window.setSlide = function(index) {
    currentImageIndex = index;
    renderGallery();
};

const addToCartBtn = document.querySelector('.add-to-cart-btn');

if (addToCartBtn) {
    addToCartBtn.onclick = () => {
        const id = document.getElementById('productId').innerText;
        const title = document.getElementById('productTitle').innerText;
        const priceRaw = document.getElementById('productPrice').innerText;
        const price = parseFloat(priceRaw.replace(/[^\d.]/g, ''));

        const imgEl = document.querySelector('#mainImageContainer img');
        const imageSrc = imgEl ? imgEl.src : '';

        const product = {
            id: parseInt(id),
            title: title,
            unit: currentProductUnit || "PIECE",
            image: imageSrc,
            price: price
        };

        CartService.addToCart(product, 1);
    };
}