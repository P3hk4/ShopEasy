import { addToCart } from './cart.js';

document.addEventListener('DOMContentLoaded', function() {
    // Функция для получения параметра из URL
    function getUrlParameter(name) {
        name = name.replace(/[\[\]]/g, '\\$&');
        const regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)');
        const results = regex.exec(window.location.href);
        if (!results) return null;
        if (!results[2]) return '';
        return decodeURIComponent(results[2].replace(/\+/g, ' '));
    }

    // Получаем ID товара из URL
    const productId = getUrlParameter('id');

    if (!productId) {
        console.error('ID товара не указан в URL');
        showErrorMessage('Товар не найден. Пожалуйста, вернитесь в магазин.');
        return;
    }

    // Загружаем данные товара
    fetch(`/api/products/id/${productId}`)
        .then(response => {
            if (!response.ok) {
                throw new Error(`Ошибка HTTP: ${response.status}`);
            }
            return response.json();
        })
        .then(product => {
            // Заполняем данные на странице
            updateProductDetails(product);

            // Добавляем обработчики для кнопок после загрузки данных товара
            setupCartButtons(product);
        })
        .catch(error => {
            console.error('Ошибка при загрузке товара:', error);
            showErrorMessage('Не удалось загрузить информацию о товаре. Пожалуйста, попробуйте позже.');
        });

    // Функция для обновления данных товара на странице
    async function updateProductDetails(product) {
        console.log(product);
        // Основная информация
        document.querySelector('.h2').textContent = product.name || 'Название товара';
        document.querySelector('.h3.py-2').textContent = `₽${product.price?.toFixed(2) || '0.00'}`;

        // Бренд
        const brandElement = document.querySelector('.text-muted strong');
        if (brandElement) {
            brandElement.textContent = await getBrandNameById(product.manufacturerId);
        }

        // Описание
        const descriptionElement = document.querySelector('.card-body p:nth-of-type(2)');
        if (descriptionElement) {
            descriptionElement.textContent = product.description ||
                'Описание товара отсутствует. Lorem ipsum dolor sit amet, consectetur adipiscing elit.';
        }

        // Характеристики
        const specsList = document.querySelector('.list-unstyled.pb-3');
        if (specsList && product.specifications) {
            specsList.innerHTML = product.specifications
                .map(spec => `<li>${spec}</li>`)
                .join('');
        }

        // Изображения (основное и галерея)
        updateProductImages(product);
    }

    async function getBrandNameById(manufacturerId) {
        try {
            // Отправляем запрос к API
            const response = await fetch(`/api/manufacturers/id/${manufacturerId}`);

            // Проверяем статус ответа
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            // Парсим JSON ответ
            const manufacturer = await response.json();

            // Возвращаем название производителя
            return manufacturer.name || 'Неизвестный производитель';

        } catch (error) {
            console.error('Ошибка при получении производителя:', error);
            // Возвращаем значение по умолчанию в случае ошибки
            return 'Неизвестный производитель';
        }
    }

    function formatCategoryId(id) {
        return id.toString().padStart(3, '0'); // Преобразует 1 в "001"
    }

    // Обновление изображений товара
    function updateProductImages(product) {
        const mainImage = document.getElementById('product-detail');
        if (mainImage) {
            mainImage.src = `/assets/img/product_category_${String(formatCategoryId(product.categoryId))}.jpg`;
            mainImage.alt = product.name || 'Изображение товара';
            // Добавляем fallback на случай ошибки загрузки изображения
            mainImage.onerror = function() {
                this.src = '/assets/img/product_placeholder.jpg';
            };
        }
    }

    // Показать сообщение об ошибке
    function showErrorMessage(message) {
        const productDetailSection = document.querySelector('.card-body');
        if (productDetailSection) {
            productDetailSection.innerHTML = `
                <div class="alert alert-danger">
                    ${message}
                    <a href="/shop" class="alert-link">Вернуться в магазин</a>
                </div>
            `;
        }
    }

    function setupCartButtons(product) {
        // Кнопка "Add To Cart"
        const addToCartBtn = document.querySelector('button[name="submit"][value="addtocard"]');
        if (addToCartBtn) {
            addToCartBtn.addEventListener('click', function(e) {
                e.preventDefault();
                addToCart(product);
            });
        }

        // Кнопка "Buy"
        const buyBtn = document.querySelector('button[name="submit"][value="buy"]');
        if (buyBtn) {
            buyBtn.addEventListener('click', function(e) {
                e.preventDefault();
                addToCart(product);
                window.location.href = '/basket'; // Перенаправляем на страницу корзины
            });
        }
    }
});