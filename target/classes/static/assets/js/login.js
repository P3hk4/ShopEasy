// document.addEventListener('DOMContentLoaded', function() {
//     const loginForm = document.getElementById('login-form');
//     const errorMessage = document.getElementById('error-message');
//
//     loginForm.addEventListener('submit', async function(e) {
//         e.preventDefault();
//
//         const login = document.getElementById('username').value;
//         const password = document.getElementById('password').value;
//
//         try {
//             const response = await fetch('/api/auth/login', {
//                 method: 'POST',
//                 headers: {
//                     'Content-Type': 'application/json'
//                 },
//                 body: JSON.stringify({ login, password })
//             });
//             if (response.ok) {
//                 console.log("log")
//                 const data = await response.json();
//                 console.log(data);
//                 // Сохраняем токен (для JWT)
//                 localStorage.setItem('authToken', data.token);
//                 console.log(data.token);
//                 // Перенаправляем на защищенную страницу
//                 window.location.href = '/';
//             } else {
//                 const error = await response.json();
//                 console.log(response)
//                 showError(error.message || 'Ошибка авторизации');
//             }
//         } catch (err) {
//             showError('Ошибка сети или сервера');
//         }
//     });
//
//     function showError(message) {
//         errorMessage.textContent = message;
//         errorMessage.style.display = 'block';
//     }
// });

document.addEventListener('DOMContentLoaded', function() {
    const loginForm = document.getElementById('login-form');
    const errorMessage = document.getElementById('error-message');

    loginForm.addEventListener('submit', async function(e) {
        e.preventDefault();

        const login = document.getElementById('username').value;
        const password = document.getElementById('password').value;

        try {
            const response = await fetch('/api/auth/login', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json'
                },
                body: JSON.stringify({ login, password })
            });

            if (response.ok) {
                const data = await response.json();
                // Сохраняем токен и данные пользователя
                localStorage.setItem('accessToken', data.accessToken);
                localStorage.setItem('userId', data.userId); // Сохраняем ID пользователя
                // Перенаправляем в личный кабинет
                window.location.href = '/account';
            } else {
                const error = await response.json();
                showError(error.message || 'Ошибка авторизации');
            }
        } catch (err) {
            showError('Ошибка сети или сервера');
        }
    });

    function showError(message) {
        errorMessage.textContent = message;
        errorMessage.style.display = 'block';
    }
});