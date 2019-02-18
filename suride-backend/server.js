const http = require('http');

const hostname = 'localhost';
const port = 3000;

var express = require('express');
var Session = require('express-session');

var MongoClient = require('mongodb').MongoClient
var _db = null;
MongoClient.connect("mongodb://localhost:27017/suride-db", function(err, db){
    if(err) {return console.dir(err);}
    console.log("Connected to database.");
    _db = db.db('suride-db');
    _db.createCollection('users',function(err,collection) {});
    _db.createCollection('rides',function(err,collection){});
});

var {google} = require('googleapis');
var OAuth2 = google.auth.OAuth2;
var oauth2 = google.oauth
var plus = google.plus('v1');
const ClientId = "783185784468-e3g1j42l9jlvvoulj6rm6j0fbuk03giu.apps.googleusercontent.com";
const ClientSecret = "jUf52BSm9cnnthne3Boap1UB";

// const RedirectionUrl = "http://localhost:9000";
const RedirectionUrl = "http://localhost:3000";

//extra requires
var request = require("request");
var path    = require("path");

var app = express();
var bodyParser = require('body-parser');
app.use(bodyParser.json());
app.use(bodyParser.urlencoded({ extended: false }));
app.use(Session({
    secret: 'suride-secret-53114152679',
    resave: false,
    saveUninitialized: true
}));

app.use("/node_modules", express.static('node_modules'));
app.use("/public", express.static('public'));
app.use(function (req, res, next) {

    // Website you wish to allow to connect
    res.setHeader('Access-Control-Allow-Origin', 'http://localhost:3000');

    // Request methods you wish to allow
    res.setHeader('Access-Control-Allow-Methods', 'GET, POST, OPTIONS, PUT, PATCH, DELETE');

    // Request headers you wish to allow
    res.setHeader('Access-Control-Allow-Headers', 'X-Requested-With,content-type');

    // Set to true if you need the website to include cookies in the requests sent
    // to the API (e.g. in case you use sessions)
    res.setHeader('Access-Control-Allow-Credentials', true);

    // Pass to next layer of middleware
    if ('OPTIONS' == req.method) {
        res.sendStatus(200);
    }
    else {
        next();
    }
});

function isAuthenticated(req, res, next) {
  // do any checks you want to in here

  // CHECK THE USER STORED IN SESSION FOR A CUSTOM VARIABLE
  // you can do this however you want with whatever variables you set up
  if(req.session.user!=null){
  if (req.session.user.id)
      return next();
    }
  // IF A USER ISN'T LOGGED IN, THEN REDIRECT THEM SOMEWHERE
  res.redirect('/');
}


function getOAuthClient () {
    return new OAuth2(ClientId ,  ClientSecret, RedirectionUrl);
}

function getAuthUrl () {
    var oauth2Client = getOAuthClient();
    // generate a url that asks permissions for Google+ and Google Calendar scopes
    var scopes = [
        'https://www.googleapis.com/auth/plus.login',
          'https://www.googleapis.com/auth/plus.me',
          'https://www.googleapis.com/auth/plus.profile.emails.read'
    ];

    var url = oauth2Client.generateAuthUrl({
        access_type: 'offline',
        scope: scopes // If you only need one scope you can pass it as string
    });

    return url;
}

app.get("/", function(req,res) {
	// var url = getAuthUrl();
 //    var session = req.session;
 //    console.log(session);
 //    if(session.tokens){
 //        res.send(req.session['tokens']+`Google OAuth<br/>
 //            <a href=${url}>Login</a>
 //            `);
 //    }
 //    else{
 //    	res.send(`Google OAuth<br/>
 //    		<a href=${url}>Login</a>
 //    		`);
 //    }
 var session = req.session;
 console.log(req.session.user);
 if(req.session.user==null)
  res.sendFile(path.join(__dirname+'/login.html'));
else {
  res.redirect('/home');
}
});
//all our custom urls
app.get("/index.html", isAuthenticated,function(req,res){
    res.sendFile(path.join(__dirname+'/index.html'));
});
app.get("/index.js", isAuthenticated,function(req,res){
    res.sendFile(path.join(__dirname+'/index.js'));
});
app.get("/google_login.js",function(req,res){
    res.sendFile(path.join(__dirname+'/google_login.js'));
});
app.get("/form", isAuthenticated,function(req,res){
    res.sendFile(path.join(__dirname+'/form.html'));
});

app.get("/form.js",isAuthenticated,function(req,res){
    res.sendFile(path.join(__dirname+'/form.js'));
});

app.get("/home", isAuthenticated,function(req,res){
    res.sendFile(path.join(__dirname+'/home.html'));
});

app.get("/app.js",isAuthenticated,function(req,res){
    res.sendFile(path.join(__dirname+'/app.js'));
});
app.get("/trial", isAuthenticated,function(req,res){
    res.sendFile(path.join(__dirname+'/trial.html'));
});
app.get("/trial.js",isAuthenticated,function(req,res){
    res.sendFile(path.join(__dirname+'/trial.js'));
});

app.get("/profile", isAuthenticated,function(req,res){
    res.sendFile(path.join(__dirname+'/profile.html'));
});
app.get("/profile.js",isAuthenticated,function(req,res){
    res.sendFile(path.join(__dirname+'/profile.js'));
});
app.get("/details",isAuthenticated,function(req,res){
    res.sendFile(path.join(__dirname+'/details.html'));
});

app.get("/node_modules/angular/angular.js",isAuthenticated,function(req,res){
    res.sendFile(path.join(__dirname+'/node_modules/angular/angular.js'));
});
app.get("/img/maps.png",isAuthenticated,function(req,res){
    res.sendFile(path.join(__dirname+'/img/maps.png'));
});

app.get("/map.tmpl.html",isAuthenticated,function(req,res){
    res.sendFile(path.join(__dirname+'/map.tmpl.html'));
});

app.get("/jquery.js",function(req,res){
    res.sendFile(path.join(__dirname+'/jquery.js'));
});
//all our custom urls end here

app.get("/userinfo", isAuthenticated, function(req,res){
  _db.collection('users').findOne({_id:{$eq:req.session.user.id}},{name:1}, function(err, doc){
    if(err)console.log(err);
    if(doc){
      if(!doc.rating)doc.rating = 0;
      if(!doc.rides)doc.rides=0;
      res.send(doc);
    }
  });
});

app.get("/userdata",function(req,res){
    _db.collection('users').find({"_id":req.session.user.id}).toArray(function(err, docs) {
      console.log(docs);
    });

     request({
      method: 'GET',
      uri: 'https://www.googleapis.com/plus/v1/people/'+req.session.user.id,
      headers: {'Authorization': 'Bearer ' +  req.session.tokens.access_token}
    }, function (error, response, body){
      if(!error){
        console.log("ok");
        res.json(JSON.parse(body));
      }
    })


});

function setupUser(tokens,res,req){

    var url = "https://www.googleapis.com/oauth2/v1/userinfo?alt=json&access_token="+tokens.access_token;

    request({
    url: url,
    json: true
}, function (error, response, body) {
    if(!(body.email.endsWith("sabanciuniv.edu"))){res.send('Not from Sabanci');}
    if (!error && response.statusCode === 200 && (body.email.endsWith("sabanciuniv.edu"))) {
        // res.send(body) // Print the json response
        _db.collection('users').insertOne({_id:body.id,name:body.name,picture:body.picture,gender:body.gender,tokens:tokens}, function(err, r){if(!err){console.log('User '+body+' added to DB.')}});
        req.session.tokens=tokens;
        req.session.user = {id:body.id, name:body.name};
        req.session.save(function(err){if(err){console.log(err);}});
        // res.json(body);
        console.log(body.name+" successfully logged in.");
        res.send('Login successful');
    }
});
}

app.get("/checklogin",function(req,res){
    var oauth2Client = getOAuthClient();
    var code = req.query.code;

    console.log(req.query);
    oauth2Client.getToken(code, function(err, tokens) {
        if(err){console.log(err);}
        if(!err){
            oauth2Client.setCredentials(tokens);

            // plus = google.plus({version:'v1',auth:oauth2Client});
            // const res = await plus.people.get({ userId: 'me' });
             // console.log(res.data);
            setupUser(tokens,res,req);

        }
    });

});

app.post("/login",function(req,res){
   var oauth2Client = getOAuthClient();
    var code = req.body.code;

    console.log(req.body);
    oauth2Client.getToken(code, function(err, tokens) {
        if(err){console.log(err);}
        if(!err){
            oauth2Client.setCredentials(tokens);

            // plus = google.plus({version:'v1',auth:oauth2Client});
            // const res = await plus.people.get({ userId: 'me' });
             // console.log(res.data);
            setupUser(tokens,res,req);

        }
    });
});

app.post("/logout", function(req,res){
  console.log("User: " + req.session.user.id + " logged out.");
  req.session.destroy();
  res.send("logout");
});


app.post("/auth/google/callback", function (req, res) {
	console.log("auth called");
	console.log('body: ' + req.body.code);
    oauth2Client = getOAuthClient();
    var session = req.session;
    // var code = req.query.code; // the query param code
    var code = req.body.code;
    console.log("Code is: " + code);
    oauth2Client.getToken(code, function(err, tokens) {
      // Now tokens contains an access_token and an optional refresh_token. Save them.
 	if(err){console.log(err);}
      if(!err) {
        oauth2Client.setCredentials(tokens);
        //saving the token to current session
        session["tokens"]=tokens;
        res.send(`
            &lt;h3&gt;Login successful!!&lt;/h3&gt;
            <a href="/details">details</a>
        `);
      }
      else{
        res.send(`
            &lt;h3&gt;Login failed!!&lt;/h3&gt;
        `);
      }
    });
});

app.post("/postride", function(req,res){
    // res.send(req.body);
    _db.collection('users').findOne({_id:{$eq:req.session.user.id}},{name:1}, function(err, doc){
        if(err) throw err;
        _db.collection('users').findOneAndUpdate({_id:{$eq:req.session.user.id}},{$inc: {rides:1}});
        _db.collection('rides').insertOne({_id:req.session.user.id+req.body.deptime,user:req.session.user.id,name:doc.name,dest:req.body.dest,deptime:req.body.deptime,seats:req.body.seats,desc:req.body.desc,timeposted:Date.now(),loc:req.body.loc}, function(err, r){if(!err){console.log('Ride '+req.body+' added to DB.')}else{console.log("User tried to make same time and ride.")}});
        res.send("Ride posted: "+req.session.user.id+req.body.deptime);
        console.log("Ride posted: "+req.session.user.id+req.body.deptime);
    });
});

app.get("/getrides", function(req,res){
    _db.collection('rides').find({}).toArray(function(err,result){
        if(err) throw err;
        result.forEach(function(r){if(r){r.mapurl = "https://maps.googleapis.com/maps/api/staticmap?zoom=12&size=300x240&maptype=roadmap&markers=color:red%7Clabel:%7C"+r.loc.lat+","+r.loc.long+"&key=AIzaSyDtZptno5dSTfvD-php6kPSBRCXhzDgHa8"}});
        res.send(result);
    });
});

app.get("/getride",function(req,res){
    _db.collection('rides').find({"_id":req.query.id}).toArray(function(err,result){
      console.log("Ride called " + req.query.id);
      res.send(result[0]);
    });
});

app.get("/applyride", isAuthenticated, function(req,res){
    _db.collection('rides').find({"_id":req.query.id}).toArray(function(err,result){
        if(result.user == req.session.user.id){res.send("You cannot apply to your own ride.")}
        else{
                _db.collection('rides').findOneAndUpdate({"_id":req.query.id},{$addToSet: {applicants:req.session.user.id+"s1337p"+req.session.user.name}});
                res.send("You have successfully applied to this ride.");
            }
        console.log("User: " + req.session.user.id + " has applied to " + req.query.id);
    });
});

app.get("/editapplicants",isAuthenticated, function(req, res){
    res.sendFile(path.join(__dirname+'/editapplicants.html'));
});

app.get("/getapplicants", isAuthenticated, function(req,res){
    _db.collection('rides').find({"_id":req.query.id}).toArray(function(err,result){
      applicants = [];
        result[0].applicants.forEach(function(r){if(r){var arr = r.split("s1337p"); applicants.push({"id":arr[0],"name":arr[1]});} });
        console.log(applicants);
        res.send(applicants);  
    });
});

app.get("/acceptapplicant", isAuthenticated, function(req,res){
    _db.collection('rides').find({"_id":req.query.id}).toArray(function(err,result){
        _db.collection('rides').findOneAndUpdate({"_id":req.query.id},{$addToSet: {applicants_accepted:req.query.applicantid}});
        console.log("Applicant " + req.query.applicantid + " got accepted to ride "+ req.query.id);
        res.send("You have successfully accepted the applicant to this ride.");
    });
});

app.listen(port, hostname, () => {
  console.log(`Server running at http://${hostname}:${port}/`);
});
