// Текущая страница
let currentPage = 1;
// Общее количество страниц (можно получить из заголовков или отдельным запросом)
let totalPages = 1;

let currentCategory = "all";


document.addEventListener('DOMContentLoaded', function() {
    // Сначала получаем общее количество страниц
    getTotalPages()
        .then(pages => {
            totalPages = pages;
            // Затем загружаем первую страницу товаров
            loadPage(currentPage);
        })
        .catch(error => {
            console.error('Ошибка при получении количества страниц:', error);
            document.getElementById('products-container').innerHTML =
                '<div class="col-12"><p>Произошла ошибка при загрузке данных</p></div>';
        });
});

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

// Функция для загрузки страницы
function loadPage(page) {

    // Отправляем POST-запрос как указано в API
    fetch(`/api/products/${page}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
        }
    })
        .then(response => {
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }
            return response.json();
        })
        .then(data => {
            if (data && data.length > 0) {
                console.log(data);
                displayProducts(data);
                updatePagination(page);
                currentPage = page;

                // Если у вас есть информация об общем количестве страниц:
                // totalPages = data.totalPages; или из заголовков
            } else {
                document.getElementById('products-container').innerHTML =
                    '<div class="col-12"><p>Товары не найдены</p></div>';
            }
        })
        .catch(error => {
            console.error('Ошибка:', error);
            document.getElementById('products-container').innerHTML =
                '<div class="col-12"><p>Произошла ошибка при загрузке товаров</p></div>';
        });
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
                    <img class="card-img rounded-0 img-fluid" src="/assets/img/product_category_${String(formatCategoryId(product.categoryId))}.jpg"">
                    <div class="card-img-overlay rounded-0 product-overlay d-flex align-items-center justify-content-center">
                        <ul class="list-unstyled">
                            <li><a class="btn btn-success text-white" href="shop-single.html?id=${product.id}"><i class="far fa-heart"></i></a></li>
                            <li><a class="btn btn-success text-white mt-2" href="shop-single.html?id=${product.id}"><i class="far fa-eye"></i></a></li>
                            <li><a class="btn btn-success text-white mt-2" href="shop-single.html?id=${product.id}"><i class="fas fa-cart-plus"></i></a></li>
                        </ul>
                    </div>
                </div>
                <div class="card-body">
                    <a href="shop-single.html?id=${product.id}" class="h3 text-decoration-none">${product.name || 'Название товара'}</a>
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
}

    // Генерация цветных точек
    function generateColorDots(colors) {
    return colors.map(color =>
    `<span class="product-color-dot color-dot-${color} float-left rounded-circle ml-1"></span>`
    ).join('');
}

// Обновление пагинации
function updatePagination(currentPage) {
    const pagination = document.getElementById('pagination');
    pagination.innerHTML = '';

    // Добавляем кнопку "Назад"
    const prevItem = document.createElement('li');
    prevItem.className = `page-item ${currentPage === 1 ? 'disabled' : ''}`;
    const prevLink = document.createElement('a');
    prevLink.className = 'page-link rounded-0 mr-3 shadow-sm border-top-0 border-left-0 text-dark';
    prevLink.href = '#';
    prevLink.innerHTML = '&laquo;';
    prevLink.addEventListener('click', (e) => {
        e.preventDefault();
        if (currentPage > 1) loadPage(currentPage - 1);
    });
    prevItem.appendChild(prevLink);
    pagination.appendChild(prevItem);

    // Всегда показываем первую страницу
    if (currentPage > 4) {
        addPageNumber(1);
        if (currentPage > 5) {
            addEllipsis();
        }
    }

    // Определяем диапазон страниц для отображения
    const startPage = Math.max(1, currentPage - 3);
    const endPage = Math.min(totalPages, currentPage + 3);

    // Добавляем страницы в диапазоне
    for (let i = startPage; i <= endPage; i++) {
        addPageNumber(i);
    }

    // Всегда показываем последнюю страницу
    if (currentPage < totalPages - 3) {
        if (currentPage < totalPages - 4) {
            addEllipsis();
        }
        addPageNumber(totalPages);
    }

    // Добавляем кнопку "Вперед"
    const nextItem = document.createElement('li');
    nextItem.className = `page-item ${currentPage === totalPages ? 'disabled' : ''}`;
    const nextLink = document.createElement('a');
    nextLink.className = 'page-link rounded-0 shadow-sm border-top-0 border-left-0 text-dark';
    nextLink.href = '#';
    nextLink.innerHTML = '&raquo;';
    nextLink.addEventListener('click', (e) => {
        e.preventDefault();
        if (currentPage < totalPages) loadPage(currentPage + 1);
    });
    nextItem.appendChild(nextLink);
    pagination.appendChild(nextItem);

    // Вспомогательная функция для добавления номера страницы
    function addPageNumber(pageNumber) {
        // Не добавляем дубликаты (может быть, если текущая страница близка к краям)
        if (pagination.querySelector(`a[data-page="${pageNumber}"]`)) return;

        const pageItem = document.createElement('li');
        pageItem.className = 'page-item';

        const pageLink = document.createElement('a');
        pageLink.className = `page-link rounded-0 mr-3 shadow-sm border-top-0 border-left-0 ${pageNumber === currentPage ? 'active' : 'text-dark'}`;
        pageLink.href = '#';
        pageLink.textContent = pageNumber;
        pageLink.dataset.page = pageNumber;

        pageLink.addEventListener('click', (e) => {
            e.preventDefault();
            if (pageNumber !== currentPage) loadPage(pageNumber);
        });

        pageItem.appendChild(pageLink);
        pagination.appendChild(pageItem);
    }

    // Вспомогательная функция для добавления многоточия
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
