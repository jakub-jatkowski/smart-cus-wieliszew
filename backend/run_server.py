import os
import sys
from pathlib import Path
import uvicorn

# Configure UTF-8 on Windows console to prevent UnicodeEncodeError with cp1250/cp852
if sys.platform.startswith("win"):
    if sys.stdout and hasattr(sys.stdout, "reconfigure"):
        try:
            sys.stdout.reconfigure(encoding="utf-8")
        except Exception:
            pass
    if sys.stderr and hasattr(sys.stderr, "reconfigure"):
        try:
            sys.stderr.reconfigure(encoding="utf-8")
        except Exception:
            pass

# Paths
CURRENT_DIR = Path(__file__).resolve().parent
PROJECT_ROOT = CURRENT_DIR.parent

# Add both current dir and project root to sys.path
for p in [str(CURRENT_DIR), str(PROJECT_ROOT)]:
    if p not in sys.path:
        sys.path.insert(0, p)

# Propagate PYTHONPATH to child processes spawned by uvicorn reloader
existing_pp = os.environ.get("PYTHONPATH", "")
os.environ["PYTHONPATH"] = os.pathsep.join(
    [str(CURRENT_DIR), str(PROJECT_ROOT)] + ([existing_pp] if existing_pp else [])
)

if __name__ == "__main__":
    print("==================================================")
    print("  STARTING SMART CUS WIELISZEW BACKEND SERVER     ")
    print("  REST API & SQLite Database Service              ")
    print("  Documentation: http://localhost:8000/docs       ")
    print("==================================================")

    # Run uvicorn pointing to main:app with app_dir set to CURRENT_DIR
    uvicorn.run(
        "main:app",
        host="0.0.0.0",
        port=8000,
        reload=True,
        app_dir=str(CURRENT_DIR)
    )
