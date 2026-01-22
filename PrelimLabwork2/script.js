// Audio context and playBeep are defined once below to avoid redeclaration.

// ==========================
// 🔊 SOUND SETUP (TOP PART)
// ==========================

// Create beep sound programmatically
const audioContext = new (window.AudioContext || window.webkitAudioContext)();

function playBeep() {
    const oscillator = audioContext.createOscillator();
    const gainNode = audioContext.createGain();
    
    oscillator.connect(gainNode);
    gainNode.connect(audioContext.destination);
    
    oscillator.frequency.value = 800;
    oscillator.type = 'sine';
    
    gainNode.gain.setValueAtTime(0.3, audioContext.currentTime);
    gainNode.gain.exponentialRampToValueAtTime(0.01, audioContext.currentTime + 0.5);
    
    oscillator.start(audioContext.currentTime);
    oscillator.stop(audioContext.currentTime + 0.5);
}

// 🎵 MP3 SOUND FILES (DITO ILALAGAY)
const sounds = {
    loginSuccess: new Audio('sounds/tadaaa.mp3'),
    logIn: new Audio('sounds/ahh_gLSTOu4.mp3'),
    logOut: new Audio('sounds/kuya-natanggal.mp3'),
};

// Preload sounds
Object.values(sounds).forEach(sound => {
    sound.preload = 'auto';
    sound.volume = 0.7;
});

// Secure user data storage
const users = {
    'chin': { password: 'chin', fullName: 'Chin', course: 'BSIT_GD - 1st Year' },
    'cj': { password: 'cj', fullName: 'CJ', course: 'BSIT_GD - 1st Year' },
    'pablo': { password: 'sabog', fullName: 'Pablo', course: 'BSIT_GD - 1st Year' }
};

let currentUser = null;
let currentSessionLoggedIn = false;

// Get elements
const loginPage = document.getElementById('loginPage');
const dashboardPage = document.getElementById('dashboardPage');
const loginForm = document.getElementById('loginForm');
const errorMessage = document.getElementById('errorMessage');

// Create floating particles
function createParticles() {
    const particlesContainer = document.createElement('div');
    particlesContainer.className = 'particles';
    document.body.appendChild(particlesContainer);
    
    for (let i = 0; i < 20; i++) {
        const particle = document.createElement('div');
        particle.className = 'particle';
        particle.style.left = Math.random() * 100 + '%';
        particle.style.width = (Math.random() * 10 + 5) + 'px';
        particle.style.height = particle.style.width;
        particle.style.animationDelay = Math.random() * 15 + 's';
        particle.style.animationDuration = (Math.random() * 10 + 10) + 's';
        particlesContainer.appendChild(particle);
    }
}

// Initialize particles
createParticles();

// Login form submission
loginForm.addEventListener('submit', (e) => {
    e.preventDefault();
    const username = document.getElementById('username').value.trim();
    const password = document.getElementById('password').value;

    if (users[username] && users[username].password === password) {
        currentUser = username;
        
        // Show success overlay
        showSuccessOverlay('Login Successful!', 'Welcome to the system', () => {
            sounds.loginSuccess.play();
            setTimeout(() => {
                showDashboard();
            }, 2000);
        });
        
        errorMessage.classList.remove('show');
    } else {
        errorMessage.textContent = '❌ Invalid username or password. Please try again.';
        errorMessage.classList.add('show');
        playBeep();
        document.getElementById('password').value = '';
    }
});

function showSuccessOverlay(title, subtitle, callback) {
    // Create overlay
    const overlay = document.createElement('div');
    overlay.className = 'success-overlay show';
    
    const content = document.createElement('div');
    content.className = 'success-content';
    content.innerHTML = `
        <div class="success-icon">🎉</div>
        <div class="success-text">${title}</div>
        <div class="success-subtext">${subtitle}</div>
    `;
    
    overlay.appendChild(content);
    document.body.appendChild(overlay);
    
    if (callback) callback();
    
    setTimeout(() => {
        overlay.remove();
    }, 2000);
}

function showDashboard() {
    loginPage.classList.add('hidden');
    dashboardPage.classList.remove('hidden');
    
    const user = users[currentUser];
    document.getElementById('userAvatar').textContent = user.fullName.charAt(0).toUpperCase();
    document.getElementById('welcomeName').textContent = `Welcome, ${user.fullName}!`;
    document.getElementById('userSignature').textContent = generateSignature();
    
    loadRecords();
    currentSessionLoggedIn = false;
}

function generateSignature() {
    const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789';
    let signature = currentUser.toUpperCase() + '-';
    for (let i = 0; i < 8; i++) {
        signature += chars.charAt(Math.floor(Math.random() * chars.length));
    }
    return signature;
}

// Attendance actions with 3D effects
document.getElementById('logInBtn').addEventListener('click', function() {
    if (currentSessionLoggedIn) {
        showNotification('⚠️ You have already logged in for this session!', 'warning');
        return;
    }
    
    // Add 3D press effect
    this.style.transform = 'scale(0.95) translateY(2px)';
    setTimeout(() => {
        this.style.transform = '';
    }, 200);
    
    addRecord('Log In');
    currentSessionLoggedIn = true;
    
    // Play sound and show success
    sounds.logIn.play();
    showSuccessOverlay('Logged In!', 'Attendance recorded successfully', () => {
        setTimeout(() => {
            showNotification('✅ Successfully logged in!', 'success');
        }, 1500);
    });
});

document.getElementById('logOutBtn').addEventListener('click', function() {
    if (!currentSessionLoggedIn) {
        showNotification('⚠️ You need to log in first before logging out!', 'warning');
        return;
    }
    
    // Add 3D press effect
    this.style.transform = 'scale(0.95) translateY(2px)';
    setTimeout(() => {
        this.style.transform = '';
    }, 200);
    
    addRecord('Log Out');
    currentSessionLoggedIn = false;
    
    // Play sound and show success
    sounds.logOut.play();
    showSuccessOverlay('Logged Out!', 'See you next time', () => {
        setTimeout(() => {
            showNotification('✅ Successfully logged out!', 'success');
        }, 1500);
    });
});

function showNotification(message, type) {
    const notification = document.createElement('div');
    notification.style.cssText = `
        position: fixed;
        top: 20px;
        right: 20px;
        padding: 15px 25px;
        background: ${type === 'success' ? 'linear-gradient(135deg, #d4edda 0%, #c3e6cb 100%)' : 'linear-gradient(135deg, #fff3cd 0%, #ffeaa7 100%)'};
        color: ${type === 'success' ? '#155724' : '#856404'};
        border-radius: 12px;
        box-shadow: 0 10px 30px rgba(0, 0, 0, 0.2);
        z-index: 2000;
        font-weight: 600;
        animation: slideInRight 0.5s ease-out;
        border-left: 4px solid ${type === 'success' ? '#28a745' : '#ffc107'};
    `;
    notification.textContent = message;
    document.body.appendChild(notification);
    
    setTimeout(() => {
        notification.style.animation = 'slideOutRight 0.5s ease-in';
        setTimeout(() => notification.remove(), 500);
    }, 3000);
}

function addRecord(action) {
    const now = new Date();
    const date = now.toLocaleDateString('en-US', { 
        weekday: 'long',
        year: 'numeric', 
        month: 'long', 
        day: 'numeric' 
    });
    const time = now.toLocaleTimeString('en-US', { 
        hour: '2-digit', 
        minute: '2-digit', 
        second: '2-digit' 
    });
    
    const records = getRecords();
    records.push({ action, date, time, timestamp: now.getTime() });
    saveRecords(records);
    loadRecords();
}

function getRecords() {
    const stored = localStorage.getItem(`attendance_${currentUser}`);
    return stored ? JSON.parse(stored) : [];
}

function saveRecords(records) {
    localStorage.setItem(`attendance_${currentUser}`, JSON.stringify(records));
}

function loadRecords() {
    const records = getRecords();
    const tbody = document.getElementById('recordsBody');
    
    if (records.length === 0) {
        tbody.innerHTML = '<tr><td colspan="3" class="no-records">No attendance records yet</td></tr>';
        return;
    }

    tbody.innerHTML = records.map((record, index) => `
        <tr style="animation-delay: ${index * 0.1}s">
            <td><span class="status-badge ${record.action === 'Log In' ? 'status-in' : 'status-out'}">${record.action}</span></td>
            <td class="highlight">${record.date}</td>
            <td class="highlight">${record.time}</td>
        </tr>
    `).join('');
}

// Download records with enhanced formatting
document.getElementById('downloadBtn').addEventListener('click', function() {
    const records = getRecords();
    if (records.length === 0) {
        showNotification('⚠️ No records to download!', 'warning');
        return;
    }
    
    // Add 3D press effect
    this.style.transform = 'scale(0.95) translateY(2px)';
    setTimeout(() => {
        this.style.transform = '';
    }, 200);

    const user = users[currentUser];
    let content = `╔════════════════════════════════════════════════════════╗\n`;
    content += `║     PERPETUAL MOLINO - ATTENDANCE RECORDS              ║\n`;
    content += `╚════════════════════════════════════════════════════════╝\n\n`;
    content += `Student Name      : ${user.fullName}\n`;
    content += `Course            : ${user.course}\n`;
    content += `Digital Signature : ${document.getElementById('userSignature').textContent}\n`;
    content += `Generated         : ${new Date().toLocaleString()}\n`;
    content += `\n${'='.repeat(60)}\n`;
    content += `ATTENDANCE LOG\n`;
    content += `${'='.repeat(60)}\n\n`;

    records.forEach((record, index) => {
        content += `Record #${index + 1}\n`;
        content += `${'-'.repeat(60)}\n`;
        content += `Action : ${record.action}\n`;
        content += `Date   : ${record.date}\n`;
        content += `Time   : ${record.time}\n\n`;
    });

    content += `${'='.repeat(60)}\n`;
    content += `Total Records: ${records.length}\n`;
    content += `${'='.repeat(60)}\n\n`;
    content += `Thank you for using Perpetual Molino Attendance System!\n`;

    const blob = new Blob([content], { type: 'text/plain' });
    const link = document.createElement('a');
    link.href = URL.createObjectURL(blob);
    link.download = `Attendance_${currentUser}_${Date.now()}.txt`;
    link.click();
    
    showNotification('✅ Records downloaded successfully!', 'success');
});

// Clear history with confirmation
document.getElementById('clearBtn').addEventListener('click', function() {
    // Add 3D press effect
    this.style.transform = 'scale(0.95) translateY(2px)';
    setTimeout(() => {
        this.style.transform = '';
    }, 200);
    
    if (confirm('⚠️ Are you sure you want to clear all attendance records?\n\nThis action cannot be undone!')) {
        localStorage.removeItem(`attendance_${currentUser}`);
        loadRecords();
        currentSessionLoggedIn = false;
        showNotification('✅ Attendance history cleared successfully!', 'success');
    }
});

// Logout from system
document.getElementById('logoutBtn').addEventListener('click', function() {
    // Add 3D press effect
    this.style.transform = 'scale(0.95) translateY(2px)';
    setTimeout(() => {
        this.style.transform = '';
    }, 200);
    
    if (confirm('Are you sure you want to logout from the system?')) {
        currentUser = null;
        currentSessionLoggedIn = false;
        
        // Animate transition
        dashboardPage.style.animation = 'slideOut 0.5s ease-in';
        setTimeout(() => {
            loginPage.classList.remove('hidden');
            dashboardPage.classList.add('hidden');
            dashboardPage.style.animation = '';
            document.getElementById('username').value = '';
            document.getElementById('password').value = '';
        }, 500);
    }
});

// Add extra CSS animations
const style = document.createElement('style');
style.textContent = `
    @keyframes slideInRight {
        from { transform: translateX(100%); opacity: 0; }
        to { transform: translateX(0); opacity: 1; }
    }
    
    @keyframes slideOutRight {
        from { transform: translateX(0); opacity: 1; }
        to { transform: translateX(100%); opacity: 0; }
    }
    
    @keyframes slideOut {
        from { transform: translateY(0); opacity: 1; }
        to { transform: translateY(50px); opacity: 0; }
    }
`;
document.head.appendChild(style);