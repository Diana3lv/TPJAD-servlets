<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Flower Shop Stock</title>
    <style>
        :root {
            --bg-color: #FAEDED;      
            --text-main: #4A3F3F;     
            --dusty-rose: #C29591;    
            --dusty-hover: #A87C78;   
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

        h1, h2 {
            font-weight: 400;
            color: var(--text-main);
            margin-bottom: 1.5rem;
        }

        h2 {
            font-size: 1.25rem;
            margin-top: 2rem;
            border-bottom: 1px solid var(--border-color);
            padding-bottom: 0.5rem;
        }

        .message {
            background-color: var(--white);
            border-left: 4px solid var(--dusty-rose);
            padding: 1rem;
            border-radius: 4px;
            margin: 1.5rem 0;
            box-shadow: 0 2px 4px rgba(0,0,0,0.02);
        }

        table {
            width: 100%;
            border-collapse: collapse;
            background: var(--white);
            border-radius: 8px;
            overflow: hidden;
            box-shadow: 0 4px 6px rgba(0,0,0,0.02);
            margin-bottom: 2rem;
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
            background-color: #fcf4f4; /* slight hover effect for better reading */
        }

        /* New class for just the text */
        .status-low {
            color: #dc2626; /* A clear, standard red (not rosy) */
            font-weight: 700;
        }

        form {
            background: var(--white);
            padding: 1.5rem;
            border-radius: 8px;
            box-shadow: 0 4px 6px rgba(0,0,0,0.02);
            display: flex;
            align-items: center;
            gap: 1rem;
            flex-wrap: wrap;
            margin-bottom: 1.5rem;
        }

        label {
            font-weight: 500;
            font-size: 0.9rem;
            color: var(--text-main);
        }

        select, input {
            padding: 10px 14px;
            border: 1px solid var(--border-color);
            border-radius: 6px;
            background-color: var(--bg-color);
            color: var(--text-main);
            outline: none;
            transition: border-color 0.2s;
            font-family: inherit;
        }

        select:focus, input:focus {
            border-color: var(--dusty-rose);
            background-color: var(--white);
        }

        button {
            background-color: var(--dusty-rose);
            color: var(--white);
            border: none;
            padding: 10px 20px;
            border-radius: 6px;
            cursor: pointer;
            font-weight: 500;
            transition: all 0.2s ease;
            font-family: inherit;
        }

        button:hover {
            background-color: var(--dusty-hover);
            transform: translateY(-1px);
        }
    </style>
</head>
<body>
    <h1>Flower Shop Stock</h1>
    <c:if test="${not empty message}">
        <p class="message"><c:out value="${message}"/></p>
    </c:if>

    <table>
        <thead>
        <tr><th>Flower ID</th><th>Name</th><th>Quantity</th><th>State</th></tr>
        </thead>
        <tbody>
        <c:forEach var="flower" items="${stock}">
            <!-- Removed the conditional row class entirely -->
            <tr>
                <td><c:out value="${flower.flowerId}"/></td>
                <td><c:out value="${flower.name}"/></td>
                <td><c:out value="${flower.quantity}"/></td>
                <td>
                    <!-- Wrapped the LOW text in a span with the new red text class -->
                    <c:if test="${flower.quantity lt lowStockThreshold}">
                        <span class="status-low">LOW</span>
                    </c:if>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <h2>Request stock from warehouse</h2>
    <form method="post">
        <input type="hidden" name="action" value="request">
        <label for="request-flower">Flower</label>
        <select id="request-flower" name="flowerId" required>
            <c:forEach var="flower" items="${stock}">
                <option value="${flower.flowerId}"><c:out value="${flower.name}"/></option>
            </c:forEach>
        </select>
        <label for="request-quantity">Quantity</label>
        <input id="request-quantity" type="number" name="quantity" min="1" required>
        <button type="submit">Request stock</button>
    </form>

    <h2>Decrease shop stock</h2>
    <form method="post">
        <input type="hidden" name="action" value="decrease">
        <label for="decrease-flower">Flower</label>
        <select id="decrease-flower" name="flowerId" required>
            <c:forEach var="flower" items="${stock}">
                <option value="${flower.flowerId}"><c:out value="${flower.name}"/></option>
            </c:forEach>
        </select>
        <label for="decrease-quantity">Quantity</label>
        <input id="decrease-quantity" type="number" name="quantity" min="1" required>
        <button type="submit">Decrease stock</button>
    </form>
</body>
</html>