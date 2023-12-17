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
        stringParam('JOB_NAME', '', 'Git URL of the repository to clone')
        stringParam('IMAGE_REPO_LOCATION', 'europe-west2', 'Location of the image repository')
        stringParam('IMAGE_REPO_URL', '', 'URL of the image repository')
        booleanParam('IS_DOCKER_REPO_PRIVATE', false, 'Is the docker repository private?')
        fileParam('key_file.json', 'Key file to access the google cloud repository')
    }
    steps {
        dsl {
            text ('''
                    freeStyleJob('Projects/' + JOB_NAME) {
                        description('Job linked')
                        wrappers {
                          preBuildCleanup()
                        }
                        triggers {
                            pollSCM('* * * * *')
                        }
                        environmentVariables {
                            env('PROJECT_NAME', JOB_NAME)
                            env('GIT_REPOSITORY_URL', GIT_REPOSITORY_URL)
                            env('IMAGE_REPO_LOCATION', IMAGE_REPO_LOCATION)
                            env('IS_DOCKER_REPO_PRIVATE', IS_DOCKER_REPO_PRIVATE)
                            env('IMAGE_NAME', IMAGE_REPO_URL + '/' + JOB_NAME.toLowerCase().replace(' ', '_'))
                        }
                        steps {
                            scm {
                                git {
                                    remote {
                                        url(GIT_REPOSITORY_URL)
                                        credentials(SSH_KEY_REPO)
                                    }
                                    branches('*/main')
                                }
                            }
                            if (IS_DOCKER_REPO_PRIVATE) {
                                shell('cat ${JENKINS_HOME}/persistent/${PROJECT_NAME}_key_file.json |  docker login -u _json_key --password-stdin https://'+IMAGE_REPO_LOCATION+'-docker.pkg.dev')
                            }
                            shell("docker build -t \\${IMAGE_NAME}:v1.\\${BUILD_NUMBER} -t \\${IMAGE_NAME}:latest -f /usr/local/images/`/usr/local/bin/find_lang \\${WORKSPACE}`/Dockerfile.standalone \\${WORKSPACE}")
                            shell("docker push -a \\${IMAGE_NAME}")
                                script {
                                    def pythonImage() {
                                        build(job: 'Whanos base images/whanos-puthon')
                                        return docker.image('whanos-python-base')
                                    }
                                    def javascriptImage() {
                                        build(job: 'Whanos base images/whanos-javascript')
                                        return docker.image('whanos-javascript-base')
                                    }
                                    def javaImage() {
                                        build(job: 'Whanos base images/whanos-java')
                                        return docker.image('whanos-java-base')
                                    }
                                    def cImage() {
                                        build(job: 'Whanos base images/whanos-c')
                                        return docker.image('whanos-c-base')
                                    }
                                    def befungeImage() {
                                        build(job: 'Whanos base images/whanos-befunge')
                                        return docker.image('whanos-befunge-base')
                                    }

                                    def lang = sh(script: 'python find_lang ${env.WORKSPACE}'', returnStdout: true)
                                    def image
                                    if (lang == 'whanos-python') {
                                        image = pythonImage()
                                    } else if (lang == 'whanos-javascript') {
                                        image = javascriptImage()
                                    } else if (lang == 'whanos-C') {
                                        image = cImage()
                                    } else if (lang == 'whanos-befunge') {
                                        image = befungeImage()
                                    } els if (lang == 'whanos-java') {
                                        image = javaImage()
                                    }
                                    def container = image.run('-d -p 127.0.0.1:3000:3000', '--name my-container-name')
                                }
                        }
                    }
            ''')
        shell('mkdir -p ${JENKINS_HOME}/persistent && \
        mv key_file.json ${JENKINS_HOME}/persistent/${PROJECT_NAME}_key_file.json')
        }
    }
}
