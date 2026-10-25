<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Warehouse Stock</title>
    <style>
        :root {
            --bg-color: #FAEDED;      
            --text-main: #4A3F3F;     
            --dusty-rose: #C29591;    
            --border-color: #E8D8D8;  
            --white: #FFFFFF;
        }

        body {
            font-family: 'Inter', 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: var(--bg-color);
            color: var(--text-main);
            max-width: 900px;
            margin: 3rem auto;
            padding: 0 1.5rem;
            line-height: 1.6;
        }

        h1 {
            font-weight: 400;
            color: var(--text-main);
            margin-bottom: 1.5rem;
            border-bottom: 2px solid var(--border-color);
            padding-bottom: 1rem;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            background: var(--white);
            border-radius: 8px;
            overflow: hidden;
            box-shadow: 0 4px 6px rgba(0,0,0,0.02);
        }

        th, td {
            padding: 14px 20px;
            text-align: left;
            border-bottom: 1px solid var(--border-color);
        }

        th {
            background-color: var(--dusty-rose);
            color: var(--white);
            font-weight: 500;
            text-transform: uppercase;
            font-size: 0.85rem;
            letter-spacing: 0.05em;
        }

        tr:last-child td { border-bottom: none; }
        
        tr:hover {
            background-color: #fcf4f4;
        }
    </style>
</head>
<body>
    <h1>Warehouse Stock</h1>
    <table>
        <thead>
        <tr><th>Flower ID</th><th>Name</th><th>Quantity</th></tr>
        </thead>
        <tbody>
        <c:forEach var="flower" items="${stock}">
            <tr>
                <td><c:out value="${flower.flowerId}"/></td>
                <td><c:out value="${flower.name}"/></td>
                <td><c:out value="${flower.quantity}"/></td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</body>
</html>