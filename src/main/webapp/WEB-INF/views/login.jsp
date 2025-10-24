<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.0/css/all.min.css" 
          integrity="sha512-iecdLmaskl7CV087vwFh88xT240wLw7hU7+3A1c0T70d/p8a5/S628D+8g5E5s5t5T5A5p5p5A5p5p5A5p5p5A==" 
          crossorigin="anonymous" referrerpolicy="no-referrer" />
    <link rel="stylesheet" href="./assets/css/Login.css"> 
    <title>Iniciar Sesion</title>
</head>
<body>
    <div class="Contenedores">
        <img src="./Imagenes/01.jpg" alt="Imagen de bienvenida">
        <h1><ins>BIENVENIDO USUARIO</ins></h1>
        <h3>Inicia Sesion con tu Cuenta </h3>
        
        <form action="login" method="post"> 
            <div class="Contenedor-1">
                <input type="text" placeholder="CorreoElectronico" name="usuario" required>
                <i class="fa-solid fa-user"></i>
            </div>
            <br>
            <div class="Contenedor-2">
                <input type="password" placeholder="Contraseña" name="password" required>
                <i class="fa-solid fa-lock"></i>
            </div>
            <br>
            <div class="Contenido-Adentro">
                <label>
                    <input type="checkbox" name="Recordarme">
                    Recordarme 
                </label>
                <button type="submit">Iniciar Sesion</button>
                <a href="#">¿Te olvidaste tu Contraseña?</a>
            </div>
        </form>

        <p style="color:red; margin-top: 10px; font-weight: bold;">
            ${error}
        </p>

        <div class="Contenedores-Iconos">
            <h4>O Iniciar Sesión Con</h4>
            <div class="Contenedores-Grupales">
                <i class="fa-brands fa-facebook-f"></i>
                <i class="fa-brands fa-google"></i>
                <i class="fa-brands fa-x-twitter"></i>
            </div>
        </div>
    </div>
</body>
</html>