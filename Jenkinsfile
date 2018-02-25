String version = ''
String gitCommit = ''
String branchName = ''
String projectName = 'ontrack'

pipeline {

    agent {
        dockerfile {
            label "docker"
            args "--volume /var/run/docker.sock:/var/run/docker.sock"
        }
    }

    options {
        // General Jenkins job properties
        buildDiscarder(logRotator(numToKeepStr: '40'))
        // Timestamps
        timestamps()
    }

    stages {

        stage('Setup') {
            steps {
                script {
                    branchName = ontrackBranchName(BRANCH_NAME)
                    echo "Ontrack branch name = ${branchName}"
                }
                ontrackBranchSetup(project: projectName, branch: branchName, script: """
                    branch.config {
                        gitBranch '${branchName}', [
                            buildCommitLink: [
                                id: 'git-commit-property'
                            ]
                        ]
                    }
                """)
            }
        }

        stage('Build') {
            steps {
                sh '''\
git checkout -B ${BRANCH_NAME}
git clean -xfd
'''
                sh '''\
./gradlew \\
    clean \\
    versionDisplay \\
    versionFile \\
    test \\
    build \\
    integrationTest \\
    publishToMavenLocal \\
    dockerLatest \\
    -Pdocumentation \\
    -PbowerOptions='--allow-root' \\
    -Dorg.gradle.jvmargs=-Xmx2048m \\
    --stacktrace \\
    --profile \\
    --console plain
'''
                script {
                    // Reads version information
                    def props = readProperties(file: 'build/version.properties')
                    version = props.VERSION_DISPLAY
                    gitCommit = props.VERSION_COMMIT
                }
                sh """\
echo "(*) Building the test extension..."
cd ontrack-extension-test
./gradlew \\
    clean \\
    build \\
    -PontrackVersion=${version} \\
    -PbowerOptions='--allow-root' \\
    -Dorg.gradle.jvmargs=-Xmx2048m \\
    --stacktrace \\
    --profile \\
    --console plain
"""
            }
            post {
                success {
                    ontrackBuild(project: projectName, branch: branchName, build: version, gitCommit: gitCommit)
                    stash name: "delivery", includes: "build/distributions/ontrack-*-delivery.zip"
                }
            }
        }

        stage('Local acceptance tests') {
            steps {
                // Runs the acceptance tests
                timeout(time: 25, unit: 'MINUTES') {
                    sh """\
echo "Launching environment..."
cd ontrack-acceptance/src/main/compose
docker-compose up -d ontrack selenium
"""
                    sh """\
echo "Launching tests..."
cd ontrack-acceptance/src/main/compose
docker-compose up ontrack_acceptance
"""
                }
            }
            post {
                always {
                    sh """\
#!/bin/bash
set -e
echo "Cleanup..."
mkdir -p build
cp -r ontrack-acceptance/src/main/compose/build build/acceptance
cd ontrack-acceptance/src/main/compose
docker-compose down --volumes
"""
                    archiveArtifacts 'build/acceptance/**'
                    junit 'build/acceptance/*.xml'
                    ontrackValidate(
                            project: projectName,
                            branch: branchName,
                            build: version,
                            validationStamp: 'ACCEPTANCE',
                            buildResult: currentBuild.result,
                    )
                }
            }
        }

        // Docker push
        stage('Docker publication') {
            environment {
                DOCKER_HUB = credentials("DOCKER_HUB")
                ONTRACK_VERSION = "${version}"
            }
            steps {
                // TODO Confirmation before going further (disabled for development)
                // timeout(time: 1, unit: 'HOURS') {
                //     input "Pushing version ${version} to the Docker Hub?"
                // }
                script {
                    sh '''\
#!/bin/bash
set -e
docker login --username ${DOCKER_HUB_USR} --password ${DOCKER_HUB_PSW}
docker push nemerosa/ontrack:${ONTRACK_VERSION}
docker push nemerosa/ontrack-acceptance:${ONTRACK_VERSION}
docker push nemerosa/ontrack-extension-test:${ONTRACK_VERSION}
'''
                }
            }
            post {
                always {
                    ontrackValidate(
                            project: projectName,
                            branch: branchName,
                            build: version,
                            validationStamp: 'DOCKER',
                            buildResult: currentBuild.result,
                    )
                }
            }
        }

        // OS tests + DO tests in parallel

        stage('Platform tests') {
            environment {
                ONTRACK_VERSION = "${version}"
            }
            parallel {
                // TODO CentOS7
                stage('CentOS7') {
                    when {
                        branch 'release/.*'
                    }
                    steps {
                        ontrackValidate(
                                project: projectName,
                                branch: branchName,
                                build: version,
                                validationStamp: 'ACCEPTANCE.CENTOS.7',
                                buildResult: currentBuild.result,
                        )
                    }
                }
                // TODO Debian
                stage('Debian') {
                    when {
                        branch 'release/.*'
                    }
                    steps {
                        ontrackValidate(
                                project: projectName,
                                branch: branchName,
                                build: version,
                                validationStamp: 'ACCEPTANCE.DEBIAN',
                                buildResult: currentBuild.result,
                        )
                    }
                }
                // Extension tests
                stage('Local extension tests') {
                    steps {
                        timeout(time: 25, unit: 'MINUTES') {
                            // Cleanup
                            sh """\
rm -rf ontrack-acceptance/src/main/compose/build
"""
                            // Launches the extension environment
                            sh """\
echo "Launching environment..."
cd ontrack-acceptance/src/main/compose
docker-compose --project-name ext --file docker-compose-ext.yml up -d ontrack selenium
"""
                            // Launches the tests
                            sh """\
echo "Launching tests..."
cd ontrack-acceptance/src/main/compose
docker-compose --project-name ext --file docker-compose-ext.yml up ontrack_acceptance
"""
                        }
                    }
                    post {
                        always {
                            sh """\
echo "Cleanup..."
mkdir -p build
cp -r ontrack-acceptance/src/main/compose/build build/acceptance
cd ontrack-acceptance/src/main/compose
docker-compose --project-name ext --file docker-compose-ext.yml down --volumes
"""
                            archiveArtifacts 'build/acceptance/**'
                            junit 'build/acceptance/*.xml'
                            ontrackValidate(
                                    project: projectName,
                                    branch: branchName,
                                    build: version,
                                    validationStamp: 'EXTENSIONS',
                                    buildResult: currentBuild.result,
                            )
                        }
                    }
                }
                // Digital Ocean
                stage('Digital Ocean') {
                    environment {
                        ONTRACK_VERSION = "${version}"
                        DROPLET_NAME = "ontrack-acceptance-${version}"
                        DO_TOKEN = credentials("DO_NEMEROSA_JENKINS2_BUILD")
                    }
                    steps {
                        timeout(time: 60, unit: 'MINUTES') {
                            sh '''\
#!/bin/bash

echo "(*) Cleanup..."
rm -rf ontrack-acceptance/src/main/compose/build

echo "(*) Removing any previous machine: ${DROPLET_NAME}..."
docker-machine rm --force ${DROPLET_NAME} > /dev/null

# Failing on first error from now on
set -e

echo "(*) Creating ${DROPLET_NAME} droplet..."
docker-machine create \\
    --driver=digitalocean \\
    --digitalocean-access-token=${DO_TOKEN} \\
    --digitalocean-image=docker \\
    --digitalocean-region=fra1 \\
    --digitalocean-size=1gb \\
    --digitalocean-backups=false \\
    ${DROPLET_NAME}

echo "(*) Gets ${DROPLET_NAME} droplet IP..."
DROPLET_IP=`docker-machine ip ${DROPLET_NAME}`
echo "Droplet IP = ${DROPLET_IP}"

echo "(*) Target Ontrack application..."
export ONTRACK_ACCEPTANCE_TARGET_URL="http://${DROPLET_IP}:8080"

echo "(*) Launching the remote Ontrack ecosystem..."
eval $(docker-machine env --shell bash ${DROPLET_NAME})
docker-compose \\
    --file ontrack-acceptance/src/main/compose/docker-compose-do-server.yml \\
    --project-name ontrack \\
    up -d

echo "(*) Launching the test environment locally..."
eval $(docker-machine env --shell bash --unset)
docker-compose \\
    --file ontrack-acceptance/src/main/compose/docker-compose-do-client.yml \\
    --project-name acceptance \\
    up -d selenium

echo "(*) Running the tests..."
docker-compose \\
    --file ontrack-acceptance/src/main/compose/docker-compose-do-client.yml \\
    --project-name acceptance \\
    up ontrack_acceptance

'''
                        }
                    }
                    post {
                        always {
                            sh '''\
#!/bin/bash

echo "(*) Copying the test results..."
mkdir -p build
cp -r ontrack-acceptance/src/main/compose/build build/acceptance

echo "(*) Removing the test environment..."
docker-compose \\
    --file ontrack-acceptance/src/main/compose/docker-compose-do-client.yml \\
    --project-name acceptance \\
    down

echo "(*) Removing any previous machine: ${DROPLET_NAME}..."
docker-machine rm --force ${DROPLET_NAME}
'''
                            archiveArtifacts 'build/acceptance/**'
                            junit 'build/acceptance/*.xml'
                            ontrackValidate(
                                    project: projectName,
                                    branch: branchName,
                                    build: version,
                                    validationStamp: 'ACCEPTANCE.DO',
                                    buildResult: currentBuild.result,
                            )
                        }
                    }
                }
            }
        }

        // Publication

        stage('Publication') {
            parallel {
                stage('Docker push') {
                    steps {
                        echo "TODO Docker push"
                    }
                }
                stage('Maven publication') {
                    steps {
                        echo "TODO Docker push"
                    }
                }
            }
        }

        // Release

        stage('Release') {
            when {
                branch 'experimental/pipeline-release'
                // FIXME branch 'release/*'
            }
            steps {
                echo "TODO Release"
            }
            post {
                success {
                    ontrackPromote(
                            project: projectName,
                            branch: branchName,
                            build: version,
                            promotionLevel: 'RELEASE',
                    )
                }
            }
        }

        // TODO Site
        // TODO Ontrack validation --> SITE
        // TODO Production
        // TODO Ontrack promotion --> ONTRACK
        // TODO Production tests
        // TODO Ontrack validation --> ACCEPTANCE.PRODUCTION

    }

    post {
        always {
            junit '**/build/test-results/**/*.xml'
        }
    }

}