import http.server
import socketserver
import webbrowser
import os
import sys

# Ensure UTF-8 output on Windows consoles
if sys.platform == 'win32':
    try:
        sys.stdout.reconfigure(encoding='utf-8', errors='replace')
        sys.stderr.reconfigure(encoding='utf-8', errors='replace')
    except Exception:
        pass

PORT = 8080
DIRECTORY = os.path.join(os.path.dirname(os.path.abspath(__file__)), "app_preview")

class Handler(http.server.SimpleHTTPRequestHandler):
    def __init__(self, *args, **kwargs):
        super().__init__(*args, directory=DIRECTORY, **kwargs)

if __name__ == "__main__":
    os.chdir(DIRECTORY)
    print("================================================================")
    print(">> Uruchamianie Symulatora Aplikacji Smart CUS Wieliszew")
    print(f">> Serwer lokalny: http://localhost:{PORT}")
    print(f">> Dostęp w sieci LAN / na telefonie: http://192.168.1.152:{PORT}")
    print(f">> Folder zrodlowy: {DIRECTORY}")
    print("================================================================")
    
    # Try opening the web browser automatically
    try:
        webbrowser.open(f"http://localhost:{PORT}")
    except Exception as e:
        print(f"Nie udalo sie automatycznie otworzyc przegladarki: {e}")

    with socketserver.TCPServer(("", PORT), Handler) as httpd:
        print("Serwer dziala. Nacisnij Ctrl+C, aby zatrzymac.")
        try:
            httpd.serve_forever()
        except KeyboardInterrupt:
            print("\nZatrzymano serwer preview.")
