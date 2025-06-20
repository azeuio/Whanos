folder('Whanos base images') {
  description('Folder base images jobs.')
}

folder('Projects') {
  description('Folder base images jobs.')
}

freeStyleJob('Whanos base images/whanos-befunge') {
    description('Build Docker Image triggered by GitHub push')

    steps {
        shell('docker build -t whanos-befunge-base -f /usr/local/images/befunge/Dockerfile.base .')
    }
    triggers {
        upstream('Whanos base images/Build all base images', 'SUCCESS')
    }
}

freeStyleJob('Whanos base images/whanos-c') {
    description('Build Docker Image triggered by GitHub push')

    steps {
        shell('docker build -t whanos-c-base -f /usr/local/images/c/Dockerfile.base .')
    }
    triggers {
        upstream('Whanos base images/Build all base images', 'SUCCESS')
    }
}

freeStyleJob('Whanos base images/whanos-javascript') {
    description('Build Docker Image triggered by GitHub push')

    steps {
        shell('docker build -t whanos-javascript-base -f /usr/local/images/javascript/Dockerfile.base .')
    }
    triggers {
        upstream('Whanos base images/Build all base images', 'SUCCESS')
    }
}


freeStyleJob('Whanos base images/whanos-java') {
    description('Build Docker Image triggered by GitHub push')

    steps {
        shell('docker build -t whanos-java-base -f /usr/local/images/java/Dockerfile.base .')
    }
    triggers {
        upstream('Whanos base images/Build all base images', 'SUCCESS')
    }
}

freeStyleJob('Whanos base images/whanos-python') {
    description('Build Docker Image triggered by GitHub push')

    steps {
        shell('docker build -t whanos-python-base -f /usr/local/images/python/Dockerfile.base .')
    }

    triggers {
        upstream('Whanos base images/Build all base images', 'SUCCESS')
    }
}

freeStyleJob('Whanos base images/Build all base images') {
    description('Build Docker Image triggered by GitHub push')

}

freeStyleJob('link-project') {
    description('link project repo test')
    description('link project repo')
    parameters {
        stringParam('GIT_REPOSITORY_URL', '', 'Git URL of the repository to clone')
        booleanParam('IS_GIT_REPO_PRIVATE', false, 'Is the git repository private?')
        credentialsParam('GIT_CREDENTIALS') {
            description('Credentials to access the git repository')
            defaultValue('git-credentials')
        }
        stringParam('JOB_NAME', '', 'Name of the job to create')
        stringParam('IMAGE_REPO_LOCATION', 'europe-west2', 'Location of the image repository')
        stringParam('IMAGE_REPO_URL', '', 'URL of the image repository')
        stringParam('PORT_FORWARD', '8080:8080', 'Port to forward to the container')
        booleanParam('IS_DOCKER_REPO_PRIVATE', false, 'Is the docker repository private?')
        fileParam('key_file.json', 'Key file to access the google cloud repository')
        stringParam('BRANCH', 'main', 'Branch to build')
    }
    steps {
        dsl {
            text ('''
                    freeStyleJob('Projects/' + JOB_NAME) {
                        description('Job linked')
                        wrappers {
                          preBuildCleanup()
                        }
                        scm {
                            if (IS_GIT_REPO_PRIVATE) {
                                git {
                                    branch(BRANCH)
                                    remote {
                                        url(GIT_REPOSITORY_URL)
                                        credentials(GIT_CREDENTIALS)
                                    }
                                }
                            } else {
                                git {
                                    branch(BRANCH)
                                    remote {
                                        url(GIT_REPOSITORY_URL)
                                    }
                                }
                            }
                        }
                        triggers {
                            scm('*/1 * * * *')
                        }
                        environmentVariables {
                            env('PROJECT_NAME', JOB_NAME)
                            env('GIT_REPOSITORY_URL', GIT_REPOSITORY_URL)
                            env('IMAGE_REPO_LOCATION', IMAGE_REPO_LOCATION)
                            env('IS_DOCKER_REPO_PRIVATE', IS_DOCKER_REPO_PRIVATE)
                            env('IMAGE_NAME', IMAGE_REPO_URL + '/' + JOB_NAME.toLowerCase().replace(' ', '_'))
                            env('PORT_FORWARD', PORT_FORWARD)
                        }
                        steps {
                            if (IS_DOCKER_REPO_PRIVATE) {
                                shell('cat ${JENKINS_HOME}/persistent/${PROJECT_NAME}_key_file.json |  docker login -u _json_key --password-stdin https://'+IMAGE_REPO_LOCATION+'-docker.pkg.dev')
                            }
                            shell("docker build -t \\${IMAGE_NAME}:v1.\\${BUILD_NUMBER} -t \\${IMAGE_NAME}:latest -f /usr/local/images/`/usr/local/bin/find_lang \\${WORKSPACE}`/Dockerfile.standalone \\${WORKSPACE}")
                            shell("docker push -a \\${IMAGE_NAME}")
                            shell("docker stop \\${PROJECT_NAME} || true")
                            shell("docker rm \\${PROJECT_NAME} || true")
                            if (PORT_FORWARD != '') {
                                shell("docker run -d --name \\${PROJECT_NAME} -p \\${PORT_FORWARD} \\${IMAGE_NAME}:latest")
                            } else {
                                shell("docker run -d --name \\${PROJECT_NAME} \\${IMAGE_NAME}:latest")
                            }
                        }
                    }
            ''')
        shell('mkdir -p ${JENKINS_HOME}/persistent && \
        mv key_file.json ${JENKINS_HOME}/persistent/${JOB_NAME}_key_file.json')
        }
    }
}
