function start() {
  gapi.load('auth2', function() {
    auth2 = gapi.auth2.init({
      client_id: '783185784468-e3g1j42l9jlvvoulj6rm6j0fbuk03giu.apps.googleusercontent.com',
      // Scopes to request in addition to 'profile' and 'email'
      //scope: 'additional_scope'
    });
  });
}


function signInCallback(authResult) {
  // $.get('http://localhost:3000/auth/google/callback',"asd",function(data){
  //   alert(data);
  // });


  if (authResult['code']) {

    // Hide the sign-in button now that the user is authorized, for example:
    $('#signinButton').attr('style', 'display: none');

    var data = {};
          data.code = authResult['code'];

          $.ajax({
            type: 'POST',
            data: JSON.stringify(data),
            headers: {
        'X-Requested-With': 'XMLHttpRequest'
      },
                contentType: 'application/json',
                        url: 'http://localhost:3000/login',
                        xhrFields: {withCredentials: true},
                        success: function(data) {
                            if(data=='Login successful'){window.location.replace("http://localhost:3000/home");}
                            if(data=='Not from Sabanci'){
                              alert("The email used was not from sabanci. Please try again.");
                              window.location.reload();
                            }
                        }
                    });
      } else {
    // There was an error.
    alert("error");
  }
}
