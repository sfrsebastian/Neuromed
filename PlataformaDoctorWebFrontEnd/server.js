var express = require('express');
var app = express();

app.set('port', process.env.PORT || 3000);
app.set('views', __dirname + '/app');
app.use("/", express.static(__dirname + '/app'));

app.get('/', function(req, res, next) {
    res.render("index.html");
});

app.listen(app.get('port'), function(){
	console.log("Server initiated on port " + app.get('port'));
});