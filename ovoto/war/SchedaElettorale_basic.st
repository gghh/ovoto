<!DOCTYPE HTML>
<html>
<head>
<title>
$title$
</title>

   <script type="text/javascript" language="javascript">
    var howMany = 0;
    var choices = [];
    var update_count = function (e) {
      if ( e.checked ) {
        if ( howMany >= $num_choices$ ) {
              if($num_choices$ == 1) {
                 alert("Invalid selection: \nyou cannot select more than 1 item.");
 			  } else {
                 alert("Invalid selection: \nyou cannot select more than $num_choices$ items.");
              }
              e.checked=false;
              return;
        }
        choices[e.name]=e.value;
        howMany++;
      } else {
       delete choices[e.name];
       howMany--;
      }
    }

    var ask_confirm = function () {
    var s = "";
    for ( var key in choices ) {
        s+= choices[key] + " ";
     }
     return confirm("Please confirm your choices:\n" +s);
   }
  </script>


</head>

<body>

<h3>$title$</h3>
<form name='vote' method='POST' action='$action$'>
<ol>
$labels:{ l |
  <li>
    <input type='checkbox' name='$l.id$' value='$l.key$' onclick="javascript: update_count(this);"/>$l.value$
  </li>
}$
</ol>
<input type='submit'  onClick="return ask_confirm();" name='submit' value='submit'/>
<input type='hidden' name='ballotId' value='$ballot_public_id$'/><br/>
<input type='hidden' name='token' value='$token_text$'/><br/>
<input type='hidden' name='mode' value='REGISTERVOTE'/><br/>
</form>
</body>
</html>


