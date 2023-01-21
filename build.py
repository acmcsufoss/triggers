import os

try:
    os.remove('triggers.jar')
except OSError:
    pass

os.system('gradle build')
os.system('gradle docker')
