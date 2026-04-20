const CART_KEY = 'stroy1click_cart';

const CartService = {

    getCart() {
        const cart = localStorage.getItem(CART_KEY);
        return cart ? JSON.parse(cart) : [];
    },

    addToCart(product, quantity = 1) {
        const cart = this.getCart();
        const existingItem = cart.find(item => item.id === product.id);
        const maxQ = product.maxQuantity || 999999; // если нет лимита, ставим большое число

        if (existingItem) {
            let newQty = existingItem.quantity + quantity;
            if (newQty > maxQ) {
                alert(`Нельзя добавить больше ${maxQ} шт. товара "${product.title}"`);
                newQty = maxQ;
            }
            existingItem.quantity = newQty;
            existingItem.maxQuantity = maxQ; // обновляем на случай изменения
        } else {
            let addQty = Math.min(quantity, maxQ);
            cart.push({
                id: product.id,
                title: product.title,
                unit: product.unit,
                image: product.image,
                price: product.price,
                quantity: addQty,
                maxQuantity: maxQ
            });
            if (addQty < quantity) {
                alert(`Доступно только ${maxQ} шт. товара "${product.title}"`);
            }
        }

        localStorage.setItem(CART_KEY, JSON.stringify(cart));
        this.updateCartBadge();
        alert('Товар добавлен в корзину!');
    },

    removeFromCart(productId) {
        let cart = this.getCart();
        cart = cart.filter(item => item.id !== productId);
        localStorage.setItem(CART_KEY, JSON.stringify(cart));

        if (window.location.pathname.includes('cart')) {
            if (typeof renderCartPage === 'function') renderCartPage();
        }
        this.updateCartBadge();
    },

    updateQuantity(productId, newQuantity) {
        let qty = parseInt(newQuantity);
        if (isNaN(qty)) qty = 1;

        const cart = this.getCart();
        const item = cart.find(i => i.id === productId);
        if (item) {
            const maxQ = item.maxQuantity || 999999;
            // Ограничиваем снизу (минимум 1) и сверху (максимум)
            let validatedQty = Math.min(Math.max(qty, 1), maxQ);
            if (validatedQty !== qty) {
                if (qty > maxQ) {
                    alert(`Максимальное количество для "${item.title}" – ${maxQ} шт.`);
                } else if (qty < 1) {
                    validatedQty = 1;
                }
                // Обновляем значение в input на странице
                const input = document.querySelector(`.qty-input[data-id="${productId}"]`);
                if (input) input.value = validatedQty;
            }
            item.quantity = validatedQty;
            localStorage.setItem(CART_KEY, JSON.stringify(cart));
            if (window.location.pathname.includes('cart') && typeof renderCartPage === 'function') {
                renderCartPage();
            }
        }
        this.updateCartBadge();
    },

    clearCart() {
        localStorage.removeItem(CART_KEY);
        this.updateCartBadge();
    },

    updateCartBadge() {
        const cart = this.getCart();
        const totalItems = cart.reduce((sum, item) => sum + item.quantity, 0);
        const badge = document.getElementById('cartBadge');
        if (badge) {
            badge.innerText = totalItems;
            badge.style.display = totalItems > 0 ? 'inline-flex' : 'none';
        }
    },

    /**
     * Оформление заказа – возвращает Promise с результатом.
     * При ошибке показывает детальные сообщения от сервера и не редиректит.
     */
    async checkout(orderData) {
        const cart = this.getCart();
        if (cart.length === 0) {
            alert("Корзина пуста");
            return { success: false };
        }

        const orderItems = cart.map(item => ({
            productId: item.id,
            productTitle: item.title,
            quantity: item.quantity,
            unit: item.unit
        }));

        const orderDto = {
            userId: parseInt(orderData.userId),
            legalName: orderData.legalName,
            legalForm: orderData.legalForm,
            notes: orderData.notes || "",
            deliveryAddress: orderData.deliveryAddress,
            inn: orderData.inn,
            kpp: orderData.kpp || null,
            orderStatus: "CREATED",
            orderItems: orderItems,
            contactName: orderData.contactName,
            contactPhone: orderData.contactPhone,
            contactEmail: orderData.contactEmail
        };

        try {
            const response = await fetch('/api/v1/orders', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(orderDto)
            });

            if (response.ok) {
                alert("✅ Заказ успешно оформлен! Наш менеджер свяжется с вами.");
                this.clearCart();
                return { success: true };
            } else {
                // Пытаемся распарсить ProblemDetail
                let errorMessage = "Ошибка при оформлении заказа.";
                try {
                    const errorData = await response.json();
                    console.error("ProblemDetail:", errorData);

                    // Если есть поле errors (например, @Valid)
                    if (errorData.errors && typeof errorData.errors === 'object') {
                        // Отображаем ошибки под конкретными полями
                        for (const [field, message] of Object.entries(errorData.errors)) {
                            // Маппинг названий полей из бэка на ID элементов
                            let fieldId = mapBackendFieldToId(field);
                            if (fieldId && typeof window.showFieldError === 'function') {
                                window.showFieldError(fieldId, message);
                            } else {
                                errorMessage += `\n${field}: ${message}`;
                            }
                        }
                    } else if (errorData.detail) {
                        errorMessage = errorData.detail;
                    } else if (errorData.title) {
                        errorMessage = errorData.title;
                    }
                } catch (e) {
                    // Если ответ не JSON
                    errorMessage = `Ошибка сервера: ${response.status}`;
                }
                alert(errorMessage);
                return { success: false, error: errorMessage };
            }
        } catch (e) {
            console.error("Network error:", e);
            alert("Ошибка сети. Проверьте соединение.");
            return { success: false, error: e.message };
        }
    }
};

// Вспомогательная функция для маппинга полей ошибок из бэка на ID полей в форме
function mapBackendFieldToId(backendField) {
    const mapping = {
        'legalName': 'legalName',
        'inn': 'inn',
        'kpp': 'kpp',
        'contactName': 'contactName',
        'contactPhone': 'phone',
        'contactEmail': 'email',
        'deliveryAddress': 'address'
    };
    return mapping[backendField] || null;
}

// Делаем функции доступными глобально для вызова из HTML
window.CartService = CartService;
window.mapBackendFieldToId = mapBackendFieldToId;