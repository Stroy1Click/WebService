document.addEventListener('DOMContentLoaded', () => {
    const match = window.location.pathname.match(/\/products\/(\d+)/);
    const productId = match ? match[1] : null;

    if (productId) {
        loadProductInfo(productId);
        loadProductImages(productId);
        initQuantitySelector();
    } else {
        console.error("Не удалось определить ID товара из URL");
    }
});

let currentProductUnit = null;
let currentProductInStock = true;
let currentProductId = null;

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
        const data = await response.json();

        currentProductId = data.id;
        document.getElementById('productId').textContent = data.id;
        document.getElementById('productTitle').textContent = data.title;
        document.getElementById('productDescription').textContent = data.description;

        const unit = unitLabels[data.unit] || 'шт.';
        document.getElementById('productPrice').textContent = `${data.price.toLocaleString()} ₽ / ${unit}`;
        currentProductUnit = data.unit;
        currentProductInStock = data.inStock === true;

        const stockEl = document.getElementById('productStock');
        if (currentProductInStock) {
            stockEl.textContent = 'В наличии';
            stockEl.className = 'stock in-stock';
        } else {
            stockEl.textContent = 'Нет в наличии';
            stockEl.className = 'stock out-of-stock';
            disablePurchaseControls(true);
        }
    } catch (e) {
        console.error(e);
        document.getElementById('productTitle').textContent = 'Ошибка загрузки товара';
    }
}

function disablePurchaseControls(disabled) {
    const addBtn = document.getElementById('addToCartBtn');
    const minus = document.getElementById('qtyMinus');
    const plus = document.getElementById('qtyPlus');
    const qtyInput = document.getElementById('quantityInput');
    if (addBtn) addBtn.disabled = disabled;
    if (minus) minus.disabled = disabled;
    if (plus) plus.disabled = disabled;
    if (qtyInput) qtyInput.disabled = disabled;
}

function initQuantitySelector() {
    const qtyInput = document.getElementById('quantityInput');
    const minusBtn = document.getElementById('qtyMinus');
    const plusBtn = document.getElementById('qtyPlus');
    const addToCartBtn = document.getElementById('addToCartBtn');

    if (!qtyInput) {
        createQuantitySelector();
        return;
    }

    qtyInput.min = 1;
    qtyInput.value = 1;
    qtyInput.max = 999999;

    qtyInput.addEventListener('keydown', (e) => {
        e.preventDefault();
    });

    if (minusBtn) {
        minusBtn.onclick = (e) => {
            e.preventDefault();
            changeQuantity(-1);
        };
    }
    if (plusBtn) {
        plusBtn.onclick = (e) => {
            e.preventDefault();
            changeQuantity(1);
        };
    }

    qtyInput.addEventListener('change', () => {
        let val = parseInt(qtyInput.value);
        if (isNaN(val) || val < 1) val = 1;
        if (val > 999999) val = 999999;
        qtyInput.value = val;
    });

    if (addToCartBtn) {
        addToCartBtn.onclick = () => {
            if (!currentProductInStock) {
                alert('Товар отсутствует в наличии');
                return;
            }
            const quantity = parseInt(qtyInput.value);
            if (quantity < 1) {
                alert('Количество должно быть не менее 1');
                return;
            }
            addToCart(quantity);
        };
    }
}

function createQuantitySelector() {
    const purchaseDiv = document.querySelector('.product-purchase');
    if (!purchaseDiv) return;
    const selectorHtml = `
        <div class="quantity-selector">
            <label>Количество:</label>
            <div class="qty-control">
                <button class="qty-btn" id="qtyMinus">−</button>
                <input type="number" id="quantityInput" value="1" min="1" readonly>
                <button class="qty-btn" id="qtyPlus">+</button>
            </div>
        </div>
    `;
    const addBtn = purchaseDiv.querySelector('.add-to-cart-btn');
    if (addBtn) {
        addBtn.insertAdjacentHTML('beforebegin', selectorHtml);
    } else {
        purchaseDiv.innerHTML += selectorHtml;
    }
    initQuantitySelector();
}

function changeQuantity(delta) {
    const qtyInput = document.getElementById('quantityInput');
    if (!qtyInput) return;
    let val = parseInt(qtyInput.value);
    if (isNaN(val)) val = 1;
    val += delta;  // delta = -1 или +1
    if (val < 1) val = 1;
    if (val > 999999) val = 999999;
    qtyInput.value = val;
}

function addToCart(quantity) {
    const id = currentProductId || document.getElementById('productId').innerText;
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
        price: price,
        maxQuantity: 999999
    };

    CartService.addToCart(product, quantity);
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