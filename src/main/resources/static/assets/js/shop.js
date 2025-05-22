

// Текущая страница
let currentPage = 1;
// Общее количество страниц (можно получить из заголовков или отдельным запросом)
let totalPages = 1;

import { addToCart } from './cart.js';

let currentCategory =  parseInt(localStorage.getItem('preselectedCategory')) || "0";

document.addEventListener('DOMContentLoaded', function() {
    // Инициализация категорий
    initCategoryLinks();
    // Загрузка первой страницы
    loadInitialData();
});

function initCategoryLinks() {
    const categoryLinks = document.querySelectorAll('#collapseCategories a');
    categoryLinks.forEach(link => {
        link.addEventListener('click', function(e) {
            e.preventDefault();
            const categoryId = this.getAttribute('data-category-id');
            currentCategory = categoryId;
            localStorage.removeItem('preselectedCategory'); // Очищаем после использования
            currentPage = 1;
            loadCategoryData(categoryId);
        });
    });

    // Если есть предварительно выбранная категория - активируем её
    if (currentCategory !== "0") {
        const categoryLink = document.querySelector(`#collapseCategories a[data-category-id="${currentCategory}"]`);
        if (categoryLink) {
            categoryLink.classList.add('active-category');
            // Прокручиваем к выбранной категории
            setTimeout(() => {
                categoryLink.scrollIntoView({ behavior: 'smooth', block: 'nearest' });
            }, 100);
        }
    }


}

function loadInitialData() {
    if (currentCategory === "0") {
        getTotalPages()
            .then(pages => {
                totalPages = pages;
                loadPage(currentPage);
            })
            .catch(handleError);
    } else {
        loadCategoryData(currentCategory);
    }
    localStorage.removeItem('preselectedCategory'); // Очищаем после использования
}

function loadCategoryData(categoryId) {
    if (categoryId === "0") {
        getTotalPages()
            .then(pages => {
                totalPages = pages;
                loadPage(currentPage);
            })
            .catch(handleError);
    } else {
        fetch(`/api/products/category/${categoryId}/pages`)
            .then(response => {
                if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
                return response.json();
            })
            .then(pages => {
                totalPages = pages;
                loadPage(currentPage, categoryId); // Передаем categoryId в loadPage
            })
            .catch(handleError);
    }
}

function handleError(error) {
    console.error('Ошибка:', error);
    document.getElementById('products-container').innerHTML =
        '<div class="col-12"><p>Произошла ошибка при загрузке товаров</p></div>';
}

// Функция для получения общего количества страниц
function getTotalPages() {
    return fetch('/api/products/pages')
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            if (data !== undefined && data !== null) {
                return data;
            } else {
                throw new Error('Неверный формат данных о количестве страниц');
            }
        });
}

function loadPage(page, categoryId = currentCategory) {
    const url = categoryId === "0"
        ? `/api/products/${page}`
        : `/api/products/category/${categoryId}/page/${page}`;

    fetch(url, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
        }
    })
    .then(response => {
        if (!response.ok) throw new Error(`HTTP error! status: ${response.status}`);
        return response.json();
    })
    .then(data => {
        if (data && data.length > 0) {
            displayProducts(data);
            updatePagination(page, categoryId); // Обновляем пагинацию с учетом категории
            currentPage = page;
        } else {
            document.getElementById('products-container').innerHTML =
                '<div class="col-12"><p>Товары не найдены</p></div>';
        }
    })
    .catch(handleError);
}

    function formatCategoryId(id) {
    return id.toString().padStart(3, '0'); // Преобразует 1 в "001"
}

 // Функция для отображения товаров
function displayProducts(products) {
    const container = document.getElementById('products-container');
    container.innerHTML = '';

    products.forEach(product => {
        const productHtml = `
            <div class="col-md-4">
                <div class="card mb-4 product-wap rounded-0">
                    <div class="card rounded-0">
                        <img class="card-img rounded-0 img-fluid" src="/assets/img/product_category_${String(formatCategoryId(product.categoryId))}.jpg">
                        <div class="card-img-overlay rounded-0 product-overlay d-flex align-items-center justify-content-center">
                            <ul class="list-unstyled">
                                <li><a class="btn btn-success text-white" href="shop-single?id=${product.productId}"><i class="far fa-heart"></i></a></li>
                                <li><a class="btn btn-success text-white mt-2" href="shop-single?id=${product.productId}"><i class="far fa-eye"></i></a></li>
                                <li><button class="btn btn-success text-white mt-2 add-to-cart" data-product='${JSON.stringify(product)}'>
                                    <i class="fas fa-cart-plus"></i>
                                </button></li>
                            </ul>
                        </div>
                    </div>
                    <div class="card-body">
                        <a href="shop-single?id=${product.productId}" class="h3 text-decoration-none">${product.name || 'Название товара'}</a>
                        <ul class="w-100 list-unstyled d-flex justify-content-between mb-0">
                            <li class="pt-2">
                                ${generateColorDots(product.colors || ['red', 'blue', 'black'])}
                            </li>
                        </ul>
                        <p class="text-center mb-0">₽${product.price || '0.00'}</p>
                    </div>
                </div>
            </div>
        `;

        container.innerHTML += productHtml;
    });

    // Добавляем обработчики событий для кнопок "Добавить в корзину"
    document.querySelectorAll('.add-to-cart').forEach(button => {
        button.addEventListener('click', (e) => {
            e.preventDefault();
            const product = JSON.parse(button.dataset.product);
            addToCart(product);
        });
    });
}

    // Генерация цветных точек
    function generateColorDots(colors) {
    return colors.map(color =>
    `<span class="product-color-dot color-dot-${color} float-left rounded-circle ml-1"></span>`
    ).join('');
}

function updatePagination(currentPage, categoryId = currentCategoryId) {
    const pagination = document.getElementById('pagination');
    pagination.innerHTML = '';

    // Кнопка "Назад"
    const prevItem = document.createElement('li');
    prevItem.className = `page-item ${currentPage === 1 ? 'disabled' : ''}`;
    const prevLink = document.createElement('a');
    prevLink.className = 'page-link rounded-0 mr-3 shadow-sm border-top-0 border-left-0 text-dark';
    prevLink.href = '#';
    prevLink.innerHTML = '&laquo;';
    prevLink.addEventListener('click', (e) => {
        e.preventDefault();
        if (currentPage > 1) loadPage(currentPage - 1, categoryId);
    });
    prevItem.appendChild(prevLink);
    pagination.appendChild(prevItem);

    // Показываем первую страницу
    if (currentPage > 4) {
        addPageNumber(1, categoryId);
        if (currentPage > 5) {
            addEllipsis();
        }
    }

    // Диапазон страниц
    const startPage = Math.max(1, currentPage - 3);
    const endPage = Math.min(totalPages, currentPage + 3);

    for (let i = startPage; i <= endPage; i++) {
        addPageNumber(i, categoryId);
    }

    // Показываем последнюю страницу
    if (currentPage < totalPages - 3) {
        if (currentPage < totalPages - 4) {
            addEllipsis();
        }
        addPageNumber(totalPages, categoryId);
    }

    // Кнопка "Вперед"
    const nextItem = document.createElement('li');
    nextItem.className = `page-item ${currentPage === totalPages ? 'disabled' : ''}`;
    const nextLink = document.createElement('a');
    nextLink.className = 'page-link rounded-0 shadow-sm border-top-0 border-left-0 text-dark';
    nextLink.href = '#';
    nextLink.innerHTML = '&raquo;';
    nextLink.addEventListener('click', (e) => {
        e.preventDefault();
        if (currentPage < totalPages) loadPage(currentPage + 1, categoryId);
    });
    nextItem.appendChild(nextLink);
    pagination.appendChild(nextItem);

    function addPageNumber(pageNumber, catId) {
        if (pagination.querySelector(`a[data-page="${pageNumber}"]`)) return;

        const pageItem = document.createElement('li');
        pageItem.className = 'page-item';

        const pageLink = document.createElement('a');
        pageLink.className = `page-link rounded-0 mr-3 shadow-sm border-top-0 border-left-0 ${
            pageNumber === currentPage ? 'active' : 'text-dark'
        }`;
        pageLink.href = '#';
        pageLink.textContent = pageNumber;
        pageLink.dataset.page = pageNumber;

        pageLink.addEventListener('click', (e) => {
            e.preventDefault();
            if (pageNumber !== currentPage) loadPage(pageNumber, catId);
        });

        pageItem.appendChild(pageLink);
        pagination.appendChild(pageItem);
    }

    function addEllipsis() {
        const ellipsisItem = document.createElement('li');
        ellipsisItem.className = 'page-item disabled';
        const ellipsisLink = document.createElement('a');
        ellipsisLink.className = 'page-link rounded-0 mr-3 shadow-sm border-top-0 border-left-0';
        ellipsisLink.href = '#';
        ellipsisLink.innerHTML = '...';
        ellipsisItem.appendChild(ellipsisLink);
        pagination.appendChild(ellipsisItem);
    }
}

