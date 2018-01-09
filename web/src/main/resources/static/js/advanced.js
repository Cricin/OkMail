$.fn.extend({
  startrRotate: function () {
    var e = $("#" + this[0].id);
    var degree = 0;
    e.handle = setInterval(() => {
      degree += 5;
      if (degree > 360) {
        degree %= 360;
      }
      e.css("transform", "rotate(" + degree + "deg)");
    }, 16);
  },

  stopRotate: function () {
    var e = $("#" + this[0].id);
    if (e.handle != undefined) {
      clearInterval(handle);
    }
  }
});

$('#test-button').on('click', e => {
  console.log('click');
  $('#loading').startrRotate();
});