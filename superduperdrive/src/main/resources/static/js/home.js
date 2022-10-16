window.addEventListener('load', (event) => {
  var notesModal = new bootstrap.Modal(document.getElementById('notesModal'), {
    keyboard: false
  });
  var credentialsModal = new bootstrap.Modal(document.getElementById('credentialsModal'), {
    keyboard: false
  });
  if (document.getElementById('note-id').value !== '' || document.getElementById('note-error-message') !== null)
    notesModal.show();
  if (document.getElementById('credential-id').value !== '' || document.getElementById('credential-error-message') !== null)
    credentialsModal.show();
  var alertNode = document.querySelector('.alert')
  if (alertNode !== null) {
    setTimeout(
      () => { 
        let successAlert = document.getElementById('success-alert');
        let bsSuccessAlert = new bootstrap.Alert(successAlert);
        bsSuccessAlert.close();
        let errorAlert = document.getElementById('error-alert');
        let bsErrorAlert = new bootstrap.Alert(errorAlert);
        bsErrorAlert.close();
      },
      3000
    );
  }
});