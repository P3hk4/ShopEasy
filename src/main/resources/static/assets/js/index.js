document.addEventListener('DOMContentLoaded', function() {
    // Функция для загрузки товаров
    function loadRecommendedProducts() {
        const recommendedProductsSection = document.querySelector('.bg-light .row:last-child');

        // Очищаем существующие товары (оставляем только первый ряд с заголовком)
        const rows = recommendedProductsSection.querySelectorAll('.row');
        if (rows.length > 1) {
            for (let i = 1; i < rows.length; i++) {
                rows[i].remove();
            }
        }

        // Создаем новый контейнер для товаров
        const productsContainer = document.createElement('div');
        productsContainer.className = 'row';
        recommendedProductsSection.appendChild(productsContainer);

        // Отправляем AJAX запрос к API
        fetch('/api/products/42') // Запрашиваем первую страницу товаров
            .then(response => {
                if (!response.ok) {
                    throw new Error('Network response was not ok');
                }
                return response.json();
            })
            .then(products => {
                // Берем только первые 3 товара
                const firstThreeProducts = products.slice(0, 3);

                firstThreeProducts.forEach(product => {
                    const productCol = document.createElement('div');
                    productCol.className = 'col-12 col-md-4 mb-4';
                    productCol.innerHTML = `
                        <div class="card h-100">
                            <a href="shop-single.html">
                                <img src="/assets/img/product_category_${String(formatCategoryId(product.categoryId))}.jpg" class="card-img-top" alt="${product.name}">
                            </a>
                            <div class="card-body">
                                <ul class="list-unstyled d-flex justify-content-between">
                                    <li class="text-muted text-right">₽${product.price.toFixed(2)}</li>
                                </ul>
                                <a href="shop-single.html" class="h2 text-decoration-none text-dark">${product.name}</a>
                                <p class="card-text">${product.description || ''}</p>
                            </div>
                        </div>
                    `;

                    productsContainer.appendChild(productCol);
                });
            })
            .catch(error => {
                console.error('Error loading recommended products:', error);
                // Можно добавить fallback контент или сообщение об ошибке
                productsContainer.innerHTML = `
                    <div class="col-12 text-center">
                        <p>Не удалось загрузить рекомендуемые товары. Пожалуйста, попробуйте позже.</p>
                    </div>
                `;
            });
    }

    // Загружаем товары при загрузке страницы
    loadRecommendedProducts();
});

function formatCategoryId(id) {
    return id.toString().padStart(3, '0'); // Преобразует 1 в "001"
}