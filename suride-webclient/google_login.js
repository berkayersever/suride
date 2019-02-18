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

    alert(authResult['code']);

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
                            console.log(JSON.stringify(data));
                        }
                    });
      } else {
    // There was an error.
    alert("error");
  }
}


   !function(A,n,g,u,l,a,r){A.GoogleAnalyticsObject=l,A[l]=A[l]||function(){
   (A[l].q=A[l].q||[]).push(arguments)},A[l].l=+new Date,a=n.createElement(g),
   r=n.getElementsByTagName(g)[0],a.src=u,r.parentNode.insertBefore(a,r)
}(window,document,'script','https://www.google-analytics.com/analytics.js','ga');

   ga('create', 'UA-XXXXX-X');
   ga('send', 'pageview');
