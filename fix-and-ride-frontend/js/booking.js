import CONFIG from "./config.js";
import { initAuth } from './features/auth.js';

// Service data 
const services = {
  "car-labor": {
    id: "car-labor",
    name: "Car + Labor",
    icon: "🚗",
    description: "I drive my own car and help you move.",
    duration: 60, // minutes
    basePrice: 50 // fallback
  },
  "labor-transporter": {
    id: "labor-transporter",
    name: "Labor + Transporter",
    icon: "🚚",
    description: "I drive a hired transporter.",
    duration: 120,
    basePrice: 80
  },
  "labor-only": {
    id: "labor-only",
    name: "Labor Only",
    icon: "💪",
    description: "Heavy lifting, loading/unloading.",
    duration: 60,
    basePrice: 40
  },
  "repairs": {
    id: "repairs",
    name: "Repairs",
    icon: "🔧",
    description: "Fixing furniture, plumbing, etc.",
    duration: 45,
    basePrice: 45
  },
  "tool-lending": {
    id: "tool-lending",
    name: "Tool Lending",
    icon: "🧰",
    description: "Rent professional tools.",
    duration: 1440, // 1 day
    basePrice: 20
  },
  "taxi": {
    id: "taxi",
    name: "Taxi Service",
    icon: "🚕",
    description: "Emergency pickup or drop-off.",
    duration: 30,
    basePrice: 30
  }
};

// State
let state = {
  selectedServiceId: new URLSearchParams(window.location.search).get('service') || "car-labor",
  selectedDate: null, // "YYYY-MM-DD"
  selectedTime: null, // "HH:MM"
  viewDate: new Date(),
  user: null
};

// Mocks
const bookedSlots = {
  "2025-08-10": ["10:00", "11:00"],
};

document.addEventListener('DOMContentLoaded', async () => {
  // DOM Elements
  const DOM = {
    serviceList: document.getElementById('service-list'),
    calendarBody: document.getElementById('calendar-body'),
    monthYear: document.getElementById('month-year'),
    prevMonth: document.getElementById('prev-month'),
    nextMonth: document.getElementById('next-month'),
    timeSlots: document.getElementById('time-slots'),
    summaryService: document.getElementById('summary-service'),
    summaryDate: document.getElementById('summary-date'),
    summaryTime: document.getElementById('summary-time'),
    summaryCost: document.getElementById('summary-cost'),
    confirmBtn: document.getElementById('confirm-btn'),
    // Auth
    loginBtn: document.getElementById('login-btn'),
    authModal: document.getElementById('auth-modal'),
    closeAuth: document.getElementById('close-auth'),
    tabLogin: document.getElementById('tab-login'),
    tabSignup: document.getElementById('tab-signup'),
  };

  // --- Initialization ---
  renderServices();
  renderCalendar();
  renderTimeSlots();

  // Init Auth
  const auth = initAuth(DOM, { apiBase: `${CONFIG.BASE_URL}/api/user/auth` });
  await auth.init();
  state.user = auth.getUser();


  // --- Event Listeners ---
  DOM.prevMonth.addEventListener('click', () => {
    state.viewDate.setMonth(state.viewDate.getMonth() - 1);
    renderCalendar();
  });

  DOM.nextMonth.addEventListener('click', () => {
    state.viewDate.setMonth(state.viewDate.getMonth() + 1);
    renderCalendar();
  });

  DOM.confirmBtn.addEventListener('click', handleBookingConfirm);


  // --- Functions ---

  function renderServices() {
    DOM.serviceList.innerHTML = '';
    Object.values(services).forEach(service => {
      const el = document.createElement('div');
      el.className = `service-item ${service.id === state.selectedServiceId ? 'selected' : ''}`;
      el.innerHTML = `
          <div class="flex items-center gap-sm">
            <span style="font-size: 1.5rem;">${service.icon}</span>
            <div>
              <div style="font-weight: 600;">${service.name}</div>
              <div style="font-size: 0.8rem; color: var(--text-muted);">${service.duration} min</div>
            </div>
          </div>
        `;
      el.addEventListener('click', () => {
        state.selectedServiceId = service.id;
        renderServices();
        updateSummary();
      });
      DOM.serviceList.appendChild(el);
    });
  }

  function renderCalendar() {
    DOM.calendarBody.innerHTML = '';
    const year = state.viewDate.getFullYear();
    const month = state.viewDate.getMonth();

    DOM.monthYear.textContent = new Date(year, month).toLocaleString('default', { month: 'long', year: 'numeric' });

    const firstDay = new Date(year, month, 1).getDay(); // 0 = Sun
    const daysInMonth = new Date(year, month + 1, 0).getDate();

    // Adjust for Monday start (Mon=0, Sun=6)
    const startOffset = (firstDay + 6) % 7;

    let day = 1;
    for (let r = 0; r < 6; r++) {
      const tr = document.createElement('tr');
      for (let c = 0; c < 7; c++) {
        const td = document.createElement('td');
        if (r === 0 && c < startOffset || day > daysInMonth) {
          td.className = 'empty';
        } else {
          const iso = `${year}-${String(month + 1).padStart(2, '0')}-${String(day).padStart(2, '0')}`;
          td.textContent = day;

          if (iso === state.selectedDate) td.classList.add('selected');

          // Check if passed
          if (new Date(iso) < new Date().setHours(0, 0, 0, 0)) {
            td.classList.add('booked'); // Disable past dates
          } else {
            td.addEventListener('click', () => {
              state.selectedDate = iso;
              // state.selectedTime = null; // Optional: Reset time on date change
              renderCalendar(); // Re-render to show selection
              renderTimeSlots();
              updateSummary();
            });
          }
          day++;
        }
        tr.appendChild(td);
      }
      DOM.calendarBody.appendChild(tr);
      if (day > daysInMonth) break;
    }
  }

  function renderTimeSlots() {
    DOM.timeSlots.innerHTML = '';

    if (!state.selectedDate) {
      DOM.timeSlots.innerHTML = '<li style="grid-column: 1/-1; text-align: center; color: var(--text-muted); padding: 20px;">Select a date first</li>';
      return;
    }

    // Generate slots
    const interval = 30; // minutes
    const booked = bookedSlots[state.selectedDate] || [];

    for (let h = 9; h < 18; h++) { // Business hours 9-18
      for (let m = 0; m < 60; m += interval) {
        const timeStr = `${String(h).padStart(2, '0')}:${String(m).padStart(2, '0')}`;

        const li = document.createElement('li');
        li.className = 'time-slot-item';
        li.textContent = timeStr;

        if (booked.includes(timeStr)) {
          li.classList.add('disabled');
        } else if (state.selectedTime === timeStr) {
          li.classList.add('selected');
        } else {
          li.addEventListener('click', () => {
            state.selectedTime = timeStr;
            renderTimeSlots(); // Re-render to show selection
            updateSummary();
          });
        }
        DOM.timeSlots.appendChild(li);
      }
    }
  }

  // --- API & Pricing ---
  async function getServicePrice(serviceId) {
    try {
      const res = await fetch(`${CONFIG.BASE_URL}/api/user/service/${serviceId}/price`, {
        method: "GET", credentials: "include", mode: "cors"
      });
      if (!res.ok) throw new Error("Failed");
      const data = await res.json();
      return Number(data.price);
    } catch (err) { return services[serviceId]?.basePrice || 0; }
  }

  async function updateSummary() {
    const service = services[state.selectedServiceId];

    // Update Service Name
    if (DOM.summaryService) {
      DOM.summaryService.textContent = service.name;
    }

    DOM.summaryDate.textContent = state.selectedDate ? new Date(state.selectedDate).toDateString() : 'Select a Date';

    // Time & Duration
    if (state.selectedTime) {
      const start = state.selectedTime;
      const [h, m] = start.split(':').map(Number);
      const endDateObj = new Date();
      endDateObj.setHours(h, m + service.duration);
      const end = `${String(endDateObj.getHours()).padStart(2, '0')}:${String(endDateObj.getMinutes()).padStart(2, '0')}`;

      DOM.summaryTime.textContent = `${start} - ${end}`;
    } else {
      DOM.summaryTime.textContent = '--:--';
    }

    // Cost Calculation
    let hourlyRate = await getServicePrice(service.id);
    // Cost = (duration_minutes / 60) * hourly_rate
    const totalCost = (service.duration / 60) * hourlyRate;

    DOM.summaryCost.textContent = `€${totalCost.toFixed(2)}`;
    DOM.confirmBtn.disabled = !(state.selectedServiceId && state.selectedDate && state.selectedTime);
  }

  async function handleBookingConfirm() {
    if (!state.user) { alert("Please login."); DOM.loginBtn.click(); return; }

    const service = services[state.selectedServiceId];

    // Calculate End Time for payload logic (simple derivation)
    const [h, m] = state.selectedTime.split(':').map(Number);
    let endH = h + Math.floor(service.duration / 60);
    let endM = m + (service.duration % 60);
    if (endM >= 60) { endH++; endM -= 60; }
    const endTimeStr = `${String(endH).padStart(2, '0')}:${String(endM).padStart(2, '0')}`;

    const payload = {
      serviceId: service.id,
      serviceName: service.name,
      userEmail: state.user.email,
      startDate: state.selectedDate,
      startTime: state.selectedTime,
      endDate: state.selectedDate,
      endTime: endTimeStr,
      cost: DOM.summaryCost.textContent.replace('€', '')
    };

    try {
      const response = await fetch(`${CONFIG.BASE_URL}/api/user/service/confirm-booking`, {
        method: "POST", headers: { "Content-Type": "application/json" },
        body: JSON.stringify(payload)
      });
      if (!response.ok) throw new Error("Failed to book");
      alert("Booking Confirmed!");
      window.location.href = 'index.html';
    } catch (e) { alert(e.message); }
  }
});
