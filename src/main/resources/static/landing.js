const inputAlert = document.getElementById("navInput");

inputAlert.addEventListener("click", function () {
  alert("기능이 아직 구현되지 않았습니다!");
});

window.addEventListener("resize", function () {
  const phone = document.getElementById("phone-img");
  const maxHeight = (74 * this.window.innerHeight) / 100;

  if (phone.clientHeight > maxHeight) {
    phone.style.height = maxHeight + "px";
  }
});

window.addEventListener("resize", function () {
  const banner = document.getElementById("main-banner");
  const maxHeight = (20 * this.window.innerHeight) / 100;

  if (banner.clientHeight > maxHeight) {
    banner.style.display = 'none';
  }
});

const toggleBtn = document.querySelector('.toggle-btn');
const menu = document.querySelector('.nav-menu');

toggleBtn.addEventListener('click', () => {
        menu.classList.toggle('active');
});
