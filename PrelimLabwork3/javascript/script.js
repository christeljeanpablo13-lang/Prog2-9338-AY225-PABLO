const btn = document.getElementById("calcBtn");

const attendance = document.getElementById("attendance");
const lab1 = document.getElementById("lab1");
const lab2 = document.getElementById("lab2");
const lab3 = document.getElementById("lab3");
const output = document.getElementById("output");
const progressBar = document.getElementById("progress-bar");

const clickSound = document.getElementById("clickSound");
const errorSound = document.getElementById("errorSound");

clickSound.volume = 0.4;
errorSound.volume = 0.6;

btn.addEventListener("click", () => {

  const a = parseFloat(attendance.value);
  const l1 = parseFloat(lab1.value);
  const l2 = parseFloat(lab2.value);
  const l3 = parseFloat(lab3.value);

  if (![a, l1, l2, l3].every(v => !isNaN(v) && v >= 0 && v <= 100)) {
    errorSound.currentTime = 0;
    errorSound.play();
    output.value = "❌ INPUT ERROR\nAll values must be between 0 and 100.";
    shakeCard();
    return;
  }

  clickSound.currentTime = 0;
  clickSound.play();

  const labAvg = (l1 + l2 + l3) / 3;
  const classStanding = (a * 0.4) + (labAvg * 0.6);
  const requiredExam = (75 - (classStanding * 0.7)) / 0.3;

  let remark = "Needs Review";
  if (classStanding >= 90) remark = "🌟 Excellent";
  else if (classStanding >= 75) remark = "✅ Good";

  output.value =
`PRELIM GRADE ANALYSIS
-------------------------
Attendance Grade : ${a.toFixed(2)}%
Lab Work Average : ${labAvg.toFixed(2)}%
Class Standing   : ${classStanding.toFixed(2)}%

Required Prelim Exam:
${requiredExam.toFixed(2)}%

Final Remark: ${remark}
`;

  updateProgress(classStanding);

  if (requiredExam >= 0 && requiredExam <= 100) {
    launchConfetti();
  }
});

function updateProgress(value) {
  const rounded = Math.round(value);
  progressBar.style.width = rounded + "%";
  progressBar.textContent = rounded + "%";
  progressBar.style.background = rounded >= 75 ? "#2ecc71" : "#e74c3c";
}

function shakeCard() {
  const card = document.querySelector(".card");
  card.classList.add("shake");
  setTimeout(() => card.classList.remove("shake"), 400);
}

function launchConfetti() {
  for (let i = 0; i < 25; i++) {
    const c = document.createElement("div");
    c.className = "confetti";
    c.style.left = Math.random() * 100 + "vw";
    c.style.background = Math.random() > 0.5 ? "#ffd700" : "#800000";
    document.body.appendChild(c);
    setTimeout(() => c.remove(), 3000);
  }
}
