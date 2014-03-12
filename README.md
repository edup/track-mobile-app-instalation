<h1>Mobile instalation TrackingId Test</h1>  

<div>The problem that you solve with this is when you're user doesn't have the app installed and you would like to track from each source he/she installs from.</div>
<div>In my case i wanted to take an action after the user installs the app and that action was dependent of the trackingId</div>

<ul>
	<li>1. You should open this page on your smartphone</li>  
	<li>2. This is the trackingId that will appear in your mobile <span id='trackingId'></span> </li>
	<li>3. You can now install the app on your smartphone</li>  
</ul>

<div id="output"></div> 

<br/>

<div id='notes'>
	<div><b>Notes for developers:</b></div>
	<br/>
	<div>This example is to test a tracking id when your user doesn't have the app installed and you need to pass some information to it after the 1º instalation. Example: When you advertise you need to know who is generating more downloads.</div>
	<br/>
	<div><b>The behaviour in android is different than iphone so we have to serve different scripts according to each platform:</b> </div>
	<ul>
		<li>
			<p>android.js</p> 
			<p>This example only works for browsers with websocket support. If your browser doesn't support it you should implement a long pooling solution in your android/ios app too. Though, in that case, the solution is exactly the same but with long pooling.</p>
		</li>
		<li>
			<p>iphone.js</p> 
			<p>This approach should work in any iphone</p> 
		</li>
	</ul>
</div>