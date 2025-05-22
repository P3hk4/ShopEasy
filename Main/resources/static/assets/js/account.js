
class Account {
    constructor() {
        this.accessToken = localStorage.getItem('accessToken');
        this.userId = localStorage.getItem('userId');

        if (!this.accessToken || !this.userId) {
            window.location.href = '/login';
            return;
        }

        this.loadUserData();
        this.loadOrders();
        this.loadShippingAddresses();
        this.setupEventListeners();
        this.setupShippingModal();
        this.setupLogoutButton();
    }

    async loadUserData() {
        try {
            const response = await fetch(`/api/clients/my`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${this.accessToken}`,
                    'Content-Type': 'application/json'
                }
            });

            if (response.ok) {
                const userData = await response.json();
                this.displayUserData(userData);
            } else if (response.status === 401) {
                // Если токен недействителен, перенаправляем на страницу входа
                window.location.href = '/login';
            } else {
                throw new Error('Ошибка загрузки данных пользователя');
            }
        } catch (error) {
            console.error("Ошибка загрузки данных пользователя:", error);
            window.location.href = '/login';
        }
    }

    displayUserData(userData) {
        document.getElementById('user-fullname').textContent = `${userData.firstName} ${userData.lastName}`;
        document.getElementById('user-username').textContent = userData.username;
        document.getElementById('user-email').textContent = userData.email;

        document.getElementById('user-fullname-details').textContent = `${userData.firstName} ${userData.lastName}`;
        document.getElementById('user-email-details').textContent = userData.email;
        document.getElementById('user-username-details').textContent = userData.username;

        // Заполняем поля формы редактирования
        document.getElementById('edit-firstName').value = userData.firstName;
        document.getElementById('edit-lastName').value = userData.lastName;
        document.getElementById('edit-email').value = userData.email;
    }

    async loadOrders() {
        try {
            const response = await fetch(`/api/orders/my`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${this.accessToken}`,
                    'Content-Type': 'application/json'
                }
            });

            if (response.ok) {
                const orders = await response.json();
                this.displayOrders(orders);
            } else {
                throw new Error('Ошибка загрузки заказов');
            }
        } catch (error) {
            console.error("Ошибка загрузки заказов:", error);
        }
    }

    displayOrders(orders) {
        const ordersList = document.getElementById('orders-list');
        if (!orders || orders.length === 0) {
            ordersList.innerHTML = `
                <tr>
                    <td colspan="6" class="text-center py-4">У вас пока нет заказов</td>
                </tr>
            `;
            return;
        }

        ordersList.innerHTML = orders.map(order => `
            <tr>
                <td>#${order.orderId}</td>
                <td>${new Date(order.date).toLocaleDateString()}</td>
                <td>${order.shippingId}</td>
                <td>${order.trackNumber || 'Не указан'}</td>
                <td>${order.totalCost.toLocaleString()} ₽</td>
                <td>
                    <button class="btn btn-sm btn-outline-success view-order" data-order-id="${order.orderId}">
                        <i class="fas fa-eye"></i> Подробнее
                    </button>
                </td>
            </tr>
        `).join('');
    }

    setupEventListeners() {
        // Сохранение изменений профиля
        document.getElementById('save-profile-changes').addEventListener('click', () => {
            this.saveProfileChanges();
        });

        // Просмотр деталей заказа
        document.addEventListener('click', (e) => {
            if (e.target.closest('.view-order')) {
                const orderId = e.target.closest('.view-order').dataset.orderId;
                this.viewOrderDetails(orderId);
            }
        });
    }

    async saveProfileChanges() {
        const firstName = document.getElementById('edit-firstName').value;
        const lastName = document.getElementById('edit-lastName').value;
        const email = document.getElementById('edit-email').value;
        const password = document.getElementById('edit-password').value;
        const confirmPassword = document.getElementById('edit-confirm-password').value;

        if (password && password !== confirmPassword) {
            alert("Пароли не совпадают!");
            return;
        }

        try {
            const response = await fetch(`/api/clients/my`, {
                method: 'PUT',
                headers: {
                    'Authorization': `Bearer ${this.accessToken}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    firstName,
                    lastName,
                    email,
                    ...(password && { password }) // добавляем password, только если он передан
                })
            });

            if (response.ok) {
                // Ожидаем текстовый ответ, а не JSON
                const successMessage = await response.text(); // вместо response.json()

                // Если API всё же возвращает JSON в некоторых случаях, можно сделать проверку:
                // const data = await response.text();
                // const userData = data.startsWith('{') ? JSON.parse(data) : null;
                // if (userData) this.displayUserData(userData);

                // Закрываем модальное окно
                const modal = bootstrap.Modal.getInstance(document.getElementById('editProfileModal'));
                modal.hide();
                alert(successMessage || "Изменения сохранены успешно!");

                window.location.reload(); // ← Перезагрузка страницы

            } else {
                // Если API возвращает текст ошибки (например, "Пароль слишком короткий")
                const errorText = await response.text();
                throw new Error(errorText || 'Ошибка сохранения профиля');
            }
        } catch (error) {
            console.error("Ошибка сохранения профиля:", error);
            alert(error.message || "Произошла ошибка при сохранении изменений");
        }
    }

    async viewOrderDetails(orderId) {
        try {
            const response = await fetch(`/api/orders/id/${orderId}`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${this.accessToken}`,
                    'Content-Type': 'application/json'
                }
            });

            if (response.ok) {
                const orderDetails = await response.json();
                // Показываем модальное окно с деталями заказа
                this.showOrderDetailsModal(orderDetails);
            } else {
                throw new Error('Ошибка загрузки деталей заказа');
            }
        } catch (error) {
            console.error("Ошибка загрузки деталей заказа:", error);
            alert("Произошла ошибка при загрузке деталей заказа");
        }
    }

    showOrderDetailsModal(orderDetails) {
        // Устанавливаем номер заказа
        document.getElementById('order-details-id').textContent = orderDetails.orderId;

        // Очищаем список товаров
        const productsList = document.getElementById('order-products-list');
        productsList.innerHTML = '';

        // Добавляем товары в список
        let totalSum = 0;

        if (orderDetails.orderProducts && orderDetails.orderProducts.length > 0) {
            // Создаем массив промисов для загрузки информации о товарах
            const productPromises = orderDetails.orderProducts.map(item => {
                return fetch(`/api/products/id/${item.productId}`)
                    .then(response => {
                        if (!response.ok) {
                            throw new Error('Ошибка загрузки товара');
                        }
                        return response.json();
                    })
                    .then(product => {
                        return {
                            productId: item.productId,
                            count: item.count,
                            productData: product
                        };
                    })
                    .catch(error => {
                        console.error(`Ошибка загрузки товара ${item.productId}:`, error);
                        return {
                            productId: item.productId,
                            count: item.count,
                            productData: {
                                name: `Товар #${item.productId}`,
                                price: 0,
                                categoryId: null
                            }
                        };
                    });
            });

            // Ожидаем загрузку всех товаров
            Promise.all(productPromises)
                .then(products => {
                    products.forEach(item => {
                        const product = item.productData;
                        const sum = product.price * item.count;
                        totalSum += sum;

                        const row = document.createElement('tr');
                        row.innerHTML = `
                        <td>
                            <div class="d-flex align-items-center">
                                <img src="/assets/img/product_category_${product.categoryId?.toString().padStart(3, '0')}.jpg"
                                     class="img-thumbnail me-3" 
                                     style="width: 60px; height: 60px; object-fit: cover" 
                                     alt="${product.name}">
                                <div>
                                    <h6 class="mb-1">${product.name}</h6>
                                    <small class="text-muted">Арт. ${item.productId}</small>
                                </div>
                            </div>
                        </td>
                        <td>${item.count}</td>
                        <td>${product.price.toLocaleString()} ₽</td>
                        <td>${sum.toLocaleString()} ₽</td>
                    `;
                        productsList.appendChild(row);
                    });

                    // Устанавливаем общую сумму
                    document.getElementById('order-total-sum').textContent = `${totalSum.toLocaleString()} ₽`;
                })
                .catch(error => {
                    console.error("Ошибка загрузки товаров:", error);
                    productsList.innerHTML = `
                    <tr>
                        <td colspan="4" class="text-center py-4">Произошла ошибка при загрузке товаров</td>
                    </tr>
                `;
                });
        } else {
            productsList.innerHTML = `
            <tr>
                <td colspan="4" class="text-center py-4">Нет информации о товарах</td>
            </tr>
        `;
        }

        // Показываем модальное окно
        const modal = new bootstrap.Modal(document.getElementById('orderDetailsModal'));
        modal.show();
    }

    async loadShippingAddresses() {
        try {
            const response = await fetch('/api/shippings/my', {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${this.accessToken}`,
                    'Content-Type': 'application/json'
                }
            });

            if (response.ok) {
                const addresses = await response.json();
                this.displayShippingAddresses(addresses);
            } else {
                throw new Error('Ошибка загрузки адресов доставки');
            }
        } catch (error) {
            console.error("Ошибка загрузки адресов доставки:", error);
        }
    }

    displayShippingAddresses(addresses) {
        const container = document.getElementById('shipping-addresses-list');
        if (!addresses || addresses.length === 0) {
            container.innerHTML = `
                <tr>
                    <td colspan="5" class="text-center py-4">У вас пока нет сохранённых адресов</td>
                </tr>
            `;
            return;
        }

        container.innerHTML = addresses.map(address => `
            <tr>
                <td>${address.shippingId}</td>
                <td>${address.city}</td>
                <td>${address.address}</td>
                <td>${address.postCode}</td>
                <td>
                    <button class="btn btn-sm btn-outline-danger delete-shipping" data-shipping-id="${address.shippingId}">
                        <i class="fas fa-trash"></i> Удалить
                    </button>
                </td>
            </tr>
        `).join('');
    }

    setupShippingModal() {
        // Обработчик сохранения нового адреса
        document.getElementById('save-shipping-address')?.addEventListener('click', async () => {
            const city = document.getElementById('shipping-city').value;
            const address = document.getElementById('shipping-address').value;
            const postCode = document.getElementById('shipping-postCode').value;

            if (!city || !address || !postCode) {
                alert('Заполните все поля');
                return;
            }

            try {
                const response = await fetch('/api/shippings/my', {
                    method: 'PUT',
                    headers: {
                        'Authorization': `Bearer ${this.accessToken}`,
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({
                        city,
                        address,
                        postCode
                    })
                });

                if (response.ok) {
                    const result = await response.text();
                    alert(result);
                    // Закрываем модальное окно
                    const modal = bootstrap.Modal.getInstance(document.getElementById('addShippingModal'));
                    modal.hide();
                    // Очищаем форму
                    document.getElementById('shipping-form').reset();
                    // Обновляем список адресов
                    this.loadShippingAddresses();
                } else {
                    const error = await response.text();
                    throw new Error(error || 'Ошибка сохранения адреса');
                }
            } catch (error) {
                console.error("Ошибка сохранения адреса:", error);
                alert(error.message || "Произошла ошибка при сохранении адреса");
            }
        });

        // Обработчик удаления адреса
        document.addEventListener('click', async (e) => {
            if (e.target.closest('.delete-shipping')) {
                const shippingId = e.target.closest('.delete-shipping').dataset.shippingId;
                if (confirm('Вы уверены, что хотите удалить этот адрес?')) {
                    try {
                        const response = await fetch(`/api/shippings/my/${shippingId}`, {
                            method: 'DELETE',
                            headers: {
                                'Authorization': `Bearer ${this.accessToken}`,
                                'Content-Type': 'application/json'
                            }
                        });

                        if (response.ok) {
                            const result = await response.text();
                            alert(result);
                            // Обновляем список адресов
                            this.loadShippingAddresses();
                        } else {
                            const error = await response.text();
                            throw new Error(error || 'Ошибка удаления адреса');
                        }
                    } catch (error) {
                        console.error("Ошибка удаления адреса:", error);
                        alert(error.message || "Произошла ошибка при удалении адреса");
                    }
                }
            }
        });
    }

    setupLogoutButton() {
        document.getElementById('logout-btn')?.addEventListener('click', () => {
            if (confirm('Вы уверены, что хотите выйти из аккаунта?')) {
                this.logout();
            }
        });
    }

    logout() {
        // Очищаем данные авторизации
        localStorage.removeItem('accessToken');
        localStorage.removeItem('userId');

        // Перенаправляем на страницу входа
        window.location.href = '/login';
    }
}

// Инициализация при загрузке страницы
document.addEventListener('DOMContentLoaded', () => {
    const account = new Account();
});