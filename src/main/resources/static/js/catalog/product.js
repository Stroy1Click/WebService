const SIZE = 2;

document.addEventListener('DOMContentLoaded', () => {
    const match = window.location.pathname.match(/\/product-types\/(\d+)/);
    const productTypeId = match ? match[1] : null;

    if (productTypeId) {
        loadFilters(productTypeId);
        loadInitialProducts(productTypeId, 0);

        const applyBtn = document.getElementById('applyBtn');
        if (applyBtn) {
            applyBtn.onclick = () => applyFilters(productTypeId, 0);
        }
    }
});

function goToProduct(id) {
    window.location.href = `/products/${id}`;
}

async function loadFilters(productTypeId) {
    const container = document.getElementById('dynamic-filters');
    try {
        const res = await fetch(`/api/v1/product-type-attribute-values?productTypeId=${productTypeId}`);
        const values = await res.json();

        const grouped = values.reduce((acc, item) => {
            acc[item.attributeId] = acc[item.attributeId] || [];
            acc[item.attributeId].push(item);
            return acc;
        }, {});

        container.innerHTML = '';
        for (const attrId in grouped) {
            const attrRes = await fetch(`/api/v1/attributes/${attrId}`);
            const attrData = await attrRes.json();

            container.insertAdjacentHTML('beforeend', `
                <div class="filter-group">
                    <h4 class="filter-title">${attrData.title}</h4>
                    <div class="filter-options">
                        ${grouped[attrId].map(v => `
                            <label class="filter-label">
                                <input type="checkbox" class="filter-checkbox" value="${v.value}">
                                <span>${v.value}</span>
                            </label>
                        `).join('')}
                    </div>
                </div>`);
        }
    } catch (e) { console.error("Ошибка фильтров", e); }
}

async function applyFilters(productTypeId, page = 0) {
    const grid = document.getElementById('grid');
    grid.innerHTML = 'Загрузка...';

    const filterData = { attributes: {} };
    document.querySelectorAll('.filter-group').forEach(group => {
        const title = group.querySelector('.filter-title').innerText.trim();
        const checked = Array.from(group.querySelectorAll('.filter-checkbox:checked')).map(cb => cb.value);
        if (checked.length > 0) {
            filterData.attributes[title] = checked.join(',');
        }
    });

    try {
        const res = await fetch(`/api/v1/product-attributes/filter?page=${page}&size=${SIZE}`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(filterData)
        });

        const pageData = await res.json();
        const productIds = [...new Set(pageData.content.map(item => item.productId))];

        const products = await Promise.all(
            productIds.map(id => fetch(`/api/v1/products/${id}`).then(r => r.json()))
        );

        renderGrid(products);
        renderPagination(pageData, productTypeId, true);
    } catch (e) {
        grid.innerHTML = 'Ошибка загрузки данных.';
    }
}

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
            <img src="/api/v1/storage/${img.link}" class="carousel-image ${idx === 0 ? 'active' : ''}">
        `).join('');

        if (images.length > 1) {
            html += `
                <button class="carousel-btn prev-btn" onclick="changeSlide(event, ${productId}, -1)">❮</button>
                <button class="carousel-btn next-btn" onclick="changeSlide(event, ${productId}, 1)">❯</button>
            `;
        }
        container.innerHTML = html;
    } catch (e) { container.innerHTML = `<img src="/placeholder.png" class="carousel-image active">`; }
}

/**
 * Смена слайда с остановкой всплытия события
 */
window.changeSlide = function(event, productId, dir) {
    event.stopPropagation(); // Чтобы не сработал переход на товар
    const container = document.getElementById(`carousel-${productId}`);
    const imgs = container.querySelectorAll('.carousel-image');
    let idx = Array.from(imgs).findIndex(img => img.classList.contains('active'));
    imgs[idx].classList.remove('active');
    idx = (idx + dir + imgs.length) % imgs.length;
    imgs[idx].classList.add('active');
}

async function loadInitialProducts(productTypeId, page) {
    try {
        const res = await fetch(`/api/v1/products?productTypeId=${productTypeId}&page=${page}&size=${SIZE}`);
        const data = await res.json();
        renderGrid(data.content);
        renderPagination(data, productTypeId, false);
    } catch (e) { console.error(e); }
}


function renderPagination(data, productTypeId, isFilter) {
    const container = document.getElementById('pagination');
    container.innerHTML = '';

    if (data.totalPages <= 1) return;

    for (let i = 0; i < data.totalPages; i++) {
        const btn = document.createElement('button');
        btn.innerText = i + 1;

        if (i === data.page) {
            btn.classList.add('active');
        }

        btn.onclick = () => {
            if (isFilter) {
                applyFilters(productTypeId, i);
            } else {
                loadInitialProducts(productTypeId, i);
            }
            window.scrollTo({ top: 0, behavior: 'smooth' });
        };
        container.appendChild(btn);
    }
}

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

    products.forEach(p => loadProductImages(p.id));
}
