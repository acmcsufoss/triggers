import os
import platform
import argparse

# Parse arguments
parser = argparse.ArgumentParser(
    description='Builds the project and creates a docker image',
    exit_on_error=True
)

parser.add_argument(
    '-d',
    '--docker',
    action='store_true',
    help='Builds the docker image'
)

args = parser.parse_args()

# Clean root jars
# Reason: `gradle clean` does not remove the jar files in the root directory
for file in os.listdir('.'):
    if file.endswith('.jar'):
        os.remove(file)

# Clean and build jar
if platform.system() == 'Windows':
    os.system('.\gradlew clean')
    os.system('.\gradlew build')
else:
    os.system('./gradlew clean')
    os.system('./gradlew build')

# Select version number from build.gradle
with open('build.gradle', 'r') as f:
    for line in f:
        if 'version' in line:
            version = line.split('\'')[1]

# Check if Docker should be built
if args.docker:
    
    # Copy jar file to root
    if platform.system() == 'Windows':
        os.system(f'COPY triggers-{version}-all.jar triggers.jar')
    else:
        os.system(f'cp triggers-{version}-all.jar ./triggers.jar')
    
    # Build docker image and remove temporary jar file
    os.system(f'docker build -t triggers:{version} .')
    os.system('rm triggers.jar')
