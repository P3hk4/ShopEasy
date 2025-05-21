// class Account {
//     constructor() {
//         this.loadUserData();
//         this.loadOrders();
//         this.setupEventListeners();
//     }
//
//     async loadUserData() {
//         try {
//             // Здесь должен быть запрос к API для получения данных пользователя
//             // Для примера используем mock данные
//             const mockUserData = {
//                 id: 1,
//                 username: "user123",
//                 email: "user@example.com",
//                 firstName: "Иван",
//                 lastName: "Иванов"
//             };
//
//             this.displayUserData(mockUserData);
//         } catch (error) {
//             console.error("Ошибка загрузки данных пользователя:", error);
//         }
//     }
//
//     displayUserData(userData) {
//         document.getElementById('user-fullname').textContent = `${userData.firstName} ${userData.lastName}`;
//         document.getElementById('user-username').textContent = userData.username;
//         document.getElementById('user-email').textContent = userData.email;
//
//         document.getElementById('user-fullname-details').textContent = `${userData.firstName} ${userData.lastName}`;
//         document.getElementById('user-email-details').textContent = userData.email;
//         document.getElementById('user-username-details').textContent = userData.username;
//
//         // Заполняем поля формы редактирования
//         document.getElementById('edit-firstName').value = userData.firstName;
//         document.getElementById('edit-lastName').value = userData.lastName;
//         document.getElementById('edit-email').value = userData.email;
//     }
//
//     async loadOrders() {
//         try {
//             // Здесь должен быть запрос к API для получения заказов пользователя
//             // Для примера используем mock данные согласно структуре Order
//             const mockOrders = [
//                 {
//                     orderId: 1001,
//                     shippingId: 5001,
//                     clientId: 1,
//                     totalCost: 12500,
//                     trackNumber: "TRACK123456",
//                     date: "2023-05-15"
//                 },
//                 {
//                     orderId: 1002,
//                     shippingId: 5002,
//                     clientId: 1,
//                     totalCost: 8900,
//                     trackNumber: "TRACK789012",
//                     date: "2023-06-20"
//                 }
//             ];
//
//             this.displayOrders(mockOrders);
//         } catch (error) {
//             console.error("Ошибка загрузки заказов:", error);
//         }
//     }
//
//     displayOrders(orders) {
//         const ordersList = document.getElementById('orders-list');
//
//         if (orders.length === 0) {
//             ordersList.innerHTML = `
//                 <tr>
//                     <td colspan="6" class="text-center py-4">У вас пока нет заказов</td>
//                 </tr>
//             `;
//             return;
//         }
//
//         ordersList.innerHTML = orders.map(order => `
//             <tr>
//                 <td>#${order.orderId}</td>
//                 <td>${order.date}</td>
//                 <td>${order.shippingId}</td>
//                 <td>${order.trackNumber || 'Не указан'}</td>
//                 <td>${order.totalCost.toLocaleString()} ₽</td>
//                 <td>
//                     <button class="btn btn-sm btn-outline-success view-order" data-order-id="${order.orderId}">
//                         <i class="fas fa-eye"></i> Подробнее
//                     </button>
//                 </td>
//             </tr>
//         `).join('');
//     }
//
//     setupEventListeners() {
//         // Сохранение изменений профиля
//         document.getElementById('save-profile-changes').addEventListener('click', () => {
//             this.saveProfileChanges();
//         });
//
//         // Просмотр деталей заказа (пример)
//         document.addEventListener('click', (e) => {
//             if (e.target.closest('.view-order')) {
//                 const orderId = e.target.closest('.view-order').dataset.orderId;
//                 this.viewOrderDetails(orderId);
//             }
//         });
//     }
//
//     async saveProfileChanges() {
//         const firstName = document.getElementById('edit-firstName').value;
//         const lastName = document.getElementById('edit-lastName').value;
//         const email = document.getElementById('edit-email').value;
//         const password = document.getElementById('edit-password').value;
//         const confirmPassword = document.getElementById('edit-confirm-password').value;
//
//         if (password && password !== confirmPassword) {
//             alert("Пароли не совпадают!");
//             return;
//         }
//
//         try {
//             // Здесь должен быть запрос к API для сохранения изменений
//             console.log("Сохранение изменений профиля:", { firstName, lastName, email, password });
//
//             // Обновляем данные на странице без перезагрузки
//             const userData = {
//                 username: document.getElementById('user-username').textContent,
//                 email,
//                 firstName,
//                 lastName
//             };
//
//             this.displayUserData(userData);
//
//             // Закрываем модальное окно
//             const modal = bootstrap.Modal.getInstance(document.getElementById('editProfileModal'));
//             modal.hide();
//
//             alert("Изменения сохранены успешно!");
//         } catch (error) {
//             console.error("Ошибка сохранения профиля:", error);
//             alert("Произошла ошибка при сохранении изменений");
//         }
//     }
//
//     viewOrderDetails(orderId) {
//         // Здесь должна быть логика отображения деталей заказа
//         // Можно открыть модальное окно или перейти на отдельную страницу
//         alert(`Просмотр деталей заказа #${orderId}`);
//     }
// }
//
// // Инициализация при загрузке страницы
// document.addEventListener('DOMContentLoaded', () => {
//     const account = new Account();
// });


class Account {
    constructor() {
        this.accessTonken = localStorage.getItem('accessToken');
        this.userId = localStorage.getItem('userId');

        if (!this.accessTonken || !this.userId) {
            window.location.href = '/login';
            return;
        }

        this.loadUserData();
        this.loadOrders();
        //this.setupEventListeners();
    }

    async loadUserData() {
        try {
            const response = await fetch(`/api/clients/my`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${this.accessTonken}`,
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
                    'Authorization': `Bearer ${this.accessTonken}`,
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
        alert(orders);
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
            const response = await fetch(`/api/users/${this.userId}`, {
                method: 'PUT',
                headers: {
                    'Authorization': `Bearer ${this.accessTonken}`,
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({
                    firstName,
                    lastName,
                    email,
                    password: password || undefined
                })
            });

            if (response.ok) {
                const userData = await response.json();
                this.displayUserData(userData);

                // Закрываем модальное окно
                const modal = bootstrap.Modal.getInstance(document.getElementById('editProfileModal'));
                modal.hide();

                alert("Изменения сохранены успешно!");
            } else {
                throw new Error('Ошибка сохранения профиля');
            }
        } catch (error) {
            console.error("Ошибка сохранения профиля:", error);
            alert("Произошла ошибка при сохранении изменений");
        }
    }

    async viewOrderDetails(orderId) {
        try {
            const response = await fetch(`/api/orders/${orderId}`, {
                method: 'GET',
                headers: {
                    'Authorization': `Bearer ${this.accessTonken}`,
                    'Content-Type': 'application/json'
                }
            });

            if (response.ok) {
                const orderDetails = await response.json();
                // Здесь можно открыть модальное окно с деталями заказа
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
        // Реализация отображения модального окна с деталями заказа
        console.log("Детали заказа:", orderDetails);
        alert(`Детали заказа #${orderDetails.orderId}\nСумма: ${orderDetails.totalCost} ₽`);
    }
}

// Инициализация при загрузке страницы
document.addEventListener('DOMContentLoaded', () => {
    const account = new Account();
});