<html>
<head>
  <script type="text/javascript">
    function get_check_value() {
      var c_value = "";
      var count =0;
      for(var i=0; i < $num_labels$ ; i++) {
        var e = document.vote['v'+i]
        if(e.checked) {
          count++;
          if(count > $num_choices$ ) {
            alert("Invalid selection: \nyou cannot select mote than $num_choices$ items.");
            return false;
          }
          c_value = c_value + e.value + "\n";
      }
      return confirm("Please confirm your choices:\n" +c_value);
    }
  </script>
</head>
<body>
<h3>$title$</h3>
<form name='vote' method='POST' action='$action$'>
<ol>
$labels:{ l |
  <li>
    <input type='checkbox' name='$l.id$' value='$l.key$'/>$l.value$
  </li>
}$
</ol>
<input type='submit'  onClick='return get_check_value();' name='submit' value='submit'/>
<input type='hidden' name='ballotId' value='$ballot_public_id$'/></br>
<input type='hidden' name='token' value='$token_text$'/></br>
<input type='hidden' name='mode' value='REGISTERVOTE'/></br>
</form>
</body>
</html>


