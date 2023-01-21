import os

version = None

try:
    for file in os.listdir("."):
        if file.startswith('triggers') and file.endswith('.jar'):
            os.remove(file)
            break
except OSError:
    pass

os.system('gradle build')

for file in os.listdir("."):
    if file.startswith('triggers') and file.endswith('.jar'):
        version = file.replace('triggers-', '').replace('.jar', '')
        os.system(f'mv {file} triggers.jar')
        break

os.system(f'docker build -t triggers:{version} .')
