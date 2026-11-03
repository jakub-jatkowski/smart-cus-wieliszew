import uvicorn
import os
import sys

if __name__ == "__main__":
    print("==================================================")
    print("🏛️  STARTING SMART CUS WIELISZEW BACKEND SERVER   ")
    print("    REST API & SQLite Database Service           ")
    print("    Documentation: http://localhost:8000/docs    ")
    print("==================================================")
    
    # Run uvicorn server on all network interfaces
    uvicorn.run("backend.main:app", host="0.0.0.0", port=8000, reload=True)

