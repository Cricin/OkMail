$(document).ready(e => {


  var div = document.createElement('div');
  div.innerHTML = '<img src="/images/icon-skin.png" width="24px" height="24px">';
  div.id = 'skin';
  div.classList.add('clearfix');
  document.body.appendChild(div);
  var skin = $('#skin')
      .css('float', 'right')
      .css('z-index', '100')
      .css('margin-top', '10px')
      .css('margin-right', '10px');

  skin.on('click', e => {

  })


})
