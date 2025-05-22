class Cart {
    constructor() {
        this.items = JSON.parse(localStorage.getItem('cart')) || [];
        this.updateCartCount();
        this.renderCart();
        this.setupClearButton(); // Добавляем обработчик очистки
        this.loadShippingAddresses(); // Загружаем адреса при инициализации
        this.setupShippingModal(); // Настраиваем обработчики модального окна
        this.setupCheckoutButton();
    }
    // Добавляем метод updateCartCount
    updateCartCount() {
        const count = this.getTotalItems();
        const cartCountElements = document.querySelectorAll('.cart-count');

        cartCountElements.forEach(element => {
            element.textContent = count;
            element.style.display = count > 0 ? 'inline-block' : 'none';
        });
    }

    // Метод для получения общего количества товаров
    getTotalItems() {
        return this.items.reduce((total, item) => total + item.quantity, 0);
    }

    // Остальные методы (addItem, removeItem и т.д.)
    addItem(product) {
        // 1. Проверка входных данных
        if (!product || typeof product !== 'object') {
            console.error('Некорректный товар:', product);
            return;
        }

        // 2. Определяем ID товара (унифицированно)
        const productId = product.id || product.productId;
        if (!productId) {
            console.error('Товар не содержит идентификатора:', product);
            return;
        }

        // 3. Поиск существующего товара
        const existingItem = this.items.find(item => item.id === productId);

        // 4. Обновление или добавление
        if (existingItem) {
            existingItem.quantity += 1;
        } else {
            this.items.push({
                id: productId, // Гарантируем наличие id
                name: product.name || 'Без названия',
                price: product.price || 0,
                categoryId: product.categoryId || null,
                image: product.image || '/assets/img/default-product.jpg',
                quantity: 1
            });
        }

        // 5. Сохранение с обработкой ошибок
        try {
            this.save();
        } catch (error) {
            console.error('Ошибка сохранения корзины:', error);
        }
    }

    clear() {
        this.items = [];
        this.save();
        this.renderCart(); // Обновляем отображение
    }

    setupClearButton() {
        const clearButton = document.querySelector('.clear-cart');
        if (clearButton) {
            clearButton.addEventListener('click', () => {
                this.clear();
                alert('Корзина очищена!');
            });
        }
    }

    save() {
        localStorage.setItem('cart', JSON.stringify(this.items));
        this.updateCartCount(); // Обновляем счётчик после сохранения
        this.renderCart(); // Добавляем перерисовку при сохранении
    }

    renderCart() {
        const container = document.querySelector('.cart-items-container');
        if (!container) return;

        if (this.items.length === 0) {
            container.innerHTML = `
            <tr>
                <td colspan="5" class="text-center py-4">
                    <i class="fas fa-shopping-cart fa-2x mb-3"></i>
                    <p>Ваша корзина пуста</p>
                </td>
            </tr>
        `;
        } else {
            container.innerHTML = this.items.map(item => `
            <tr class="cart-item" data-id="${item.id}">
                <td>
                    <div class="d-flex align-items-center">
                        <img src="/assets/img/product_category_${item.categoryId?.toString().padStart(3, '0')}.jpg"
                             class="img-thumbnail me-3" 
                             style="width: 60px; height: 60px; object-fit: cover" 
                             alt="${item.name}">
                        <div>
                            <h6 class="mb-1">${item.name}</h6>
                            <small class="text-muted">Арт. ${item.id}</small>
                        </div>
                    </div>
                </td>
                <td class="align-middle text-center">${item.price} ₽</td>
                <td class="align-middle text-center">
                    <div class="d-flex justify-content-center align-items-center">
                        <button class="btn btn-sm btn-outline-secondary btn-minus">
                            <i class="fas fa-minus"></i>
                        </button>
                        <span class="mx-2 quantity-display">${item.quantity}</span>
                        <button class="btn btn-sm btn-outline-secondary btn-plus">
                            <i class="fas fa-plus"></i>
                        </button>
                    </div>
                </td>
                <td class="align-middle text-center">${item.price * item.quantity} ₽</td>
                <td class="align-middle text-center">
                    <button class="btn btn-sm btn-outline-danger remove-item">
                        <i class="fas fa-trash"></i>
                    </button>
                </td>
            </tr>
        `).join('');
        }
        this.updateTotal();
        this.addEventHandlers();
    }

    addEventHandlers() {
        // Общая функция для получения ID товара
        const getItemId = (element) => {
            const cartItem = element.closest('.cart-item');
            if (!cartItem) {
                console.warn('Элемент корзины не найден');
                return null;
            }

            const itemId = cartItem.dataset?.id;
            if (!itemId) {
                console.warn('Атрибут data-id не найден', cartItem);
                return null;
            }

            return itemId;
        };

        // Обработчик для кнопок +/-
        const handleQuantityButton = (e, operation) => {
            const itemId = getItemId(e.target);
            if (!itemId) return;

            const item = this.items.find(i => i?.id?.toString() === itemId.toString());
            if (!item) {
                console.warn(`Товар ${itemId} не найден`);
                return;
            }

            if (operation === 'plus') {
                item.quantity++;
            } else {
                item.quantity = item.quantity > 1 ? item.quantity - 1 : 1;
            }

            this.save();
        };

        // Обработчик поля ввода
        const handleQuantityInput = (e) => {
            const itemId = getItemId(e.target);
            if (!itemId) return;

            const item = this.items.find(i => i?.id?.toString() === itemId.toString());
            if (!item) return;

            const newValue = Math.max(1, parseInt(e.target.value) || 1);
            item.quantity = newValue;
            this.save();
        };

        // Обработчик удаления
        const handleRemoveItem = (e) => {
            const itemId = getItemId(e.target);
            if (!itemId) return;

            if (confirm('Удалить товар из корзины?')) {
                this.items = this.items.filter(i => i?.id?.toString() !== itemId.toString());
                this.save();
            }
        };

        // Назначение обработчиков
        document.querySelectorAll('.btn-plus').forEach(btn => {
            btn.addEventListener('click', (e) => handleQuantityButton(e, 'plus'));
        });

        document.querySelectorAll('.btn-minus').forEach(btn => {
            btn.addEventListener('click', (e) => handleQuantityButton(e, 'minus'));
        });

        document.querySelectorAll('.quantity-input').forEach(input => {
            input.addEventListener('change', handleQuantityInput);
        });

        document.querySelectorAll('.remove-item').forEach(btn => {
            btn.addEventListener('click', handleRemoveItem);
        });
    }

    updateTotal() {
        const subtotal = this.items.reduce((sum, item) => sum + (item.price * item.quantity), 0);
        const totalItems = this.items.reduce((sum, item) => sum + item.quantity, 0);

        // Обновляем итоговую сумму
        document.querySelectorAll('.cart-total').forEach(el => {
            el.textContent = `${subtotal.toFixed(2)} ₽`;
        });

        // Обновляем счетчик в шапке
        document.querySelectorAll('.cart-count').forEach(el => {
            el.textContent = totalItems;
            el.style.display = totalItems > 0 ? 'inline-block' : 'none';
        });
    }

    async loadShippingAddresses() {
        const token = localStorage.getItem('accessToken');

        try {
            const response = await fetch('/api/shippings/my', {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                }
            });

            if (response.ok) {
                const addresses = await response.json();
                this.renderShippingAddresses(addresses);
            } else if (response.status === 401) {
                console.log('Пользователь не авторизован');
            } else {
                console.error('Ошибка загрузки адресов:', response.status);
            }
        } catch (error) {
            console.error('Не удалось загрузить адреса:', error);
        }
    }

    renderShippingAddresses(addresses) {
        const select = document.querySelector('.shipping-address-select');
        if (!select) return;

        // Очищаем существующие варианты, кроме первого
        select.innerHTML = '<option value="" selected disabled>Выберите адрес доставки</option>';

        if (addresses && addresses.length > 0) {
            addresses.forEach(address => {
                const option = document.createElement('option');
                option.value = address.shippingId;
                option.textContent = `${address.city}, ${address.address} (${address.postCode})`;
                select.appendChild(option);
            });

            // Добавляем кнопку для нового адреса
            const addNewOption = document.createElement('option');
            addNewOption.value = 'new';
            addNewOption.textContent = 'Добавить новый адрес...';
            select.appendChild(addNewOption);

            select.addEventListener('change', (e) => {
                if (e.target.value === 'new') {
                    // Показываем модальное окно для добавления нового адреса
                    const modal = new bootstrap.Modal(document.getElementById('addShippingModal'));
                    modal.show();
                    // Сбрасываем выбор
                    select.value = '';
                }
            });
        } else {
            // Сообщение, если адресов нет
            const option = document.createElement('option');
            option.value = 'new';
            option.textContent = 'Добавить адрес доставки...';
            select.appendChild(option);

            select.addEventListener('change', (e) => {
                if (e.target.value === 'new') {
                    // Показываем модальное окно для добавления нового адреса
                    const modal = new bootstrap.Modal(document.getElementById('addShippingModal'));
                    modal.show();
                    // Сбрасываем выбор
                    select.value = '';
                }
            });
        }
    }

    setupShippingModal() {
        const saveBtn = document.getElementById('saveShippingBtn');
        if (saveBtn) {
            saveBtn.addEventListener('click', async () => {
                const postCode = document.getElementById('postCode').value;
                const city = document.getElementById('city').value;
                const address = document.getElementById('address').value;

                if (!postCode || !city || !address) {
                    alert('Заполните все поля');
                    return;
                }

                const token = localStorage.getItem('accessToken');
                if (!token) {
                    alert('Для сохранения адреса необходимо авторизоваться');
                    window.location.href = '/login';
                    return;
                }

                try {
                    const response = await fetch('/api/shippings/my', {
                        method: 'PUT',
                        headers: {
                            'Authorization': `Bearer ${token}`,
                            'Content-Type': 'application/json'
                        },
                        body: JSON.stringify({
                            postCode,
                            city,
                            address
                        })
                    });

                    if (response.ok) {
                        const result = await response.text();
                        alert(result);
                        // Закрываем модальное окно и обновляем список
                        bootstrap.Modal.getInstance(document.getElementById('addShippingModal')).hide();
                        this.loadShippingAddresses();
                        // Очищаем форму
                        document.getElementById('shippingForm').reset();
                    } else {
                        const error = await response.text();
                        alert(error);
                    }
                } catch (error) {
                    console.error('Ошибка сохранения адреса:', error);
                    alert('Ошибка при сохранении адреса');
                }
            });
        }
    }

    async checkout() {
        const token = localStorage.getItem('accessToken');

        if (!token) {
            alert('Для оформления заказа необходимо авторизоваться');
            window.location.href = '/login';
            return;
        }

        const shippingSelect = document.querySelector('.shipping-address-select');
        const shippingId = shippingSelect.value;

        if (!shippingId) {
            alert('Выберите адрес доставки');
            return;
        }

        // Подготавливаем данные для заказа
        const orderData = {
            shippingId: parseInt(shippingId),
            products: this.items.map(item => ({
                productId: item.id,
                quantity: item.quantity
            }))
        };

        try {
            const response = await fetch('/api/orders/my', {
                method: 'POST',
                headers: {
                    'Authorization': `Bearer ${token}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify(orderData)
            });

            if (response.ok) {
                const result = await response.text();
                alert(result);
                // Очищаем корзину после успешного оформления
                this.clear();
                // Перенаправляем на страницу с подтверждением заказа
                window.location.href = '/account';
            } else {
                const error = await response.text();
                alert('Ошибка при оформлении заказа: ' + error);
            }
        } catch (error) {
            console.error('Ошибка при оформлении заказа:', error);
            alert('Произошла ошибка при оформлении заказа. Пожалуйста, попробуйте позже.');
        }
    }

    getTotalCost() {
        return this.items.reduce((sum, item) => sum + (item.price * item.quantity), 0);
    }

    // Добавляем обработчик кнопки оформления заказа
    setupCheckoutButton() {
        const checkoutBtn = document.querySelector('.checkout-btn');
        if (checkoutBtn) {
            checkoutBtn.addEventListener('click', (e) => {
                e.preventDefault();
                this.checkout();
            });
        }
    }

}

// Создаём экземпляр корзины
const cart = new Cart();
// Функция для добавления товара в корзину (используется в shop.js)
function addToCart(product) {
    cart.addItem(product);
    alert(`Товар "${product.name}" добавлен в корзину!`);
}
// Экспортируем для использования в других файлах
export { cart, addToCart };