<%-- Directiva de página básica para asegurar el tipo de contenido y la codificación --%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <%-- Asegúrate de que esta URL de Font Awesome es correcta, la original parecía truncada --%>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.5.1/css/all.min.css" integrity="sha512-DTOQO9RWCH3ppGqcWaEA1BIZOC6xxalwEsw9c2QQeAIftl+Vegovlnee1c9QX4TctnWMn13TZye+giMm8e2LwA==" crossorigin="anonymous" referrerpolicy="no-referrer" />
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/Login.css">
    <title>Iniciar Sesion</title>
</head>
<body>
    
    <%-- 
        ¡Recomendación importante!
        En un JSP real para un formulario de inicio de sesión, el contenido de autenticación 
        debe estar envuelto en una etiqueta <form> para enviar datos. 
    --%>
    <div class="Contenedores">
        <img src="<%= request.getContextPath() %>/Imagenes/01.jpg" alt="Imagen de Bienvenida">
        <h1><ins>BIENVENIDO USUARIO</ins></h1>
        <h3>Inicia Sesion con tu Cuenta </h3>
        
        <%-- Aquí se recomienda usar la etiqueta <form> --%>
        <form action="<%= request.getContextPath() %>/login" method="POST"> 
            
            <div class="Contenedor-1">
                <input type="text" placeholder="Correo Electronico" name="usuario">
                <i class="fa-solid fa-user"></i>
            </div>
            <br>
            <div class="Contenedor-2">
                <input type="password" placeholder="Contraseña" name="password">
                <i class="fa-solid fa-lock"></i>
            </div>
            <br>
            <div class="Contenido-Adentro">
                <label>
                    <input type="checkbox" name="recordarme">
                    Recordarme 
                </label>
                
                <%-- 
                    Cambié el botón de aquí para que esté dentro del <form> y funcione 
                    correctamente con el envío de datos.
                --%>
                <button type="submit">Iniciar Sesion</button>
                
                <a href="#">¿Te olvidaste tu Contraseña?</a>
            </div>

        </form>
        
        <%-- El enlace a Dashboard.html debería hacerse después de la autenticación --%>
        
        <div class="Contenedores-Iconos">
            <h4>O Iniciar Sesión Con</h4>
            <div class="Contenedores-Grupales">
                <i class="fa-brands fa-facebook-f"></i>
                <i class="fa-brands fa-google"></i>
                <i class="fa-brands fa-x-twitter"></i>
            </div>
        </div>
    </div>
    
    <%-- Ejemplo de una expresión JSP para la fecha actual --%>
    <%-- <p>Página generada el: <%= new java.util.Date() %></p> --%>
</body>
</html>