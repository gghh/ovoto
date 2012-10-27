<!DOCTYPE HTML>
<html>
<head>
<title>
$title$
</title>

<link rel="stylesheet" type="text/css" href="/styleSIMAIBallot.css" />


   <script type="text/javascript" language="javascript">
    var ask_confirm = function () {
       return confirm("Confermi la tua scelta?");
    }
  </script>


</head>

<body>



<div id="page">
  <table width="100%">
    <tr>
      <td>
        <div class="title">
 			$title$
        </div>
      </td>
      <td>
        <img style="width: 300px;" src="/logo_simai.jpg"/>
      </td>
    </tr>
  </table>


  <div class="preamble">
    <p>
	Il nuovo statuto SIMAI con evidenziate le modifiche &egrave; disponibile all' url
 	<a href="http://www.simai.eu/it/news-home/nuovo-statuto-simai.html">http://www.simai.eu/it/news-home/nuovo-statuto-simai.html</a>
     </p>
    <p>
      Per approvare le modifiche proposte clicca su <span style="font-weight: bold">APPROVO</span>;
    </p>
    <p>
      Per rifiutare le modifiche proposte clicca su <span style="font-weight: bold">NON APPROVO</span>;
    </p>
  </div>


  <div class="form">
    <form name='vote' method='POST' action='$action$'>
	  <input type='hidden' name='ballotId' value='$ballot_public_id$'/>
	  <input type='hidden' name='token' value='$token_text$'/>
	  <input type='hidden' name='mode' value='REGISTERVOTE'/>
      <table cellspacing="10">
        <tr>
          <td>
            <input type="radio" id="v0" name="choices" class="regular-radio big-radio" value="APPROVO"/><label for="v0"></label>
          </td>
          <td>
            APPROVO
          </td>
          <td class="spacer"></td>
          <td>
            <input type="radio" id="v1" name="choices" class="regular-radio big-radio" value="NON APPROVO"/><label for="v1"></label>
          </td>
          <td>
            NON APPROVO
          </td>
        </tr>
        <tr>
          <th colspan="2">
            <input type='reset' name='reset' value='Reset'/>
          </th>
          <th colspan="3">
            <input type='submit'  onClick="return ask_confirm();" name='submit' value='INVIA'/>
          </th>
        </tr>
      </table>
    </form>
  </div>

  <div class="postamble">
  </div>

</div>

</body>
</html>


<!--
<ol>
$labels:{ l |
  <li>
    <input type='checkbox' name='$l.id$' value='$l.key$' onclick="javascript: update_count(this);"/>$l.value$
  </li>
}$
</ol>
<input type='submit'  onClick="return ask_confirm();" name='submit' value='submit'/>
-->


