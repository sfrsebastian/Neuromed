(function() {
  var leaseMeter, meterBar, meterBarWidth, meterValue, progressNumber;

  /*Get value of value attribute*/
  var valueGetter = function() {
    leaseMeter = document.getElementsByClassName('leaseMeter');
    for (var i=0; i<leaseMeter.length; i++) {
      meterValue = leaseMeter[i].getAttribute('value');
      metermax = leaseMeter[i].getAttribute('max');
      var answer = parseInt(meterValue)/parseInt(metermax)*100;
      return answer;
    }
  }

  /*Convert value of value attribute to percentage*/
  var getPercent = function() {
    meterBarWidth = parseInt(valueGetter());
    meterBarWidth.toString;
    meterBarWidth = meterBarWidth + "%";
    return meterBarWidth;
  }

  /*Apply percentage to width of .meterBar*/
  var adjustWidth = function() {
    meterBar = document.getElementsByClassName('meterBar');
    for (var i=0; i<meterBar.length; i++) {
      var valor = valueGetter();
      meterBar[i].style['height'] = getPercent();
      if(valor >= 80){
        meterBar[i].style['background-color'] = '#DB1212';
      }
      else if(valor >= 40){
        meterBar[i].style['background-color'] = '#FFFF33';
      }
      else{
        meterBar[i].style['background-color'] = '#46DF2E';
      }
    }
  }

  /*Update value indicator*/
  var indicUpdate = function() {
    progressNumber = document.getElementsByClassName('progressNumber');
    for (var i=0; i<progressNumber.length; i++) {
      progressNumber[i].innerHTML = valueGetter()/10;
    }
  }

  adjustWidth();
  indicUpdate();
})();


