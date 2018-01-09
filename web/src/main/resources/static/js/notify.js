//data and style
var node = document.createElement('div')
node.id = 'notify-bar'
node.innerHTML = '<span id="notify-text"></span>'

var style = document.createElement('style')
style.innerHTML = `#notify-bar {
  margin: 0;
  padding: 0;
  margin-top: -20px;
  animation-duration: 0.3s;
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  z-index: 1000;
}

#notify-text {
  display: block;
  height: 20px;
  background-color: #25b7ff;
  margin: 0 auto;
  color: white;
  font-size: 16px;
  font-weight: 600;
  text-align: center;
  line-height: 20px;
}

@keyframes notify-in {
  from {
    margin-top: -20px;
  }
  to {
    margin-top: 0px;
  }
}

@keyframes notify-out {
  from {
    margin-top: 0px;
  }
  to {
    margin-top: -20px;
  }
}`

document.body.appendChild(style)
document.body.appendChild(node)


//notify
var notifyTimer = 0
var animating = false;
var goingToHide = true;
var textDelayed
var bar = document.getElementById('notify-bar')
bar.addEventListener('webkitAnimationEnd', (event) => {
  if (event.srcElement.id != 'notify-bar') {
    return;
  }
  animating = false
  if (goingToHide) {
    event.srcElement.style.marginTop = '-20px'
  } else {
    event.srcElement.style.marginTop = '0px'
  }
  if (!goingToHide) {
    clearAndSetTimeout()
  }
  if (textDelayed != undefined) {
    document.getElementById('notify-text').innerHTML = text
    animate(true)
  }
})
bar.addEventListener('webkitAnimationStart', (event) => {
  if (event.srcElement.id != 'notify-bar') {
    return;
  }
  animating = true
})

function animate(show) {
  if (show) {
    goingToHide = false
    bar.style.animationName = 'notify-in'
  } else {
    goingToHide = true
    bar.style.animationName = 'notify-out'
  }
}

function clearAndSetTimeout() {
  clearTimeout(notifyTimer)
  notifyTimer = setTimeout(animate, 3000, goingToHide)
}

function notify(text) {
  document.getElementById('notify-text').innerHTML = text
  if (animating) {
    if (goingToHide) {
      //正在隐藏
      textDelayed = text
    }
  } else {
    if (goingToHide) {
      animate(true)
    } else {
      clearAndSetTimeout()
    }
  }
}

