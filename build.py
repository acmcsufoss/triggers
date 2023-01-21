import os

try:
    os.remove('triggers-1.0-SNAPSHOT-all.jar')
except OSError:
    pass

os.system('gradle build')
os.system('gradle docker')
