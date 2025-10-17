<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
    <link rel="stylesheet" href="../assets/css/estilos.css">
</head>
<body>
    <h2>Iniciar sesión</h2>

    <form action="login" method="post">
        <label>Usuario:</label><br>
        <input type="text" name="usuario" required><br><br>

        <label>Contraseña:</label><br>
        <input type="password" name="password" required><br><br>

        <button type="submit">Entrar</button>
    </form>

    <p style="color:red;">
        ${error}
    </p>
</body>
</html>
