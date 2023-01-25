import os
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

# Clean and build project
os.system('./gradlew clean')
os.system('./gradlew build')

# Select version number from build.gradle
with open('build.gradle', 'r') as f:
    for line in f:
        if 'version' in line:
            version = line.split('\'')[1]

# Check if Docker should be built
if args.docker:
    os.system(f'docker build -t triggers:{version} .')
