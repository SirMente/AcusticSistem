document.addEventListener('DOMContentLoaded', function() {
    const menuIcon = document.getElementById('menu-icon');
    const navbar = document.querySelector('.navbar'); 
    
    menuIcon.addEventListener('click', () => {
        
        navbar.classList.toggle('open');
        
        if (navbar.classList.contains('open')) {
            menuIcon.classList.remove('bx-menu');
            menuIcon.classList.add('bx-x'); 
        } else {
            menuIcon.classList.remove('bx-x');
            menuIcon.classList.add('bx-menu'); 
        }
    });

    const navLinks = document.querySelectorAll('.navbar a');
    
    navLinks.forEach(link => {
        link.addEventListener('click', () => {
            if (navbar.classList.contains('open')) {
                navbar.classList.remove('open');
                menuIcon.classList.remove('bx-x');
                menuIcon.classList.add('bx-menu');
            }
        });
    });
});