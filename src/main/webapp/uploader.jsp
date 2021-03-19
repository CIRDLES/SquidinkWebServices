<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>Java Ajax Servlet File Upload</title>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/CssServlet/squidCSS.css">
	<link href="https://unpkg.com/material-components-web@latest/dist/material-components-web.min.css" rel="stylesheet">
	<script src="https://cdnjs.cloudflare.com/ajax/libs/react/15.4.2/react.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/react/15.4.2/react-dom.js"></script>
	<script src="https://cdnjs.cloudflare.com/ajax/libs/babel-standalone/6.21.1/babel.min.js"></script>
	<script src="https://unpkg.com/material-components-web@latest/dist/material-components-web.min.js"></script>
	<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/css/bootstrap.min.css">
	<!-- jQuery library -->
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
	<!-- Latest compiled JavaScript -->
	<script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.4.1/js/bootstrap.min.js"></script>
	<!-- Beacon API -->

	<script>
        //Redundancy in dropdown functions between FileUlpad, XLS, and Prawn
        var xls;
        var prawn;
        var bg;
		async function uploadFile() {
			let formData = new FormData();
			formData.append("file", ajaxfile.files[0]);
			await fetch('fileuploadservlet', {
				method: "POST",
				body: formData
			});
			alert('The file upload with Ajax and Java was a success!');
		}
		$(window).on('load',function() {
			// Animate loader off screen
			$(".se-pre-con").fadeOut("slow");;
		});
        function hideDropdownXls(){
            document.getElementById('dropdown-menu-xls').style.display = "none";
        }
        async function hideDropdownAndDelete() {
            document.getElementById('dropdown-menu-prawn').style.display = "none";
        }

        function showDropdownXls(){
            let element = document.getElementById('dropdown-menu-xls').style.display;
            if (element === "none"){
                document.getElementById('dropdown-menu-xls').style.display = "block";
            }
            else {
                document.getElementById('dropdown-menu-xls').style.display = "none";
            }
        }


        async function getFilesXls() {
            const data = await fetch('fileuploadservlet', {
                method: "GET",
                body: null
            }).then(function(response) {return response.text();})
                .then(function(data) {
                    console.log(data); // this will be a string
                    console.log(typeof(data));
                    let dataArr = data.split("***");
                    console.log(dataArr);
                    let file_list = document.getElementById('dropdown-menu-xls');
                    file_list.innerHTML = '';
                    file_list.innerHTML = file_list.innerHTML + "<form style=\"display: margin: 0; padding: 0;\" class=\"form-horizontal form-validate form-inline\" method=\"post\" action=\"fileuploadservlet\" enctype=\"multipart/form-data\">\n" +
                        //"\t\t\t\t\t<button style=\"display:inline;width:120px; height:30px;\" onclick=\"document.getElementById('getFile').click()\">Add File</button>\n" +
                        "\t\t\t\t\t<input style=\"display: inline;\" id=\"getFile\" type=\"file\" name=\"file\" />\n" +
                        "<button type=\"submit\" onclick=\"hideDropdown()\" style=\"display: inline;\"  value=\"Upload\" class=\"btn btn-primary mb-2\">Submit</button>" +
                        //"\t\t\t\t\t<input onclick=\"hideDropdown()\" style=\"display: inline;\" type=\"submit\" value=\"Upload\" />\n" +
                        "\t\t\t\t</form>"

                    dataArr.forEach(renderProductList);
                    showDropdownXls();
                    function downloadURI(name)
                    {
                        var link = document.createElement("a");
                        // If you don't know the name or want to use
                        // the webserver default set name = ''
                        link.setAttribute('download', name);
                        link.href = "/usr/local/user_files/";
                        document.body.appendChild(link);
                        link.click();
                        link.remove();
                    }

                    function renderProductList(element, index, arr) {
                        if (element !== ""){
                            let li = document.createElement('li');
                            li.setAttribute('class', 'item');
                            let hold = element;
                            file_list.appendChild(li);
                            li.innerHTML = li.innerHTML + element + '<input type="button" value="Select" onClick="selectXls(\'' + element + '\')" />'
                        }
                    }

                });
            //alert('The file upload with Ajax and Java was a success!');
        }

		function hideDropdown(){
			document.getElementById('dropdown-menu').style.display = "none";
		}
		async function hideDropdownAndDelete() {
			document.getElementById('dropdown-menu').style.display = "none";
		}

		function showDropdown(){
			let element = document.getElementById('dropdown-menu').style.display;
			if (element === "none"){
				document.getElementById('dropdown-menu').style.display = "block";
			}
			else {
				document.getElementById('dropdown-menu').style.display = "none";
			}
		}

        function hideDropdownPrawn(){
            document.getElementById('dropdown-menu-prawn').style.display = "none";
        }
        async function hideDropdownAndDelete() {
            document.getElementById('dropdown-menu-prawn').style.display = "none";
        }

        function showDropdownPrawn(){
            let element = document.getElementById('dropdown-menu-prawn').style.display;
            if (element === "none"){
                document.getElementById('dropdown-menu-prawn').style.display = "block";
            }
            else {
                document.getElementById('dropdown-menu-prawn').style.display = "none";
            }
        }


        async function getFilesPrawn() {
            const data = await fetch('fileuploadservlet', {
                method: "GET",
                body: null
            }).then(function(response) {return response.text();})
                .then(function(data) {
                    console.log(data); // this will be a string
                    console.log(typeof(data));
                    let dataArr = data.split("***");
                    console.log(dataArr);
                    let file_list = document.getElementById('dropdown-menu-prawn');
                    file_list.innerHTML = '';
                    file_list.innerHTML = file_list.innerHTML + "<form style=\"display: margin: 0; padding: 0;\" class=\"form-horizontal form-validate form-inline\" method=\"post\" action=\"fileuploadservlet\" enctype=\"multipart/form-data\">\n" +
                        //"\t\t\t\t\t<button style=\"display:inline;width:120px; height:30px;\" onclick=\"document.getElementById('getFile').click()\">Add File</button>\n" +
                        "\t\t\t\t\t<input style=\"display: inline;\" id=\"getFile\" type=\"file\" name=\"file\" />\n" +
                        "<button type=\"submit\" onclick=\"hideDropdown()\" style=\"display: inline;\"  value=\"Upload\" class=\"btn btn-primary mb-2\">Submit</button>" +
                        //"\t\t\t\t\t<input onclick=\"hideDropdown()\" style=\"display: inline;\" type=\"submit\" value=\"Upload\" />\n" +
                        "\t\t\t\t</form>"

                    dataArr.forEach(renderProductList);
                    showDropdownPrawn();
                    function downloadURI(name)
                    {
                        var link = document.createElement("a");
                        // If you don't know the name or want to use
                        // the webserver default set name = ''
                        link.setAttribute('download', name);
                        link.href = "/usr/local/user_files/";
                        document.body.appendChild(link);
                        link.click();
                        link.remove();
                    }

                    function renderProductList(element, index, arr) {
                        if (element !== ""){
                            let li = document.createElement('li');
                            li.setAttribute('class', 'item');
                            let hold = element;
                            file_list.appendChild(li);
                            li.innerHTML = li.innerHTML + element + '<input type="button" value="Select" onClick="selectPrawn(\'' + element + '\')" />'
                        }
                    }

                });
            //alert('The file upload with Ajax and Java was a success!');
        }

		async function getFiles() {
			const data = await fetch('fileuploadservlet', {
				method: "GET",
				body: null
			}).then(function(response) {return response.text();})
					.then(function(data) {
						console.log(data); // this will be a string
						console.log(typeof(data));
						let dataArr = data.split("***");
						console.log(dataArr);
						let file_list = document.getElementById('dropdown-menu');
						file_list.innerHTML = '';
						file_list.innerHTML = file_list.innerHTML + "<form style=\"display: margin: 0; padding: 0;\" class=\"form-horizontal form-validate form-inline\" method=\"post\" action=\"fileuploadservlet\" enctype=\"multipart/form-data\">\n" +
								//"\t\t\t\t\t<button style=\"display:inline;width:120px; height:30px;\" onclick=\"document.getElementById('getFile').click()\">Add File</button>\n" +
								"\t\t\t\t\t<input style=\"display: inline;\" id=\"getFile\" type=\"file\" name=\"file\" />\n" +
								"<button type=\"submit\" onclick=\"hideDropdown()\" style=\"display: inline;\"  value=\"Upload\" class=\"btn btn-primary mb-2\">Submit</button>" +
								//"\t\t\t\t\t<input onclick=\"hideDropdown()\" style=\"display: inline;\" type=\"submit\" value=\"Upload\" />\n" +
								"\t\t\t\t</form>"

						dataArr.forEach(renderProductList);
						showDropdown();
						function downloadURI(name)
						{
							var link = document.createElement("a");
							// If you don't know the name or want to use
							// the webserver default set name = ''
							link.setAttribute('download', name);
							link.href = "/usr/local/user_files/";
							document.body.appendChild(link);
							link.click();
							link.remove();
						}

						function renderProductList(element, index, arr) {
							if (element !== ""){
								let li = document.createElement('li');
								li.setAttribute('class', 'item');
								let hold = element;
								file_list.appendChild(li);
								li.innerHTML = li.innerHTML + element + '<input type="button" value="Delete" onClick="del(\'' + element + '\')" />' + "<button " + "onclick=\"location.href='/squid_servlet/download/usr/local/userfiles/" + hold + "'\" type=\"button\">Download</button> </a>";
							}
						}

					});
			//alert('The file upload with Ajax and Java was a success!');
		}
		async function del(filename) {
			console.log(filename)
			const data = await fetch('delete/usr/local/user_files/' + filename, {
				method: "POST",
				body: null
			}).then(function(response) {return response.text()}).then(function(data) {
				hideDropdown();
				console.log(data); // this will be a string
			})
	}
        async function clickActionDemo() {
		    const data = await fetch('clickServlet', {
		        method: "POST",
                body: null
            })
        }
        async function fileManager() {
			const data = await fetch('FileManagerServlet', {
				method: "POST",
				body: null
			})
		}
        async function clickActionGen() {
            const data = await fetch('reportsServlet', {
                method: "POST",
                body: null
            })
        }
        async function selectXls(filename) {
            xls = filename;
            console.log(xls);
            hideDropdownXls();
        }

        async function selectPrawn(filename) {
            prawn = filename;
            console.log(prawn);
            hideDropdownPrawn();
        }
        function submitFunc() {
		    let form = document.getElementById('squidForm');
		    let formData = new FormData(form);
		    formData.append("prawnFile", prawn);
		    formData.append("taskFile", xls);
            for (var value of formData.values()) {
                console.log(value);
            }
            sendSubmit(formData);
        }
        async function sendSubmit(form) {
		    const data = await fetch('squidServlet/submit', {
		        method: "POST",
                body: form
            }).then(function(response) {return response.text();}).then(function(data) {
                console.log(data);
            })
            //var oReq = new XMLHttpRequest();
            //oReq.setRequestHeader("Content-Type", "multipart/form-data")
            //oReq.open("POST", "squidServlet/servlet", true);
            //oReq.send(form);
            //oReq.onload = function() {
            //    console.log(oReq.response);
            //}
        }
        async function selectFileHelper(context, type) {
			if(context != undefined) {
			let bindingElement = document.getElementById('modal-content');
			let para = document.createElement("p");
			let div = document.createElement("div");
			let horizRule = document.createElement("hr");
			let button = document.createElement("button");
			let node = document.createTextNode(context);
			button.appendChild(document.createTextNode("SELECT"))
			if(context.charAt(0) == '?') {
			button.addEventListener('click', function(){
				selectFile(context);
			});
			}
			else {
				button.addEventListener('click', function(){
					sendFile(context, type);
				});
			}
			para.appendChild(node);
			para.style.display = "inline-block";
			button.style.textAlign = "right";
			button.style.clear = "left";
			div.style.whiteSpace= "nowrap"
			bindingElement.appendChild(div);
			div.appendChild(para);
			div.appendChild(button);
			bindingElement.appendChild(horizRule);
			}

		}
		async function selectFile(dir, type) {
			const data = await fetch('fileuploadservlet', {
				method: "POST",
				body: dir
			}).then(function(response) {return response.text();})
					.then(function(data) {
						console.log(data);
						console.log(typeof(data));
						//Split recieved files into individual components
						let dataArr = data.split("***");
						delete dataArr[dataArr.length - 1];
						console.log(dataArr);
						//Purge current list elements for updated elements
						let bindingElement = document.getElementById('modal-content');
						while(bindingElement.lastChild !== (document.getElementById('staticHR') || document.getElementById('staticClose') || document.getElementById('backBtn') || document.getElementById('defaultP') || document.getElementById('defaultDiv'))) {
							if(bindingElement.lastChild !== (document.getElementById('staticHR') || document.getElementById('staticClose') || document.getElementById('backBtn') || document.getElementById('defaultP') || document.getElementById('defaultDiv'))) {
							bindingElement.removeChild(bindingElement.lastChild);
							}
						}
						//For each component, send to helper to generate element per component
						dataArr.forEach(function (context) {
							selectFileHelper(context, type)
						})
					});
			//alert('The file upload with Ajax and Java was a success!');
		}
		async function fileManager(){
			setTimeout(function () { window.open("http://localhost:8090/files/") }, 1000);
		}
		async function logout() {
			const data = await fetch('filesenderservlet', {
				method: "POST",
				body: null
			}).then(function(response) {return response.text();}).then(function(data) {
				console.log(data); // this will be a string
		})}
		async function sendFile(context, type) {
			const data = await fetch('OpenServlet/' + type, {
				method: "POST",
				body: context
			}).then(function(response) {return response.text();}).then(function(data) {
				console.log(data); // this will be a string
			})
		};
		$( document ).ready(function() {
			bg = document.getElementById("initial-bg")
			jQuery('.DefaultState').fadeTo(500,0.2);
		});
		window.onload = function(){
			// Get the modal
			var modal = document.getElementById("myModal");

			// Get the button that opens the modal
			var btn = document.getElementById("openSquidProj");

			var geoBtn = document.getElementById("newGeochron");

			// Get the <span> element that closes the modal
			var span = document.getElementsByClassName("close")[0];

			// When the user clicks the button, open the modal
			btn.onclick = function() {
				modal.style.display = "block";
			}

			geoBtn.onclick = function() {
				modal.style.display = "block";
			}

			// When the user clicks on <span> (x), close the modal
			span.onclick = function() {
				modal.style.display = "none";
			}

			// When the user clicks anywhere outside of the modal, close it
			window.onclick = function(event) {
				if (event.target == modal) {
					modal.style.display = "none";
				}
			}
		};
	</script>
</head>
<body>
<!-- Upper Navbar -->
<div class="rownav navbar">
	<div class="dropdown">
		<button class="dropbtn">Project
			<i class="fa fa-caret-down"></i>
		</button>
		<div class="dropdown-content">
			<a class="DefaultState" href="#">Manage Project</a>
			<a id="newGeochron"class="dropdown-content-child" href="#">New Squid GEOCHRON Project</a>
			<a class="dropdown-content-child" href="#">New Squid RATIO Project &lt;BETA&gt; </a>
			<a id="openSquidProj"class="dropdown-content-child" href="#">Open Squid Project</a>
			<a class="dropdown-content-child" href="#">Open Recent Squid Project</a>
			<a onclick="clickActionDemo()" class="" href="#">Open Demonstration Squid Project</a>
			<a class="DefaultState" href="#">Save Squid Project</a>
			<a class="DefaultState" href="#">Save Squid Project as ...</a>
			<a class="DefaultState" href="#">Close Squid Project</a>
			<a class="DefaultState" href="#">Quit Squid</a>
		</div>
	</div>
	<div class="dropdown">
		<button class="dropbtn">Data
			<i class="fa fa-caret-down"></i>
		</button>
		<div class="dropdown-content">
			<a href="#">Link 1</a>
			<a href="#">Link 2</a>
			<a href="#">Link 3</a>
		</div>
	</div>
	<div class="dropdown">
		<button class="dropbtn">Task--&gt;
			<i class="fa fa-caret-down"></i>
		</button>
		<div class="dropdown-content">
			<a href="#">Link 1</a>
			<a href="#">Link 2</a>
			<a href="#">Link 3</a>
		</div>
	</div>
	<div class="dropdown">
		<button class="dropbtn">Isotopes
			<i class="fa fa-caret-down"></i>
		</button>
		<div class="dropdown-content">
			<a href="#">Link 1</a>
			<a href="#">Link 2</a>
			<a href="#">Link 3</a>
		</div>
	</div>
	<div class="dropdown">
		<button class="dropbtn">Expressions
			<i class="fa fa-caret-down"></i>
		</button>
		<div class="dropdown-content">
			<a href="#">Link 1</a>
			<a href="#">Link 2</a>
			<a href="#">Link 3</a>
		</div>
	</div>
	<div class="dropdown">
		<button class="dropbtn">Common Pb
			<i class="fa fa-caret-down"></i>
		</button>
		<div class="dropdown-content">
			<a href="#">Link 1</a>
			<a href="#">Link 2</a>
			<a href="#">Link 3</a>
		</div>
	</div>
	<div class="dropdown">
		<button class="dropbtn">Interpretations
			<i class="fa fa-caret-down"></i>
		</button>
		<div class="dropdown-content">
			<a href="#">Link 1</a>
			<a href="#">Link 2</a>
			<a href="#">Link 3</a>
		</div>
	</div>
	<div class="dropdown">
		<button class="dropbtn">Reports
			<i class="fa fa-caret-down"></i>
		</button>
		<div class="dropdown-content">
			<a href="#">Custom Report Builder</a>
			<a href="#">Summary Expressions and Values</a>
			<a href="#">Project Audit</a>
			<a href="#">Task Audit</a>
			<a onclick="clickActionGen()" href="#">Generate All Reports</a>
			<a href="#">Misc. Reports</a>
		</div>
	</div>
	<div class="dropdown">
		<button class="dropbtn">Archiving
			<i class="fa fa-caret-down"></i>
		</button>
		<div class="dropdown-content">
			<a href="#">Link 1</a>
			<a href="#">Link 2</a>
			<a href="#">Link 3</a>
		</div>
	</div>
	<div class="dropdown">
		<button class="dropbtn">Parameters
			<i class="fa fa-caret-down"></i>
		</button>
		<div class="dropdown-content">
			<a href="#">Link 1</a>
			<a href="#">Link 2</a>
			<a href="#">Link 3</a>
		</div>
	</div>
	<div class="dropdown">
		<button class="dropbtn">About
			<i class="fa fa-caret-down"></i>
		</button>
		<div class="dropdown-content">
			<a href="#">About Squid3</a>
			<a href="#">How to Cite Squid3</a>
			<a href="#">Squid3 Github Repository</a>
			<a href="#">Squid3 Development Notes</a>
			<a href="#">CIRDLES.org</a>
			<a href="#">Topsoil Github Repository</a>
			<a href="#">Enjoy!</a>
		</div>
	</div>
	<div class="dropdown">
		<button class="dropbtn">Help
			<i class="fa fa-caret-down"></i>
		</button>
		<div class="dropdown-content">
			<a href="#">Introduction and Guide to Menu</a>
			<a href="#">Video Tutorials</a>
			<a href="#">Contribute an Issue on Github</a>
		</div>
	</div>
	<div class="dropdown">
		<button class="dropbtn">My Files
			<i class="fa fa-caret-down"></i>
		</button>
		<div class="dropdown-content">
			<a href="#" onclick="fileManager()">File Manager</a>
		</div>
	</div>
</div>
</body>
<!--
<body>
<button
		onclick="logout()" class="btn btn-primary dropdown-toggle" type="button" data-toggle="dropdown">Logout
</button>
<button
    onclick="clickActionDemo()" class="btn btn-primary dropdown-toggle" type="button" data-toggle="dropdown">openDemonstrationSquid3Project
</button>
<button
        onclick="clickActionGen()" class="btn btn-primary dropdown-toggle" type="button" data-toggle="dropdown">generateAllSquid3ProjectReports
</button>
</body>
-->

<body id="initial-bg" style="background: #3A4B87 url('${pageContext.request.contextPath}/ImageServlet/SquidInk.svg') no-repeat fixed center center;">

<!-- The Modal -->
<div id="myModal" class="modal">

	<!-- Modal content -->
	<div id="modal-content" class="modal-content">
		<p style="text-align: center"id="defaultP">These functionalities are handled by right-click actions with the FileManager</p>
	</div>

</div>


<!--
<div class="container" style="min-width: 100%; min-height: 100%">
	<div style="z-index: 10" class="dropdown">
		<button onclick="getFiles()" class="btn btn-primary dropdown-toggle" type="button" data-toggle="dropdown">File Manager
			<span class="caret"></span></button>
		<ul id="dropdown-menu" style="display: none;" class="dropdown-menu pre-scrollable">
		</ul>
	</div>
	<div class="fileUpload" style=" border: 2px solid teal; min-width: 80%; min-height: 80%; background-color: lightyellow; display: inline-block; margin: 0; position: absolute; left: 50%; top: 50%; transform: translate(-50%, -50%)">
		<div class="prawnXML" style="display: inline-block; padding-left: 10%">
			<h4>Zipped Prawn XML File:</h4>
			<div style="z-index: 10" class="dropdown">
				<button onclick="getFilesPrawn()" class="btn btn-primary dropdown-toggle" type="button" data-toggle="dropdown">Select File
					<span class="caret"></span></button>
				<ul id="dropdown-menu-prawn" style="display: none;" class="dropdown-menu pre-scrollable">
				</ul>
			</div>

		</div>

        <div class="SquidXLS" style="display: inline-block; padding-left: 80px">
            <h4>Squid.* Task XLS File:</h4>
            <div style="z-index: 10" class="dropdown">
                <button onclick="getFilesXls()" class="btn btn-primary dropdown-toggle" type="button" data-toggle="dropdown">Select File
                    <span class="caret"></span></button>
                <ul id="dropdown-menu-xls" style="display: none;" class="dropdown-menu pre-scrollable">
                </ul>
            </div>

        </div>
        <form id="squidForm" name="squidForm">
            <div>
                <label for="RefMatSamp">
                    Reference Material Sample Name Filter:
                </label>
                <input type="text" id="RefMatSamp" name="RefMatSamp">
            </div>
            <div>
                <label for="ConcRefMatSamp">
                    Concentration Reference Material Sample Name Filter:
                </label>
                <input type="text" id="ConcRefMatSamp" name="ConcRefMatSamp">
            </div>
            <div>
                <label id="NormIonCounts">
                    Normalize Ion Counts for SBM?
                </label>
                <input type="radio" name="NormIonCounts" value="true">Yes<br>
                <input type="radio" name="NormIonCounts" value="false">No<br>
            </div>
            <div>
                <label id="UseLinFits">
                    Ratio Calculation Method:
                </label>
                <input type="radio" name="UseLinFits" value="true">Linear regression to burn mid-time<br>
                <input type="radio" name="UseLinFits" value="false">Spot average (time-invariant)<br>
            </div>
            <input type="button" id="submit" value="Submit" onclick="submitFunc()">
        </form>
        <div class="referenceInput" style="padding-left: 10%; padding-top: 5%;">
            <div>
                <form>
                    <table>
                        <tr>
                            <td align="right"><h4 style="display: inline-block">Reference Material Sample Name Filter: </h4></td>
                            <td align="left"><input type="text" name="first" /></td>
                        </tr>
                        <tr>
                            <td align="right"><h4 style="display: inline-block">Concentration Reference Material Sample Name Filter: </h4></td>
                            <td align="left"><input type="text" name="first" /></td>
                        </tr>

                    </table>
                </form>
            </div>
        </div>
                    <div class="countsAndCalculations" style="padding-top: 5%">
                        <div >
                            <form>
                                <table style="border-collapse: collapse; width: 100%;">
                                    <tr style="border-bottom: 2px solid teal; border-top: 2px solid teal">
                                        <td align="right"><h4 style="display: inline-block">Normalize Ion Counts for SBM? </h4></td>
                                        <td align="left" style="padding-left: 20px">
                                            <div style="display: list-item; list-style-type: none;">
                                                <button style="display: inline-block" class="w3-button w3-tiny w3-circle w3-teal">+</button>
                                                <h6 style="display: inline-block">Yes</h6>
                                            </div>
                                            <div style="display: list-item; list-style-type: none;">
                                                <button style="display: inline-block" class="w3-button w3-tiny w3-circle w3-teal">+</button>
                                                <h6 style="display: inline-block">No</h6>
                                            </div>
                                        </td>
                                    </tr>
                                    <tr>
                                        <td align="right"><h4 style="display: inline-block">Ratio Calculation Method: </h4></td>
                                        <div>
                                            <td align="left" style="padding-left: 20px">
                                                <div style="display: list-item; list-style-type: none;">
                                                    <button style="display: inline-block" class="w3-button w3-tiny w3-circle w3-teal">+</button>
                                                    <h6 style="display: inline-block">Linear regression to burn mid-time</h6>
                                                </div>
                                                <div style="display: list-item; list-style-type: none;">
                                                    <button style="display: inline-block" class="w3-button w3-tiny w3-circle w3-teal">+</button>
                                                    <h6 style="display: inline-block">Spot average (time-invariant)</h6>
                                                </div>
                                            </td>
                                        </div>
                                    </tr>
                                    <tr style="border-top: 2px solid teal">
                                        <td align="right"><h4 style="display: inline-block">Preferred Index Isotope: </h4></td>
                                        <td align="left" style="padding-left: 20px">
                                            <div style="display: list-item; list-style-type: none;">
                                                <button style="display: inline-block" class="w3-button w3-tiny w3-circle w3-teal">+</button>
                                                <h6 style="display: inline-block">204Pb</h6>
                                            </div>
                                            <div style="display: list-item; list-style-type: none;">
                                                <button style="display: inline-block" class="w3-button w3-tiny w3-circle w3-teal">+</button>
                                                <h6 style="display: inline-block">207Pb</h6>
                                            </div>
                                            <div style="display: list-item; list-style-type: none;">
                                                <button style="display: inline-block" class="w3-button w3-tiny w3-circle w3-teal">+</button>
                                                <h6 style="display: inline-block">207Pb</h6>
                                            </div>
                                        </td>
                                    </tr>

                                </table>
                            </form>
                        </div>
                    </div>
                </div>



                <div id="root"></div>
            </div>

-->
            </body>
			<div class="se-pre-con"></div>
            </html>


