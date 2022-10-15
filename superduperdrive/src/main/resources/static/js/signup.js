window.addEventListener('load', (event) => {
  let currentUrl = window.location.href;
  if (document.getElementById('success-msg') !== null) {
    setTimeout(
      () => { window.location.replace(currentUrl.replace('signup', 'login')); },
      1000
    );
  }
});