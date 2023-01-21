import os

# Remove old jar
try:
    for file in os.listdir("."):
        if file.startswith('triggers') and file.endswith('.jar'):
            os.remove(file)
            break
except OSError:
    pass

# Build jar
os.system('gradle build')

version = None

# Get version number and rename jar
for file in os.listdir("."):
    if file.startswith('triggers') and file.endswith('.jar'):
        version = file.replace('triggers-', '').replace('.jar', '')
        os.system(f'mv {file} triggers.jar')
        break

# Build Docker image with tag
os.system(f'docker build -t triggers:{version} .')
