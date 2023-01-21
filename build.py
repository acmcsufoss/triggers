import os

try:
    os.remove('triggers.jar')
except OSError:
    pass

os.system('gradle build')
os.system('docker build -t triggers .')
