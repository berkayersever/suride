// config/auth.js

// expose our config directly to our application using module.exports
module.exports = {
	'googleAuth' : {
        'clientID'      : '783185784468-e3g1j42l9jlvvoulj6rm6j0fbuk03giu.apps.googleusercontent.com',
        'clientSecret'  : 'jUf52BSm9cnnthne3Boap1UB',
        'callbackURL'   : 'http://localhost:8080/auth/google/callback'
    }
}