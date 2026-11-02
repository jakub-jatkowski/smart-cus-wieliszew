import http.server
import socketserver
import webbrowser
import os
import sys
import socket

# Ensure UTF-8 output on Windows consoles
if sys.platform == 'win32':
    try:
        sys.stdout.reconfigure(encoding='utf-8', errors='replace')
        sys.stderr.reconfigure(encoding='utf-8', errors='replace')
    except Exception:
        pass

PORT = 8080
DIRECTORY = os.path.join(os.path.dirname(os.path.abspath(__file__)), "app_preview")

def get_lan_ip():
    """Detect actual primary LAN IP of the host machine."""
    try:
        s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        s.settimeout(0.5)
        # Doesn't have to be reachable, just triggers OS routing table lookup
        s.connect(('10.254.254.254', 1))
        ip = s.getsockname()[0]
        s.close()
        return ip
    except Exception:
        try:
            return socket.gethostbyname(socket.gethostname())
        except Exception:
            return "127.0.0.1"

class Handler(http.server.SimpleHTTPRequestHandler):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, directory=DIRECTORY, **kwargs)

if __name__ == "__main__":
    lan_ip = get_lan_ip()
    os.chdir(DIRECTORY)
    print("==================================================================")
    print(">> 🏛️  Smart CUS Wieliszew - Serwer Aplikacji i Symulatora")
    print(f">> Dostęp lokalny na komputerze:   http://localhost:{PORT}")
    print(f">> Dostęp w sieci Wi-Fi (telefon): http://{lan_ip}:{PORT}")
    print(f">> Folder źródłowy PWA:            {DIRECTORY}")
    print("==================================================================")
    print(">> Wskazówka: Otwórz powyższy adres na smartfonie i wybierz")
    print(">> 'Dodaj do ekranu głównego', aby zainstalować aplikację PWA.")
    print("==================================================================")
    
    # Try opening the web browser automatically
    try:
        webbrowser.open(f"http://localhost:{PORT}")
    except Exception as e:
        print(f"Nie udało się automatycznie otworzyć przeglądarki: {e}")

    with socketserver.TCPServer(("", PORT), Handler) as httpd:
        print("Serwer działa. Naciśnij Ctrl+C, aby zatrzymać.\n")
        try:
            httpd.serve_forever()
        except KeyboardInterrupt:
            print("\nZatrzymano serwer Smart CUS.")
