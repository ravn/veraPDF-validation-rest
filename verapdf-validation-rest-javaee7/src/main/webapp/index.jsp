<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>VeraPDF validator</title>
</head>
<body>
<label for="url">URL for PDF file: </label>
<input type="text" id="url">
<p>Target:
    <a id="finalurl"></a>
</p>

<script>
    const baseUrl = "verapdf/validate/";

    const urlInput = document.querySelector("#url");
    const link = document.querySelector("#finalurl");

    urlInput.addEventListener("input", function (event) {
        link.innerHTML = baseUrl + encodeURIComponent(event.target.value);
        link.href = baseUrl + encodeURIComponent(event.target.value);
    });

    // Dispatch an input event on render, to display the finalurl field
    urlInput.dispatchEvent(
        new Event("input", {
            bubbles: true,
            cancelable: true
        })
    );
</script>

</body>
</html>
