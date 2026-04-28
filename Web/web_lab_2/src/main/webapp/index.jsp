<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.example.Point" %>
<%@ page import="java.util.List" %>
<%@ page import="jakarta.servlet.ServletContext" %>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>First Lab</title>
        <style>
            html {
                height: 100%;
            }

            body {
                min-height: 100vh;
                margin: 0;
                padding: 0;
                font-family: monospace;
                font-size: 16px;
                background-color: #1e1e1e;
                color: #f0f0f0;
            }

            .navbar {
                padding: 1%;
            }

            .navbar .number {
                color: #ffd500;
            }

            .navbar .var {
                color: #ae09ff;
            }

            #info {
                font-size: 16px;
            }


            .button-group {
                display: flex;
                flex-wrap: wrap;
                gap: 8px;
                margin: 10px 0;
            }


            .x-btn, .r-btn {
                border: 1px solid #444;
                border-radius: 4px;
                background-color: #333;
                color: #f0f0f0;
                padding: 8px 15px;
                cursor: pointer;
            }


            .x-btn.selected, .r-btn.selected {
                background-color: #007bff;
                color: white;
                border-color: #0056b3;
            }


            #y {
                border: 1px solid #444;
                border-radius: 4px;
                background-color: #333;
                color: #f0f0f0;
                padding: 8px;
                width: 200px;
                margin: 5px 0;
            }


            #y:focus {
                border-color: #ae09ff;
            }


            #submit-btn {
                border: 1px solid #444;
                border-radius: 4px;
                background-color: #333;
                color: #f0f0f0;
                padding: 10px 20px;
                cursor: pointer;
                margin-top: 15px;
            }

            #submit-btn:active {
                background-color: #26c648;
            }



            #graph {
                border: 1px solid #444;
                border-radius: 4px;
            }


            #err {
                color: #FF6961;
                padding: 1%;
                margin-top: 10px;
            }
        </style>
    </head>

    <body>
        <nav class="navbar">
            <div id="info">
                Nekrutenko Maksim
                <br>
                P3106
                <br>
                <span class="var">v. <span class="number">466853</span></span>
            </div>
        </nav>

        <main class="input-section">
            <div class="input">
                <form method="get" id="data-form" action="${pageContext.request.contextPath}/controller">
                    <input type="hidden" id="x-value" name="x">
                    <input type="hidden" id="r-value" name="r">

                    <label>Select X coordinate:</label>
                    <div class="button-group" id="x-buttons">
                        <%
                        int[] xValues = {-3, -2, -1, 0, 1, 2, 3, 4, 5};
                        for (int xVal : xValues) {
                        %>
                            <button type="button" class="x-btn" data-value="<%= xVal %>"><%= xVal %></button>
                        <% } %>
                    </div>


                    <label for="y">Enter Y coordinate (-5 ... 5):</label>
                    <input type="text" id="y" name="y" required
                        placeholder="Enter number from -5 to 5">

                    <br>

                    <label>Select Radius R:</label>
                    <div class="button-group" id="r-buttons">
                        <%
                        String[] rValues = {"1", "1.5", "2", "2.5", "3"};
                        for (String rVal : rValues) {
                        %>
                            <button type="button" class="r-btn" data-value="<%= rVal %>"><%= rVal %></button>
                        <% } %>
                    </div>

                    <button type="submit" id="submit-btn">Check Point</button>
                </form>

                <br>

                <div id="err" style="color: red;" hidden></div>
            </div>

            <div class="result">

                <svg id="graph" width="300" height="300" style="background: black; border: 1px solid #ccc; margin: 10px 0;">
                    <rect x="150" y="50" width="100" height="100" style="fill: rgba(51,153,255,0.2)" />
                    <polygon points="150,150 100,150 150,50" fill="rgba(51,153,255,0.2)" />
                    <path d="M200,150 A50,50 0 0,1 150,200 L150,150 L200,150 Z" fill="rgba(51,153,255,0.2)" />

                    <line x1="150" y1="0" x2="150" y2="300" stroke="white" stroke-width="1" />
                    <line x1="0" y1="150" x2="300" y2="150" stroke="white" stroke-width="1" />

                    <text x="156" y="144" fill="white" font-family="monospace" font-size="12">0</text>
                    <text x="194" y="144" fill="white" font-family="monospace" font-size="12">R/2</text>
                    <text x="244" y="144" fill="white" font-family="monospace" font-size="12">R</text>
                    <text x="106" y="144" fill="white" font-family="monospace" font-size="12">-R/2</text>
                    <text x="42" y="144" fill="white" font-family="monospace" font-size="12">-R</text>
                    <text x="156" y="106" fill="white" font-family="monospace" font-size="12">R/2</text>
                    <text x="156" y="56" fill="white" font-family="monospace" font-size="12">R</text>
                    <text x="156" y="194" fill="white" font-family="monospace" font-size="12">-R/2</text>
                    <text x="156" y="244" fill="white" font-family="monospace" font-size="12">-R</text>
                </svg>
            </div>
        </main>


        <script>

            const state = {
                x: null,
                y: null,
                r: null,
            };

            const possibleXs = new Set([-3,-2,-1,0,1,2,3,4,5]);
            const possibleRs = new Set([1,1.5,2,2.5,3]);

            let points = [];



            document.querySelectorAll(".x-btn").forEach(button => {
                button.addEventListener("click", function() {
                    document.querySelectorAll(".x-btn").forEach(btn=>{
                        btn.classList.remove("selected");
                    });

                    this.classList.add("selected");
                    state.x = this.getAttribute("data-value");
                    document.getElementById("x-value").value = state.x;
                });
            });


            document.querySelectorAll(".r-btn").forEach(button => {
                button.addEventListener("click", function() {
                    document.querySelectorAll(".r-btn").forEach(btn => {
                        btn.classList.remove("selected");
                    });

                    this.classList.add("selected");
                    state.r = this.getAttribute("data-value");
                    document.getElementById("r-value").value = state.r;
                    drawGraph();
                });
            });


            const yInput = document.getElementById("y");
            if (yInput) {
                yInput.addEventListener("change", (ev) => {
                    state.y = ev.target.value;
                });
            }

            const validate = (state) => {
                if (!state.x || !possibleXs.has(parseFloat(state.x))) {
                    alert('Please select X coordinate');
                    return false;
                }

                if (!state.r || !possibleRs.has(parseFloat(state.r))) {
                    alert('Please select R value');
                    return false;
                }

                const yNum = parseFloat(state.y);
                if (isNaN(yNum) || yNum<-5 || yNum>5) {
                    alert('Y must be a number between -5 and 5');
                    return false;
                }
                return true;
            }


            const dataForm = document.getElementById("data-form");
            if (dataForm) {
                dataForm.addEventListener("submit", function(event) {
                    if (!validate(state)) {
                        event.preventDefault();
                    }
                });
            }


            const graph = document.getElementById("graph");
            if (graph) {
                graph.addEventListener("click", function (event) {
                    if (!state.r) {
                        alert("To determine the coordinates of a point, you need to set the radius R");
                        return;
                    }

                    const rect = graph.getBoundingClientRect();
                    const clickX = event.clientX - rect.left;
                    const clickY = event.clientY - rect.top;

                    const mathX = ((clickX-150) / 100) * parseFloat(state.r);
                    const mathY = ((150-clickY) / 100) * parseFloat(state.r);


                    const closestX = findClosestX(mathX);

                    setFormValues(closestX,mathY);

                    submitForm();
                });
            }


            function findClosestX(mathX) {
                const possibleX = [-3, -2, -1, 0, 1, 2, 3, 4, 5];
                let closest = possibleX[0];
                let minDiff = Math.abs(mathX - closest);

                for (let i = 1; i<possibleX.length; i++) {
                    const diff = Math.abs(mathX-possibleX[i]);
                    if (diff<minDiff){
                        minDiff = diff;
                        closest = possibleX[i];
                    }
                }
                return closest;
            }



            function setFormValues(x, y) {
                document.querySelectorAll(".x-btn").forEach(btn => {
                    btn.classList.remove("selected");
                    if (parseFloat(btn.getAttribute("data-value")) === x) {
                        btn.classList.add("selected");
                    }
                });
                state.x = x.toString();
                document.getElementById("x-value").value = state.x;
                const limitedY = Math.max(-5,Math.min(5,y));
                document.getElementById("y").value = limitedY;
                state.y = limitedY.toString();
            }

            function submitForm() {
                if (validate(state)) {
                    document.getElementById("data-form").submit();
                }
            }

            document.addEventListener("DOMContentLoaded", function() {
                loadExistingPoints();
            });


            function loadExistingPoints() {
                <%
                List<Point> results = (List<Point>) application.getAttribute("results");
                if (results != null) {
                    for (Point point : results) {
                %>
                        points.push({
                            x: <%= point.getX() %>,
                            y: <%= point.getY() %>,
                            r: <%= point.getR() %>,
                            isHit: "<%= point.getIsShoot() %>"
                        });
                <%
                    }
                }
                %>
                drawGraph();
            }

            function drawGraph() {
                document.querySelectorAll('.graph-point').forEach(point=>{
                    point.remove();
                });

                points.forEach(point => {
                    addPointToGraph(point.x, point.y, point.r, point.isHit);
                });

                updateLabels();
            }


            function updateLabels() {
                const currentR = state.r ? parseFloat(state.r) : 1;
                const halfR = (currentR / 2).toFixed(1);


                const numHalfR = parseFloat(halfR);
                const numCurrentR = parseFloat(currentR);

                document.querySelector('text[x="194"]').textContent = halfR;
                document.querySelector('text[x="244"]').textContent = currentR;
                document.querySelector('text[x="106"]').textContent = -numHalfR;
                document.querySelector('text[x="42"]').textContent = -numCurrentR;

                document.querySelector('text[y="106"]').textContent = halfR;
                document.querySelector('text[y="56"]').textContent = currentR;
                document.querySelector('text[y="194"]').textContent = -numHalfR;
                document.querySelector('text[y="244"]').textContent = -numCurrentR;
            }


            function addPointToGraph(x, y, r, isHit) {
                const currentR = state.r ? parseFloat(state.r) : 1;

                const svgX = 150 + (x/currentR) * 100;
                const svgY = 150 - (y/currentR) * 100;

                const point = document.createElementNS('http://www.w3.org/2000/svg', 'circle');
                point.setAttribute("cx", svgX);
                point.setAttribute("cy", svgY);
                point.setAttribute("r", 3);
                point.setAttribute('fill', '#888888');
                point.setAttribute('stroke', '#ffffff');
                point.setAttribute('stroke-width', 1);
                point.setAttribute('class', 'graph-point');

                const graph = document.getElementById("graph");
                graph.appendChild(point);
            }
        </script>
    </body>
</html>