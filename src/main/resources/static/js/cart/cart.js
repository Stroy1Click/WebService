/**
 * Управление корзиной (используем localStorage)
 * Структура объекта в хранилище:
 * [
 * { id: 1, title: "Товар", price: 1000, image: "link", quantity: 1 },
 * ...
 * ]
 */

const CART_KEY = 'stroy1click_cart';

const CartService = {

    // === 1. Получить данные корзины ===
    getCart() {
        const cart = localStorage.getItem(CART_KEY);
        return cart ? JSON.parse(cart) : [];
    },

    // === 2. Добавить товар ===
    // Мы сохраняем title и price сразу, чтобы не делать лишних запросов на странице корзины
    addToCart(product, quantity = 1) {
        const cart = this.getCart();
        const existingItem = cart.find(item => item.id === product.id);

        if (existingItem) {
            existingItem.quantity += quantity;
        } else {
            cart.push({
                id: product.id,
                title: product.title,
                price: product.price,
                image: product.image, // Первая картинка или заглушка
                quantity: quantity
            });
        }

        localStorage.setItem(CART_KEY, JSON.stringify(cart));
        this.updateCartBadge();
        alert('Товар добавлен в корзину!');
    },

    // === 3. Удалить товар ===
    removeFromCart(productId) {
        let cart = this.getCart();
        cart = cart.filter(item => item.id !== productId);
        localStorage.setItem(CART_KEY, JSON.stringify(cart));

        // Перерисовать корзину, если мы на странице корзины
        if (window.location.pathname.includes('cart')) {
            renderCartPage();
        }
        this.updateCartBadge();
    },

    // === 4. Изменить количество ===
    updateQuantity(productId, newQuantity) {
        const cart = this.getCart();
        const item = cart.find(i => i.id === productId);
        if (item) {
            item.quantity = parseInt(newQuantity);
            if (item.quantity <= 0) {
                this.removeFromCart(productId);
                return;
            }
            localStorage.setItem(CART_KEY, JSON.stringify(cart));
            if (window.location.pathname.includes('cart')) {
                renderCartPage(); // Пересчитать итоги
            }
        }
    },

    // === 5. Очистить корзину ===
    clearCart() {
        localStorage.removeItem(CART_KEY);
        this.updateCartBadge();
    },

    // === 6. Обновить счетчик в хедере (если есть бейдж) ===
    updateCartBadge() {
        const cart = this.getCart();
        const totalItems = cart.reduce((sum, item) => sum + item.quantity, 0);
        const badge = document.querySelector('.cart-badge'); // Добавь <span class="cart-badge"> в хедер
        if (badge) {
            badge.innerText = totalItems;
            badge.style.display = totalItems > 0 ? 'inline-block' : 'none';
        }
    },

    // === 7. Оформить заказ (POST запрос) ===
    async checkout(contactPhone, notes, userId) {
        const cart = this.getCart();

        if (cart.length === 0) {
            alert("Корзина пуста");
            return;
        }

        // Формируем OrderItemDto
        const orderItems = cart.map(item => ({
            productId: item.id,
            quantity: item.quantity
        }));

        // Формируем OrderDto
        // Важно: сервер требует LocalDateTime, отправляем ISO строку.
        // В идеале сервер сам ставит время создания, но раз в DTO @NotNull, шлем текущее.
        const orderDto = {
            userId: userId, // ID текущего юзера (нужно передать откуда-то)
            contactPhone: contactPhone,
            notes: notes || "",
            orderStatus: "CREATED", // Или enum, который ждет сервер (например 'CREATED')
            createdAt: new Date().toISOString(),
            updatedAt: new Date().toISOString(),
            orderItems: orderItems
        };

        try {
            const response = await fetch('/api/v1/orders', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(orderDto)
            });

            if (response.ok) {
                alert("Заказ успешно оформлен!");
                this.clearCart();
                window.location.href = '/'; // Редирект на главную
            } else {
                const errorData = await response.json();
                console.error("Ошибка заказа:", errorData);
                alert("Ошибка при оформлении: " + (errorData.message || "Проверьте данные"));
            }
        } catch (e) {
            console.error(e);
            alert("Ошибка сети");
        }
    }
};

// Инициализация бейджа при загрузке
document.addEventListener('DOMContentLoaded', () => {
    CartService.updateCartBadge();
});