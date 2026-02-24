(() => {
    const TRANSITION_DURATION_MS = 260;
    const STORAGE_KEY = 'page-slide-direction';

    const shouldHandleLink = (link) => {
        if (!link || !link.href) return false;
        if (link.target && link.target !== '_self') return false;
        if (link.hasAttribute('download')) return false;

        const href = link.getAttribute('href') || '';
        if (!href || href.startsWith('#') || href.startsWith('javascript:')) return false;

        const url = new URL(link.href, window.location.href);
        return url.origin === window.location.origin;
    };

    const getDirection = (link) => {
        const navItems = Array.from(document.querySelectorAll('[data-transition-nav]'));
        if (!navItems.length) {
            return 'forward';
        }

        const clickedIndex = navItems.indexOf(link);
        const currentPath = window.location.pathname;
        const currentIndex = navItems.findIndex((item) => {
            const itemPath = new URL(item.href, window.location.origin).pathname;
            return itemPath === currentPath;
        });

        if (clickedIndex < 0 || currentIndex < 0 || clickedIndex === currentIndex) {
            return 'forward';
        }

        return clickedIndex > currentIndex ? 'forward' : 'backward';
    };

    const startLeaveAnimation = (direction) => {
        document.body.classList.remove('slide-enter-forward', 'slide-enter-backward');
        document.body.classList.add(direction === 'backward' ? 'slide-leave-backward' : 'slide-leave-forward');
    };

    document.addEventListener('click', (event) => {
        const link = event.target.closest('a');
        if (!shouldHandleLink(link)) return;
        if (event.metaKey || event.ctrlKey || event.shiftKey || event.altKey) return;

        const direction = getDirection(link);
        sessionStorage.setItem(STORAGE_KEY, direction);

        event.preventDefault();
        startLeaveAnimation(direction);

        window.setTimeout(() => {
            window.location.assign(link.href);
        }, TRANSITION_DURATION_MS);
    });

    window.addEventListener('pageshow', () => {
        const direction = sessionStorage.getItem(STORAGE_KEY) || 'forward';
        sessionStorage.removeItem(STORAGE_KEY);

        document.body.classList.remove('slide-leave-forward', 'slide-leave-backward');
        document.body.classList.add(direction === 'backward' ? 'slide-enter-backward' : 'slide-enter-forward');

        window.setTimeout(() => {
            document.body.classList.remove('slide-enter-forward', 'slide-enter-backward');
        }, TRANSITION_DURATION_MS);
    });
})();
