import os
import platform
import subprocess
import argparse

class bcolors:
    HEADER = '\033[95m'
    OKBLUE = '\033[94m'
    OKCYAN = '\033[96m'
    OKGREEN = '\033[92m'
    WARNING = '\033[93m'
    FAIL = '\033[91m'
    ENDC = '\033[0m'
    BOLD = '\033[1m'
    UNDERLINE = '\033[4m'

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

windows = platform.system() == 'Windows'

# Clean and build jar
if windows:
    os.system('.\\gradlew clean')
    os.system('.\\gradlew build')
else:
    os.system('./gradlew clean')
    os.system('./gradlew build')

version = None

# Select version number from build.gradle
with open('build.gradle', 'r') as f:
    for line in f:
        if 'version' in line:
            version = line.split('\'')[1].strip()

# Check if Docker should be built
if args.docker:
    
    if version is None or version == '':
        version = 'latest'
        
        print('\n' + bcolors.WARNING + '=' * 50)
        print(bcolors.BOLD + '[WARNING]\n' + bcolors.ENDC + bcolors.WARNING)
        print('Could not find version number in build.gradle')
        print(f'Defaulting to `{version}`')
        print('=' * 50 + bcolors.ENDC + '\n')
        
        decision = input('Continue? [y/n]: ').strip().lower()
        while decision != 'y' and decision != 'n':
            
            if decision == 'n':
                quit()
            elif decision == 'y':
                os.system(f'docker build -t triggers:{version} .')
                break
            else:
                decision = input()
                continue
    