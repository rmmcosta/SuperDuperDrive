window.addEventListener('load', (event) => {
  var notesModal = new bootstrap.Modal(document.getElementById('notesModal'), {
    keyboard: false
  });
  var credentialsModal = new bootstrap.Modal(document.getElementById('credentialsModal'), {
    keyboard: false
  });
  if(document.getElementById('note-id').value!=='')
    notesModal.show();
  if(document.getElementById('credential-id').value!=='')
    credentialsModal.show();
});