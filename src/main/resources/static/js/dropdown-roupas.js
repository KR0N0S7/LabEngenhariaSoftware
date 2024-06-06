
document.addEventListener('DOMContentLoaded', function () {
    const dropdownToggles = document.querySelectorAll('.dropdown-toggle');

    dropdownToggles.forEach(toggle => {
        toggle.addEventListener('click', function (event) {
            event.preventDefault();
            const submenu = this.nextElementSibling;
            submenu.classList.toggle('show');
        });
    });

    document.addEventListener('click', function (event) {
        const dropdowns = document.querySelectorAll('.submenu');
        dropdowns.forEach(dropdown => {
            if (!dropdown.parentElement.contains(event.target) && dropdown.classList.contains('show')) {
                dropdown.classList.remove('show');
            }
        });
    });
});

