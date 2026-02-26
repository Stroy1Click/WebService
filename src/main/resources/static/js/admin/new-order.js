/**
 * Создает DOM-элемент карточки заказа на основе нового OrderDto
 */
function createOrderCard(order) {
    const card = document.createElement('div');
    card.className = 'order-card new-order-pulse'; // Добавим класс анимации для нового заказа

    const date = order.createdAt
        ? new Date(order.createdAt).toLocaleString('ru-RU', { day: '2-digit', month: 'long', hour: '2-digit', minute: '2-digit' })
        : 'Только что';

    // Маппинг статусов (можно расширить)
    const statusText = order.orderStatus || 'NEW';
    const statusClass = `status-${statusText.toLowerCase()}`;

    // Состав заказа
    const itemsHtml = order.orderItems.map(item => `
        <div class="item-row">
            <span>Товар ID: <b>${item.productId}</b></span>
            <span>x ${item.quantity} шт.</span>
        </div>
    `).join('');

    // Заметки
    const notesHtml = order.notes
        ? `<div class="order-notes" style="background: rgba(52, 152, 219, 0.1); border-left: 3px solid #3498db; padding: 8px; margin-top: 10px; border-radius: 4px;">
            <small style="color: #3498db; display: block; margin-bottom: 2px;">Комментарий:</small>
            <span>💬 ${order.notes}</span>
          </div>`
        : '';

    // Формируем ИНН/КПП строку
    const nalogInfo = order.kpp
        ? `ИНН ${order.inn} / КПП ${order.kpp}`
        : `ИНН ${order.inn}`;

    card.innerHTML = `
        <div class="order-header">
            <div class="order-id">Заказ #${order.id}</div>
            <div class="order-status ${statusClass}">${statusText}</div>
        </div>

        <div class="order-info-grid">
            <div class="info-item" style="grid-column: span 2; border-bottom: 1px solid rgba(255,255,255,0.05); padding-bottom: 8px;">
                <label>Организация</label>
                <div style="font-weight: bold; color: #fff;">${order.legalForm} "${order.legalName}"</div>
                <small style="color: #888;">${nalogInfo}</small>
            </div>

            <div class="info-item">
                <label>Контактное лицо</label>
                <span>${order.contactName}</span>
            </div>

            <div class="info-item">
                <label>Телефон</label>
                <span>${formatPhone(order.contactPhone)}</span>
            </div>

            <div class="info-item" style="grid-column: span 2;">
                <label>Адрес доставки</label>
                <span style="color: #ecf0f1;">📍 ${order.deliveryAddress}</span>
            </div>
            
            <div class="info-item">
                <label>Дата создания</label>
                <span>${date}</span>
            </div>
            
            <div class="info-item">
                <label>Email</label>
                <span>${order.contactEmail}</span>
            </div>
        </div>

        <div class="info-item" style="margin-top: 10px;">
            <label>Состав заказа:</label>
            <div class="order-items-list">
                ${itemsHtml}
            </div>
        </div>

        ${notesHtml}

        <div style="margin-top: 15px; text-align: right; border-top: 1px solid rgba(255,255,255,0.1); padding-top: 10px;">
             <a href="/admin/orders/${order.id}" style="color: #3498db; text-decoration: none; font-size: 14px; font-weight: bold;">
                Открыть управление →
             </a>
        </div>
    `;

    return card;
}

function formatPhone(phone) {
    if(!phone) return "Не указан";
    // Учитываем формат +7 или 8
    const cleaned = ('' + phone).replace(/\D/g, '');
    const match = cleaned.match(/^(7|8|)?(\d{3})(\d{3})(\d{2})(\d{2})$/);
    if (match) {
        return `+7 (${match[2]}) ${match[3]}-${match[4]}-${match[5]}`;
    }
    return phone;
}