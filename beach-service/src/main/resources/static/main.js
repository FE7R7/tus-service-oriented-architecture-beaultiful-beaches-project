const API_URL = '/beaches';
let currentETag = null;
let currentData = [];
let formMode = 'add'; // 'add' or 'edit'

// Fetch all beaches with ETag caching logic
function fetchBeaches() {
    const refreshBtn = document.getElementById('refreshBtn');
    const icon = refreshBtn.querySelector('i');

    // Animate spinner
    icon.classList.add('fa-spin');

    const headers = { 'Accept': 'application/json' };

    // Include the ETag if we have one from a previous request
    if (currentETag) {
        headers['If-None-Match'] = currentETag;
    }

    fetch(API_URL, { method: 'GET', headers: headers })
        .then(response => {
            // Stop spinner
            setTimeout(() => icon.classList.remove('fa-spin'), 300);

            // Handle the response caching
            if (response.status === 304) {
                console.log("304 Not Modified - Using cached data");
                showStatus(304, '304 Not Modified (Cached)');
                return null; // Return null to skip json processing because body is empty
            } else if (response.status === 200) {
                console.log("200 OK - Fetched fresh data");

                // Save new ETag
                const etagHeader = response.headers.get('ETag');
                if (etagHeader) {
                    currentETag = etagHeader;
                }
                showStatus(200, '200 OK (Fresh Data)');
                return response.json();
            } else {
                throw new Error("Unexpected status: " + response.status);
            }
        })
        .then(data => {
            if (data) {
                // Log raw API response for debugging country data structure
                console.log('API Response:', JSON.stringify(data[0], null, 2));
                currentData = data;
                renderTable(data);
            }
        })
        .catch(error => console.error('Error fetching beaches:', error));
}

// Render table with separate country columns
function renderTable(data) {
    const tbody = document.getElementById('tableBody');
    tbody.innerHTML = '';

    data.forEach(beach => {
        const tr = document.createElement('tr');
        tr.onclick = () => openModal('edit', beach);

        const beachId = beach.beachId || beach.id;
        const countryId = beach.country ? beach.country.countryId : (beach.countryId || '');
        const countryName = beach.country ? beach.country.name : '';
        const continent = beach.country ? beach.country.continent : '';
        const capital = beach.country ? beach.country.capital : '';

        tr.innerHTML = `
            <td style="color: var(--text-muted)">#${beachId}</td>
            <td style="font-weight: 600; color: white;">${beach.name}</td>
            <td>${beach.description}</td>
            <td>${beach.city || ''}</td>
            <td><span class="rating-chip"><i class="fa-solid fa-star" style="font-size: 0.8rem"></i> ${beach.rating}</span></td>
            <td><span style="color: var(--primary)">${countryId}</span></td>
            <td><span style="color: var(--primary)">${countryName}</span></td>
            <td><span style="color: var(--primary)">${continent}</span></td>
            <td><span style="color: var(--primary)">${capital}</span></td>
        `;
        tbody.appendChild(tr);
    });
}

// Show status badge
function showStatus(code, msg) {
    const badge = document.getElementById('statusBadge');
    badge.className = 'status-badge show ' + (code === 200 ? 'status-200' : 'status-304');
    badge.innerHTML = code === 200
        ? `<i class="fa-solid fa-cloud-arrow-down"></i> ${msg}`
        : `<i class="fa-solid fa-bolt"></i> ${msg}`;

    // Hide badge after 4 seconds
    setTimeout(() => {
        badge.classList.remove('show');
    }, 4000);
}

// Modal handling
function openModal(mode, beach = null) {
    formMode = mode;
    const modalTitle = document.getElementById('modalTitle');

    if (mode === 'add') {
        modalTitle.textContent = 'Add New Beach';
        document.getElementById('beachForm').reset();
        document.getElementById('formId').value = '';
    } else {
        modalTitle.textContent = 'Edit Beach';
        document.getElementById('formId').value = beach.beachId || beach.id;
        document.getElementById('formName').value = beach.name;
        document.getElementById('formDesc').value = beach.description;
        document.getElementById('formCity').value = beach.city || '';
        document.getElementById('formRating').value = beach.rating;
        document.getElementById('formCountryId').value = beach.countryId || (beach.country ? beach.country.countryId : '');
    }

    document.getElementById('modalOverlay').classList.add('active');
}

// Close modal
function closeModal() {
    document.getElementById('modalOverlay').classList.remove('active');
}

// Form Submission -> POST or PUT
function handleFormSubmit(e) {
    e.preventDefault();

    const id = document.getElementById('formId').value;
    const beachData = {
        name: document.getElementById('formName').value,
        description: document.getElementById('formDesc').value,
        city: document.getElementById('formCity').value,
        rating: parseFloat(document.getElementById('formRating').value),
        countryId: parseInt(document.getElementById('formCountryId').value)
    };

    const method = formMode === 'add' ? 'POST' : 'PUT';
    const url = formMode === 'add' ? API_URL : `${API_URL}/${id}`;

    fetch(url, {
        method: method,
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(beachData)
    })
        .then(response => {
            if (response.ok) {
                closeModal();
                const action = formMode === 'add' ? 'added' : 'updated';
                alert(`Beach ${action} successfully. Click Refresh Beaches to update the table.`);
            } else {
                alert('Error saving beach!');
            }
        })
        .catch(error => console.error('Error saving data:', error));
}

// Delete a beach by ID using the input field
function deleteBeachById() {
    const id = document.getElementById('idInput').value.trim();

    if (!id) {
        alert('Please enter a Beach ID to delete.');
        return;
    }

    if (!confirm(`Are you sure you want to delete Beach #${id}?`)) {
        return;
    }

    fetch(`${API_URL}/${id}`, { method: 'DELETE' })
        .then(response => {
            if (response.ok || response.status === 204) {
                alert(`Beach #${id} deleted successfully. Click Refresh Beaches to update the table.`);
                // Table is NOT auto-refreshed so the user can manually click
                // Refresh Beaches to demonstrate ETag caching (200 vs 304).
            } else if (response.status === 404) {
                alert(`Beach with ID ${id} was not found.`);
            } else {
                alert('Error deleting beach!');
            }
        })
        .catch(error => console.error('Error deleting beach:', error));
}

// Initial fetch on load
document.addEventListener("DOMContentLoaded", () => {
    fetchBeaches();
});
