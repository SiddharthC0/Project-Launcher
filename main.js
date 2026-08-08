const container = document.querySelector(".container");

let currentPage = 0;

const pages = document.querySelectorAll(".screen");
const loader = document.getElementById("mc-loading");

// ------------------------------
// Helper Functions
// ------------------------------

function pauseAllVideos() {
    document.querySelectorAll("video").forEach(video => {
        video.pause();
    });
}

function playCurrentVideos() {
    const videos = pages[currentPage].querySelectorAll("video");

    videos.forEach(video => {
        video.play().catch(() => {});
    });
}

function optimizePages() {

    pages.forEach((page, index) => {

        if (index === currentPage) {

            page.style.visibility = "visible";
            page.style.pointerEvents = "auto";

        } else {

            page.style.visibility = "hidden";
            page.style.pointerEvents = "none";

        }

    });

}

// ------------------------------
// Page Change
// ------------------------------

function changePage(targetPage) {

    loader.style.opacity = "1";
    loader.style.pointerEvents = "all";

    pauseAllVideos();

    setTimeout(() => {

        currentPage = targetPage;

        optimizePages();

        pages[currentPage].style.visibility = "visible";

        pages[currentPage].scrollIntoView({
            behavior: "instant"
        });

        playCurrentVideos();

        setTimeout(() => {

            loader.style.opacity = "0";
            loader.style.pointerEvents = "none";

        }, 350);

    }, 200);

}

// ------------------------------
// Buttons
// ------------------------------

function next() {

    if (currentPage < pages.length - 1) {

        changePage(currentPage + 1);

    }

}

function back() {

    if (currentPage > 0) {

        changePage(currentPage - 1);

    }

}

function goTo(section) {

    const target = document.getElementById(section);

    if (!target) return;

    const targetIndex = [...pages].indexOf(target);

    if (targetIndex !== -1) {

        changePage(targetIndex);

    }

}

// ------------------------------
// Initial Optimization
// ------------------------------

window.addEventListener("load", () => {

    optimizePages();

    playCurrentVideos();

    loader.style.opacity = "0";
    loader.style.pointerEvents = "none";

});

// ------------------------------
// Background Music
// ------------------------------

const music = document.getElementById("bg-music");

if (music) {

    music.volume = 0.25;

    music.play().catch(() => {

        document.addEventListener("click", () => {

            music.play();

        }, { once: true });

    });

}

const portalLayer=document.querySelector(".portal-particles");

for(let i=0;i<100;i++){

    const particle=document.createElement("div");

    particle.className="portal-particle";

    const size=1+Math.random()*4;

    particle.style.width=size+"px";
    particle.style.height=size+"px";

    particle.style.left=Math.random()*100+"vw";
    particle.style.animationDuration=(3+Math.random()*6)+"s";
    particle.style.animationDelay=Math.random()*5+"s";
    particle.style.setProperty("--drift",(Math.random()*100-50)+"px");

    portalLayer.appendChild(particle);

}

const themeColors = [
    "#AAFF42", // Electric Yellow
    "#FC7D32", // Inferno Orange
    "#32ACFC", // Nebula
    "#32FC79", // Neon Green
    "#F478FF", // Hyper Pink
    "#F73636", // Crimson Red
    "#A855FF"  // UV
];


let themeIndex = 0;


setInterval(()=>{

    themeIndex++;

    if(themeIndex >= themeColors.length)
        themeIndex = 0;


    document.documentElement.style.setProperty(
        "--theme-color",
        themeColors[themeIndex]
    );


},3000);