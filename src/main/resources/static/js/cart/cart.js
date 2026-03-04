const CART_KEY = 'stroy1click_cart';

const CartService = {

    getCart() {
        const cart = localStorage.getItem(CART_KEY);
        return cart ? JSON.parse(cart) : [];
    },

    addToCart(product, quantity = 1) {
        const cart = this.getCart();
        const existingItem = cart.find(item => item.id === product.id);

        if (existingItem) {
            existingItem.quantity += quantity;
        } else {
            cart.push({
                id: product.id,
                title: product.title,
                unit: product.unit,
                image: product.image,
                price: product.price,
                quantity: quantity
            });
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
            renderCartPage();
        }

        this.updateCartBadge();
    },

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
                renderCartPage();
            }
        }
    },

    clearCart() {
        localStorage.removeItem(CART_KEY);
        this.updateCartBadge();
    },

    updateCartBadge() {
        const cart = this.getCart();
        const totalItems = cart.reduce((sum, item) => sum + item.quantity, 0);
        const badge = document.querySelector('.cart-badge');

        if (badge) {
            badge.innerText = totalItems;
            badge.style.display = totalItems > 0 ? 'inline-block' : 'none';
        }
    },

    // 🔥 ГЛАВНЫЙ МЕТОД ОФОРМЛЕНИЯ
    async checkout(orderData) {

        const cart = this.getCart();

        if (cart.length === 0) {
            alert("Корзина пуста");
            return;
        }

        const orderItems = cart.map(item => ({
            productId: item.id, // В DTO это Integer, отправляем число
            productTitle: item.title,
            quantity: item.quantity,
            unit: item.unit
            // Поле price мы здесь намеренно не передаем,
            // бэкенд должен рассчитать его сам на основе productId
        }));

        // Маппинг формы
        const legalFormMap = {
            "ООО": "LLC",
            "ИП": "IE",
            "LLC": "LLC",
            "IE": "IE"
        };

        const normalizedLegalForm =
            legalFormMap[orderData.legalForm?.trim().toUpperCase()] || "LLC";

        const orderDto = {
            userId: parseInt(orderData.userId),
            legalName: orderData.legalName,
            legalForm: normalizedLegalForm,
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

        console.log("ORDER DTO:", orderDto);

        try {
            const response = await fetch('/api/v1/orders', {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify(orderDto)
            });

            if (response.ok) {
                alert("Заказ успешно оформлен! Наш менеджер свяжется с вами.");
                this.clearCart();
                window.location.href = '/';
            } else {
                const errorData = await response.json();
                console.error("Ошибка заказа:", errorData);
                alert("Ошибка: " + (errorData.message || "Проверьте заполнение формы"));
            }
        } catch (e) {
            console.error(e);
            alert("Ошибка сети");
        }
    }
};

document.addEventListener('DOMContentLoaded', () => {
    CartService.updateCartBadge();
});