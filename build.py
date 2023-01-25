import os

# Clean and build project
os.system('./gradlew clean')
os.system('./gradlew build')

# Select version number from build.gradle
with open('build.gradle', 'r') as f:
    for line in f:
        if 'version' in line:
            version = line.split('\'')[1]

os.system(f'docker build -t triggers:{version} .')
