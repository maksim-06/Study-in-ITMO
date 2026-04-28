<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.example.Point" %>
<%@ page import="java.util.List" %>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Results - First Lab</title>
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

            #result-table {
                width: 100%;
                border-collapse: collapse;
                margin-top: 20px;
            }

            #result-table th, #result-table td {
                border: 1px solid #555;
                padding: 10px;
                text-align: center;
            }

            #result-table th {
                background-color: #333;
                color: #e6c20d;
            }

            #result-table td {
                background-color: #333;
            }

            .hit {
                color: green;
                font-weight: bold;
            }

            .miss {
                color: red;
                font-weight: bold;
            }
        </style>
    </head>

    <body>
        <main class="input-section">

            <h2 style="color: #ae09ff; text-align: center;">Results History</h2>

            <div style="max-height: 400px; overflow-y: auto; width: 100%">
                <table id="result-table" style="width: 100%; table-layout: fixed">
                    <tr>
                        <th style="width: 20%">isShoot</th>
                        <th style="width: 20%">x</th>
                        <th style="width: 20%">y</th>
                        <th style="width: 20%">r</th>
                        <th style="width: 20%">time</th>
                        <th style="width: 20%">execution time</th>
                    </tr>
                    <%
                    List<Point> results = (List<Point>) application.getAttribute("results");
                    if (results != null) {
                        for (Point point : results) {
                    %>
                    <tr>
                        <td class="<%= point.getIsShoot().equals("Попал") ? "hit" : "miss" %>">
                            <%= point.getIsShoot() %>
                        </td>
                        <td><%= point.getX() %></td>
                        <td style="
                            word-wrap: break-word;
                            word-break: break-word;
                            overflow-wrap: break-word;
                            max-width: 200px;">
                            <%= point.getY() %>
                        </td>
                        <td><%= point.getR() %></td>
                        <td><%= point.getTime() %></td>
                        <td><%= point.getExectime() %></td>
                    </tr>
                    <%
                        }
                    }
                    %>
                </table>
            </div>

            <a href="${pageContext.request.contextPath}/controller" style="display: inline-block; margin: 20px; padding: 10px 20px; background: #007bff; color: white; text-decoration: none; border-radius: 4px;">
                Back to Form
            </a>

        </main>
    </body>
</html>