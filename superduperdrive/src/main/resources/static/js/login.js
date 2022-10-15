window.addEventListener('load', (event) => {
  let currentUrl = window.location.href;
  if (currentUrl.includes('login?error')) {
    document.getElementById('login-error-message').classList.remove('d-none');
  }
});