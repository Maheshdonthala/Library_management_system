// script.js - Library System UI helpers
console.log('Library System JS loaded');

document.addEventListener('DOMContentLoaded', function () {
	// Toast helper
	function showToast(message, type = 'info') {
		const toast = document.createElement('div');
		toast.className = 'ls-toast ls-toast-' + type;
		toast.textContent = message;
		Object.assign(toast.style, {
			position: 'fixed',
			right: '20px',
			bottom: '20px',
			background: type === 'error' ? '#dc2626' : (type === 'success' ? '#16a34a' : '#2563eb'),
			color: '#fff',
			padding: '10px 14px',
			borderRadius: '6px',
			boxShadow: '0 6px 18px rgba(0,0,0,0.08)',
			zIndex: 9999,
		});
		document.body.appendChild(toast);
		setTimeout(() => { toast.style.opacity = '0'; toast.addEventListener('transitionend', () => toast.remove()); }, 3000);
	}

	// Add-book form validation (works if form has id="add-book-form")
	const addBookForm = document.querySelector('#add-book-form') || document.querySelector('form[action="/add-book"]');
	if (addBookForm) {
		addBookForm.addEventListener('submit', function (e) {
			const title = addBookForm.querySelector('[name=title]');
			const author = addBookForm.querySelector('[name=author]');
			let valid = true;
			if (title && title.value.trim() === '') {
				valid = false;
				showToast('Please enter a title', 'error');
				title.focus();
			} else if (author && author.value.trim() === '') {
				valid = false;
				showToast('Please enter an author', 'error');
				author.focus();
			}
			if (!valid) e.preventDefault();
		});
	}

	// Table search/filter (works if an input with id="search" exists and a table is present)
	const searchInput = document.querySelector('#search');
	const booksTable = document.querySelector('table');
	if (searchInput && booksTable) {
		const tbody = booksTable.tBodies[0];
		const rows = Array.from(tbody.rows);
		const noResultsRow = document.createElement('tr');
		noResultsRow.className = 'empty-row';
		noResultsRow.innerHTML = '<td colspan="3" class="empty-state">No books found</td>';

		searchInput.addEventListener('input', function () {
			const q = searchInput.value.trim().toLowerCase();
			let visible = 0;
			rows.forEach(r => {
				const text = r.textContent.toLowerCase();
				const match = text.indexOf(q) !== -1;
				r.style.display = match ? '' : 'none';
				if (match) visible++;
			});
			if (visible === 0) {
				if (!tbody.querySelector('.empty-row')) tbody.appendChild(noResultsRow);
			} else {
				const er = tbody.querySelector('.empty-row');
				if (er) er.remove();
			}
		});
	}

	// Row hover/click enhancements for tables
	if (booksTable) {
		booksTable.addEventListener('mouseover', (e) => {
			const tr = e.target.closest('tr');
			if (tr && tr.parentElement.tagName.toLowerCase() === 'tbody') tr.style.background = '#fbfcfe';
		});
		booksTable.addEventListener('mouseout', (e) => {
			const tr = e.target.closest('tr');
			if (tr && tr.parentElement.tagName.toLowerCase() === 'tbody') tr.style.background = '';
		});
	}

	// Delete confirmation for forms with class .delete-form
	const deleteForms = document.querySelectorAll('.delete-form');
	deleteForms.forEach(form => {
		form.addEventListener('submit', function (e) {
			if (!confirm('Are you sure you want to delete this book? This action cannot be undone.')) {
				e.preventDefault();
			}
		});
	});

});

// End of script.js
