<?php

define('psm\DEMO',  TRUE);
define('psm\DEBUG', TRUE);
//define('psm\DEFAULT_MODULE', 'builder');
//define('psm\DEFAULT_PAGE',   'current');

// load the portal
include_once(__DIR__.'/psm/Loader.php');
$portal = \psm\Portal::auto();

?>