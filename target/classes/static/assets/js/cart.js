class Cart {
    constructor() {
        this.items = JSON.parse(localStorage.getItem('cart')) || [];
        this.updateCartCount();
        this.renderCart();
        this.setupClearButton(); // Добавляем обработчик очистки
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